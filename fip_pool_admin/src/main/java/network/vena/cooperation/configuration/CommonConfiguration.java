package network.vena.cooperation.configuration;

import org.jeecg.config.ShiroConfig;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;


@Configuration
@AutoConfigureAfter(ShiroConfig.class)
public class CommonConfiguration {

}
