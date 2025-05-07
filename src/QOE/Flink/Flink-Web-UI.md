



![image-20250425141820468](/Users/sunhao/Documents/IdeaProjects/typora/src/QOE/Qoe业务代码/img/posts/Flink-Web-UI.asserts/image-20250425141820468.png)







# Submit New Job

如何执行权限隔离

| **方案**                                  | **拦截点**                          | **保护范围**                            | **优点**                                                     | **缺点**                                 |
| ----------------------------------------- | ----------------------------------- | --------------------------------------- | ------------------------------------------------------------ | ---------------------------------------- |
| **1. 禁 UI**web.submit.enable=false       | Flink Web UI 按钮                   | 只屏蔽 Web UI “Submit new job”          | 配置极其简单、立刻生效；防止非技术用户误点                   | 无法阻止 CLI/REST/API 提交；安全边界薄弱 |
| **2. 强制 REST mTLS + 反代**              | JobManager 的 REST 接口             | REST API + Web UI                       | 真正做到了“有证书/有凭证才能调用”；可接公司统一认证（LDAP/API Key） | 证书/反代配置复杂，需要运维配合          |
| **3. 资源管理层 ACL**YARN ACL 或 K8s RBAC | 底层资源管理（YARN 队列或 K8s API） | 所有提交渠道（CLI、REST、Web UI、脚本） | 最彻底：无论用哪种方式都必须有底层权限                       | 依赖底层集群（YARN/K8s）配置；灵活性略差 |



