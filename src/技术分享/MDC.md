## MDC

Mapped Diagnostic Context

### TAUC使用

不同微服务之间使用一套Reqid，用于日志系统。



### 基本使用方法

1. **设置MDC**：可以通过`MDC.put(key, value)`方法将键值对放入当前线程的MDC中。这允许你将特定的数据（如用户ID、请求ID等）与当前线程关联起来。
2. **从MDC获取数据**：可以通过`MDC.get(key)`方法根据键从MDC中获取值。
3. **清理MDC**：使用完MDC后，应通过`MDC.clear()`或`MDC.remove(key)`方法来清理MDC中的数据，以避免内存泄漏。

### 切片读取

```java
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.slf4j.MDC;

@Aspect
public class LoggingAspect {

    @Pointcut("execution(* com.yourpackage..*.*(..))") // 定义切点，指定哪些方法会被拦截
    public void applicationPackagePointcut() {
        // 方法本身作为切点的标记，无需实现逻辑
    }

    @Around("applicationPackagePointcut()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            MDC.put("key", "value"); // 在方法执行前，往MDC中添加信息
            return joinPoint.proceed(); // 继续执行原方法
        } finally {
            MDC.remove("key"); // 在方法执行后，清理MDC中的信息
        }
    }
}

```

### 使用注意

1. 多线程或者服务之间需要手动传递MDC上下文，因其具体实现依赖ThreadLocal

2. 应该保证即便出现异常也能清除MDC

```java
try {
    MDC.put("key", "value");
    // 执行业务逻辑...
} finally {
    MDC.clear();
}
```



## Uuid 以及转化生成

客户端实现
创建一个ClientInterceptor：这个拦截器会向所有的gRPC调用添加指定的Metadata。

```java
import io.grpc.*;

public class MdcClientInterceptor implements ClientInterceptor {
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> method,
            CallOptions callOptions,
            Channel next) {
        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(
                next.newCall(method, callOptions)) {

            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                // 假设我们从MDC获取用户ID
                String userId = MDC.get("userId");
                if (userId != null) {
                    Metadata.Key<String> userIdKey = Metadata.Key.of("userId", Metadata.ASCII_STRING_MARSHALLER);
                    headers.put(userIdKey, userId);
                }
                super.start(responseListener, headers);
            }
        };
    }
}
```

配置ManagedChannel以使用这个拦截器：

```java
ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
        .intercept(new MdcClientInterceptor())
        .usePlaintext()
        .build();
```

创建和使用blockingStub：

```java
MyServiceGrpc.MyServiceBlockingStub blockingStub = MyServiceGrpc.newBlockingStub(channel);
// 现在，当使用blockingStub发起调用时，MDC中的用户ID将被添加到每个请求的Metadata中

```

服务器端实现
在服务器端，你需要在gRPC服务器中添加一个ServerInterceptor来读取传入请求的Metadata并相应地处理它。

创建ServerInterceptor：

```java
public class MdcServerInterceptor implements ServerInterceptor {
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call, final Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {
        String userId = headers.get(Metadata.Key.of("userId", Metadata.ASCII_STRING_MARSHALLER));
        if (userId != null) {
            // 将用户ID设置到MDC中
            MDC.put("userId", userId);
        }

        return next.startCall(call, headers);
    }
}
```


在gRPC服务器配置中添加这个拦截器：

```java
Server server = ServerBuilder.forPort(50051)
        .addService(new MyServiceImpl())
        .intercept(new MdcServerInterceptor())
        .build()
        .start();
```

这样，客户端的每个请求都会携带MDC中的userId，服务器端可以从Metadata中读取这个userId并使用它，比如记录日志。

注意
确保在客户端和服务器端正确处理Metadata键的名称，它们是区分大小写的。
在服务器端拦截器中，记得在请求处理完成后清除MDC中的数据，以避免内存泄漏或错误的数据传递。
这种方式适用于需要在服务调用链上传递上下文信息的场景，比如日志跟踪、用户认证等。