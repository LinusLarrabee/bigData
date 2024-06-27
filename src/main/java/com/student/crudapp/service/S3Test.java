package com.student.crudapp.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class S3Test {
    public static void main(String[] args) {
        String accessKey = "";
        String secretKey = "";
        String bucketName = "";
        String region = "";
        String key = "";
        String content = "This is a test file";

        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();

        try {
            s3Client.putObject(bucketName, key, content);
            System.out.println("成功写入测试文件到 S3");
        } catch (Exception e) {
            System.err.println("写入测试文件到 S3 时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
