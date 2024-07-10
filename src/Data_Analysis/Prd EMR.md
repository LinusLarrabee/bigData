Theme

1. 批与流
   1. Flink使用？
   2. 任务如何批化
   3. 任务如何流化

2. 启动配置
   1. 使用固定EIP
   2. 配置分布式Notebook
3. 存储调研



# 一、批与流

## 1.2. 任务批化

使用 cron 来定期运行启动 EMR 集群的脚本。例如，每天凌晨 2 点启动 EMR 集群并运行批处理脚本

### 技术选型（GPT）：

| **特点**       | **AWS Data Pipeline**       | **Apache Airflow**           | **AWS Lambda 和 CloudWatch Events** | **AWS Step Functions**       | **XXL-JOB**                |
| -------------- | --------------------------- | ---------------------------- | ----------------------------------- | ---------------------------- | -------------------------- |
| **易用性**     | 配置复杂，适合 AWS 生态用户 | 配置灵活，需要一定学习曲线   | 易于设置，适合简单任务              | 配置较简单，图形界面易于理解 | 界面友好，配置简单         |
| **集成性**     | 深度集成 AWS 服务           | 可扩展性强，支持多种外部工具 | 深度集成 AWS 服务                   | 深度集成 AWS 服务            | 支持多种任务类型和外部系统 |
| **可扩展性**   | 高                          | 高                           | 中                                  | 高                           | 高                         |
| **监控和日志** | 内置监控和重试机制          | 丰富的监控和日志功能         | 基本的日志和监控功能                | 丰富的监控和日志功能         | 提供任务日志和监控功能     |
| **成本**       | 成本较高                    | 开源免费，需要资源配置成本   | 成本低，按需计费                    | 成本中等，按使用量计费       | 开源免费，需要资源配置成本 |
| **实时性**     | 主要用于批处理，实时性较低  | 支持实时和批处理任务         | 适合低延迟的任务                    | 适合实时和批处理任务         | 支持定时和实时任务         |
| **适用场景**   | 数据管道和批处理任务        | 复杂工作流和依赖任务         | 简单的任务调度和事件驱动任务        | 复杂任务编排和状态管理       | 分布式任务调度和批处理任务 |
| **学习曲线**   | 陡峭                        | 中等                         | 平缓                                | 平缓                         | 平缓                       |

### 结论（GPT）

- **AWS Data Pipeline** 适合已经在 AWS 生态系统中的用户，用于复杂的数据管道和批处理任务。
- **Apache Airflow** 适合需要灵活配置和复杂依赖关系的用户，特别是需要管理复杂工作流的场景。
- **AWS Lambda 和 CloudWatch Events** 适合需要快速部署和低成本的简单任务调度。
- **AWS Step Functions** 适合需要复杂任务编排和状态管理的用户，特别是已经在使用 AWS 的用户。
- **XXL-JOB** 适合需要一个开源、易用且功能强大的分布式任务调度系统的用户，尤其是在需要多种任务类型和外部系统集成的场景。

### 重点关注点

- 是否需要重依赖AWS生态
- 实时性支持
- 成本：资源和学习

此外发现Airflow可以在做任务调度的同时做好ETL。

## 1.3. 任务流化



# 二、启动配置



## EMR使用固定IP

需要给EMR角色添加下述权限，最后修改为elastic IP的分配号。

```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "ec2:AssociateAddress",
                "ec2:DisassociateAddress",
                "ec2:DescribeAddresses"
            ],
            "Resource": "arn:aws:ec2:*:*:elastic-ip/eipalloc-0068ca7b6cdae8b88"
        }
    ]
}
```



然后bootstrap使用下述脚本，其中REGION和EIP_ALLOC_ID需替换为目标区域的目标IP。

```shell
#!/bin/bash
# Allocate an EIP to this instance and ensure required packages are installed

# Install necessary dependencies using the system package manager
sudo yum install -y python3-pip
sudo pip3 install python-dateutil

# Ensure AWS CLI is installed and use its full path
AWS_CLI_PATH="/usr/bin/aws"

# Get the instance's private IP address
PRIVATE_IP=$(hostname -I | awk '{print $1}')
echo "Private IP: $PRIVATE_IP"

# Get the instance ID using the private IP address
INSTANCE_ID=$($AWS_CLI_PATH ec2 describe-instances --filters "Name=private-ip-address,Values=$PRIVATE_IP" --query "Reservations[*].Instances[*].InstanceId" --output text --region us-east-1)
echo "Instance ID: $INSTANCE_ID"

# Set the region and EIP allocation ID
REGION="us-east-1"  # Replace with your region
EIP_ALLOC_ID="eipalloc-0068ca7b6cdae8b88"  # Replace with your EIP Allocation ID

# Associate the EIP with the instance
$AWS_CLI_PATH ec2 associate-address --instance-id $INSTANCE_ID --allocation-id $EIP_ALLOC_ID --region $REGION

```



# 三、存储调研

