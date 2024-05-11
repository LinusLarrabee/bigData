package com.student.crudapp.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class HadoopFileService {

    @Autowired
    private Configuration hadoopConfig; // 注入Hadoop配置

    /**
     * 创建一个新文件在HDFS上。
     * @param path 文件的HDFS路径
     */
    public void createFile(String path) {
        Path filePath = new Path(path);
        try (FileSystem fs = FileSystem.get(hadoopConfig)) {
            if (!fs.exists(filePath)) {
                fs.create(filePath).close(); // 创建文件并立即关闭
                System.out.println("File created successfully");
            } else {
                System.out.println("File already exists");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取HDFS上的文件内容。
     * @param path 文件的HDFS路径
     * @return 文件内容
     */
    public String readFile(String path) {
        Path filePath = new Path(path);
        StringBuilder stringBuilder = new StringBuilder();
        try (FileSystem fs = FileSystem.get(hadoopConfig);
             BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(filePath)))) {
            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
