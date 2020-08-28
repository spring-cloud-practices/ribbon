package ribbon.demo;

import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.PingUrl;
import org.springframework.context.annotation.Bean;

public class ProductLoadbalancerConfig {

    // @Bean
    // public IPing ribbonPing() {
    //     return new PingUrl(false, "/health");
    // }

    // @Bean
    // public IRule ribbonRule() {
    //     return new RoundRobinRule();
    // }

    // @Bean
    // public ServerList<Server> ribbonServerList(IClientConfig config) {
    //     ConfigurationBasedServerList serverList = new ConfigurationBasedServerList();
    //     serverList.initWithNiwsConfig(config);
    //     return serverList;
    // }

    // @Bean
    // public ServerListSubsetFilter serverListFilter() {
    //     ServerListSubsetFilter filter = new ServerListSubsetFilter();
    //     return filter;
    // }

}
