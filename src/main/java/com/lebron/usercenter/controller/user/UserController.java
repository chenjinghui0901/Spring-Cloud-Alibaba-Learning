package com.lebron.usercenter.controller.user;

import com.lebron.usercenter.domain.entity.user.User;
import com.lebron.usercenter.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LeBron
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * @Value 不能直接注入 static 静态的属性, 注入静态属性需要用 set 方法注入
     */
    @Value("${server.port}")
    private int port;

    private static int port2;

    @Value("${server.port}")
    public void setPort2(int port) {
        port2 = port;
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable Integer id) {
        log.info("我被请求了, port : " + port + port2);
        return userService.findById(id);
    }

    @GetMapping("/rest-template/{id}")
    public User findById0(@PathVariable Integer id) {
        log.info("我被请求了, rest-template/port : " + port + port2);
        return userService.findById(id);
    }


}
