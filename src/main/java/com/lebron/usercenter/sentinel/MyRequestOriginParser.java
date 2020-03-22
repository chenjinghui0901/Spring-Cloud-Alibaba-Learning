package com.lebron.usercenter.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 实现区分来源
 *
 * <link>https://www.jianshu.com/p/0fe4d78e2986</link>
 * @author LeBron
 **/
@Slf4j
@Component
public class MyRequestOriginParser implements RequestOriginParser {

    @Override
    public String parseOrigin(HttpServletRequest request) {
        // 从header中获取名为 origin 的参数并返回
        String origin = request.getParameter("origin");
        if (StringUtils.isBlank(origin)) {
            // 如果获取不到，则抛异常
            String err = "origin param must not be blank!";
            log.error("parse origin failed: {}", err);
            throw new IllegalArgumentException(err);
        }

        return origin;
    }
}