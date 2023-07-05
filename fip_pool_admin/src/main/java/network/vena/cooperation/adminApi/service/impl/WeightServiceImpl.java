package network.vena.cooperation.adminApi.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import network.vena.cooperation.adminApi.dto.WeightDTO;
import network.vena.cooperation.adminApi.entity.Weight;
import network.vena.cooperation.adminApi.mapper.WeightMapper;
import network.vena.cooperation.adminApi.service.IWeightService;
import network.vena.cooperation.util.BigDecimalUtil;
import network.vena.cooperation.util.WebHttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: weight
 * @Author: jeecg-boot
 * @Date: 2020-04-13
 * @Version: V1.0
 */
@Service
public class WeightServiceImpl extends ServiceImpl<WeightMapper, Weight> implements IWeightService {
    private static Gson gson = new Gson();


    @Autowired
    private WeightMapper weightMapper;

    @Autowired
    private WebHttpUtil webHttpUtil;

    @Autowired
    private GcAssetServiceImpl gcAssetService;

    @Autowired
    private DistributionDetailServiceImpl distributionDetailService;

    @Override
    @Transactional
    public void exchangeWeight(WeightDTO weightDTO) {

        if (2 == weightDTO.getOperation()) {
            weightDTO.setQuantity(weightDTO.getQuantity().multiply(new BigDecimal(-1)));
        }
        Set<Integer> activity = Set.of(2, 6, 5); //活动奖励 type 6
        Set<Integer> rests = Set.of(8, 4); //推广奖励 type 9

        List<Weight> weightDAOList = new ArrayList<>();

        if (activity.contains(weightDTO.getType())) {
            for (Integer integer : activity) {
                weightDAOList.addAll(weightMapper.getWeightAllByApiKeyAndType(weightDTO.getApiKey(), integer, new Date()));
            }
        } else if (rests.contains(weightDTO.getType())) {
            for (Integer rest : rests) {
                weightDAOList.addAll(weightMapper.getWeightAllByApiKeyAndType(weightDTO.getApiKey(), rest, new Date()));
            }
        } else if (weightDTO.getType() == 1) {
            weightDAOList = weightMapper.getMinimumWeightAllByApiKeyAndType(weightDTO.getApiKey(), weightDTO.getType(), new Date());
        } else if (weightDTO.getType() == 10) {
            weightDAOList = weightMapper.getBeBeneathWeightAllByApiKeyAndType(weightDTO.getApiKey(), 1, new Date());
        } else {
            weightDAOList.addAll(weightMapper.getWeightAllByApiKeyAndType(weightDTO.getApiKey(), weightDTO.getType(), new Date()));
        }

        distributionDetailService.exchangeSelfPurchase(weightDTO.getApiKey(), BigDecimalUtil.multiply(weightDTO.getQuantity(), 1000));
        //List<Weight> weights = weightMapper.listValidHashrateBytype(weightDTO.getApiKey(), new Date(), weightDTO.getType());


        BigDecimal divide = BigDecimalUtil.divide(weightDTO.getQuantity(), weightDAOList.size());


        BigDecimal pre = BigDecimal.ZERO;

        for (Weight weight : weightDAOList) {
            BigDecimal amount = divide;
            if ("GB".equals(weight.getUnit())) {
                weight.setUnit("TB");
                weight.setQuantity(BigDecimalUtil.divide(weight.getQuantity(), 1000));
            }
            if (2 == weightDTO.getOperation()) {
                pre = BigDecimalUtil.add(pre, amount);
                if (BigDecimalUtil.greater(pre.abs(), weight.getQuantity())) {
                    pre = BigDecimalUtil.add(pre, weight.getQuantity());
                    weight.setQuantity(BigDecimal.ZERO);
                } else {
                    weight.setQuantity(BigDecimalUtil.add(pre, weight.getQuantity()));
                    pre = BigDecimalUtil.sub(pre, pre);
                }
            } else {
                weight.setQuantity(weight.getQuantity().add(amount));
            }
        }

        saveOrUpdateBatch(weightDAOList);
        distributionDetailService.exchangeSelfPurchase(weightDTO.getApiKey(), BigDecimalUtil.multiply(weightDTO.getQuantity(), 1000));
    }

    public void edit(Weight baseParam) {
        Weight weight = lambdaQuery().eq(Weight::getId, baseParam.getId()).one();
        if (Set.of(0, 2).contains(weight.getStatus())) {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("remark", sysUser.getRealname() + "人工修改价格");
            weight.setPaymentQuantity(baseParam.getPaymentQuantity());
            weight.setUpdateTime(new Date()).setRemark(jsonObject.toString());
            weight.setCreateTime(baseParam.getCreateTime());
            saveOrUpdate(weight);
        } else if (1 == weight.getStatus()) {
            //String remark = weight.getRemark();
            BigDecimal quantity = baseParam.getQuantity();
            /*if (StringUtils.isNotBlank(remark)) {
                JSONObject jsonObject = JSONObject.parseObject(remark);
                String newRemark = jsonObject.get("remark").toString() + ",人工修改算力:" + quantity;
                jsonObject.put("remark", newRemark);
                weight.setRemark(jsonObject.toString());
            }*/
            weight.setServiceChargeRate(baseParam.getServiceChargeRate());
            if ("GB".equals(weight.getUnit())) {
                weight.setQuantity(BigDecimalUtil.multiply(quantity, 1000));
            } else {
                weight.setQuantity(quantity);
            }
            weight.setUpdateTime(new Date());
            weight.setCreateTime(baseParam.getCreateTime());
            saveOrUpdate(weight);
        }
    }


}
