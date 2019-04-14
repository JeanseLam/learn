package com.lin.learn.kafka;

public class KafkaDemo {

    public static void main(String[] args) {
        Producer producerThread = new Producer(KafkaProperties.TOPIC, false);
        new Thread(producerThread).start();

        Consumer consumerThread = new Consumer(KafkaProperties.TOPIC);
        new Thread(consumerThread).start();
    }
}
