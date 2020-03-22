package ribbonconfiguration;

import com.lebron.usercenter.configuration.NacosFinalRule;
import com.lebron.usercenter.configuration.NacosSameClusterWeightedRule;
import com.lebron.usercenter.configuration.NacosWeightedRule;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 定制 Ribbon 负载均衡规则
 *
 * spring 的上下文和 Ribbon 的上下文
 *
 * 不能放在 Application 的包下面, 父子上下文不能重叠, 如果重叠可能会导致事务不成功
 * 不能被 @ComponentScan 重复扫描, 否则就会被所有的 @RibbonClients 共享，就会变成一个全局的配置，不能指定使用
 *
 * 代码配置
 *     优点：基于代码，灵活
 *     缺点：有小坑，父子上下文，线下修改需要重新打包、发布
 *
 * 属性配置 （推荐使用）
 *     优点：配置更直观、无需打包发布、优先级比代码配置更高
 *     缺点：极端情况下没有代码灵活
 * @author LeBron
 */
@Configuration
public class RibbonConfiguration {

    @Bean
    public IRule ribbonRule() {
//        return new RandomRule();
//        return new NacosWeightedRule();
//        return new NacosSameClusterWeightedRule();
        return new NacosFinalRule();
    }
}
