package com.sxli.kafkademo.多线程Consumer方式二.ForkJoin实现方式;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * Consumer Handler 负责处理ConsumerRecord
 */
public class ConsumerHandler<K, V> {

    /**
     * 执行
     */
    public void execute(ConsumerRecord<K, V> consumerRecord) {
        if (consumerRecord == null) {
            throw new NullPointerException("consumerRecord is null");
        }
        System.out.printf("threadId = %s,partition = %s,offset = %d,key = %s,value = %s%n",
                Thread.currentThread().getId(), consumerRecord.partition(),
                consumerRecord.offset(), consumerRecord.key(), consumerRecord.value());
    }
}
