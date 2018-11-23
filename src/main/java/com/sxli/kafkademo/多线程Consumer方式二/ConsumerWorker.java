package com.sxli.kafkademo.多线程Consumer方式二;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Consumer Worker 负责拉取消息、给多线程分配消费任务、提交offset等
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
            ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(3);
            for (ConsumerRecord<K, V> record : records) {
                // 多线程处理
                threadPoolExecutor.submit(new ConsumerHandler<>(record));
            }
            // 提交offset
            consumer.commitSync();
        }
    }
}
