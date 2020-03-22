package com.lebron.usercenter.configuration;

import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Configuration;
import ribbonconfiguration.RibbonConfiguration;

/**
 * 全局 Ribbon 负载均衡规则  @RibbonClients(defaultConfiguration = RibbonConfiguration.class)
 * java 代码形式
 *
 * @author LeBron
 */
@Configuration
@RibbonClients(defaultConfiguration = RibbonConfiguration.class)
public class GlobalRibbonConfiguration {
}
