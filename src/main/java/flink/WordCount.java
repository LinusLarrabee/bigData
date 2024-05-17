package flink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

public class WordCount {

    public static void main(String[] args) throws Exception {
        // 设置执行环境
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        // 从输入文件读取数据
        DataSet<String> text = env.readTextFile("src/main/resources/input.txt");

        DataSet<Tuple2<String, Integer>> counts = text
                .flatMap(new Tokenizer())
                .groupBy(0)
                .sum(1);

        // 将结果输出到文件
        counts.writeAsCsv("src/main/resources/output.csv", "\n", " ");

        // 执行程序
        env.execute("WordCount Example");
    }

    public static final class Tokenizer implements FlatMapFunction<String, Tuple2<String, Integer>> {
        @Override
        public void flatMap(String value, Collector<Tuple2<String, Integer>> out) {
            // 正则表达式分割
            String[] tokens = value.toLowerCase().split("\\W+");

            for (String token : tokens) {
                if (token.length() > 0) {
                    out.collect(new Tuple2<>(token, 1));
                }
            }
        }
    }
}
