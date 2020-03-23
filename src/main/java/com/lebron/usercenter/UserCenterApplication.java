package com.lebron.usercenter;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import com.lebron.usercenter.configuration.GlobalFeignConfiguration;
import com.lebron.usercenter.rocketmq.MySink;
import com.lebron.usercenter.rocketmq.MySource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.binder.PollableMessageSource;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 文档
 * <link>https://www.cnblogs.com/xjknight/p/12349102.html</link>
 *
 * 三板斧：
 *  加依赖
 *  写配置
 *  加注解
 */
// 扫描mybatis的接口
@MapperScan("com.lebron.usercenter.dao")
@SpringBootApplication
@EnableFeignClients// (defaultConfiguration = GlobalFeignConfiguration.class)
//@EnableBinding({MySource.class, MySink.class})
@Slf4j
public class UserCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserCenterApplication.class, args);
    }


    /**
     * 在 spring 容器里面，创建一个对象，类型 RestTemplate，名称 ID 是方法名 restTemplate
     * <bean id="restTemplate" class="xxx.RestTemplate"></bean>
     *
     * LoadBalanced : 整合 Ribbon
     *
     * SentinelRestTemplate : 整合 Sentinel
     */
    @Bean
    @LoadBalanced
    @SentinelRestTemplate
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


//    @Bean
//    public ApplicationRunner runner(PollableMessageSource source,
//                                    MessageChannel dest) {
//        return args -> {
//            while (true) {
//                boolean result = source.poll(m -> {
//                    String payload = (String) m.getPayload();
//                    log.info("Received: " + payload);
//                    dest.send(MessageBuilder.withPayload(payload.toUpperCase())
//                            .copyHeaders(m.getHeaders())
//                            .build());
//                }, new ParameterizedTypeReference<String>() { });
//                if (result) {
//                    log.info("Processed a message");
//                }
//                else {
//                    log.info("Nothing to do");
//                }
//                Thread.sleep(5_000);
//            }
//        };
//    }
//
//    public static interface PolledProcessor {
//
//        @Input
//        PollableMessageSource source();
//
//        @Output
//        MessageChannel dest();
//
//    }
}
