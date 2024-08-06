

## Supreset生产配置

Superset的日志指出，在生产上需要配置的内容为：Redis，WSGI，PostgreSql

| 组件          | 开发环境使用方式                     | 生产环境转化过程                                             |
| ------------- | ------------------------------------ | ------------------------------------------------------------ |
| Flask-Limiter | 使用内存作为速率限制的存储后端       | 配置并使用Redis等外部存储后端，以支持多服务器环境和持久化    |
| Werkzeug      | 使用内置的开发服务器，便于调试和测试 | 替换为生产级WSGI服务器（如Gunicorn）以处理高并发和高负载     |
| SQLite        | 作为元数据数据库，便于快速开发和测试 | 迁移到更强大的数据库系统（如PostgreSQL或MySQL）以提高性能和扩展性 |

其中redis和Gunicorn都是为了高可用，感觉节约资源可以不配。新建一个数据库存放账户信息和所有的chart图表信息，和前端的superset做分离还是有必要的。



## 手动创建账户

我能否通过superset的命令行创建一个admin



ChatGPT

可以通过 Superset 命令行工具（Superset CLI）来创建一个管理员用户。以下是详细步骤：

### 使用 Superset CLI 创建管理员用户

1. **进入 Superset 容器**

   首先，需要进入运行 Superset 的 Docker 容器：

   ```
   bash
   Copy code
   docker exec -it <superset_container_id> /bin/bash
   ```

   你可以使用 `docker ps` 命令来获取 Superset 容器的 ID：

   ```
   bash
   Copy code
   docker ps
   ```

2. **使用 `superset fab create-admin` 命令创建管理员用户**

   在容器内，使用以下命令创建一个管理员用户：

   ```
   bash
   Copy code
   superset fab create-admin \
     --username sunhao \
     --firstname Admin \
     --lastname User1 \
     --email sunhao@example.com \
     --password sunhao
   ```

   这个命令会创建一个名为 `admin` 的用户，并设置其电子邮件为 `admin@example.com`，密码为 `admin`。

3. **初始化数据库**

   如果这是你第一次运行 Superset，还需要初始化数据库：

   ```
   bash
   Copy code
   superset db upgrade
   ```

4. **启动 Superset**

   确保 Superset 正常启动：

   ```
   bash
   Copy code
   superset init
   superset run -p 8088 --with-threads --reload --debugger
   ```

## Postgres

使用原因：生产环境使用内置SQLite性能和安全太弱，需要引入其他数据库。

PostgreSql配置上经常配了初始密码就难以登陆，不配置初始密码又不确切密码格式，采用的是初始密码配一套A去库里拿数据，然后不设置初始密码B在库里写A的帐密信息。最后能得到一个能启动能配密码的系统。

注意，superset密码保存只保存加密后的密码，因此不具备账号恢复密码的可能性，只能删掉重来。

代码可参考文件，需要docker-compose.yml和superset_config.py，前者提供两个版本，分别对应带密码和不带密码的。



然后PSQL保存superset的所有信息，方便进行数据备份

```postgresql
INSERT INTO ab_user (id, username, password, email, first_name, last_name, active, created_on, changed_on)
VALUES (nextval('ab_user_id_seq'), 'admin', 'pbkdf2:sha256:600000$VJ1fRaQZo5Qg1iko$6d5bad7bfcc23ebe87031049e6f4c63da40c66c1d6138bd39876981a24f8f21b', 'admin@example.com', 'Admin', 'User', True, current_timestamp, current_timestamp);

INSERT INTO ab_user (id, username, password, email, first_name, last_name, active, created_on, changed_on)
VALUES (nextval('ab_user_id_seq'), 'admin', ' scrypt:32768:8:1$Coos3han96PQwBBf$3035cf6e89602df000728619b18e1d5e9e54d83874eacdf0bc2ed68b0d89966c09a80064ff10fc8f834848793ab119962e81048271d14b666c43802227678c5b ', 'admin@example.com', 'Admin', 'User', True, current_timestamp, current_timestamp);

INSERT INTO ab_user (username, password, email, first_name, last_name, active, created_on, changed_on)
VALUES ('admin1', ' scrypt:32768:8:1$kD3Dw7EfsqA1xo3N$715b7e3919226aca1efc57dd811cd22571c43107088d268e261b3dc2feb12cb7a56c7f2d9fd0981725db8bad91d054615be9ae4380cd652c04dc31b46434c1b3', 'admin1@example.com', 'Admin1', 'User1', True, current_timestamp, current_timestamp);

```





superset fab create-admin \
  --username admin1 \
  --firstname Admin1 \
  --lastname User1 \
  --email admin1@example.com \
  --password admin

### WSGI

```shell
superset-1  | 2024-07-08 14:02:25,210:INFO:werkzeug:WARNING: This is a development server. Do not use it in a production deployment. Use a production WSGI server instead.
```

在Dockerfile增加下述内容即可

```dockerfile
RUN pip install gunicorn
CMD ["gunicorn", "-b", "0.0.0.0:8088", "superset.app:create_app()"]
```



### Redis

```
superset-1  | 2024-07-08 14:02:17,044:INFO:root:Configured event logger of type <class 'superset.utils.log.DBEventLogger'>
superset-1  | /usr/local/lib/python3.10/site-packages/flask_limiter/extension.py:293: UserWarning: Using the in-memory storage for tracking rate limits as no storage was explicitly specified. This is not recommended for production use. See: https://flask-limiter.readthedocs.io#configuring-a-storage-backend for documentation about configuring the storage backend.
superset-1  |   warnings.warn(
```



### 后续部署

是否需要挂到K8s上实现高可用：考虑目前计划开放内部及产品使用，单机部署即可。



### 常用命令

```shell
#查看容器日志
docker logs -f superset
docker logs -f postgre

# 带环境参数启动容器
sudo docker-compose --env-file initial_var.env up -d

# 容器内部执行

docker exec -it test3-superset-1 superset db upgrade 
docker exec -it test3-superset-1 superset init
docker exec -it test3-postgres-1 psql -U superset -d superset
```





## 使用Gpt部署EC2 on Superset

### Docker创建

将下述文件保存，并执行docker创建

```bash
#!/bin/bash

# 更新包索引
sudo yum update -y

# 安装 Docker
sudo amazon-linux-extras install docker -y

# 启动 Docker 服务
sudo service docker start

# 将当前用户添加到 Docker 组
sudo usermod -aG docker $USER

# 提示用户重新登录以应用组更改
echo "Docker 安装完成。请重新登录以应用组更改。"

```



```bash
# 创建脚本文件
nano install_docker.sh

# 粘贴脚本内容并保存


# 运行脚本
./install_docker.sh

# 重新登录以应用组更改
exit

# 重新连接后查看 Docker 容器
docker ps

```





**You:**

我能否在aws ec2上容器化部署superset，还是说ec2本身就已经是容器化的

* * *

**ChatGPT:**

EC2本身并不是容器化的，但您可以在EC2实例上使用容器化技术（如Docker）来部署应用程序，包括Superset。以下是使用Docker在AWS EC2上容器化部署Superset的步骤：

1. **启动EC2实例**：
  
    * 登录到AWS管理控制台。
    * 导航到EC2服务并启动一个新实例。
    * 选择合适的Amazon Machine Image (AMI)，例如Amazon Linux 2或Ubuntu。
    * 选择实例类型，推荐选择至少t2.medium以确保有足够的内存和CPU资源。
    * 配置实例详细信息并确保安全组允许端口8088（Superset默认端口）和22（SSH）开放。
    * 添加存储并启动实例。
2. **连接到实例**：
  
    * 使用SSH连接到您的EC2实例。命令如下：
      
        ```sh
        ssh -i /path/to/your-key.pem ec2-user@your-ec2-public-ip
        ```
        
    * 替换`/path/to/your-key.pem`为您的密钥文件路径，`ec2-user`为您的用户名（如果是Ubuntu实例，则是`ubuntu`用户），`your-ec2-public-ip`为您的实例的公有IP地址。
3. **安装Docker**：
  
    * 在Amazon Linux 2上，运行以下命令：
      
        ```sh
        sudo yum update -y
        sudo amazon-linux-extras install docker -y
        sudo service docker start
        sudo usermod -a -G docker ec2-user
        ```
        
    * **拉取并运行Superset的Docker镜像**：
    
    * 重新连接到您的实例以应用组更改，然后运行以下命令拉取并启动Superset容器：
      
        ```sh
        docker pull apache/superset
        docker run -d -p 8088:8088 --name superset apache/superset
        ```
    
5. **初始化Superset**：
  
    * 进入Superset容器并运行初始化命令：
      
        ```sh
        docker exec -it superset superset fab create-admin --username admin --firstname Admin --lastname User --email admin@example.com --password admin
        docker exec -it superset superset db upgrade
        docker exec -it superset superset init
        ```
    
6. **访问Superset**：
  
    * 在您的浏览器中，输入您的EC2实例的公有IP地址和端口8088，例如：
      
        ```vbnet
        http://your-ec2-public-ip:8088
        ```
        
    * 使用之前创建的管理员账号登录。

通过这种方式，您可以在AWS EC2上容器化部署Superset。如果有任何问题或需要进一步帮助，请告诉我！

* * *

**You:**

[ec2-user@ip-172-31-29-17 ~]$ docker pull apache/superset
Using default tag: latest
Got permission denied while trying to connect to the Docker daemon socket at unix:///var/run/docker.sock: Post "http://%2Fvar%2Frun%2Fdocker.sock/v1.24/images/create?fromImage=apache%2Fsuperset&tag=latest": dial unix /var/run/docker.sock: connect: permission denied

* * *

**ChatGPT:**

这是因为当前用户没有权限访问Docker守护进程。您可以通过以下步骤解决这个问题：

1. **将当前用户添加到docker组**：
  
    * 确保当前用户已添加到`docker`组。如果还没有，请运行以下命令：
      
        ```sh
        sudo usermod -aG docker $USER
        ```
        
    * 之后，您需要退出当前会话并重新登录，以使组更改生效。
2. **重启Docker守护进程**（可选）：
  
    * 确保Docker守护进程正在运行：
      
        ```sh
        sudo service docker restart
        ```
    
3. **重新连接到实例并验证权限**：
  
    * 退出当前SSH会话并重新连接，以使用户组更改生效：
      
        ```sh
        exit
        ssh -i /path/to/your-key.pem ec2-user@your-ec2-public-ip
        ```
        
    * 之后，验证是否已成功添加到docker组：
      
        ```sh
        groups
        ```
        
    * 您应该看到`docker`列在当前用户的组列表中。
4. **拉取并运行Superset的Docker镜像**：
  
    * 重新尝试拉取Superset的Docker镜像：
      
        ```sh
        docker pull apache/superset
        ```

* * *

**You:**

```
[ec2-user@ip-172-31-29-17 ~]$ docker exec -it superset superset fab create-admin --username admin --firstname Admin --lastname User --email admin@example.com --password admin
docker exec -it superset superset db upgrade
docker exec -it superset superset init

--------------------------------------------------------------------------------
                                    WARNING
--------------------------------------------------------------------------------
A Default SECRET_KEY was detected, please use superset_config.py to override it.
Use a strong complex alphanumeric string and use a tool to help you generate 
a sufficiently random sequence, ex: openssl rand -base64 42
--------------------------------------------------------------------------------
--------------------------------------------------------------------------------
Refusing to start due to insecure SECRET_KEY
```



* * *

**ChatGPT:**

这是因为Superset检测到使用了默认的`SECRET_KEY`，这是不安全的。我们需要为Superset配置一个强密码的`SECRET_KEY`。您可以按照以下步骤操作：

1. **生成一个新的`SECRET_KEY`**：
  
    * 使用`openssl`生成一个强密码的`SECRET_KEY`：
      
        ```sh
        openssl rand -base64 42
        
        WhzQErPurMkXoQ4z3OlOcX1OspWOaPMwxsMGv8thZWliy7+P9t9sGskY
        D1Sg7vfGHj5VpuTFt7p1lrdwWGJaDkyO3tFR8q+cK1+zimAfMVIoAvDx
        
        ```
        
    * 复制生成的密钥。
2. **创建Superset配置文件**：
  
    * 在您的主目录中创建一个Superset配置文件`superset_config.py`：
      
        ```sh
        nano ~/superset_config.py
        ```
        
    * 在文件中添加以下内容，并将`YOUR_SECRET_KEY`替换为上一步生成的密钥：
      
        ```python
        SECRET_KEY = 'YOUR_SECRET_KEY'
        ```
    
3. **启动Superset容器时挂载配置文件**：
  
    * 停止并删除当前的Superset容器：
      
        ```sh
        docker stop superset
        docker rm superset
        ```
        
    * 重新启动Superset容器，并挂载配置文件：
      
        ```sh
        docker run -d -p 8088:8088 --name superset -v ~/superset_config.py:/app/pythonpath/superset_config.py apache/superset
        ```
    
4. **重新初始化Superset**：
  
    * 创建管理员用户：
      
        ```sh
        docker exec -it superset superset fab create-admin --username admin --firstname Admin --lastname User --email admin@example.com --password admin
        ```
        
    * 升级数据库：
      
        ```sh
        docker exec -it superset superset db upgrade
        ```
        
    * 初始化Superset：
      
        ```sh
        docker exec -it superset superset init
        ```
        

完成这些步骤后，Superset应该会使用新的`SECRET_KEY`安全地启动。如果您遇到任何问题或需要进一步的帮助，请告诉我！



**You:**

选择docker构建superset相比于直接在ec2上构建superset的优缺点

* * *

**ChatGPT:**

在AWS EC2上使用Docker容器化部署Superset与直接在EC2上部署Superset各有优缺点。以下是两种方法的比较：

### 使用Docker容器化部署Superset

#### 优点：

1. **环境一致性**：Docker可以确保在不同环境（开发、测试、生产）中运行相同的应用程序，从而避免“在我的环境中可以正常工作”的问题。
2. **隔离性**：每个Docker容器都在自己的隔离环境中运行，这减少了依赖冲突的风险，并提高了系统的安全性。
3. **便捷的扩展和缩放**：通过Docker Compose或Kubernetes，可以轻松地扩展或缩放Superset实例。
4. **简化部署和管理**：可以使用Docker镜像快速部署和更新Superset，并且容器管理工具（如Docker Compose、Kubernetes）使得管理变得更加简单。
5. **快速恢复**：Docker镜像使得在出现问题时可以快速恢复到之前的状态。

#### 缺点：

1. **学习曲线**：如果对Docker不熟悉，可能需要一些时间来学习如何构建、管理和部署Docker容器。
2. **额外的资源开销**：虽然Docker本身比较轻量，但在资源有限的环境中，容器化可能会带来额外的开销。
3. **复杂性**：对于简单的部署需求，使用Docker可能会增加不必要的复杂性。

### 直接在EC2上部署Superset

#### 优点：

1. **简单性**：对于小型和简单的部署需求，直接在EC2上安装和配置Superset可能更为简单和直接。
2. **无容器开销**：直接部署不会有Docker容器带来的额外资源开销，所有的系统资源都可以用于运行Superset。
3. **学习成本低**：如果对Docker不熟悉，直接部署可以避免学习和使用Docker的复杂性。

#### 缺点：

1. **环境不一致性**：不同环境中的配置和依赖可能会有所不同，可能会遇到“在我的环境中可以正常工作”的问题。
2. **依赖管理复杂**：需要手动管理和解决各种依赖问题，可能会导致冲突。
3. **难以扩展和缩放**：相比Docker Compose和Kubernetes，直接在EC2上部署的扩展和缩放变得更加复杂和困难。
4. **更新和恢复困难**：手动更新和恢复Superset可能会更耗时，并且容易出错。

### 结论

如果您需要一个快速、简单的Superset部署并且系统资源有限，可以考虑直接在EC2上部署Superset。如果您希望部署一个可扩展、易于管理的生产环境，并且不介意花费时间学习Docker，使用Docker容器化部署Superset可能是更好的选择。Docker提供了更好的环境一致性和管理便利性，但对于简单的部署需求，直接部署可能更为直接和高效。