package com.lebron.usercenter.controller.user;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.lebron.usercenter.auth.CheckAuthorization;
import com.lebron.usercenter.auth.CheckLogin;
import com.lebron.usercenter.domain.entity.user.User;
import com.lebron.usercenter.service.user.UserService;
import com.lebron.usercenter.util.JwtOperator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LeBron
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;

    private final WxMaService wxMaService;

    private final JwtOperator jwtOperator;

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
    @CheckLogin
    @CheckAuthorization("admin")
    public User findById(@PathVariable Integer id) {
        log.info("我被请求了, port : " + port + port2);
        return userService.findById(id);
    }

    @GetMapping("/rest-template/{id}")
    public User findById0(@PathVariable Integer id) {
        log.info("我被请求了, rest-template/port : " + port + port2);
        return userService.findById(id);
    }

    /**
     * 模拟生成token（假的登录）
     */
    @PostMapping("/gen-token")
    public String genToken() {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", 1);
        userInfo.put("wxNickname", "lebron");
        userInfo.put("role", "user");
        String token = jwtOperator.generateToken(userInfo);
        return token;
    }

    @PostMapping("/login")
    public String login(String code, String avatarUrl, String nickname) throws WxErrorException {
        log.info("我被请求了, login: " + port + port2);
        WxMaJscode2SessionResult sessionInfo = wxMaService.getUserService().getSessionInfo(code);

        // 微信的 openId ，用户在微信的唯一标识
        String openid = sessionInfo.getOpenid();

        // 看用户是否注册，如果没有注册则直接插入新数据
        User user = userService.login(nickname, avatarUrl, openid);

        // 如果注册则直接颁发token
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("wxNickname", user.getWxNickname());
        userInfo.put("role", user.getRoles());
        String token = jwtOperator.generateToken(userInfo);

        log.info("用户: {} 登录成功， 生成的token: {}, 有效期: {}", user, token, jwtOperator.getExpirationDateFromToken(token));

        return token;
    }


}
