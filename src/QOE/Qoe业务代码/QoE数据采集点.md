数据上报原始协议

```java
/**
 * Bulk Data 请求结构如下
 *      {
 *          "Report" : [
 *              {
 *                  "CollectionTime": "1",
 *                  "Device": {}
 *              },
 *              {
 *                 "CollectionTime": "2",
 *                  "Device": {}
 *              }
 *          ]
 *      }
 *
 * @author Zeng Zhaoyu
 * @version 1.0
 * @since 2021/9/15
 */
@Data
public class BulkDataReqVO {

    @JsonProperty("Report")
    private List<CollectionRecordReqVO> report;

    @Data
    public static class CollectionRecordReqVO {

        @JsonProperty("CollectionTime")
        private Date collectionTime;

        @JsonProperty("Device")
        private JsonNode content;
    }
}
```

QoE的四类数据都是按照这个协议构建的，即便数据分流也是按照这个协议来往下实现。

除非在http请求端对上报数据的协议进行修改。

