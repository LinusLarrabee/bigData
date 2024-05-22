package flink;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.functions.GroupReduceFunction;
import org.apache.flink.api.java.typeutils.TypeExtractor;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;

import java.util.List;

public class FlinkKNN {
    public static void main(String[] args) throws Exception {
        // 创建 Flink 执行环境
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        // 从命令行参数中获取参数
        final ParameterTool params = ParameterTool.fromArgs(args);
        env.getConfig().setGlobalJobParameters(params);

        String trainingPath = params.get("trainingData", "hdfs://hadoop:9000/user/sunhao/two_room_data_train.csv");
        String testPath = params.get("testData", "hdfs://hadoop:9000/user/sunhao/two_room_data_test.csv");



        // 读取训练数据
        DataSet<Tuple2<Double, Integer>> trainingData = env.readCsvFile(trainingPath)
                .fieldDelimiter(",")
                .types(Double.class, Integer.class);

        // 读取测试数据
        DataSet<Tuple2<Double, Integer>> testData = env.readCsvFile(testPath)
                .fieldDelimiter(",")
                .types(Double.class, Integer.class);

        // 展示测试数据
        System.out.println("Test Data:");
        testData.print();

        // 记录开始时间
        long startTime = System.currentTimeMillis();

        // 对训练数据和测试数据进行标准化处理
        DataSet<Double> trainingRssi = trainingData.map(t -> t.f0).returns(Double.class);
        DataSet<Double> testRssi = testData.map(t -> t.f0).returns(Double.class);

        Double mean = trainingRssi.reduce((a, b) -> a + b).collect().get(0) / trainingRssi.count();
        Double std = Math.sqrt(trainingRssi.map(v -> Math.pow(v - mean, 2)).reduce((a, b) -> a + b).collect().get(0) / trainingRssi.count());

        DataSet<Tuple2<Double, Integer>> normalizedTrainingData = trainingData.map(t -> new Tuple2<>((t.f0 - mean) / std, t.f1)).returns(TypeExtractor.getForObject(new Tuple2<>(0.0, 0)));
        DataSet<Tuple2<Double, Integer>> normalizedTestData = testData.map(t -> new Tuple2<>((t.f0 - mean) / std, t.f1)).returns(TypeExtractor.getForObject(new Tuple2<>(0.0, 0)));

        // 设置 k 值
        int k = 3;

        // 广播训练数据
        DataSet<Tuple3<Double, Integer, Integer>> knnResults = normalizedTestData.flatMap(new RichFlatMapFunction<Tuple2<Double, Integer>, Tuple3<Double, Integer, Integer>>() {
                    private List<Tuple2<Double, Integer>> trainingPoints;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        this.trainingPoints = getRuntimeContext().getBroadcastVariable("trainingPoints");
                    }

                    @Override
                    public void flatMap(Tuple2<Double, Integer> testPoint, Collector<Tuple3<Double, Integer, Integer>> out) throws Exception {
//                        System.out.println("Processing test point: " + testPoint);
                        for (Tuple2<Double, Integer> trainingPoint : trainingPoints) {
                            double distance = Math.abs(trainingPoint.f0 - testPoint.f0);
                            out.collect(new Tuple3<>(distance, trainingPoint.f1, testPoint.f1));
                        }
                    }
                }).withBroadcastSet(normalizedTrainingData, "trainingPoints")
                .returns(TypeExtractor.getForObject(new Tuple3<>(0.0, 0, 0)))
                .groupBy(2) // 按测试点分组
                .sortGroup(0, Order.ASCENDING) // 按距离排序
                .first(k); // 取前 k 个最近邻

//        // 打印中间结果 knnResults
//        System.out.println("KNN Results:");
//        knnResults.print();

        // 多数投票确定分类结果
        DataSet<Tuple2<Integer, Integer>> predictions = knnResults
                .groupBy(2) // 按测试标签分组
                .reduceGroup(new GroupReduceFunction<Tuple3<Double, Integer, Integer>, Tuple2<Integer, Integer>>() {
                    @Override
                    public void reduce(Iterable<Tuple3<Double, Integer, Integer>> neighbors, Collector<Tuple2<Integer, Integer>> out) throws Exception {
                        int[] counts = new int[2]; // 假设有两个类别：1 和 2
                        Integer testLabel = null;
                        for (Tuple3<Double, Integer, Integer> neighbor : neighbors) {
                            if (neighbor.f1 >= 1 && neighbor.f1 <= 2) { // 检查类别是否在 1 和 2 之间
                                counts[neighbor.f1 - 1]++; // 将类别转换为数组索引（1 -> 0，2 -> 1）
                            }
                            testLabel = neighbor.f2;
                        }
                        int predictedLabel;
                        if (counts[0] > counts[1]) {
                            predictedLabel = 1;
                        } else if (counts[1] > counts[0]) {
                            predictedLabel = 2;
                        } else {
                            predictedLabel = counts[0] > 0 ? 1 : 2; // 处理平局情况，可以根据需求修改
                        }
                        out.collect(new Tuple2<>(testLabel, predictedLabel));
                    }
                }).returns(TypeExtractor.getForObject(new Tuple2<>(0, 0)));

        // 打印预测结果
        System.out.println("Predictions:");
        predictions.print();

        // 计算准确率
        long correct = predictions.filter(t -> t.f0.equals(t.f1)).count();
        long total = predictions.count();

        double accuracy = (double) correct / total;
        System.out.println("Accuracy: " + accuracy);

        // 记录结束时间
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // 输出运行时间
        System.out.println("Time taken: " + duration + " milliseconds");

        // 执行 Flink 作业
        env.execute("Flink KNN");
    }
}
