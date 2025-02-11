[Yaml map和list配置的使用详解-CSDN博客](https://blog.csdn.net/wk19920726/article/details/109073720)

```java
@Configuration
@ConfigurationProperties(prefix = "microservice.baseyaml")
public class BaseYamlConfig {
    private String strVaule;
    private int intValue;
    private float floatValue;
    private boolean booleanValue;
    private List<String> listStrValue;
    private Map<String, String> mapStrValue;
    //getter setter方法省略
}
```



```yaml
microservice:
  baseyaml:
    strVaule: strVaule
    intValue: 10
    floatValue: 8.5
    booleanValue: false
    listStrValue:
      -a
      -b
      -c
      -d
    mapStrValue:
      name: pharos
      age: 25
```

