//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.consumer.ConsumerRecords;
//import org.apache.kafka.clients.consumer.KafkaConsumer;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.model.PutObjectRequest;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.time.Duration;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Properties;
//
//public class DataCollectorEventSubscriber {
//
//    private static final String TOPIC = "your-topic-name";
//    private static final String BUCKET_NAME = "your-bucket-name";
//    private static final String PREFIX = "your/prefix/path/";
//
//    public static void main(String[] args) {
//        KafkaConsumer<String, String> consumer = createKafkaConsumer();
//        S3Client s3 = createS3Client();
//
//        List<String> messages = new ArrayList<>();
//        int batchSize = 20;
//
//        consumeAndProcessMessages(consumer, s3, messages, batchSize);
//    }
//
//    private static KafkaConsumer<String, String> createKafkaConsumer() {
//        Properties props = new Properties();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, "your-consumer-group-id");
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//
//        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
//        consumer.subscribe(Collections.singletonList(TOPIC));
//        return consumer;
//    }
//
//    private static S3Client createS3Client() {
//        Region region = Region.US_WEST_2; // 替换为您的区域
//        return S3Client.builder()
//                .region(region)
//                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
//                .build();
//    }
//
//    private static void consumeAndProcessMessages(KafkaConsumer<String, String> consumer, S3Client s3, List<String> messages, int batchSize) {
//        while (true) {
//            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
//
//            for (ConsumerRecord<String, String> record : records) {
//                messages.add(record.value());
//
//                if (messages.size() >= batchSize) {
//                    writeToS3(s3, messages);
//                    messages.clear();
//                }
//            }
//        }
//    }
//
//    private static void writeToS3(S3Client s3, List<String> messages) {
//        String key = PREFIX + "messages-" + System.currentTimeMillis() + ".json";
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            String content = mapper.writeValueAsString(messages);
//
//            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
//                    .bucket(BUCKET_NAME)
//                    .key(key)
//                    .build();
//
//            s3.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromString(content));
//            System.out.println("写入 " + messages.size() + " 条消息到 " + key);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
