# File

## 目录创建与删除

```JAVA
public static void CreateRoute(String route){
    File file = new File(route);
    if (!file.exists())
        file.mkdirs();
}


public static boolean DeleteRoute(File directoryToBeDeleted) {
    File[] allContents = directoryToBeDeleted.listFiles();
    if (allContents != null) {
        for (File file : allContents) {
            DeleteRoute(file);
        }
    }
    return directoryToBeDeleted.delete();
}
```

## 文件读写

```JAVA
    // 文件读写函数
    public static void ReadFile(String target, List<String> strings) throws IOException {
        FileReader fileReader = new FileReader(target);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String strline;
        do {
            strline = bufferedReader.readLine();
            if (strline == null)
                break;
            strings.add(strline);
        } while (true);
        fileReader.close();
        bufferedReader.close();
    }

    public static void ReadFile(String Route, String fileName, List<String> strings) throws IOException {
        ReadFile(Route+"\\"+fileName, strings);
    }

    public static void WriteFile(String Route, String fileName, List<String> strings) throws IOException {
        WriteFile(Route+"\\"+fileName, strings);
    }
    public static void WriteFile(String target, List<String> strings) throws IOException {
        FileWriter fileWriter = new FileWriter(target);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        for (String strline : strings) {
            bufferedWriter.write(strline);
            bufferedWriter.newLine();
        }

        bufferedWriter.close();
        fileWriter.close();
    }
```



# Path

## classpath

classpath其实就是一个路径而已，我们经常在spring的配置文件中这样写：

```java
<property name="configLocation" value="classpath:mybatis/SqlMapConfig.xml" />
```

这样配置完之后spring就知道mybatis配置文件所在的地方。


**classpath指向的就是打war包之后的classes的位置**。而classes文件夹下就是我们原项目的java文件和resources文件夹里面的内容。

所以上面的代码的意思就是在编译后的classes文件中找mybatis/SqlMapConfig.xml文件。

### 总结：

------

在编译打包后的项目中，根目录是`META-INF`和`WEB-INF` 。这个时候，我们可以看到classes这个文件夹，它就是我们要找的classpath。

`classpath:mybatis/SqlMapConfig.xml` 中，classpath就是指`WEB-INF/classes/`这个目录的路径。需要声明的一点是，使用`classpath:`这种前缀，**就只能代表一个文件**。

而另一种写法，`classpath*:**/mapper/mapping/*Mapper.xml`，使用`classpath*:`这种前缀，**则可以代表多个匹配的文件**；`**/mapper/mapping/*Mapper.xml`，双星号`**`表示在任意目录下，也就是说在`WEB-INF/classes/`下任意层的目录，只要符合后面的文件路径，都会被作为资源文件找到。



# Others

String filePicPath = "E:\\allFile\\modelFileExcelImage\\11.png";

filePicPath = filePicPath.replaceAll("\\\\","/");

得到地址E:/allFile/modelFileExcelImage/11.png

