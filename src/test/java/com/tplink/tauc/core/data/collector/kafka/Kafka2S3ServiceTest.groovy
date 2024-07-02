package com.tplink.tauc.core.data.collector.kafka

import com.amazonaws.services.s3.AmazonS3
import com.student.crudapp.kafka2s3.Kafka2S3Props
import com.student.crudapp.kafka2s3.Kafka2S3Service
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.TopicPartition
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import java.time.Duration
import java.time.ZonedDateTime

@SpringBootTest
@ContextConfiguration(classes = Kafka2S3ServiceTest.TestConfig)
class Kafka2S3ServiceTest extends Specification {

    def kafka2S3Service = new Kafka2S3Service()

    def kafka2S3Props = Mock(Kafka2S3Props)
    def consumer = Mock(KafkaConsumer)
    def s3Client = Mock(AmazonS3)

    @Configuration
    static class TestConfig {
        def factory = new DetachedMockFactory()

        @Bean
        Kafka2S3Props kafka2S3Props() {
            return factory.Mock(Kafka2S3Props)
        }

        @Bean
        KafkaConsumer<String, String> kafkaConsumer() {
            return factory.Mock(KafkaConsumer)
        }

        @Bean
        AmazonS3 amazonS3() {
            return factory.Mock(AmazonS3)
        }

        @Bean
        Kafka2S3Service kafka2S3Service() {
            return new Kafka2S3Service()
        }
    }

    def setup() {
        kafka2S3Service.kafka2S3Props = kafka2S3Props
        kafka2S3Service.consumer = consumer
        kafka2S3Service.s3Client = s3Client

        kafka2S3Props.getKafka() >> [
                getBootstrapServers: 'localhost:9092',
                getGroupId: 'test-group',
                getKeyDeserializer: 'org.apache.kafka.common.serialization.StringDeserializer',
                getValueDeserializer: 'org.apache.kafka.common.serialization.StringDeserializer',
                getAutoOffsetReset: 'earliest',
                getEnableAutoCommit: 'false',
                getMaxPollRecords: 10,
                getPollDurationMs: 1000,
                getTopic: 'test-topic'
        ]

        kafka2S3Props.getS3() >> [
                getAccessKey: 'test-access-key',
                getSecretKey: 'test-secret-key',
                getRegion: 'us-west-2',
                getBucketName: 'test-bucket',
                getEnv: 'test-env'
        ]

        kafka2S3Props.getTimeZone() >> 'UTC'
        kafka2S3Props.getMaxBatchSize() >> 5
    }

    def "test Kafka consumer creation"() {
        when:
        kafka2S3Service.init()

        then:
        kafka2S3Service.consumer != null
        kafka2S3Service.s3Client != null
    }

    def "test Kafka consumer polling and processing messages"() {
        setup:
        kafka2S3Service.startConsuming() >> CompletableFuture.completedFuture(null)

        ConsumerRecord<String, String> record1 = new ConsumerRecord<>("test-topic", 0, 0L, "key1", "value1")
        ConsumerRecord<String, String> record2 = new ConsumerRecord<>("test-topic", 0, 1L, "key2", "value2")
        ConsumerRecords<String, String> records = new ConsumerRecords<>([new TopicPartition("test-topic", 0): [record1, record2]])

        when:
        kafka2S3Service.consumeAndProcessMessages()

        then:
        1 * kafka2S3Service.consumer.poll(_ as Duration) >> records
        1 * kafka2S3Service.s3Client.putObject(_, _, _)
        1 * kafka2S3Service.consumer.commitSync(_)
    }

    def "test S3 client creation"() {
        when:
        def s3Client = kafka2S3Service.createS3Client()

        then:
        s3Client != null
    }

    def "test process and upload messages"() {
        given:
        List<ConsumerRecord<String, String>> records = [
                new ConsumerRecord<>("test-topic", 0, 0L, "key1", "value1"),
                new ConsumerRecord<>("test-topic", 0, 1L, "key2", "value2")
        ]
        ZonedDateTime date = ZonedDateTime.now(ZoneId.of("UTC"))

        when:
        kafka2S3Service.processAndUploadMessages(records, date)

        then:
        1 * kafka2S3Service.s3Client.putObject(_, _, _)
        1 * kafka2S3Service.consumer.commitSync(_)
    }
}
