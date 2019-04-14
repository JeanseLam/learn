package com.lin.learn.rocketmq;

import com.lin.learn.mysql.TestEntity;
import com.lin.learn.mysql.TestMapper;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AccountService implements TransactionListener {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    private ConcurrentHashMap<Integer, Integer> transactionCache = new ConcurrentHashMap<>();

    @Resource
    private TestMapper mapper;

    @Transactional
    public void sendValueToOtherAccount(int value) throws Exception {
        TestEntity entity = mapper.getById(3);
        entity.setCount(entity.getCount() + value);
        mapper.updateNoSafe(entity);
    }

    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {

        try {
            String value = new String(message.getBody(), RemotingHelper.DEFAULT_CHARSET);
            int sendValue = Integer.parseInt(value);
            sendValueToOtherAccount(sendValue);
            transactionCache.put(1, 414);
        } catch (Exception e) {
            e.printStackTrace();
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
        return LocalTransactionState.UNKNOW;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {

        try {
            int value = Integer.parseInt(new String(messageExt.getBody(), RemotingHelper.DEFAULT_CHARSET));
            int finalValue = transactionCache.get(1);
            if(finalValue == 314 + value) {
                logger.info("transaction success!!!!");
                return LocalTransactionState.COMMIT_MESSAGE;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return LocalTransactionState.UNKNOW;
    }
}
