### 历史节点

出现场景：个人账号建立emr成功写入到hive表，关掉之后新建emr集群无法写入。

```shell
: java.net.NoRouteToHostException: No Route to Host from  ip-172-31-5-253.ec2.internal/172.31.5.253 to ip-172-31-2-251.ec2.internal:8020 failed on socket timeout exception: java.net.NoRouteToHostException: No route to host; For more details see:  http://wiki.apache.org/hadoop/NoRouteToHost
```

当前建立的emr集群中primary-core-task三个节点都没有172-31-2-251，确定这是一个历史使用过的primary节点，如何消除这个影响？

目前能解决的方案是换区，也可能修改vpc和子网能解决。





### Can not create a Path from an empty string

在公司电脑运行spark写入hive操作出现。

跟上个一样，更改新的subnet即可解决，旧的问题出现在us-east-1d，可能是安全组规则太多...

改成1b就好了

感觉这类问题甚至可能换个干净的安全组就能解决。





### Presto连不上 Superset？

可以尝试的方法但不确定是哪个解决的

检查是否使用的是emr的地址和ec2的地址

最好还是使用一个subnet吧，虽然我感觉影响不大

安全组配置ipv6监控？



