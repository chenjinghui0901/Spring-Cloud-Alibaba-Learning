package com.lebron.usercenter;

import com.lebron.usercenter.domain.message.UserAddBonusMsgDTO;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RocketMQ
 *
 * @author LeBron
 */
@RestController
@RequestMapping("/rocket-mq")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RocketMQController {

    private final RocketMQTemplate rocketMQTemplate;

    @RequestMapping("send")
    public String send(Integer uid, Integer bonus) {

        rocketMQTemplate.convertAndSend("add-bonus",
                UserAddBonusMsgDTO.builder()
                        .uid(uid)
                        .bonus(bonus)
                        .build());
        return "success";
    }
}
