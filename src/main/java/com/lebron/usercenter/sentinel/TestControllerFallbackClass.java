package com.lebron.usercenter.sentinel;

import lombok.extern.slf4j.Slf4j;

/**
 * @author LeBron
 */
@Slf4j
public class TestControllerFallbackClass {

    /**
     * 处理降级
     *  1.6可以支持 Throwable
     * @param a
     * @return
     */
    public static String fallback(String a) {
        log.warn("限流或者降级了 fallback");
        return "限流或者降级了 fallback";
    }
}
