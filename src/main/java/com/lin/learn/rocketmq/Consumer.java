package com.lin.learn.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

public class Consumer {

    public static void main(String[] args) {

        DefaultMQPushConsumer payTaskConsumer = null;
        DefaultMQPushConsumer orderTaskConsumer = null;
        try {
            payTaskConsumer = new DefaultMQPushConsumer("test_mq_consumer_pay");
            payTaskConsumer.setNamesrvAddr("192.168.0.103:9876");
            payTaskConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            payTaskConsumer.subscribe("test", "pay");

            payTaskConsumer.registerMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                    for(int i = 0; i < list.size(); i++) {
                        System.out.println(Thread.currentThread().getName() + " receives pay task:" + list.get(i).toString());
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
            payTaskConsumer.start();

            orderTaskConsumer = new DefaultMQPushConsumer("test_mq_consumer_order");
            orderTaskConsumer.setNamesrvAddr("192.168.0.103:9876");
            orderTaskConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            orderTaskConsumer.subscribe("test", "order");

            orderTaskConsumer.registerMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                    for(int i = 0; i < list.size(); i++) {
                        System.out.println(Thread.currentThread().getName() + " receives order task:" + list.get(i).toString());
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
            orderTaskConsumer.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
