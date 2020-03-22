package com.lebron.usercenter.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sentinel 错误页面优化
 *
 * @author LeBron
 */
@Component
public class MyUrlBlockHandler implements BlockExceptionHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws Exception {
        ErrorResponse errorResponse = null;
        if (e instanceof AuthorityException) {
            errorResponse = ErrorResponse.builder()
                    .code(101)
                    .msg("授权规则不通过")
                    .build();
        } else if (e instanceof DegradeException) {
            errorResponse = ErrorResponse.builder()
                    .code(102)
                    .msg("降级了")
                    .build();
        } else if (e instanceof FlowException) {
            errorResponse = ErrorResponse.builder()
                    .code(103)
                    .msg("限流了")
                    .build();
        }  else if (e instanceof ParamFlowException) {
            errorResponse = ErrorResponse.builder()
                    .code(104)
                    .msg("热点参数限流")
                    .build();
        } else if (e instanceof SystemBlockException) {
            errorResponse = ErrorResponse.builder()
                    .code(105)
                    .msg("系统规则（负载、、、不满足要求）")
                    .build();
        }


        // http 状态码
        httpServletResponse.setStatus(500);
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);

        new ObjectMapper().writeValue(httpServletResponse.getWriter(), errorResponse);
    }
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class ErrorResponse {
    int code;
    String msg;
}

