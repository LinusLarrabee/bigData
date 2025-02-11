## AOP-最小侵入性

### 概念

1. Aspects：Aspect代表面向切面编程（AOP）的核心概念之一，是将横切关注点（cross-cutting concerns）从业务逻辑中分离出来的方法或模块。一个Aspect可以定义为一个普通的类，但它被注解或以其他方式指定为包含横切关注点的代码（如日志记录、事务管理、安全检查等），这些关注点通常跨越多个类和模块。AspectJ和Spring AOP是Java中两个流行的AOP实现。
2. Advices通知：Before/After/Around
3. Pointcuts切点：允许开发者精确指定哪些方法（连接点）应该被通知（Advice）所拦截。

```java
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



### Spring PointCuts

#### 1. execution

- 匹配特定类的所有方法执行：

  ```
  execution(* com.example.service.UserService.*(..))
  ```

  这个表达式匹配`UserService`类中所有方法的执行。

- 匹配返回类型为String的所有方法：

  ```
  execution(String *.*(..))
  ```

  这个表达式匹配任何返回类型为String的方法。

- 匹配任何以"find"开始的方法名，且参数为`String`和`int`：

  ```
  execution(* find*(String, int))
  ```

  这个表达式匹配任何类中，以"find"开始，参数类型为`String`和`int`的方法。

#### 2. @Annotation

匹配方法或类上标有特定注解的执行。

- 方法级别的注解匹配：

```java
@Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
private void anyGetOperation() {}
```

- 类级别的注解匹配：

```java
@Pointcut("@within(org.springframework.stereotype.Repository)")
private void inRepositoryLayer() {}
```





### AspectJ PointCuts

比如直接匹配方法调用、字段访问或类初始化等，就需要使用 AspectJ 提供的完整 AOP 支持了。

#### 1. handler异常处理

- **handler**：匹配异常处理的执行点。这允许开发者在异常被捕获的地方织入通知，比如进行异常日志记录。

#### 2. 方法调用（call）

- 匹配特定方法的调用：

  ```
  call(void com.example.service.UserService.updateUser(*))
  ```

  这个表达式匹配 UserService 中 updateUser 方法的调用。

#### 3. 对象初始化（initialization）

- 匹配特定构造函数的调用：

  ```
  initialization(com.example.service.UserService.new(..))
  ```

  这个表达式匹配 UserService 的任何构造函数的初始化。

#### 4. 对象实例化（preinitialization）

- 匹配对象实例化过程中的构造函数调用：

  ```
  preinitialization(com.example.service.UserService.new(..))
  ```

  这个表达式在 UserService 对象完全初始化之前，匹配其构造函数的调用。

#### 5. 类初始化（staticinitialization）

- 匹配类初始化时的静态块：

  ```
  staticinitialization(com.example.service.UserService)
  ```

  这个表达式匹配 UserService 类静态初始化块的执行。

#### 6. 对象获取（get）和对象修改（set）

- 匹配特定字段的读取：

  ```
  get(* com.example.service.UserService.name)
  ```

  这个表达式匹配`UserService`中`name`字段的所有读取操作。

- 匹配任何类中任何字段的修改：

  ```
  set(* *.*)
  ```

  这个表达式匹配任何对象中任何字段的修改操作。



### Around

- **Before Advice**: 在连接点之前执行的代码。
- After Advice: 在连接点之后执行的代码。它可以进一步分为：
  - **After Returning Advice**: 在连接点正常完成后执行。
  - **After Throwing Advice**: 在连接点抛出异常时执行。
- **Around Advice**: 在连接点前后都可以执行的代码。它可以决定是否继续执行连接点或直接返回自己的返回值。

执行语句

before通知示例
before通知在连接点执行之前运行，但它不能阻止执行流继续到连接点（除非它抛出一个异常）。

java
Copy code
@Before("execution(* com.example.service.*.*(..))")
public void logMethodEntry(JoinPoint joinPoint) {
    String methodName = joinPoint.getSignature().getName();
    System.out.println("Before method: " + methodName);
}
在这个示例中，logMethodEntry方法会在com.example.service包下任何类的任何方法执行之前被调用，打印出方法名。

after-returning通知示例
after-returning通知在方法成功执行后运行，允许访问方法的返回值。

java
Copy code
@AfterReturning(pointcut = "execution(* com.example.service.*.*(..))", returning = "result")
public void logMethodExit(JoinPoint joinPoint, Object result) {
    String methodName = joinPoint.getSignature().getName();
    System.out.println("After method: " + methodName + ", return: " + result);
}
这个示例在方法成功返回后执行，打印出方法名和返回值。

after-throwing通知示例
after-throwing通知在方法抛出异常退出时执行。

java
Copy code
@AfterThrowing(pointcut = "execution(* com.example.service.*.*(..))", throwing = "ex")
public void logException(JoinPoint joinPoint, Exception ex) {
    String methodName = joinPoint.getSignature().getName();
    System.out.println("Exception in method: " + methodName + ", exception: " + ex.getMessage());
}
当方法抛出异常时，这个示例会被执行，打印出方法名和异常信息。

around通知示例
around通知包裹一个连接点的执行，可以在方法调用之前和之后执行，并且能决定是否继续执行连接点或直接返回自己的返回值。





读取入参和出参

```java
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogParametersAspect {

    // 定义切点，这里指定使用了@LogParameters注解的方法
    @Pointcut("@annotation(LogParameters)")
    public void annotatedWithLogParameters() {}

    // 在方法执行前打印入参
    @Before("annotatedWithLogParameters()")
    public void logMethodEntry(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        System.out.println("Entering in Method :  " + methodName + " with arguments : " + Arrays.toString(args));
    }

    // 在方法执行后打印出参
    @AfterReturning(pointcut = "annotatedWithLogParameters()", returning = "result")
    public void logMethodExit(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("Method :  " + methodName + " executed successfully with result : " + result);
    }
}

```

读取入参并跳过方法执行

```java
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SkipMethodAspect {

    @Around("@annotation(SkipMethodUnderCondition)")
    public Object skipMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取方法入参，可以基于入参内容决定是否跳过方法执行
        Object[] args = joinPoint.getArgs();
        System.out.println("Arguments : " + Arrays.toString(args));
        
        // 假设我们跳过方法执行的条件是第一个参数为特定值
        if (args != null && args.length > 0 && "skip".equals(args[0])) {
            System.out.println("Skipping method execution.");
            return "Custom Result"; // 返回一个自定义结果
        }
        
        // 如果不满足跳过条件，则正常执行方法
        return joinPoint.proceed();
    }
}

```



获取方法名称

```java
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("@annotation(Loggable)")
    public void logMethodAccess(JoinPoint joinPoint) {
        // 获取方法名称
        String methodName = joinPoint.getSignature().getName();

        // 获取简单类名，不包括包名
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();

        // 打印日志
        System.out.println("Executing " + className + "" + methodName + "()");
    }
}

```







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



### gRPC传递

gRPC使用元数据（Metadata）来传递类似的信息。元数据类似于HTTP的头部，可以在客户端和服务端之间传递键值对信息。

1. 在客户端，创建并发送包含MDC信息的元数据。

```java
return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(channel.newCall(methodDescriptor, newCallOptions)) {
    @Override
    public void start(Listener<RespT> respTListener, Metadata headers) {
        String reqID = MDC.get("migration-unique-id");
        if (reqID != null){
            Metadata.Key<String> userIdKey = Metadata.Key.of("migration-unique-id", Metadata.ASCII_STRING_MARSHALLER);
            headers.put(userIdKey, reqID);
        }
        super.start(respTListener, headers);
    }
};
```

2. 在服务端，读取元数据并设置到MDC中。

```java
public class MyServerInterceptor implements ServerInterceptor {
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        String requestId = headers.get(Metadata.Key.of("requestId", Metadata.ASCII_STRING_MARSHALLER));
        MDC.put("requestId", requestId);
        
        try {
            return next.startCall(call, headers);
        } finally {
            MDC.clear(); // Ensure MDC is cleared after the call
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





## Reflection

场景：如果args代表的对象未使用toString注解，则无法在过程中展示全部内容而是只能返回一个引用

```java
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.lang.reflect.Method;
import java.util.Set;

public class ReflectionUtil {

    // 检测对象是否重写了toString方法
    public static String smartToString(Object object) {
        if (object == null) {
            return "null"; // 直接处理null值
        }
        if (isToStringMethodOverridden(object.getClass())) {
            return object.toString(); // 使用对象的toString方法
        } else {
            try {
                // 如果没有重写toString，使用ReflectionToStringBuilder生成字符串表示
                return ReflectionToStringBuilder.toString(object, ToStringStyle.SHORT_PREFIX_STYLE, false, false, true, Object.class);
            } catch (Exception e) {
                // 异常处理，避免因反射失败而导致的问题
                return "Exception in toString(): " + e.getMessage();
            }
        }
    }

    // 检查类是否重写了Object的toString方法
    private static boolean isToStringMethodOverridden(Class<?> clazz) {
        try {
            Method toStringMethod = clazz.getMethod("toString");
            return !toStringMethod.getDeclaringClass().equals(Object.class);
        } catch (NoSuchMethodException e) {
            // 这种情况不应该发生，因为所有对象都继承自Object，应该有toString方法
            return false;
        }
    }
}

```





## Uuid 以及转化生成

