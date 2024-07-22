//package com.student.crudapp.kafka2s3;
//
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.databind.util.StdDateFormat;
//import org.apache.kafka.clients.consumer.*;
//import org.apache.kafka.common.TopicPartition;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//import java.time.Duration;
//import java.time.Instant;
//import java.time.ZoneId;
//import java.time.ZonedDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//import java.util.concurrent.CompletableFuture;
//
//@Service
//public class Kafka2S3Service {
//
//    @Autowired
//    private Kafka2S3Props kafka2S3Props;
//
//    private KafkaConsumer<String, String> consumer;
//    private AmazonS3 s3Client;
//    private DateTimeFormatter dateFormatter;
//    private final ObjectMapper mapper = new ObjectMapper();
//
//    @PostConstruct
//    public void init() {
//        try {
//            System.out.println("Initializing KafkaToS3Service...");
//            this.consumer = createKafkaConsumer();
//            System.out.println("Kafka consumer created.");
//            this.s3Client = createS3Client();
//            System.out.println("S3 client created.");
//            configureObjectMapper();
//            startConsuming();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }
//
//    @PreDestroy
//    public void shutdown() {
//        if (consumer != null) {
//            consumer.close();
//            System.out.println("Kafka consumer closed.");
//        }
//    }
//
//    private KafkaConsumer<String, String> createKafkaConsumer() {
//        Properties props = new Properties();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka2S3Props.getKafka().getBootstrapServers());
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafka2S3Props.getKafka().getGroupId());
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafka2S3Props.getKafka().getKeyDeserializer());
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafka2S3Props.getKafka().getValueDeserializer());
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafka2S3Props.getKafka().getAutoOffsetReset());
//        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafka2S3Props.getKafka().getEnableAutoCommit());
//        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, kafka2S3Props.getKafka().getMaxPollRecords().toString());
//
//        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
//        consumer.subscribe(Collections.singletonList(kafka2S3Props.getKafka().getTopic()));
//        return consumer;
//    }
//
//    private AmazonS3 createS3Client() {
//        BasicAWSCredentials awsCreds = new BasicAWSCredentials(kafka2S3Props.getS3().getAccessKey(), kafka2S3Props.getS3().getSecretKey());
//        return AmazonS3ClientBuilder.standard()
//                .withRegion(kafka2S3Props.getS3().getRegion())
//                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
//                .build();
//    }
//
//    private void configureObjectMapper() {
//        mapper.enable(SerializationFeature.INDENT_OUTPUT);
//        mapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
//    }
//
//    @Async
//    public CompletableFuture<Void> startConsuming() {
//        return CompletableFuture.runAsync(this::consumeAndProcessMessages);
//    }
//
//    private void consumeAndProcessMessages() {
//        List<ConsumerRecord<String, String>> buffer = new ArrayList<>();
//        ZonedDateTime firstRecordDate = null;
//        ZoneId zoneId = ZoneId.of(kafka2S3Props.getTimeZone());
//
//        while (true) {
//            System.out.println("Polling for records...");
//            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(kafka2S3Props.getKafka().getPollDurationMs()));
//            System.out.println("Polled " + records.count() + " records.");
//
//            if (!records.isEmpty()) {
//                for (ConsumerRecord<String, String> record : records) {
//                    buffer.add(record);
//                    if (firstRecordDate == null) {
//                        firstRecordDate = Instant.ofEpochMilli(record.timestamp()).atZone(zoneId);
//                    }
//                }
//
//                // Initialize dateFormatter when it's first needed
//                if (dateFormatter == null) {
//                    dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd").withZone(zoneId);
//                    System.out.println("DateFormatter initialized with timezone: " + kafka2S3Props.getTimeZone());
//                }
//
//                while (buffer.size() >= kafka2S3Props.getMaxBatchSize()) {
//                    List<ConsumerRecord<String, String>> batch = new ArrayList<>(buffer.subList(0, kafka2S3Props.getMaxBatchSize()));
//                    processAndUploadMessages(batch, firstRecordDate);
//                    buffer = new ArrayList<>(buffer.subList(kafka2S3Props.getMaxBatchSize(), buffer.size()));
//                    if (!buffer.isEmpty()) {
//                        firstRecordDate = Instant.ofEpochMilli(buffer.get(0).timestamp()).atZone(zoneId);
//                    } else {
//                        firstRecordDate = null;
//                    }
//                }
//
//                ZonedDateTime currentDate = ZonedDateTime.now(zoneId);
//                if (firstRecordDate != null && !currentDate.toLocalDate().equals(firstRecordDate.toLocalDate()) && !buffer.isEmpty()) {
//                    processAndUploadMessages(buffer, firstRecordDate);
//                    buffer.clear();
//                    firstRecordDate = null;
//                }
//            }
//        }
//    }
//
//    private void processAndUploadMessages(List<ConsumerRecord<String, String>> records, ZonedDateTime date) {
//        System.out.println("Processing " + records.size() + " records for date: " + date);
//        List<String> messages = new ArrayList<>();
//        for (ConsumerRecord<String, String> record : records) {
//            messages.add(record.value());
//        }
//        uploadMessages(messages, date, records);
//    }
//
//    private void uploadMessages(List<String> messages, ZonedDateTime date, List<ConsumerRecord<String, String>> records) {
//        long timestamp = System.currentTimeMillis();
//        String datePath = dateFormatter.format(date);
//        String key = String.format("local/%s/%s/messages-%d.txt",
//                kafka2S3Props.getS3().getEnv().replace("-", "/"),
//                datePath,
//                timestamp);
//
//        try {
//            StringBuilder content = new StringBuilder();
//            for (String message : messages) {
//                content.append(mapper.writeValueAsString(message)).append("\n");
//            }
//
//            s3Client.putObject(kafka2S3Props.getS3().getBucketName(), key, content.toString());
//            System.out.println("写入 " + messages.size() + " 条消息到 " + key);
//
//            // 提交当前处理的偏移量
//            Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();
//            for (ConsumerRecord<String, String> record : records) {
//                TopicPartition partition = new TopicPartition(record.topic(), record.partition());
//                offsets.put(partition, new OffsetAndMetadata(record.offset() + 1));
//            }
//            consumer.commitSync(offsets); // 手动提交每个分区的偏移量
//            System.out.println("Committed offsets: " + offsets);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
