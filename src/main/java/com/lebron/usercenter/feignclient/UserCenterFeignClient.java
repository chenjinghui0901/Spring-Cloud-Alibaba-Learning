package com.lebron.usercenter.feignclient;

import com.lebron.usercenter.configuration.GlobalFeignConfiguration;
import com.lebron.usercenter.domain.entity.user.User;
import com.lebron.usercenter.feignclient.fallback.UserCenterFeignClientFallback;
import com.lebron.usercenter.feignclient.fallbackfactory.UserCenterFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

/**
 * <link>http://www.imooc.com/article/289000</link>
 * <link>http://www.imooc.com/article/289005</link> 常见问题
 *
 * GlobalFeignConfiguration : 全局配置
 * UserCenterFeignClientFallback : 限流自定义处理
 * UserCenterFeignClientFallbackFactory : 限流自定义处理，比上面的更强大，会拿到异常信息，
 * UserCenterFeignClientFallback 与 UserCenterFeignClientFallbackFactory 不能同时存在
 *
 *
 * 集群流控：
 * <link>http://wwwgithub.com/alibaba/Sentinel/wiki</link>
 * <link>http://www.jianshu.com/p/bb198c08b418</link>
 *
 * @author LeBron
 */
@FeignClient(name = "user-center"
//        , configuration = GlobalFeignConfiguration.class
//        , fallback = UserCenterFeignClientFallback.class
        , fallbackFactory = UserCenterFeignClientFallbackFactory.class
)
public interface UserCenterFeignClient {

    /**
     * http://user-center/users/{id}
     * @param id
     * @return
     */
    @GetMapping("/users/{id}")
    User findById(@PathVariable("id") Integer id);

    /**
     * 多参数构造请求
     *
     * @param user
     * @return
     */
//    @GetMapping("query")
//    User query(@SpringQueryMap User user);
//
//    /**
//     * 多参数构造请求
//     * @param user
//     * @return
//     */
//    @PostMapping("post")
//    User post(@RequestBody User user);

}
