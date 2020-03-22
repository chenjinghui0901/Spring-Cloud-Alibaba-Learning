package com.lebron.usercenter.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.UrlCleaner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * RESTful URL支持
 *
 * @author LeBron
 **/
@Slf4j
@Component
public class MyUrlCleaner implements UrlCleaner {

    @Override
    public String clean(String originUrl) {
        // 让 aaa/1 和 aaa/2 的返回值一样
        // 返回 aaa/{number}
        String[] split = originUrl.split("/");

        // 将数字转换为特定的占位标识符
        return Arrays.stream(split)
                .map(s -> NumberUtils.isCreatable(s) ? "{number}" : s)
                .reduce((a, b) -> a + "/" + b)
                .orElse("");
    }
}
