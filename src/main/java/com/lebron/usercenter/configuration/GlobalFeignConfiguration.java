package com.lebron.usercenter.configuration;

import feign.Logger;
import org.springframework.context.annotation.Bean;

/**
 * Feign 的配置类
 * 这个类别加 @Configuration , 否则必须挪到 @ComponentScan 扫描包的以外（启动类包以为），防止父子上下文
 * @EnableFeignClients (defaultConfiguration = GlobalFeignConfiguration.class)
 *
 * 优缺点同 Ribbon, 尽量使用属性配置
 *
 * <br>优先级：全局代码 < 全局属性 < 细粒度代码 < 细粒度属性</br>
 *
 * @author LeBron
 */
public class GlobalFeignConfiguration {

    @Bean
    public Logger.Level level() {

        // 让 feign 打印所有请求细节
//        Logger.Level.BASIC; 生产建议使用
        return Logger.Level.FULL;
    }
}
