package com.lebron.usercenter;

import com.lebron.usercenter.domain.message.UserAddBonusMsgDTO;
import com.lebron.usercenter.rocketmq.MySource;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

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

        // 版本一
//        rocketMQTemplate.convertAndSend("add-bonus",
//                UserAddBonusMsgDTO.builder()
//                        .uid(uid)
//                        .bonus(bonus)
//                        .build());

        // 版本二，分布式事务
        String transactionId = UUID.randomUUID().toString();
        rocketMQTemplate.sendMessageInTransaction(
                "tx-add-bonus-group",
                "add-bonus",
                MessageBuilder
                        .withPayload(UserAddBonusMsgDTO.builder().uid(uid).bonus(bonus).build())
                        .setHeader(RocketMQHeaders.TRANSACTION_ID, transactionId)
                        // header 有妙用
                        .setHeader("uid", uid)
                        .build(),
                // 可以作为传递参数
                bonus);

        return "success";
    }

    // stream 流测试
//    private final MySource mySource;
//
//    @RequestMapping("testStream-my-source")
//    public String testStreamMySource() {
//        mySource.output().send(
//                MessageBuilder.withPayload(
//                        "test-stream 消息体"
//                ).build()
//        );
//        return "success";
//    }
}
