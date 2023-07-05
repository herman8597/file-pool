package network.vena.cooperation.adminApi.service;

import network.vena.cooperation.adminApi.entity.DistributionConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: distribution_config
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
public interface IDistributionConfigService extends IService<DistributionConfig> {

    DistributionConfig getLevelConf(String apiKey);

    Integer getLevel(String apiKey);



}
