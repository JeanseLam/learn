package com.lin.learn.rocketmq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

public class Producer {

    public static void main(String[] args) {

        DefaultMQProducer producer = null;
        try {
            producer = new DefaultMQProducer("test_mq_producer");
            producer.setNamesrvAddr("192.168.0.103:9876");
            producer.start();

            for(int i = 0; i < 10; i++) {
                Message message = new Message("test", "pay", ("pay_task_" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                SendResult sendResult = producer.send(message);
                System.out.println(sendResult.toString());
            }

            for(int i = 0; i < 10; i++) {
                Message message = new Message("test", "order", ("order_task_" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                SendResult sendResult = producer.send(message);
                System.out.println(sendResult.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if(producer != null) {
                producer.shutdown();
            }
        }
    }
}
