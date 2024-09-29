

使用log.info()@Slf4j构建注释，我想构建一个yaml配置项，该配置项包括open/role两项，然后一个aop ExecuteSave，role为master时，首先check redis是否有相同的输入参数，即redis保存了key-value，value为入参或入参的摘要。如果不同，则调用预警prometheusHandler.mismatch，如果没有，则把入参保存到redis中，然后执行函数。slave则先check input，如果不同也写入mismatch，如果没有则save到cache中，不需要执行后续函数直接将切片位置返回null



另一个executeCheck的aop，master的逻辑为check input在redis是否存在，然后save input，然后process，然后save in/out，slave的逻辑为check input和check in/out，如果两者都存在，则check input是否一致，不是的话使用mismatch，一致的话拿output返回， 如果只有check input存在，则看是否要mismatch，然后等常熟时间后看是否有结果，没有就oneside告警，如果两者都不存在，save input，wait 一个常数时间，再check，没有out则告警oneside

