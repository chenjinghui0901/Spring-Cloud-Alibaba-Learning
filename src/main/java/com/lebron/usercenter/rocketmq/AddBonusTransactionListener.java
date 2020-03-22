package com.lebron.usercenter.rocketmq;

import com.lebron.usercenter.dao.rocketmq.RocketmqTransactionLogMapper;
import com.lebron.usercenter.dao.user.UserMapper;
import com.lebron.usercenter.domain.entity.rocketmq.RocketmqTransactionLog;
import com.lebron.usercenter.domain.entity.user.User;
import com.lebron.usercenter.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

/**
 * @author LeBron
 */
@Service
@RocketMQTransactionListener(txProducerGroup = "tx-add-bonus-group")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AddBonusTransactionListener implements RocketMQLocalTransactionListener {

    private final UserService userService;

    private final RocketmqTransactionLogMapper rocketmqTransactionLogMapper;

    /**
     * 执行本地事务
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {

        MessageHeaders headers = message.getHeaders();
        // 获取事务id
        String transactionId = (String) headers.get(RocketMQHeaders.TRANSACTION_ID);
        Integer uid = (Integer) headers.get("uid");
        // 可以作为传递参数

        try {
            userService.addBonusWithRocketMQLog(uid,  (Integer)o, transactionId);
            // 断网了，没有返回
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            return RocketMQLocalTransactionState.ROLLBACK ;
        }
     }

    /**
     * 回查事务
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        MessageHeaders headers = message.getHeaders();
        // 获取事务id
        String transactionId = (String) headers.get(RocketMQHeaders.TRANSACTION_ID);

        // select * from xxx where transaction_id = xxx
        RocketmqTransactionLog rocketmqTransactionLog = rocketmqTransactionLogMapper.selectOne(
                RocketmqTransactionLog.builder().transactionId(transactionId).build()
        );
        if (rocketmqTransactionLog != null) {
            return RocketMQLocalTransactionState.COMMIT;
        }
        return RocketMQLocalTransactionState.ROLLBACK ;
    }
}
