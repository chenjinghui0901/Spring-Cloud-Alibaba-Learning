package com.lebron.usercenter.service.user;

import com.lebron.usercenter.dao.rocketmq.RocketmqTransactionLogMapper;
import com.lebron.usercenter.dao.user.UserMapper;
import com.lebron.usercenter.domain.entity.rocketmq.RocketmqTransactionLog;
import com.lebron.usercenter.domain.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author LeBron
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private final UserMapper userMapper;

    private final RocketmqTransactionLogMapper rocketmqTransactionLogMapper;

    public User findById(Integer id) {
        User user = userMapper.selectByPrimaryKey(id);
        return user;
    }

    @Transactional(rollbackFor = Exception.class)
    public void addBonus(Integer uid, Integer bonus) {
        User user = userMapper.selectByPrimaryKey(uid);
        user.setBonus(user.getBonus() + bonus);
        userMapper.updateByPrimaryKey(user);
    }


    @Transactional(rollbackFor = Exception.class)
    public void addBonusWithRocketMQLog(Integer uid, Integer bonus, String transactionId) {
        this.addBonus(uid, bonus);
        rocketmqTransactionLogMapper.insert(
                RocketmqTransactionLog.builder().transactionId(transactionId).log("添加积分").build()
        );
    }
}
