package com.sxli.kafkademo.多线程Consumer方式二;

import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

/**
 * Consumer Main 负责创建KafkaConsumer实例并且启动ConsumerWorker
 */
public class ConsumerMain {
    public static void main(String[] args) {
        Properties properties = new Properties();
        // 指定kafka服务器
        properties.put("bootstrap.servers", "47.98.116.157:9092");
        // 指定消费者群组ID
        properties.put("group.id", "consumer_group");
        // 关闭自动提交
        properties.put("enable.auto.commit", "false");
        //
        properties.put("auto.commit.interval.ms", "1000");
        // 指定key反序列化类
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        // 指定value反序列化类
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Arrays.asList("my-topic"));
        // 创建ConsumerWorker
        ConsumerWorker<String, String> worker = new ConsumerWorker<>(consumer);
        // 启动
        Thread workerThread = new Thread(worker);
        workerThread.start();
    }
}
