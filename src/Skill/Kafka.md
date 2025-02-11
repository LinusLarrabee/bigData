启动kafka

```
zkServer

C:\kafka_2.12-3.6.0>.\bin\windows\kafka-server-start.bat .\config\server.properties
```

## Kafka生产者

### ProducerRecord

```JAVA
    /**
     * Creates a record with a specified timestamp to be sent to a specified topic and partition
     * 
     * @param topic The topic the record will be appended to
     * @param partition The partition to which the record should be sent
     * @param timestamp The timestamp of the record, in milliseconds since epoch. If null, the producer will assign
     *                  the timestamp using System.currentTimeMillis().
     * @param key The key that will be included in the record
     * @param value The record contents
     * @param headers the headers that will be included in the record
     */
    public ProducerRecord(String topic, Integer partition, Long timestamp, K key, V value, Iterable<Header> headers) {
    }

	public ProducerRecord(String topic, Integer partition, Long timestamp, K key, V value) {
        this(topic, partition, timestamp, key, value, null);
    }

    public ProducerRecord(String topic, Integer partition, K key, V value, Iterable<Header> headers) {
        this(topic, partition, null, key, value, headers);
    }
    
    public ProducerRecord(String topic, Integer partition, K key, V value) {
        this(topic, partition, null, key, value, null);
    }
    
    public ProducerRecord(String topic, K key, V value) {
        this(topic, null, null, key, value, null);
    }
    
    public ProducerRecord(String topic, V value) {
        this(topic, null, null, null, value, null);
    }
```



构造方式：最简化的调用仅需 topic 与 value

通过指定 partition 可以影响消息生产顺序性，而 key 在 kafka2.4 版本后在选择 sticky 分区时产生影响。

（好像不需要提前序列化

### send

是否带callback



### 生产顺序性



### 事务与幂等性







## Offset

Apache Kafka通过seek函数更新获取的位移



## Kafka 高并发方式

1. 提高分区数





## 偶发性问题

kafka未启动运行服务，启动后不消费

断电后offset





### Reference

[spring boot整合kafka(springBoot默认自动配置和自定义手动配置)_springboot kafka手写配置类-CSDN博客](https://blog.csdn.net/weixin_42669555/article/details/102678797)



[正确处理kafka多线程消费的姿势_kafkalistener 多线程-CSDN博客](https://blog.csdn.net/johnnyz1234/article/details/98318528)





[spring使用kafka的三种方式（listener、container、stream）_kafkamessagelistenercontainer-CSDN博客](https://blog.csdn.net/haiyan_qi/article/details/121066823)





[涨姿势了解一下kakfa消费位移可好？-CSDN博客](https://blog.csdn.net/weixin_39468305/article/details/106799061?spm=1001.2014.3001.5502)