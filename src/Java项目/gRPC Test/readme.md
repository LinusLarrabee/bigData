Grpc测试工具使用

## 功能

本工具根据Grpc客户端提供的proto文件自动生成一个本地可启动的Grpc服务，该服务可针对Grpc客户端传输的信息进行响应。

在此基础上，与同类工具相比提供以下特性：

1. 支持多文件嵌套与Google系统包解析。
2. 支持gitlab分支的Proto提取与本地提取两种方式。
3. 对于客户端传输的信息，服务端默认返回空值，并捕获传输的信息以供构建返回值。
4. 经过 tauc-account-cloudservice 的各个Proto接口测试。
5. 实现全自动一键式获取与编译Proto文件。

与同类工具相比，本工具有以下明显缺陷：

1. 暂不支持除unary外另外三种流式传输方式。
2. 不支持
3. 不支持包含以上及其他测试代码功能性而使用的用例。


## 本工具介绍与使用
### 使用Postman测试Grpc
Postman的Grpc功能已经相当成熟，与其提供的http测试服务相似，Postaman有两种方式去测试Grpc的功能。

一种是通过提供Proto文件进行解析，另一种通过Server Reflection进行解析。对于服务端，这两种都可以用来让Postman生成对应的样例Message，而对于Proto文件的解析，甚至可以直接模拟对应的输出响应。

### 代码目录架构
1. beta 测试新功能
2. controller 提供http接口
3. proto 暂时存放传入的proto文件
4. storage 数据库
5. stringbody 生成服务的文本模板
6. utils 解析proto并生成服务的数据结构和方法

### 提供接口

本工具需下载 Postman配合使用

本地配置好环境后，启动项目，提供接口进行文件解析，有如下两种接口

```java
@PostMapping("/gitlab")
public void gitlab(@NotNull String projectName, String nameSpace, String protoRelativeRoute, String protoName, String branch) 

@PostMapping("/local")
public void local(@NotNull String protoRoute, String protoName)
```

两种接口用于解析Proto并生成对应服务，分别对应服务端和本地的接口，选择Proto语句中Server所在的文件即可。


### 生成服务

随后重新使用maven编译Proto文件后重启服务，使用Postman调用该服务所配置的Grpc端口，可以见到两组服务。

1. 根据Proto文件构建的Mock服务，用来给Grpc客户端的消息进行响应，本组服务默认被动调用。返回值默认为空，若通过下述服务2手动构建数据则可以返回指定值。
2. 命名以“GetData”结尾的服务，将构建的Mock服务所需的消息和响应同时作为该服务的输入，随即存储到数据库中，以供第一组Mock服务调用。

### 数据库使用
本工具使用MongoDB进行文件存储，使用key-value结构。
目前Key为使用String.format对Request进行字符串化操作的结果，因Map的特性，相同Request的结果会进行覆盖。(待改善)

### 本地联调方法
修改本地客户端定义的服务端端口为本Mock服务，启动本地服务，并进行调用，如若涉及到调用本工具提供的Mock服务，则可以通过日志获取传递到Mock服务对应的输入，使用GetData服务构建对应数据以使下一次调用时获取所需返回值。

修改内容为下述
1.   grpc:
     id-management:
    - host: localhost
      port: 1555
2. SPRING_KAFKA_BOOTSTRAP_SERVERS_PAYMENT: kafka-migrate-aps1-1.base-service.svc.cluster.local:9092,kafka-migrate-aps1-2.base-service.svc.cluster.local:9092,kafka-migrate-aps1-3.base-service.svc.cluster.local:9092

    改为SPRING_KAFKA_BOOTSTRAP_SERVERS_PAYMENT: 172.29.89.69:9092
3. ID_MANAGEMENT_MODE: 从mock改为grpc

### 使用注意
1. 预留中间路径为 ```tauc\grpcapiinterfacemanagetool\proto```，使用后会删除，请勿取同名路径
2. Proto之间的依赖关系目前只支持同文件夹下的依赖


## 本工具后续改进
### 改动与改善
2. 后续可使用grpc解析的官方工具获取内部信息而非编写解析。
4. 使用多个接口呈现不同的Proto对应的服务，或者相同proto在不同分支下的服务，与此同时提供对数据库的删除。
5. 数据库操作的完善，避免不同请求调用相同参数，因此使用当前的判断不够。
7. 重启服务。

### 同类工具的其他feature（不确定是否有对应需求）
* Supports unary, client streaming, server streaming, and bidi streaming rpcs.
* Parses proto files at runtime to discover services. Supports pretty-printing discovered services.
* Supports authentication via oauth.
* Accepts request protos through stdin and can output responses to stdout to allow chaining.
* Supports plain text connections as well as TLS.
* Supports passing custom grpc metadata over the command line.
* Supports all protobuf well-known-types, including fields of type "Any".


### 自行修改文件
1. 目前关于Proto解析仅在tauc及agg项目下进行检验，可能会存在考虑不周到的情况，可针对生成的中间文件进行手动调整。 
2. `com/tplink/tauc/grpcapiinterfacemanagetool/storage`路径下存储数据库所需方法，可根据实际情况进行更改。

重名问题，rpc包含google，





