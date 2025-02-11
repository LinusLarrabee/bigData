# Proto的MongoDB存储方案

## Proto转成可存储文本

该功能选择的核心问题在于可批量化执行

首先使用的方法是通用存储的Json，当然Json的转化方案有很多，目前仅测试过Gson的方式。

### 使用Gson

如下函数使用Gson：

```JAVA
import com.google.gson.Gson;
Gson gson = new Gson();
gson.fromJson();
gson.toJson();
```

该方法存在的问题是没办法处理Map的情况，下述链接描述了一个类似的情况，Gson无法对循环依赖的问题提供支持。

[gson序列化抛出StackOverflowError异常_google.gson bind栈溢出_浅醉樱花雨的博客-CSDN博客](https://blog.csdn.net/u013314786/article/details/102586026)

另外对于相同内容，Gson生成的Json文件结果在

关于Json转化方案可参考

[Protobuf与Json的相互转化 - Boblim - 博客园 (cnblogs.com)](https://www.cnblogs.com/fnlingnzb-learner/p/13434849.html)

本文认为`com.google.protobuf/protobuf-java-util` 和 Gson 可用于处理Protobuf转Json的问题。难点在于 Map，Any 与 Oneof 的转化。

### 使用Protobuf函数

Protobuf库中提供的Message自带函数操作：toByteString, toByteArray, toString, toBuilder

因为MongoDB使用Bson进行存储，因此选用ByteArray直接存放Message。

```java
public void getFreeLicenseTypeServiceGetData(getFreeLicenseTypeServiceAll resp, StreamObserver< getFreeLicenseTypeServiceOK > responseObserver) {
    logger.info(String.format(" getFreeLicenseTypeServiceGetData方法调用的请求参数信息\\n: getFreeLicenseTypeServiceReq = {%s}, getFreeLicenseTypeServiceResp = {%s}", resp.getGetFreeLicenseTypeServiceReq(), resp.getGetFreeLicenseTypeServiceResp()));
    protoItemMethod.createProtoItems("free_license_develop","FreeLicenseService","getFreeLicenseTypeService",String.format("%s",resp.getGetFreeLicenseTypeServiceReq()),resp.getGetFreeLicenseTypeServiceReq().toByteArray(),resp.getGetFreeLicenseTypeServiceResp().toByteArray());
    getFreeLicenseTypeServiceOK reply = getFreeLicenseTypeServiceOK.newBuilder().setOK("OK").build();
    responseObserver.onNext(reply);
    responseObserver.onCompleted();
}

public void getFreeLicenseTypeService(free_license_develop_GrpcGetFreeLicenseTypeReq req, StreamObserver<free_license_develop_GrpcGetFreeLicenseTypeResp> responseObserver) {
    logger.info(String.format("getFreeLicenseTypeService方法调用的请求参数信息:\n req={%s}", req));
    byte[] resp = protoItemMethod.getProtoById(String.format("%s",req));
    free_license_develop_GrpcGetFreeLicenseTypeResp reply;
    if (resp==null)
        reply = free_license_develop_GrpcGetFreeLicenseTypeResp.getDefaultInstance();
    else {
        try {
            reply = free_license_develop_GrpcGetFreeLicenseTypeResp.parseFrom(resp);
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }
    responseObserver.onNext(reply);
    responseObserver.onCompleted();
}
```

上述为 FreeLicense 的一个例子，在 GetData 函数直接将 Message 通过 toByteArray 存放到数据库中，而在 Mock 部分，使用 parseFrom 从 ByteArray 获取数据的部分则需要抛出异常。

关于开篇可批量化执行生成的部分，在此处则指代对于通过内部消化异常问题不传递到下一层，因为Proto编译出来的代码不会默认新增异常处理。





## Utils

### Message

Message可以直接作为变量进行存放，而不用使用对应Proto变量名，如下所述，user1 和 message是相同内容。

```java
        UserProto.User user1 = null;
        String jsonObject = null;
        try {
            //反序列化
            user1 = UserProto.User.parseFrom(s);
            //proto 转 json
            jsonObject = ProtoJsonUtil.toJson(user1);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        //将 Json 数据转 proto 对象
        try {
            Message message = ProtoJsonUtil.toObject(UserProto.User.newBuilder(), jsonObject);
            System.out.println("json 转 protobuf 对象：\n " + printToUnicodeString(message));
        } catch (IOException e) {
            e.printStackTrace();
        }
```



### Json中文支持

```java
/**
 * 处理反序列化时中文出现的八进制问题（属性值为中文时可能会出现这样的八进制\346\223\215\344\275\234\345\221\230）
 * 可直接使用 protobuf 自带的 TextFormat.printToUnicodeString(message) 方法，但是这个方法过时了，直接从这个方法内部拿出来使用就可以了
 *
 * @param message 转换的 protobuf 对象
 * @return string
 */
public static String printToUnicodeString(MessageOrBuilder message) {
    return TextFormat.printer().escapingNonAscii(false).printToString(message);
}

```

# MongoDB存储

早期使用MongoDB的原因是其更适合文件存储，以及为了接触多种数据库使用。

使用JPA的初始构建MongoDB与SpringBoot，个人觉得JPA比Mybatis调用逻辑更为严谨。

[Spring Boot Integration With MongoDB Tutorial | MongoDB](https://www.mongodb.com/compatibility/spring-boot)

语法参考

[Spring Data MongoDB @Query Annotation (concretepage.com)](https://www.concretepage.com/spring-5/spring-data-mongodb-query#Technologies)

