package com.lebron.usercenter.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.stereotype.Service;

/**
 * @author LeBron
 */
//@Service
@Slf4j
public class MyStreamConsumer {

    @StreamListener(MySink.MY_INPUT)
    public void receive(String messageBody) {
        log.info("通过 Stream 收到了消息， messageBody：{}", messageBody);
        throw new IllegalArgumentException("抛异常");
    }

    @StreamListener
    public void error(Message<?> message) {
        ErrorMessage errorMessage = (ErrorMessage) message;
        log.error("发生异常，errorMessage={}", errorMessage);
    }
}
