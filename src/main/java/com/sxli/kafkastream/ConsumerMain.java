package com.sxli.kafkastream;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

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
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.LongDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer(properties);
        consumer.subscribe(Arrays.asList("streams-wordcount-output"));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(1000);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("threadId = %s,partition = %s,offset = %d,key = %s,value = %s%n",
                        Thread.currentThread().getId(), record.partition(),
                        record.offset(), record.key(), record.value());
            }
            // 提交offset
            consumer.commitSync();
        }
    }

}
