import org.apache.flink.api.common.functions.FlatMapFunction
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.api.common.eventtime.{WatermarkStrategy, SerializableTimestampAssigner}
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.util.Collector
import org.apache.flink.streaming.api.windowing.windows.TimeWindow

import java.time.Duration
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime

case class UserVisit(user_id: Int, timestamp: Long, page: String)
case class UserPath(user_id: Int, path: String)
case class PathCount(path: String, count: Long)

object FlinkLinkAnalysis {
    def main(args: Array[String]): Unit = {
        val env = StreamExecutionEnvironment.getExecutionEnvironment
        env.setParallelism(1)

        // 加载数据并计时
        val startLoadData = System.currentTimeMillis()
        val input = env.readTextFile("init.txt")
        val endLoadData = System.currentTimeMillis()
        println(s"加载数据时间: ${endLoadData - startLoadData} 毫秒")

        val visits = input
          .flatMap(new FlatMapFunction[String, UserVisit] {
              override def flatMap(value: String, out: Collector[UserVisit]): Unit = {
                  if (!value.startsWith("user_id")) {
                      val fields = value.split(",")
                      val userId = fields(0).trim.toInt
                      val timestamp = LocalDateTime.parse(fields(1).trim, DateTimeFormatter.ISO_LOCAL_DATE_TIME).atZone(java.time.ZoneId.systemDefault()).toInstant.toEpochMilli
                      val page = fields(2).trim
                      out.collect(UserVisit(userId, timestamp, page))
                  }
              }
          })
          .assignTimestampsAndWatermarks(WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(1))
            .withTimestampAssigner(new SerializableTimestampAssigner[UserVisit] {
                override def extractTimestamp(element: UserVisit, recordTimestamp: Long): Long = element.timestamp
            }))

        // 计算用户路径并计时
        val startUserPaths = System.currentTimeMillis()
        val userPaths = visits
          .keyBy(_.user_id)
          .window(TumblingEventTimeWindows.of(Time.seconds(10)))
          .process(new ProcessWindowFunction[UserVisit, UserPath, Int, TimeWindow] {
              override def process(key: Int, context: Context, elements: Iterable[UserVisit], out: Collector[UserPath]): Unit = {
                  val path = elements.toList.sortBy(_.timestamp).map(_.page).mkString("->")
                  out.collect(UserPath(key, path))
              }
          })
        val endUserPaths = System.currentTimeMillis()
        println(s"计算用户路径时间: ${endUserPaths - startUserPaths} 毫秒")

        // 统计完全一致的链路并计时
        val startPathCount = System.currentTimeMillis()
        val pathCounts = userPaths
          .map(path => (path.path, 1))
          .keyBy(_._1)
          .timeWindow(Time.seconds(10))
          .sum(1)
          .map(t => PathCount(t._1, t._2))
        val endPathCount = System.currentTimeMillis()
        println(s"统计完全一致链路时间: ${endPathCount - startPathCount} 毫秒")

        // 打印完全一致的链路统计结果
        pathCounts.print()

        // 合并相似路径并计时
        val startCombinePaths = System.currentTimeMillis()
        val combinedPaths = combineSimilarPaths(pathCounts)
        val endCombinePaths = System.currentTimeMillis()
        println(s"合并相似路径时间: ${endCombinePaths - startCombinePaths} 毫秒")

        // 打印合并后的结果
        combinedPaths.print()

        env.execute("Flink Link Analysis")
    }

    def combineSimilarPaths(pathCounts: DataStream[PathCount]): DataStream[PathCount] = {
        pathCounts
          .keyBy(_.path.split("->").take(2).mkString("->"))
          .timeWindow(Time.seconds(10))
          .reduce((p1, p2) => PathCount(p1.path.split("->").take(2).mkString("->"), p1.count + p2.count))
    }
}
