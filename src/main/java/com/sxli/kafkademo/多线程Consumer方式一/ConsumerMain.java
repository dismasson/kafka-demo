package com.sxli.kafkademo.多线程Consumer方式一;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConsumerMain {
    public static void main(String[] args) {
        ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            threadPoolExecutor.submit(new ConsumerThread());
        }
    }
}
