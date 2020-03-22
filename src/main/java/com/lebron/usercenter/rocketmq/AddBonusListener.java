package com.lebron.usercenter.rocketmq;

import com.lebron.usercenter.dao.user.UserMapper;
import com.lebron.usercenter.domain.entity.user.User;
import com.lebron.usercenter.domain.message.UserAddBonusMsgDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author LeBron
 */
@Service
@RocketMQMessageListener(consumerGroup = "consumer-group", topic = "add-bonus")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class AddBonusListener implements RocketMQListener<UserAddBonusMsgDTO> {

    private final UserMapper userMapper;

    @Override
    public void onMessage(UserAddBonusMsgDTO userAddBonusMsgDTO) {
        // 当收到的消息的业务
        User user = userMapper.selectByPrimaryKey(userAddBonusMsgDTO.getUid());
        user.setBonus(user.getBonus() + userAddBonusMsgDTO.getBonus());
        userMapper.updateByPrimaryKey(user);

        log.info("AddBonusListener#onMessage success. userAddBonusMsgDTO : {}", userAddBonusMsgDTO);

    }
}
