# 1.运行当前项目的Demo
### 创建topic
`./kafka-topics.sh --create --zookeeper localhost:2181 --topic streams-plaintext-output --partitions 1 --replication-factor 1`  
`./kafka-topics.sh --create --zookeeper localhost:2181 --topic streams-plaintext-input --partitions 1 --replication-factor 1`
### 检测topic状态
`./kafka-topics.sh --describe --zookeeper localhost:2181 --topic streams-plaintext-input`  
`./kafka-topics.sh --describe --zookeeper localhost:2181 --topic streams-plaintext-output`
### 运行demo的ConsumerMain以及ProviderMain
`ProviderMain`负责往`streams-plaintext-input`录入文字  
`WordCountMain`负责文字统计并且将计算结果录入`streams-wordcount-output`  
`ConsumerMain`打印`streams-wordcount-output`数据  
# 2.如何利用KafkaStream的demo来演示？
## 第一步：创建两个topic,"streams-plaintext-output"跟"streams-plaintext-input"
### 创建topic
`./kafka-topics.sh --create --zookeeper localhost:2181 --topic streams-plaintext-output --partitions 1 --replication-factor 1`  
`./kafka-topics.sh --create --zookeeper localhost:2181 --topic streams-plaintext-input --partitions 1 --replication-factor 1`
### 检测topic状态
`./kafka-topics.sh --describe --zookeeper localhost:2181 --topic streams-plaintext-input`  
`./kafka-topics.sh --describe --zookeeper localhost:2181 --topic streams-plaintext-output`
## 第二步：运行KafkaStream的WordCountDemo
`./kafka-run-class.sh org.apache.kafka.streams.examples.wordcount.WordCountDemo`
## 第三步：往streams-plaintext-intput发送数据
`./kafka-console-producer.sh --broker-list localhost:2181 -topic streams-plaintext-input`
## 第四步：消费streams-wordcount-output验证
`./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic streams-wordcount-output --from-beginning --formatter kafka.tools.DefaultMessageFormatter --property print.key=true --property print.value=true --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer`