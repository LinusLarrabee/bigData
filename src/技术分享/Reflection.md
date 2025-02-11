## Java反射

应用场景：到处理时才知道需要处理的类。

1. Java工具实现的反射：AOP继承了反射接口；
2. 没有写ToString的变量的读取具体内容。使用Apache Commons Lang的ReflectionToStringBuilder
3. 代码/文档自动生成：Mapper层
4. 代码功能增强。Proposal：平台组提供的kafka提供的接口只能使用event值，可以在处理流程中拿到consumerRecord放到threadLocal中然后调用接口的时候一并处理。

```JAVA
package com.tplink.cdd.tpuc.wifimanagement.infra.migration;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * Description of this file
 *
 * @author sunhao
 * @version 1.0
 * @since 2024/3/20
 */

public class ReflectionUtil {
    public static String smartToString(Object object) {
        if (object == null) {
            return "null";
        }
        if (isToStringMethodOverridden(object.getClass())) {
            return object.toString();
        } else {
            try {
                return ReflectionToStringBuilder.toString(object, ToStringStyle.SHORT_PREFIX_STYLE, false, false, true, Object.class);
            } catch (Exception e) {
                return "Exception in toString(): " + e.getMessage();
            }
        }
    }

    private static boolean isToStringMethodOverridden(Class<?> clazz) {
        try {
            Method toStringMethod = clazz.getMethod("toString");
            return !toStringMethod.getDeclaringClass().equals(Object.class);
        } catch (NoSuchMethodException e) {
            // Should not happen
            return false;
        }
    }
}
```





## 案例：Mapper层转化

编译还是运行时使用反射？

目的：需要将DTO之间进行转化。

````java
@AllArgsConstructor
@Setter
@Getter
public class User {
    private String firstName;
    private String lastName;
    private int age;
    private Date registrationDate;
}

@AllArgsConstructor
@Setter
@Getter
public class User {
    private String firstName;
    private String lastName;
    private int age;
    private Date registrationDate;
}
````

在代码运行时使用反射会影响性能，Mapper映射可以在运行时动态指明对象，也可以在编译时进行每一个实现。

比较老式的做法是使用反射进行通用的类型转化，代表方法为BeanUtils。缺点是反射造成的性能影响。



```java
import java.lang.reflect.Field;

public class ReflectionMapper {
    public static void map(Object source, Object target) {
        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();

        Field[] fields = sourceClass.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(source);

                Field targetField = targetClass.getDeclaredField(field.getName());
                targetField.setAccessible(true);
                targetField.set(target, value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
```

使用MapStruct，类似lombok在编译时自动生成代码，减少性能影响。

原理：在编译阶段使用反射搜索使用@Mapper注解的内容。进行解析然后生成所需的实例mapperImpl并和其他文件一起编译，此时处理能修改源码。

优点：通过映射关系部分地构建防腐层，能处理简单名称改变和类型转化的问题。

注意：同时使用mapstruct和Lombok需要配置两者的先后顺序。



```java
@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mappings({
            @Mapping(target = "fullName", expression = "java(user.getFirstName() + ' ' + user.getLastName())"),
            @Mapping(target = "registrationDateString", source = "registrationDate", dateFormat = "yyyy-MM-dd")
    })
    UserDTO userToUserDTO(User user);

    // Helper method to format dates, if needed elsewhere
    default String formatDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static void main(String[] args) {
        User user = new User("John", "Doe", 30, new Date());
        UserDTO userDTO = UserMapper.INSTANCE.userToUserDTO(user);
        System.out.println(userDTO.getFullName()); // 输出 John Doe
        System.out.println(userDTO.getRegistrationDateString()); // 输出日期字符串
    }
}
```


Pom更新
```

<dependency>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct</artifactId>
    <version>1.5.2.Final</version>
</dependency>
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.8.1</version>
    <configuration>
        <source>1.8</source>
        <target>1.8</target>
        <annotationProcessorPaths>
            <path>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <version>${lombok.version}</version>
            </path>
            <path>
                <groupId>org.mapstruct</groupId>
                <artifactId>mapstruct-processor</artifactId>
                <version>1.5.2.Final</version>
            </path>
        </annotationProcessorPaths>
    </configuration>
</plugin>

```

## 案例发散：自动文档生成

Mapper反射实现的思想是通过反射拿到两个类，然后进行映射。

也可以通过反射拿到一个类的信息，AOP实现其中的代码解释器部分，然后根据需求进行自动生成代码文件或者说明文档。

有了类和方法的数据，可以绘制这些类和方法的调用关系。

```JAVA
import java.lang.reflect.Method;
import java.util.Arrays;

public class ClassInspector {
    public static void inspectClass(String className) {
        try {
            Class<?> cls = Class.forName(className);
            System.out.println("Class: " + cls.getName());
            Method[] methods = cls.getDeclaredMethods();

            Arrays.stream(methods).forEach(method -> {
                System.out.println("Method: " + method.getName());
                Class<?>[] paramTypes = method.getParameterTypes();
                for (Class<?> paramType : paramTypes) {
                    System.out.println("Param type: " + paramType.getName());
                }
                Class<?> returnType = method.getReturnType();
                System.out.println("Return type: " + returnType.getName());
            });

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
```

