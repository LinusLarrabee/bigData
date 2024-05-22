## Hadoop

### 本地配置hadoop

1. javahome，环境变量，防火墙等通用配置
2. 修改配置文件和env.sh文件

本文件使用macOS intel版本进行配置，建议使用类unix系统而非windows。

本地配置hadoop可参考下述链接：

https://www.cnblogs.com/shoufeng/p/14411399.html

补充可参考

https://hadoop.apache.org/docs/stable/hadoop-project-dist/hadoop-common/SingleCluster.html



### Usage

#### MapReduce

将项目打包后在shell运行

```shell
hadoop jar /Users/sunhao/IdeaProjects/crudapp/target/crudapp-0.0.1-SNAPSHOT.jar /md/input/input.txt /md/output1
```

这里可能会有hadoop和java日志系统不兼容问题，在下述buglist讨论。

此外需要引入插件指定jar包的执行文件

```xml

            <plugin>
                <artifactId>maven-jar-plugin</artifactId>
                <version>3.2.0</version>
                <configuration>
                    <archive>
                        <manifest>
                            <mainClass>mapReduce.WordCountApp</mainClass>
                        </manifest>
                    </archive>
                </configuration>
            </plugin>

```



#### Java API

构建了一个基本的对hdfs的调用。

```java
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
```



```java
package com.student.crudapp.hadoop;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
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
                log.info("File created successfully");
            } else {
                log.info("File already exists");
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
```







### buglist

如果shell未输出错误，配置过程中可以在log目录下找到对应的错误日志。

1. 无法打开http://hadoop:9870这种本地链接

关掉vpn



2. Caused by: java.lang.reflect.InaccessibleObjectException: Unable to make protected final java.lang.Class 

解决：

export HADOOP_OPTS="--add-opens java.base/java.lang=ALL-UNNAMED $HADOOP_OPTS"

注意加的位置，哪个服务无法启动就加到对应的env中，比如我的yarn无法启动，如果加到hdfs-env中则会导致对应的dfs难以启动。当然也可以在启动命令行进行如此修改。



3. bin/mapred --daemon start historyserver ERROR: Cannot set priority of historyserver process 59812

修改zshrc的环境变量（上述第一个文档解决不了的问题只有环境变量，网络条件状况的问题



4. MapReduce项目打jar包运行时的问题：

Caused by: java.lang.IllegalArgumentException: LoggerFactory is not a Logback LoggerContext but Logback is on the classpath. Either remove Logback or the competing implementation (class org.slf4j.impl.Reload4jLoggerFactory loaded from file:/usr/local/Cellar/hadoop/3.4.0/libexec/share/hadoop/common/lib/slf4j-reload4j-1.7.36.jar). If you are using WebLogic you will need to add 'org.slf4j' to prefer-application-packages in WEB-INF/weblogic.xml: org.slf4j.impl.Reload4jLoggerFactory

Spring项目中使用的日志系统与hadoop冲突，根据使用情况引入下述包

```xml
            <exclusions>
                <exclusion>
                    <groupId>ch.qos.logback</groupId>
                    <artifactId>logback-classic</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>ch.qos.logback</groupId>
                    <artifactId>logback-core</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>org.slf4j</groupId>
                    <artifactId>slf4j-reload4j</artifactId>
                </exclusion>
            </exclusions>
```



## Hive

### 配置

hive配置较为简单，下述链接也有对hadoop的配置，但使用起来比较不方便，建议还是使用另一个链接以及参考gpt

https://github.com/heibaiying/BigData-Notes/tree/master

### Buglist

1. MetaException(message:Error creating transactional connection factory)

一般是数据库配置和连接失败，需要将数据库驱动加载到lib中。

sudo cp /path/to/download/mysql-connector-java-8.0.xx.jar /opt/module/hive/lib/



### 使用：

hive主要功能是将类似SQL的语句转化为MapReduce命令供Hadoop执行，因此按我理解这个地方的使用就是写数据库脚本。





## Zookeeper

使用：

Curator：对集群的节点进行监控

ACL：权限控制

单机对集群进行监控可能有些过度，不仅从复杂度，监控需求还是高可用都有些over



