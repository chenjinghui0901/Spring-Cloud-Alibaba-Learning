package com.lebron.usercenter;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import com.lebron.usercenter.configuration.GlobalFeignConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
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
@MapperScan("com.lebron")
@SpringBootApplication
@EnableFeignClients// (defaultConfiguration = GlobalFeignConfiguration.class)
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
}
