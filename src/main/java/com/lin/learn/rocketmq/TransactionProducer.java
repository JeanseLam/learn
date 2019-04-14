package com.lin.learn.rocketmq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.*;

@Component
public class TransactionProducer {

    private static final Logger logger = LoggerFactory.getLogger(TransactionProducer.class);

    @Resource
    private AccountService accountService;

    private TransactionMQProducer producer = new TransactionMQProducer("send_account_producer");

    @PostConstruct
    public void init() {
        try {
            ExecutorService executorService = new ThreadPoolExecutor(
                    2, 5, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("client-transaction-msg-check-thread");
                    return thread;
                }
            });

            producer.setNamesrvAddr("127.0.0.1:9876");
            producer.setExecutorService(executorService);
            producer.setTransactionListener(accountService);
            producer.start();
            logger.info("producer started...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendValueToOtherAccount(int value) throws Exception {
        Message msg = new Message("account", "send_value", Integer.toString(3),
                        (Integer.toString(value)).getBytes(RemotingHelper.DEFAULT_CHARSET));
        SendResult sendResult = producer.sendMessageInTransaction(msg, null);
        logger.info(sendResult.toString());
    }
}
