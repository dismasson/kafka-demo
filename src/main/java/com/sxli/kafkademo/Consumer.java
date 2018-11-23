package com.sxli.kafkademo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Consumer {
    public static void main(String[] args) {
        ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            threadPoolExecutor.submit(new ConsumerWorker());
        }
    }
}
