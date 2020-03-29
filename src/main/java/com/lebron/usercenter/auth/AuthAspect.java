package com.lebron.usercenter.auth;

import com.lebron.usercenter.domain.entity.user.User;
import com.lebron.usercenter.service.user.UserService;
import com.lebron.usercenter.util.JwtOperator;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author LeBron
 * <p>
 * 切面
 */
@Aspect
@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AuthAspect {

    private final JwtOperator jwtOperator;

    private final UserService userService;

    @Around("@annotation(com.lebron.usercenter.auth.CheckLogin)")
    public Object checkLogin(ProceedingJoinPoint point) throws Throwable {
        checkToken();
        return point.proceed();
    }

    @Around("@annotation(com.lebron.usercenter.auth.CheckAuthorization)")
    public Object checkAuthorization(ProceedingJoinPoint point) throws Throwable {
        try {
            // 验证token是否合法
            checkToken();

            // 验证用户角色是否匹配
            HttpServletRequest httpServletRequest = getHttpServletRequest();
            String role = (String) httpServletRequest.getAttribute("role");

            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();
            CheckAuthorization annotation = method.getAnnotation(CheckAuthorization.class);

            String value = annotation.value();
            if (!Objects.equals(role, value)) {
                throw new SecurityException("用户无权访问！");
            }

        } catch (Throwable throwable) {
            throw new SecurityException("用户无权访问！", throwable);
        }
        return point.proceed();
    }

    private void checkToken() {
        try {
            HttpServletRequest request = getHttpServletRequest();

            // 校验token是否合法,如果不合法则抛异常，如果合法放行
            String token = request.getHeader("X-token");
            Boolean validate = jwtOperator.validateToken(token);
            if (!validate) {
                throw new SecurityException("token不合法");
            }

            // 如果校验成功，那么就将用户信息设置到request的attribute里面
            Claims claims = jwtOperator.getClaimsFromToken(token);
            Integer uid = (Integer) claims.get("id");
            User user = userService.findById(uid);
            request.setAttribute("user", user);
        } catch (Throwable throwable) {
            throw new SecurityException("token不合法");
        }
    }

    private HttpServletRequest getHttpServletRequest() {
        // 从 head 里获取token
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;

        return attributes.getRequest();
    }
}
