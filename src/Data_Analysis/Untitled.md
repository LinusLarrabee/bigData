ap_network_health.json，取出X_TP_AssociatedDeviceNumberOfEntries，EstMACDataRateDownlink，SignalStrength，X_TP_Ethernet.AssociatedDevice.1.UpSpeed，X_TP_Ethernet.AssociatedDevice.1.DownSpeedX_TP_Ethernet.AssociatedDevice.1.LinkSpeed字段| X_TP_AverageTxRate  | Per Ap Per Radio                                             |
| ------------------- | ------------------------------------------------------------ | ---------------- |
| RECEIVED RATE       | X_TP_AverageRxRate                                           | Per Ap Per Radio |
| Airtime Utilization | X_TP_QoE.Factor.WiFiCoverage2GScore, X_TP_QoE.Factor.WiFiCoverage5GScore, X_TP_QoE.Factor.WiFiCoverage6GScore | Per Ap           |
| Congestion Rate     | X_TP_Congestion_Rate                                         | Per Ap Per Radio |
| Noise               | Noise                                                        | Per Ap Per Radio |
| Packet Error Rate   | X_TP_ErrorsPkt, X_TP_PacketsSent, X_TP_PacketsReceived       | Per Ap Per Radio |
| WAN Throughput      | X_TP_QoE.WANBandwidth                                        |





字段



ap表



数据格式设计



```
|{"filterKey":"AP_DATA","message":"{"collectionRecords":[{"controllerTrId":"5CE931-NX511v-2238484000065","collectionTime":1721279820,"controllerId":"5C:E9:31:47:9C:48","deviceDataList":[{"id":"5C:E9:31:47:9C:48","factor":{"numOfAlerts":"2","isController":"1","connectivityScore":"5.0","availableBandwidthScore":"1.0","wifiCoverage2GScore":"0.0","wifiCoverage5GScore":"3.8","wifiCoverage6GScore":null,"wifiAvailability2GScore":"1.0","wifiAvailability5GScore":"4.8","wifiAvailability6GScore":null,"internetDelayScore":"4.6","internetJitterScore":"5.0","systemHealthScore":"5.0","congestionScore":null},"metrics":{"wanBandwidth":"0","uploadBandwidth":null,"wanConnectivity":"0","wanThroughput":"0","uploadThroughput":null,"latency":"76","jitter":"2","memoryFree":null,"memoryTotal":null,"cpuUsage":null},"collectionData":[{"utilization":"237","averageRxRate":"9","averageTxRate":"3","band":"2.4GHz","bandWidth":"40MHz","errorsPkt":"0","ipAddress":"192.168.128.1","signalStrength":null,"packetsSent":"3052","packetsReceived":"4781","errorsSent":null,"errorsReceived":null,"bytesSent":"817247","bytesReceived":null,"noise":"62","associatedDeviceNumberOfEntries":"0","congestionRate":null,"backhaulSta":{"macAddress":"0","backhaulLinkType":"NULL","linkRate":"0","signalStrength":"0","utilization":"0","snr":null}},{"utilization":"53","averageRxRate":"0","averageTxRate":"0","band":"5GHz","bandWidth":"160MHz","errorsPkt":"2385","ipAddress":"192.168.128.1","signalStrength":null,"packetsSent":"965227","packetsReceived":"427024","errorsSent":null,"errorsReceived":null,"bytesSent":"1094124714","bytesReceived":null,"noise":"6","associatedDeviceNumberOfEntries":"0","congestionRate":null,"backhaulSta":{"macAddress":"0","backhaulLinkType":"NULL","linkRate":"0","signalStrength":"0","utilization":"0","snr":null}}]}]}]}"|

通用字段，controllerid & collection time

/date/radio/
controller

计算方法：
提取
            "collectionTime": 1721279820,
            "controllerId": "5C:E9:31:47:9C:48",

controller表，分频段
X_TP_AverageTxRate averageTxRate
X_TP_AverageRxRate averageRxRate
X_TP_Congestion_Rate congestionRate
Noise noise
X_TP_ErrorsPkt, X_TP_PacketsSent, X_TP_PacketsReceived
errorsPkt, packetsSent, packetsReceived
X_TP_QoE.WANBandwidth
wanBandwidth

X_TP_QoE.Factor.WiFiCoverage2GScore
wifiCoverage5GScore
X_TP_QoE.Factor.WiFiCoverage5GScore
X_TP_QoE.Factor.WiFiCoverage6GScore

agent表
BackhaulSta.X_TP_SignalStrength

BackhaulSta.X_TP_LinkRate


{"controllerTrId":"5CE931-NX511v-2238484000065","collectionTime":1721276220,"controllerId":"5C:E9:31:47:9C:48","deviceDataList":[{"id":"5C:E9:31:47:9C:48","factor":null,"metrics":null,"collectionData":[{"macAddress":"38:F9:D3:5F:6E:3C","ipAddress":"192.168.128.51","hostName":"zhaohangdeMBP-2","signalStrength":"116","bytesSent":null,"bytesReceived":null,"txRate":"0","rxRate":"0","lastDataDownlinkRate":"6000","lastDataUplinkRate":"0","estMACDataRateDownlink":"6","estMACDataRateUplink":"6","packetsSent":null,"packetsReceived":null,"operatingStandard":null,"lastConnectTime":null,"noise":null,"errorsSent":null,"errorsReceived":null,"retransCount":null,"factor":{"numOfAlerts":"1","availableWifiServiceQualityScore":"0.0","networkReadyTimeScore":"5.0","signalStrengthScore":"3.8","wifiConnectivityScore":"5.0","wifiProtocolScore":"4.0","clientHealthScore":null},"metrics":{"wanBandwidth":"0","wanThroughput":"0","networkReadyTime":"273","wifiConnectivity":"0"},"associationTime":null,"maxLinkRate":null,"band":"5GHz","utilization":"63","rssiFailedNum":null,"clientEfficiencyRate":null,"bssTag":null,"clientType":null,"txRetryRate":null,"rxRetryRate":null,"channelNumber":null,"steeringHistory":null,"upSpeed":null,"downSpeed":null,"linkSpeed":null,"duplexMode":null,"active":null,"interfaceType":"Wi-Fi"}]}]} 

client表
分频段，json化
EstMACDataRateDownlink
SignalStrength
UpSpeed
DownSpeed
LinkSpeed
```

