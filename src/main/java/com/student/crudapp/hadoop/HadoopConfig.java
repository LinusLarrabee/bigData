package com.student.crudapp.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.springframework.context.annotation.Bean;


@org.springframework.context.annotation.Configuration
public class HadoopConfig {

    @Bean
    public Configuration hadoopConfiguration() {
        Configuration config = new Configuration();
        config.set("fs.defaultFS", "hdfs://localhost:9000");
        // 以下设置为Hadoop环境特定的配置，如果需要的话
        config.set("hadoop.home.dir", "/usr/local/Cellar/hadoop/3.4.0");
        return config;
    }
}
