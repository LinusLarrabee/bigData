## 一、维度缺失

> 我们希望从TAUC上获取实际用户场景数据，如客户端数量，RSSI，流量及干扰等信息（具体如下）。以此为依据建立不同流量模型，提升测试过程中对用户场景的覆盖度（99.99%），提升产品品质；测试环境更加精准，提升测试效率；根据用户场景测试结果对不同测试指标进行分级，提升产品竞争力。

考虑到本需求是1月份提出并因数据结果不满意而返工，想了解一下为什么要统计极值数据，业务场景是如何使用这个极值的，每天数据同理，需要的数据指标是如何和上述需求背景关联的，指标的查看频次等相关问题。

注：个人觉得极值数据作用更多的是检测峰流下网络质量问题，而非简单对测试环境的边界性检测，所以不确定这部分提供的数据是否充足。此外极值引起的尖峰指标应该是按百分位求平均的方式计算。



TAUC这边对于network，ap，sta分别有isp_network,isp_network_deco,isp_network_client三张表对应，如果需要诸如拓扑结构等表可额外提需求说明使用用途

```
CREATE TABLE isp_network_deco
(
    device_id           CHAR(40)                                  NOT NULL,
    ctime               datetime(3) DEFAULT '1970-01-01 00:00:00' NOT NULL,
    mtime               datetime(3) DEFAULT '1970-01-01 00:00:00' NOT NULL,
    mac                 CHAR(12)                                  NULL,
    sn                  VARCHAR(32)                               NULL,
    network_id          BIGINT                                    NULL,
    isp_admin_id        VARCHAR(31)                               NOT NULL,
    topo_id             BIGINT                                    NULL,
    topo_role           TINYINT                                   NULL,
    device_model        VARCHAR(63)                               NULL,
    fw_version          VARCHAR(127)                              NULL,
    wan_ipv4            VARCHAR(15)                               NULL,
    public_ip           VARCHAR(15)                               NULL,
    wan_ipv6            VARCHAR(40)                               NULL,
    device_type         VARCHAR(45)                               NULL,
    imei                VARCHAR(15)                               NULL,
    reboot_timestamp    BIGINT                                    NULL,
    online              BIT(1)                                    NULL,
    connection_username VARCHAR(255)                              NULL,
    connection_type     TINYINT                                   NULL,
    wan_mac             CHAR(12)                                  NULL,
    wan_mode            CHAR(127)                                 NULL,
    wan_type_in_use     CHAR(127)                                 NULL,
    online_mtime        BIGINT                                    NULL,
    offnet_tag_mtime    BIGINT                                    NULL,
    offnet_tag          BIT(1)                                    NULL,
    message_time        BIGINT                                    NULL,
    CONSTRAINT pk_isp_network_deco PRIMARY KEY (device_id)
);

CREATE TABLE isp_network_client
(
    id                   BIGINT AUTO_INCREMENT                     NOT NULL,
    ctime                datetime(3) DEFAULT '1970-01-01 00:00:00' NOT NULL,
    mtime                datetime(3) DEFAULT '1970-01-01 00:00:00' NOT NULL,
    client_id            BIGINT                                    NULL,
    mac                  VARCHAR(20)                               NOT NULL,
    topo_id              BIGINT                                    NOT NULL,
    network_id           BIGINT                                    NOT NULL,
    name                 VARCHAR(200)                              NULL,
    online               BIT(1)                                    NULL,
    client_type          VARCHAR(20)                               NULL,
    last_ip              VARCHAR(20)                               NULL,
    last_ipv6            VARCHAR(50)                               NULL,
    last_connect_type    VARCHAR(20)                               NULL,
    last_connect_band    VARCHAR(20)                               NULL,
    last_connect_mac     VARCHAR(20)                               NULL,
    last_connect_network VARCHAR(40)                               NULL,
    last_online_time     datetime                                  NULL,
    last_offline_time    datetime                                  NULL,
    vendor               VARCHAR(256)                              NULL,
    connect_device_name  VARCHAR(64)                               NULL,
    channel              INT                                       NULL,
    link_rate            INT                                       NULL,
    max_link_rate        INT                                       NULL,
    block                BIT(1)                                    NULL,
    upstream             INT                                       NULL,
    downstream           INT                                       NULL,
    CONSTRAINT pk_isp_network_client PRIMARY KEY (id)
);

CREATE TABLE isp_network
(
    id                  BIGINT                                         NOT NULL,
    ctime               datetime(3)      DEFAULT '1970-01-01 00:00:00' NOT NULL,
    mtime               datetime(3)      DEFAULT '1970-01-01 00:00:00' NOT NULL,
    isp_admin_id        VARCHAR(31)                                    NOT NULL,
    isp_operator_id     VARCHAR(31)                                    NULL,
    network_name        VARCHAR(64)                                    NOT NULL,
    username            VARCHAR(64)                                    NULL,
    phone_number        VARCHAR(20)                                    NULL,
    email               VARCHAR(64)                                    NULL,
    address             VARCHAR(64)                                    NULL,
    protocol_type       VARCHAR(10)                                    NULL,
    subscribe_download  VARCHAR(31)                                    NULL,
    subscribe_upload    VARCHAR(31)                                    NULL,
    subscribe_date      datetime                                       NULL,
    delete_deco_times   TINYINT UNSIGNED DEFAULT 0                     NULL,
    source_type         VARCHAR(16)                                    NULL,
    nat_block           BIT(1)                                         NULL,
    nat_block_url       VARCHAR(1024)                                  NULL,
    support_home_shield BIT(1)                                         NULL,
    config_type         INT                                            NULL,
    config_id           VARCHAR(32)                                    NULL,
    country             VARCHAR(64)                                    NULL,
    region_group        VARCHAR(64)                                    NULL,
    state               VARCHAR(64)                                    NULL,
    city                VARCHAR(64)                                    NULL,
    district            VARCHAR(64)                                    NULL,
    site                VARCHAR(128)                                   NULL,
    house_number        VARCHAR(64)                                    NULL,
    CONSTRAINT pk_isp_network PRIMARY KEY (id)
);
```





## 二、需求数据

>1，每天无线客户端Online最大数量，且此时2.4G/5G/6G频段各有多少个客户端，有线客户端数量。
>2，客户端Online最大数量时，所有无线客户端（分频段）协商速率，有线客户端协商速率。
>3，客户端Online最大数量时，所有客户端（分频段）信号强度以及分布情况。

不考虑有线情况的话，这个部分数据来源就是下面的数据按最大在线数量的统计，但是需要明确这个统计指标的合理性

有线客户端：协商速率和信号强度

信号强度没有衡量指标，协商速率未找到明确指标，但是有当前上传和下载速率，应该不是想要的。

|                                          |                                                              |
| ---------------------------------------- | ------------------------------------------------------------ |
| 有线客户端上传速率(mb/s)                 | Report.Device.WiFi.MultiAP.APDevice.1.X_TP_Ethernet.AssociatedDevice.1.UpSpeed |
| 有线客户端下载速率(mb/s)                 | Report.Device.WiFi.MultiAP.APDevice.1.X_TP_Ethernet.AssociatedDevice.1.DownSpeed |
| 有线客户端与设备之间的有线链路速率(mb/s) | Report.Device.WiFi.MultiAP.APDevice.1.X_TP_Ethernet.AssociatedDevice.1.LinkSpeed |

>4，各频段每天TRANSMITTED RATE和RECEIVED RATE的数值（15min统计一次）。

Report.Device.WiFi.DataElements.Network.Device.2.Radio.2.BSS.2.STA.1.X_TP_RxRate

Report.Device.WiFi.DataElements.Network.Device.1.Radio.1.X_TP_AverageRxRate

这个数据有ap.radio维度的和sta维度的，想了解一下对这个数据的统计在哪个维度，然后我看需要的transmitte数据是用户维度的？





>4，各频段每天TRANSMITTED RATE和RECEIVED RATE的数值（15min统计一次）。
>5，各频段每天Airtime Utilization、Congestion、Rate、Noise、Packet Error Rate（15min统计一次）。
>6，每天WAN Throughput统计（15min统计一次）

针对4-6为Controller的

>5，各频段每天Airtime Utilization、Congestion Rate、Noise、Packet Error Rate（15min统计一次）。

| 需求字段            | QoE字段                                                      | QoE字段维度                       | QoE字段解释                                                  |
| ------------------- | ------------------------------------------------------------ | --------------------------------- | ------------------------------------------------------------ |
| Airtime Utilization | Device.WiFi.DataElements.Network.Device.*.X_TP_QoE.Factor.WiFiAvailability2GScore | Per Ap Per Radio，Radio以字段区分 | 2.4G WiFiAvailability评分（计算方式见评分细则文档2.3.6）     |
| Congestion Rate     | Report.Device.WiFi.DataElements.Network.Device.1.Radio.1.X_TP_Congestion_Rate | Per Ap Per Radio                  | 该频段的邻频干扰。Congestion_Rate  = Utilization-Transmit-ReceiveSelf |
| Packet Error Rate   | Report.Device.WiFi.DataElements.Network.Device.1.Radio.1.X_TP_ErrorsPkt | Per Ap Per Radio                  | 该频段下错误发包数                                           |
| Packet Error Rate   | Report.Device.WiFi.DataElements.Network.Device.2.Radio.2.BSS.2.STA.1.X_TP_RetryRate_TX | Per Sta                           | 如果需要的是重传率，有个具体的比值                           |
| Noise               | Report.Device.WiFi.DataElements.Network.Device.1.Radio.1.Noise | Per Ap Per Radio                  | 该频段下的噪声                                               |
|                     |                                                              |                                   |                                                              |
|                     |                                                              |                                   |                                                              |



>6，每天WAN Throughput统计（15min统计一次）

Report.Device.WiFi.DataElements.Network.Device.1.X_TP_QoE.WANThroughput

AP维度。



>7，单次统计Agent和Controller的RSSI及协商速率

Report.Device.WiFi.DataElements.Network.Device.1.Radio.1.BackhaulSta.X_TP_SignalStrength

Report.Device.WiFi.DataElements.Network.Device.2.Radio.2.BSS.2.STA.1.SignalStrength

不确定RSSI需要的是ap之间的还是ap-sta的

Report.Device.WiFi.DataElements.Network.Device.2.Radio.2.BSS.2.STA.1.LastDataUplinkRate

Report.Device.WiFi.DataElements.Network.Device.2.Radio.2.BSS.2.STA.1.EstMACDataRateUplink

对于上行协商有两个数据源，不确定使用源



>8，用户主动扫描（WiFi Interference Test）的结果，包括（干扰SSID数量、每个干扰对应的RSSI、每个干扰对应的信道及带宽）

本部分为TAUC-Network维护，对应接口为@RequestMapping("/v1/remote/networks/{masterDeviceId}/diagnostics/ap-survey")，如需提取，需要联系覃伟安排人力。