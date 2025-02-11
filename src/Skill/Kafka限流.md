## 一. 限流工具

一、使用分布式工具 Redisson实现

单机模式下可以使用Google Guava实现令牌桶算法，分布式系统下则考虑使用Redisson实现

# 一、限流工具

单机模式下可以使用Google Guava实现令牌桶算法，在Controller层创建实例使用即可。

```
private RateLimiter limiter = RateLimiter.create(1);
@GetMapping("/upload-web-data")
public String Connector() {
    limiter.acquire();
    logger.info("ratelimiter do sth");
    return "SUCC";
}
```

分布式系统下则考虑使用Redisson实现



二、命令行配置资源配额

```powershell
bin/kafka-configs.sh  \
--zookeeper 192.168.1.115:2181 \
--alter --add-config 'producer_byte_rate=10485760,consumer_byte_rate=10485760' \
--entity-type clients --entity-name gh_pro_1
```

参考[Kafka限流实测_kafka 限流_guohan_solft的博客-CSDN博客](https://blog.csdn.net/guohan_solft/article/details/116157578)

[kafka的客户端限流（资源配额） - 掘金 (juejin.cn)](https://juejin.cn/post/7212455048004632634)





![image-20231129173220268](assets/image-20231129173220268.png)

[kafka查询offset&生产者offset计算&消费offset计算_kafka根据offset查数据-CSDN博客](https://blog.csdn.net/qq_51785096/article/details/128601678)