package com.sxli.kafkademo.多线程Consumer方式二.ForkJoin实现方式;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * Consumer Worker 负责拉取消息、提交offset等
 *
 * @param <K>
 * @param <V>
 */
public class ConsumerWorker<K, V> implements Runnable {

    private KafkaConsumer<K, V> consumer;


    public ConsumerWorker(KafkaConsumer<K, V> consumer) {
        if (consumer == null) {
            throw new NullPointerException("consumer is null");
        }
        this.consumer = consumer;
    }

    public void run() {
        while (true) {
            ConsumerRecords<K, V> records = consumer.poll(1000);
            ForkJoinPool forkJoinPool = new ForkJoinPool(3);
            int count = records.count();
            List<ConsumerRecord<String, String>> list = new ArrayList<>(count);
            for (ConsumerRecord record : records) {
                list.add(record);
            }
            // 创建消息处理器
            ConsumerHandler handler = new ConsumerHandler();
            // 初始化任务
            ConsumerTask<K, V> consumerTask = new ConsumerTask(list, handler, 0, count);
            // 任务交由Fork/join处理
            forkJoinPool.invoke(consumerTask);
            // 提交offset
            consumer.commitSync();
        }
    }
}
