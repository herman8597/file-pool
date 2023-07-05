package network.vena.cooperation.adminApi.service.impl;

import com.alibaba.fastjson.JSONObject;
import network.vena.cooperation.adminApi.entity.AuthInvitedPipeline;
import network.vena.cooperation.adminApi.entity.DistributionConfig;
import network.vena.cooperation.adminApi.entity.DistributionDetail;
import network.vena.cooperation.adminApi.entity.Weight;
import network.vena.cooperation.adminApi.mapper.DistributionConfigMapper;
import network.vena.cooperation.adminApi.mapper.DistributionDetailMapper;
import network.vena.cooperation.adminApi.mapper.WeightMapper;
import network.vena.cooperation.adminApi.service.IDistributionDetailService;
import network.vena.cooperation.common.constant.PojoConstants;
import network.vena.cooperation.util.BigDecimalUtil;
import network.vena.cooperation.util.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: distribution_detail
 * @Author: jeecg-boot
 * @Date: 2020-04-13
 * @Version: V1.0
 */
@Service
public class DistributionDetailServiceImpl extends ServiceImpl<DistributionDetailMapper, DistributionDetail> implements IDistributionDetailService {
    @Value("${spring.profiles.active}")
    private String active;

    @Autowired
    private DistributionConfigMapper distributionConfigMapper;

    @Autowired
    private WeightMapper weightMapper;

    @Autowired
    private AuthInvitedPipelineServiceImpl authInvitedPipelineService;


    @Override
    public Integer exchangeSelfPurchase(String apiKey, BigDecimal quantity) {
        DistributionDetail distributionDetail = lambdaQuery().eq(DistributionDetail::getApiKey, apiKey).one();
        if (ObjectUtils.isEmpty(distributionDetail)) {
            distributionDetail = new DistributionDetail().setApiKey(apiKey);
            distributionDetail.setChildrenPurchase(BigDecimal.ZERO);
            distributionDetail.setGrandchildrenPurchase(BigDecimal.ZERO);
            distributionDetail.setSelfPurchase(BigDecimal.ZERO);
        }
        BigDecimal sumSelfQuantityByApiKey = weightMapper.sumSelfQuantityByApiKey(apiKey);
        if (ObjectUtils.isEmpty(sumSelfQuantityByApiKey)) {
            sumSelfQuantityByApiKey = BigDecimal.ZERO;
        }


        BigDecimal one = BigDecimal.ZERO;
        BigDecimal two = BigDecimal.ZERO;


        List<String> oneApiKeys = authInvitedPipelineService.lambdaQuery().eq(AuthInvitedPipeline::getInviteApiKey, apiKey).list().stream().map(AuthInvitedPipeline::getApiKey).collect(Collectors.toList());
        if (!ObjectUtils.isEmpty(oneApiKeys)) {
            one = weightMapper.sumQuantityInApiKeys(oneApiKeys);
            List<String> twoApiKeys = authInvitedPipelineService.lambdaQuery().in(AuthInvitedPipeline::getInviteApiKey, oneApiKeys).list().stream().map(AuthInvitedPipeline::getApiKey).collect(Collectors.toList());
            if (!ObjectUtils.isEmpty(twoApiKeys)) {
                two = weightMapper.sumQuantityInApiKeys(twoApiKeys);
            }

        }


        distributionDetail.setUpdateTime(new Date()).setSelfPurchase(sumSelfQuantityByApiKey);
        distributionDetail.setChildrenPurchase(one);
        distributionDetail.setGrandchildrenPurchase(two);
        List<DistributionConfig> confByKey = distributionConfigMapper.getConfByKey(PojoConstants.config.LEVEL);
        Map<String, DistributionConfig> level$Map = confByKey.stream().collect(Collectors.toMap(DistributionConfig::getValue5, e -> e));
        BigDecimal weightTotal = distributionDetail.getSelfPurchase().add(distributionDetail.getChildrenPurchase()).add(distributionDetail.getGrandchildrenPurchase());
        BigDecimal current;
        if (!StringUtils.equals(active, "hulihui") && BigDecimalUtil.greater(weightTotal, level$Map.get("1").getValue1())) {
            current = weightTotal;
        } else {
            current = sumSelfQuantityByApiKey;
        }


        Optional<DistributionConfig> first = confByKey.stream().filter(e -> BigDecimalUtil.less(e.getValue1(), current) || BigDecimalUtil.equal(e.getValue1(), current))
                .sorted(Comparator.comparing(DistributionConfig::getValue1).reversed()).findFirst();
        Integer level = -1;
        if (!ObjectUtils.isEmpty(first)) {
            level = Integer.valueOf(first.get().getValue5());
        }
        distributionDetail.setLevel(level);
        updateById(distributionDetail);
        return distributionDetail.getLevel();
    }


    @Cacheable(value = "getByApiKey", key = "#apiKey")
    public DistributionDetail getByApiKey(String apiKey) {
        DistributionDetail one = lambdaQuery().eq(DistributionDetail::getApiKey, apiKey).one();
        if (ObjectUtils.isEmpty(one)) {
            one = new DistributionDetail().setApiKey(apiKey).setMarketLevel("0").setUpdateTime(new Date());
            one.setChildrenPurchase(BigDecimal.ZERO);
            one.setGrandchildrenPurchase(BigDecimal.ZERO);
            save(one);
            return one;
        }
        Boolean falg=false;
        if (ObjectUtils.isEmpty(one.getMarketLevel())) {
            one.setMarketLevel("0");
            falg=true;
        }
        if (ObjectUtils.isEmpty(one.getOperatingLevel())) {
            one.setOperatingLevel("0");
            falg=true;
        }
        if (falg) {
            saveOrUpdate(one);
        }
        return one;
    }
}
