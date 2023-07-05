package network.vena.cooperation.adminApi.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import network.vena.cooperation.adminApi.dto.BalanceDTO;
import network.vena.cooperation.adminApi.entity.Balance;
import network.vena.cooperation.adminApi.entity.BalanceModifyPipeline;
import network.vena.cooperation.adminApi.mapper.BalanceMapper;
import network.vena.cooperation.adminApi.service.IBalanceModifyPipelineService;
import network.vena.cooperation.adminApi.service.IBalanceService;
import network.vena.cooperation.common.constant.PojoConstants;
import network.vena.cooperation.util.BigDecimalUtil;
import network.vena.cooperation.util.ObjectUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;

/**
 * @Description: balance
 * @Author: jeecg-boot
 * @Date: 2020-04-13
 * @Version: V1.0
 */
@Service
@Slf4j
public class BalanceServiceImpl extends ServiceImpl<BalanceMapper, Balance> implements IBalanceService {


    @Autowired
    private IBalanceModifyPipelineService iBalanceModifyPipelineService;


    @Override
    public synchronized Boolean exchange(BalanceDTO balanceDTO) {
        //增加
        Balance balance = lambdaQuery().eq(Balance::getApiKey, balanceDTO.getApiKey()).eq(Balance::getAsset, balanceDTO.getAsset()).one();
        if (ObjectUtils.isEmpty(balance)) {
            balance = new Balance().setApiKey(balanceDTO.getApiKey()).setAsset(balanceDTO.getAsset());
        }

        balance.setAvailable(BigDecimalUtil.add(balance.getAvailable(), balanceDTO.getAvailable()));
        balance.setFrozen(BigDecimalUtil.add(balance.getFrozen(), balanceDTO.getFrozen()));
        balance.setUpdateTime(new Date());
        log.info("操作资产_{}", balanceDTO);
        UpdateWrapper<Balance> updateWrapper = new UpdateWrapper<Balance>().eq("api_key", balance.getApiKey()).eq("asset", balance.getAsset());
        if (update(balance, updateWrapper)) {
//            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            BalanceModifyPipeline balanceModifyPipeline = new BalanceModifyPipeline().setApiKey(balanceDTO.getApiKey()).setAsset(balanceDTO.getAsset()).setCreateTime(balanceDTO.getCreateTime())
                    .setRemark(balanceDTO.getRemark()).setStatus(PojoConstants.SUCCEED).setType(balanceDTO.getType()).setChargeAsset(balanceDTO.getAsset()).setOperator("管理员");
            if (!ObjectUtils.isEmpty(balanceDTO.getAvailable())) {
                balanceModifyPipeline.setQuantity(balanceDTO.getAvailable());
            } else if (!ObjectUtils.isEmpty(balanceDTO.getFrozen())){
                balanceModifyPipeline.setQuantity(balanceDTO.getFrozen());
            }
            try {
                log.info("插入资产操作日志_{}", balanceModifyPipeline);
                iBalanceModifyPipelineService.save(balanceModifyPipeline);
            } catch (Exception e) {
                log.error("插入资产操作日志失败_{}", balanceModifyPipeline);
                e.printStackTrace();
            }
            return true;
            //assetMapper.insertNewBalanceModify()
        } else {
            return false;

        }
    }
}
