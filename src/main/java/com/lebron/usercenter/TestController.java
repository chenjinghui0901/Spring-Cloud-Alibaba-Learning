package com.lebron.usercenter;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.lebron.usercenter.dao.user.UserMapper;
import com.lebron.usercenter.domain.entity.user.User;
import com.lebron.usercenter.feignclient.BaiduFeignClient;
import com.lebron.usercenter.feignclient.UserCenterFeignClient;
import com.lebron.usercenter.sentinel.TestControllerBlockHandlerClass;
import com.lebron.usercenter.sentinel.TestControllerFallbackClass;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @author LeBron
 * <p>
 * 构造器注入
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class TestController {

    private final UserMapper userMapper;

    private final RestTemplate restTemplate;

    private final DiscoveryClient discoveryClient;

    private final UserCenterFeignClient userCenterFeignClient;


    @GetMapping("/user-insert")
    public User insert() {
        User user = new User();
        user.setAvatarUrl("aaa");
        user.setBonus(100);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        userMapper.insertSelective(user);
        return user;
    }

    @GetMapping("/user-instances")
    public List<ServiceInstance> getInstances() {
        return discoveryClient.getInstances("user-center");
    }

    @GetMapping("/get/{id}")
    public User getById(@PathVariable Integer id) {
        // 版本一
//        RestTemplate restTemplate = new RestTemplate();
//        String forObject = restTemplate.getForObject(
//                "http://localhost:8080/users/{id}",
//                String.class, id);
//        System.out.println(forObject);

        // 版本二
//        List<ServiceInstance> instances = discoveryClient.getInstances("user-center");
//        String targetUrl = instances.stream()
//                // 数据变换
//                .map(instance -> instance.getUri().toString() + "/users/{id}")
//                .findFirst()
//                .orElseThrow(() -> new IllegalArgumentException("当前没有实例"));
//        String forObject = restTemplate.getForObject(targetUrl, String.class, id);
//        log.info("请求目标地址：{}", targetUrl);

        // 版本三, 自己实现客户端负载均衡
//        List<ServiceInstance> instances = discoveryClient.getInstances("user-center");
//        int index = ThreadLocalRandom.current().nextInt(instances.size());
//        String targetUrl = instances.get(index).getUri().toString() + "/users/{id}";
//        log.info("负载均衡请求目标地址：{}", targetUrl);
//        String forObject = restTemplate.getForObject(targetUrl, String.class, id);


        // 版本四, Ribbon 负载均衡, 在 restTemplate 配置 bean 上添加 @LoadBalanced 赋予负债均衡的功能
        // 1、代码不可读
        // 2、复杂的url不难以维护 ...
//        String forObject = restTemplate.getForObject(
//                "http://user-center/users/{id}",
//                String.class, id);
//        System.out.println(forObject);

        // 版本五, Feign 远程HTTP调用, 可开启 sentinel 保护
        User user = userCenterFeignClient.findById(id);
        System.out.println(user.toString());
        return user;
    }
//
//    @GetMapping("/query")
//    public User query(User user) {
//        return userCenterFeignClient.query(user);
//    }
//
//    @PostMapping("/post")
//    public User post(User user) {
//        return userCenterFeignClient.post(user);
//    }

//    private final BaiduFeignClient baiduFeignClient;
//
//    @GetMapping(" /baidu")
//    public String baiduIndex() {
//        return baiduFeignClient.index();
//    }


    @GetMapping("test-hot")
    @SentinelResource("hot")
    public String testHot(@RequestParam(required = false) String a, @RequestParam(required = false) String b) {
        return a + " " + b;
    }

    @GetMapping("test-add-flow-rule")
    @SentinelResource("hot")
    public String flowRule() {
        return "";
    }

    /**
     * 添加流控规则
     */
    private void initFlowQpsRule() {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule("/get/1");
        // set limit qps to 20
        rule.setCount(20);
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setLimitApp("default");
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }

    @GetMapping("test-sentinel-api")
    public String testSentinelAPI(@RequestParam(required = false) String a) {

        String resourceName = "test-sentinel-api";
        ContextUtil.enter(resourceName);

        // 定义一个 sentinel 保护的资源，
        Entry entry = null;
        try {
            entry = SphU.entry(resourceName);
            if (StringUtils.isBlank(a)) {
                throw new IllegalArgumentException("a不能为空");
            }
            // 保护的逻辑
            return a;
        }
        // 如果被保护的资源被限流或降级了，就会抛出这个异常
        catch (BlockException e) {
            log.warn("限流或者降级了");
        } catch (IllegalArgumentException e2) {
            // 统计 IllegalArgumentException 发送的次数、占比等等
            Tracer.trace(e2);
            return "参数非法";
        }
        finally {
            if (entry != null) {
                entry.exit();
            }
            ContextUtil.exit();
        }

        return "限流或者降级了";
    }

    /**
     * Sentinel 保护 Spring MVC
     */
    @GetMapping("test-sentinel-resource")
    @SentinelResource(value = "test-sentinel-resource",
            blockHandlerClass = TestControllerBlockHandlerClass.class,
            blockHandler = "block",
            fallbackClass = TestControllerFallbackClass.class,
            fallback = "fallback")
    public String testSentinelResource(@RequestParam(required = false) String a) {

        // 保护的逻辑
        if (StringUtils.isBlank(a)) {
            throw new IllegalArgumentException("a is not blank");
        }
        return a;
    }

    /**
     * Sentinel 保护 RestTemplate
     */
    @GetMapping("test-rest-template-sentinel/{uid}")
    public User get(@PathVariable Integer uid) {
        User user = restTemplate.getForObject("http://user-center/users/rest-template/{uid}", User.class, uid);
        return user;
    }


    /**
     * RestTemplate 传递 token
     */
    @GetMapping("token-relay/{uid}")
    public ResponseEntity<User> tokenRelay(HttpServletRequest request, @PathVariable Integer uid) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-token", request.getHeader("X-token"));

        return restTemplate.exchange(
                "http://user-center/users/rest-template/{uid}",
                HttpMethod.GET,
                new HttpEntity<>(httpHeaders),
                User.class,
                uid);
    }

     public static void main(String[] args) {

        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        List<Integer> collect = list.stream()
                .filter(i -> i < 3)
                .collect(Collectors.toList());
        System.out.println(collect);
        System.out.println(list);
    }
}
