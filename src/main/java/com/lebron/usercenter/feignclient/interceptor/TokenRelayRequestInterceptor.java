package com.lebron.usercenter.feignclient.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author LeBron
 *
 * Feign做token传递
 */
public class TokenRelayRequestInterceptor implements RequestInterceptor {


    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 从 head 里获取token
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;

        HttpServletRequest request = attributes.getRequest();

        // 校验token是否合法,如果不合法则抛异常，如果合法放行
        String token = request.getHeader("X-token");

        // 将token传递
        if (StringUtils.isNotBlank(token)) {
            requestTemplate.header("X-token", token);
        }
    }
}
