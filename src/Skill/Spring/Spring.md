本质上`@Controller`、`@Service`、`@Repository`、`@Component`本质都是`@Component`，即Spring容器中的一个组件。

所以在Spring Boot 中只有被`@Controller`和`@RequestMapping`注解时Spring才会去扫描内部的`@RequestMapping`

MVC后端



### 数据层

<img src="assets/v2-24e3ed681c02b6434681719753c53b40_720w.webp" alt="img" style="zoom:50%;" />

[一篇文章讲清楚VO，BO，PO，DO，DTO的区别 - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/102389552)





## 注解

### Autowierd

可能存在扫描不到的情况

@ComponentScan



[Spring Framework Annotations - Spring Framework Guru](https://springframework.guru/spring-framework-annotations/)

