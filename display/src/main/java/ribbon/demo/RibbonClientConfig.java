package ribbon.demo;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@RibbonClients({
        @RibbonClient(name = "product", configuration = {ProductLoadbalancerConfig.class})
})
public class RibbonClientConfig {
}
