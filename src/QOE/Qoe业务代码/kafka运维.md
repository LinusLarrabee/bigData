```
查看topic

# 进入容器
docker exec -it kafka-1 /bin/bash

# 从头开始读取 topic “my_topic” 的前 10 条消息
kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic radio.by.wireless \
  --from-beginning \
  --max-messages 10
  
  
kafka-topics.sh \
  --bootstrap-server localhost:9092 \
  --list
```

