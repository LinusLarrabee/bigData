baseline：相信apollo不会宕机。

在此基础上，apollo管理





测试点checklist - 仅考虑Tpuc-Eventfrontend的测试点

1. 业务问题：不能影响tpuc-EventFrontend功能
   1. 使用apollo配置新链路启动结束开关
   2. 使用线程池和异步操作确保不干扰正常程序执行：目前生产亚太QPS大小为100，解决问题的时间是100ms以内，因此线程池base配置到10即可。
2. 性能问题：需确认是否影响tpuc-EventFrontend性能（明确需要修改的性能配置）
   1. 当前生产亚太cpu使用在0.2以内
   2. 内存使用偏高，结合压测表现看是否需要扩容。
3. 性能问题：需确认增加的TPS对kafka集群的影响



压测实验：

Apollo配置到