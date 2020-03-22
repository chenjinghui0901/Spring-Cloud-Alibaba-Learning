package com.lebron.usercenter.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Feign 脱离 Ribbon 的使用, 可以访问未注册 nacos 的url
 *
 * name or value 要指定名称
 *
 * @author LeBron
 */
@FeignClient(name = "baiduClient", url = "www.baidu.com")
public interface BaiduFeignClient {

    @GetMapping("")
    String index();
}
