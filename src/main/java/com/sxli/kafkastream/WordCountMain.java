package com.sxli.kafkastream;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;

import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

/**
 * 一个简易的kafka消息发送示例
 */
public class WordCountMain {
    public static void main(String[] args) throws InterruptedException {
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams_wordcount");
        // 指定kafka服务器
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "47.98.116.157:9092");
        // 指定key反序列化类
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        // 指定value反序列化类
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream stream = streamsBuilder.stream("streams-plaintext-input");
        // 当前流处理业务过程：拆分文字、文字分组、统计
        KTable<String, Long> kTable = stream.flatMapValues(new ValueMapperImpl()).groupBy(new KeyValueMapperImpl())
                .count();
        // 指定流计算过后的处理方式：往streams-wordcount-output写入流计算结果
        kTable.toStream().to("streams-wordcount-output", Produced.with(Serdes.String()
                , Serdes.Long()));
        KafkaStreams streams = new KafkaStreams(streamsBuilder.build(), properties);
        final CountDownLatch latch = new CountDownLatch(1);
        // 绑定钩子函数，在JVM关闭的时候调用此函数
        Runtime.getRuntime().addShutdownHook(new Thread(new StreamsClose(streams, latch)));
        // 启动
        streams.start();
        // 等待销毁
        latch.await();
    }

    /**
     * 文字拆分实现类，此处用空格拆分
     */
    private static class ValueMapperImpl implements ValueMapper<String, Iterable<String>> {
        @Override
        public Iterable<String> apply(String value) {
            // 用空格拆分词
            return Arrays.asList(value.toLowerCase(Locale.getDefault()).split(" "));
        }
    }

    /**
     * 文字分组实现类
     */
    private static class KeyValueMapperImpl implements KeyValueMapper {
        @Override
        public Object apply(Object key, Object value) {
            return value;
        }
    }

    /**
     * KafkaStreams Close
     */
    private static class StreamsClose implements Runnable {

        private KafkaStreams streams;

        private CountDownLatch latch;

        public StreamsClose(KafkaStreams streams, CountDownLatch latch) {
            this.streams = streams;
            this.latch = latch;
        }

        @Override
        public void run() {
            streams.close();
        }
    }
}
