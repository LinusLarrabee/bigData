#### 配置文件的优先级

（1）同 application.properties 文件一样，Spring Boot 项目中的 application.yml 配置文件一共可以出现在如下 4 个位置（优先级逐渐降低）：

- 项目根目录下的 config 文件夹
- 项目根目录下
- classpath 下的 config 文件夹
- classpath 下

（2）如果这 4 个地方都有 application.yml 文件，加载的优先级就是从 1 到 4 依次降低，Spring Boot 将按照这个优先级查找配置信息。



#### 在代码中配置环境

（1）除了像前面那样在 application.yml 中添加配置，我们也可以在代码中添加配置来完成。

（2）比如我们在启动类的 main 方法上添加如下代码，表示使用 application-dev.yml 配置文件启动项目。

```java
package com.example.demo;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplicationBuilder builder = new
                SpringApplicationBuilder(DemoApplication.class);
        builder.application().setAdditionalProfiles("dev");
        builder.run(args);
    }
}
```



#### 使用命令行参数进行配置

（1）在命令行中通过 java -jar 命令启动项目时，可以使用连续的两个减号 -- 对 application.yml 中的属性值进行赋值。

（2）比如下面命令修改 tomcat 端口号为 8081。其等价于在 application.yml 中添加属性 server.port=8081：

注意：如果 application.yml 中已经有同名属性，那么命令行属性会覆盖 application.yml 的属性。

``` SHELL
java -jar xx.jar --server.port=8081
```

