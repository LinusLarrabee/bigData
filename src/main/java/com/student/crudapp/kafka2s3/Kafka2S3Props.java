package com.student.crudapp.kafka2s3;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ConfigurationProperties(prefix = "kafka-to-s3")
@Component
public class Kafka2S3Props {

    @NotNull
    private Kafka kafka;

    @NotNull
    private S3 s3;

    @NotNull
    private String timeZone;

    @NotNull
    private Integer maxBatchSize;

    @NotNull
    private Integer maxFileSize;

    @Getter
    @Setter
    public static class Kafka {
        @NotNull
        private String bootstrapServers;
        @NotNull
        private String topic;
        @NotNull
        private String groupId;
        @NotNull
        private String keyDeserializer;
        @NotNull
        private String valueDeserializer;
        @NotNull
        private String autoOffsetReset;
        @NotNull
        private String enableAutoCommit;
        @NotNull
        private Integer pollDurationMs;
        @NotNull
        private Integer maxPollRecords;
    }

    @Getter
    @Setter
    public static class S3 {
        @NotNull
        private String bucketName;
        @NotNull
        private String prefix;  // 确保以斜杠结尾
        @NotNull
        private String accessKey;
        @NotNull
        private String secretKey;
        @NotNull
        private String env;  // 格式为 {environment}-{region}/
        @NotNull
        private String region;
    }
}
