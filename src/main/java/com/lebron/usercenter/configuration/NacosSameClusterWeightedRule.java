package com.lebron.usercenter.configuration;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.core.Balancer;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 在相同集下 cluster-name 的权重负载均衡
 *
 * @author LeBron
 */
@Slf4j
public class NacosSameClusterWeightedRule extends AbstractLoadBalancerRule {

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
        // 读取配置文件，并初始化 NacosWeightedRule
    }

    @Override
    public Server choose(Object o) {
        try {
            // 找到配置文件中的集群名称 HZ
            String clusterName = nacosDiscoveryProperties.getClusterName();

            BaseLoadBalancer loadBalancer = (BaseLoadBalancer) this.getLoadBalancer();
            // 想要请求的微服务名称
            String name = loadBalancer.getName();

            // 拿到服务发现相关API
            NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();

            // 1、找到服务所有实例 A
            List<Instance> instances = namingService.selectInstances(name, true);

            // 2、过滤出同集群下的所有实例 B
            List<Instance> sameClusterInstances = instances.stream()
                    .filter(instance -> Objects.equals(instance.getClusterName(), clusterName))
                    .collect(Collectors.toList());

            // 3、如果 B 是空, 那就用A, 否则就用B
            List<Instance> instancesToBeChosen = new ArrayList<>();
            if (CollectionUtils.isEmpty(sameClusterInstances)) {
                instancesToBeChosen = instances;
                log.warn("发生跨集群调用, name: {}, clusterName: {}, instances: {}",
                        name, clusterName, instances);
            } else {
                instancesToBeChosen = sameClusterInstances;
            }

            // 4、基于权重的负载均衡算法, 返回一个实例
            Instance instance = ExtendBalancer.getHostByRandomWeight0(instancesToBeChosen);

            log.info("选择的实例是：ip:{}, port:{}, instance:{}", instance.getIp(), instance.getPort(), instance);
            return new NacosServer(instance);

        } catch (NacosException e) {
            log.error("NacosSameClusterWeightedRule#choose, e:", e);
        }
        return null;
    }
}

/**
 * 编码技巧：别人有相关的代码可以实现，但是访问权限不够，可以写一个继承类去包装一下
 */
class ExtendBalancer extends Balancer {
    public static Instance getHostByRandomWeight0(List<Instance> list) {
        return getHostByRandomWeight(list);
    }
}