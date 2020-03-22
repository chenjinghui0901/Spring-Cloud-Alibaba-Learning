package com.lebron.usercenter.sentinel;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author LeBron
 */
@Slf4j
public class TestControllerBlockHandlerClass {

    /**
     * 处理限流或降级
     *
     * @param a
     * @param e
     * @return
     */
    public static String block(String a, BlockException e) {
        log.warn("限流或者降级了 block");
        return "限流或者降级了  block";
    }
}
