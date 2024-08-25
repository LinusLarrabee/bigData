```
curl --location 'https://aps1-tauc-event-pet.tplinkcloud.com/v1/bulk-data/client?oui=40AE&sn=2243192089&pc=Device44' \
--header 'oui: 000AEB' \
--header 'sn: 000AEB1369CC' \
--header 'pc: Device2' \
--header 'Auth;' \
--header 'BBF-Report-Format: ObjectHierarchy' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic MDAwOjAwMA==' \
--data '{
  "Report": [
    {
      "CollectionTime": 91753967,
      "Device": {
        "WiFi":{
            "MultiAP":{
                "APDevice":{
                    "a":{
                        "X_TP_Ethernet":{
                            "AssociatedDevice":{
                                "b": {
                                    "APDeviceID": "eee",
                                    "IPAddress": "eee1",
                                    "PacketReceived":"eee2"
                                }
                            }
                        }

                    }
                }
            },
            "DataElements":{
                "Network" :{
                    "ControllerID": "sssss",
                    "Device":{
                        "a":{
                            "ID": "aaa",
                            "Radio": {
                                "a1":{
                                    "utilization": "s"
                                }
                            },
                            "X_TP_QoE":{
                                "Factor":{
                                    "numOfAlerts": "s"
                                },
                                "WANBandwidth":"s"
                            }
                        }
                    }
                }
            }
        }
      } 
    }
  ]
}'curl --location 'https://aps1-tauc-event-pet.tplinkcloud.com/v1/bulk-data/client?oui=40AE&sn=2243192089&pc=Device44' \
--header 'oui: 000AEB' \
--header 'sn: 000AEB1369CC' \
--header 'pc: Device2' \
--header 'Auth;' \
--header 'BBF-Report-Format: ObjectHierarchy' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic MDAwOjAwMA==' \
--data '{
  "Report": [
    {
      "CollectionTime": 91753967,
      "Device": {
        "WiFi":{
            "MultiAP":{
                "APDevice":{
                    "a":{
                        "X_TP_Ethernet":{
                            "AssociatedDevice":{
                                "b": {
                                    "APDeviceID": "eee",
                                    "IPAddress": "eee1",
                                    "PacketReceived":"eee2"
                                }
                            }
                        }

                    }
                }
            },
            "DataElements":{
                "Network" :{
                    "ControllerID": "sssss",
                    "Device":{
                        "a":{
                            "ID": "aaa",
                            "Radio": {
                                "a1":{
                                    "utilization": "s"
                                }
                            },
                            "X_TP_QoE":{
                                "Factor":{
                                    "numOfAlerts": "s"
                                },
                                "WANBandwidth":"s"
                            }
                        }
                    }
                }
            }
        }
      } 
    }
  ]
}'
```



|      |                              |      |      |
| ---- | ---------------------------- | ---- | ---- |
| 0    | 3C52A1-Device2-T3C52A1012F3A |      |      |
| 1    | 5C628B-HB810-22346B4000037   |      |      |
| 2    | 40AE30-Device2-2243192000025 |      |      |
|      |                              |      |      |
| 4    | 00FF00-Device2-T232053000009 |      |      |
| 5    | 9CA2F4-Device2-2229032000583 |      |      |
| 6    | 40AE30-Device2-2243192001068 |      |      |
| 7    | 203626-EX820v-2247AEV000002  |      |      |
| 8    | 3C52A1-Device2-2237232000032 |      |      |
| 9    | 54AF97-Device2-2223230000510 |      |      |
| 10   | 40AE30-Device2-2243192000028 |      |      |
| 11   | 000AEB-Device2-000AEB1369CC  |      |      |

