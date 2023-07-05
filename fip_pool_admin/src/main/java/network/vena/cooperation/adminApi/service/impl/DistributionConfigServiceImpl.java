package network.vena.cooperation.adminApi.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import network.vena.cooperation.adminApi.entity.DistributionConfig;
import network.vena.cooperation.adminApi.mapper.DistributionConfigMapper;
import network.vena.cooperation.adminApi.service.IDistributionConfigService;
import network.vena.cooperation.adminApi.service.IDistributionDetailService;
import network.vena.cooperation.common.constant.PojoConstants;
import network.vena.cooperation.util.BeanUtils;
import network.vena.cooperation.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: distribution_config
 * @Author: jeecg-boot
 * @Date: 2020-04-13
 * @Version: V1.0
 */
@Service
public class DistributionConfigServiceImpl extends ServiceImpl<DistributionConfigMapper, DistributionConfig> implements IDistributionConfigService {

    @Autowired
    private DistributionConfigMapper distributionConfigMapper;



    @Autowired
    private DistributionDetailServiceImpl distributionDetailService;


    @Override
    public DistributionConfig getLevelConf(String apiKey) {
        Integer levle = distributionConfigMapper.getLevel(apiKey);
        List<DistributionConfig> confByKey = distributionConfigMapper.getConfByKey(PojoConstants.config.LEVEL);
        Map<String, DistributionConfig> value5$map = confByKey.stream().collect(Collectors.toMap(DistributionConfig::getValue5, e -> e));
        if (ObjectUtils.isEmpty(levle)) {
            return value5$map.get("-1");
        }
        return value5$map.get(levle.toString());
    }

    @Override
    public Integer getLevel(String apiKey) {
        Integer level = distributionConfigMapper.getLevel(apiKey);
        if (ObjectUtils.isEmpty(level)) {
            return distributionDetailService.exchangeSelfPurchase(apiKey, BigDecimal.ZERO);
        }
        return level;
    }

    @Override
    @Transactional
    public boolean updateById(DistributionConfig entity) {
        List<DistributionConfig> list = lambdaQuery().eq(DistributionConfig::getValue5, entity.getValue5()).list();
        for (DistributionConfig distributionConfig : list) {
            if (entity.getId() == distributionConfig.getId()) {
                distributionConfig.setValue2(entity.getValue2());
                distributionConfig.setValue3(entity.getValue3());
            }
            distributionConfig.setValue1(entity.getValue1());
            distributionConfig.setValue6(entity.getValue6());
            distributionConfig.setUpdateTime(new Date());
        }
        return updateBatchById(list);


    }
}
