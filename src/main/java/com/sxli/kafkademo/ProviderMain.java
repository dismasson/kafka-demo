package com.sxli.kafkademo;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * 一个简易的kafka消息发送示例
 */
public class ProviderMain {
    public static void main(String[] args) {
        Properties properties = new Properties();
        // 指定kafka服务器
        properties.put("bootstrap.servers", "47.98.116.157:9092");
        // 指定key序列化类
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // 指定value序列化类
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // 指定持久化方式 取值 0：不关心消息是否发送成功 1：适中，某些情况下会出现消息丢失 -1：不能容忍消息丢失
        properties.put("acks", "-1");
        // 指定失败重试次数
        properties.put("retries", 3);
        // 批次发送消息大小
        properties.put("batch.size", 323840);
        // 如果批次发送未曾填满，到时也可以发送
        properties.put("linger.ms", 10);
        // 指定缓冲区大小
        properties.put("buffer.memory", 33554432);
        properties.put("max.block.ms", 3000);

        Producer<String, String> producer = new KafkaProducer<String, String>(properties);
        for (int i = 0; i < 1000; i++) {
            producer.send(new ProducerRecord<String, String>("my-topic",
                    Integer.toString(i), Integer.toString(i)));
        }
        producer.close();
    }
}
