package com.lebron.usercenter.feignclient.fallback;

import com.lebron.usercenter.domain.entity.user.User;
import com.lebron.usercenter.feignclient.UserCenterFeignClient;
import org.springframework.stereotype.Component;

/**
 * @author LeBron
 */
//@Component
public class UserCenterFeignClientFallback implements UserCenterFeignClient {
    @Override
    public User findById(Integer id) {
        User user = new User();
        user.setId(id);
        user.setWxNickname("UserCenterFeignClientFallback");
        return user;
    }

//    @Override
//    public User query(User user) {
//        return null;
//    }
//
//    @Override
//    public User post(User user) {
//        return null;
//    }

}

