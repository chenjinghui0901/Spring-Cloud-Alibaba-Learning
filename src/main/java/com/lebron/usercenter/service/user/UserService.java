package com.lebron.usercenter.service.user;

import com.lebron.usercenter.dao.user.UserMapper;
import com.lebron.usercenter.domain.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author LeBron
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private final UserMapper userMapper;

    public User findById(Integer id) {
        User user = userMapper.selectByPrimaryKey(id);
        return user;
    }
}
