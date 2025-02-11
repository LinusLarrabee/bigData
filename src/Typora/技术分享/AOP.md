# AOP设计

**业务场景：**

需要对众多服务均需要进行相同或少数几类操作。

（比如在kafka结束后都要进行逻辑处理，随后进行数据库读写操作）

**切片优点：**

解耦，灵活，非侵入式执行。

**设计思路：**

类似古筝行动，思考在函数进行到这个位置你想要做什么，然后问GPT就行。

![image-20240720122219215](img/posts/AOP.asserts/image-20240720122219215.png)

![image-20240720122210659](img/posts/AOP.asserts/image-20240720122210659.png)

# AOP理论

## 概念

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



## Aspects：

被定义为一个普通的类，使用注解或execution或其他方式关注多个类和模块的关注点。AspectJ和Spring AOP是两个实现。

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



## Advices通知：

Before/After/Around

- **Before Advice**: 在连接点之前执行的代码。
- After Advice: 在连接点之后执行的代码。它可以进一步分为：
  - **After Returning Advice**: 在连接点正常完成后执行。
  - **After Throwing Advice**: 在连接点抛出异常时执行。
- **Around Advice**: 在连接点前后都可以执行的代码。它可以决定是否继续执行连接点或直接返回自己的返回值。

![image-20240720122518208](img/posts/AOP.asserts/image-20240720122518208.png)

## Pointcuts切点：

允许开发者精确指定哪些方法（连接点）应该被通知（Advice）所拦截。

TAUC使用Spring AOP，在切点建立的时候主要使用execution 和 annotation。



# 上下文

### JoinPoint使用

JoinPoint是指程序执行过程中的某个特定点，提供了一组方法使得advice能访问到当前切点的各种信息

getSignature：方法名，声明类型等

getArgs：获取参数

proceed：继续执行原方法，否则可以返回默认参数。

应用场景：注解在多个函数上使用时可以进行区分。

![image-20240720122607590](img/posts/AOP.asserts/image-20240720122607590.png)

Signature是 Java 反射 API 中的一个接口，用于描述方法或构造函数的签名信息。Spring AOP 中的 JoinPoint 接口继承了反射 API 中的 Member 接口，因此可以通过 JoinPoint 对象获取到方法或构造函数的 Signature 对象。

![image-20240720122620959](img/posts/AOP.asserts/image-20240720122620959.png)

![image-20240720122624713](img/posts/AOP.asserts/image-20240720122624713.png)



# 使用案例

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
        System.out.println("Executing " + className + "." + methodName + "()");
    }
}

```





