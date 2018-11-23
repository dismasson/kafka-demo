package com.sxli.kafkademo.多线程Consumer方式二.ForkJoin实现方式;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * Consumer Task 负责任务分配以及消费消息等
 */
public class ConsumerTask<K, V> extends RecursiveTask<Boolean> {

    private List<ConsumerRecord<K, V>> consumerRecords;

    // 每次处理一百条消息
    private final int THRESHOLD = 100;

    // 当前线程处理开始数
    private int start;

    // 当前线程处理结束数
    private int end;

    private ConsumerHandler handler;

    public ConsumerTask(List<ConsumerRecord<K, V>> consumerRecords, ConsumerHandler handler, int start, int end) {
        this.consumerRecords = consumerRecords;
        this.handler = handler;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Boolean compute() {
        if (end - start <= THRESHOLD) {
            // 如果任务足够小,直接计算:
            for (int i = start; i < end; i++) {
                ConsumerRecord<K, V> record = consumerRecords.get(i);
                // 执行handler
                handler.execute(record);
            }
            return Boolean.TRUE;
        }
        // 拆分任务
        int middle = (end + start) / 2;
        ConsumerTask<K, V> task1 = new ConsumerTask(consumerRecords, handler, start, middle);
        ConsumerTask<K, V> task2 = new ConsumerTask(consumerRecords, handler, middle, end);
        // 调用全部任务
        invokeAll(task1, task2);
        return Boolean.TRUE;
    }

}
