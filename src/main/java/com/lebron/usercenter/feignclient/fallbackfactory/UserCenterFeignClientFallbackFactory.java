package com.lebron.usercenter.feignclient.fallbackfactory;

import com.lebron.usercenter.domain.entity.user.User;
import com.lebron.usercenter.feignclient.UserCenterFeignClient;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author LeBron
 */
@Slf4j
@Component
public class UserCenterFeignClientFallbackFactory implements FallbackFactory<UserCenterFeignClient> {

    @Override
    public UserCenterFeignClient create(Throwable throwable) {
        return new UserCenterFeignClient() {
            @Override
            public User findById(Integer id) {
                log.warn("远程调用被限流了", throwable);
                User user = new User();
                user.setId(id);
                user.setWxNickname("UserCenterFeignClientFallbackFactory");
                return user;
            }

//            @Override
//            public User query(User user) {
//                return null;
//            }
//
//            @Override
//            public User post(User user) {
//                return null;
//            }
        };
    }
}
