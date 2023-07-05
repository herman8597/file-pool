package network.vena.cooperation.adminApi.service.impl;

import com.alibaba.fastjson.JSONObject;
import network.vena.cooperation.adminApi.entity.AuthUser;
import network.vena.cooperation.adminApi.entity.ReplenishmentRecord;
import network.vena.cooperation.adminApi.entity.Weight;
import network.vena.cooperation.adminApi.mapper.ReplenishmentRecordMapper;
import network.vena.cooperation.adminApi.service.IReplenishmentRecordService;
import network.vena.cooperation.common.constant.PojoConstants;
import network.vena.cooperation.util.BigDecimalUtil;
import network.vena.cooperation.util.ObjectUtils;
import network.vena.cooperation.util.OrderNumberUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 补单记录
 * @Author: jeecg-boot
 * @Date: 2020-04-21
 * @Version: V1.0
 */
@Service
public class ReplenishmentRecordServiceImpl extends ServiceImpl<ReplenishmentRecordMapper, ReplenishmentRecord> implements IReplenishmentRecordService {
    @Value("${spring.profiles.active}")
    private String active;
    @Autowired
    private AuthUserServiceImpl authUserService;

    @Autowired
    private WeightServiceImpl weightService;

    @Autowired
    private BalanceServiceImpl balanceService;

    @Autowired
    private DistributionDetailServiceImpl distributionDetailService;


    @Override
    public boolean  save(ReplenishmentRecord entity) {
        AuthUser authUser = authUserService.lambdaQuery().select(AuthUser::getApiKey).eq(AuthUser::getAccount, entity.getAccount()).or().eq(AuthUser::getEmail,entity.getAccount()).one();
        if (ObjectUtils.isEmpty(authUser)) {
            throw new RuntimeException("账号不存在");
        }
        entity.setApiKey(authUser.getApiKey()).setCreateTime(new Date()).setPid(OrderNumberUtil.getOrderNumber()).setStatus(0);
        return super.save(entity);
    }


    @Transactional
    public synchronized Result<Object> audit(ReplenishmentRecord replenishmentRecord) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        ReplenishmentRecord record = lambdaQuery().eq(ReplenishmentRecord::getPid, replenishmentRecord.getPid()).one();
        if (ObjectUtils.isEmpty(record)) {
            throw new RuntimeException("查询不到记录");
        }

        if (record.getStatus() == PojoConstants.ReplenishmentRecord.CONSENT && 1 == replenishmentRecord.getIsEdit()) {
            record.setAsset(replenishmentRecord.getAsset()).setAmount(replenishmentRecord.getAmount()).setHashrate(replenishmentRecord.getHashrate());
            Weight resultWeight = weightService.lambdaQuery().eq(Weight::getRelatedId, record.getId()).eq(Weight::getApiKey, replenishmentRecord.getApiKey()).one();
            JSONObject jsonObject = JSONObject.parseObject(resultWeight.getRemark());
            jsonObject.put("人工操作补单", "修改数量");
            resultWeight.setRemark(jsonObject.toString()).setQuantity(replenishmentRecord.getHashrate());
            resultWeight.setAsset(replenishmentRecord.getAsset()).setPaymentQuantity(replenishmentRecord.getAmount());
            BigDecimal price = BigDecimalUtil.divide(replenishmentRecord.getAccount(), replenishmentRecord.getHashrate());
            resultWeight.setPrice(price);
            record.setPrice(price);
            weightService.saveOrUpdate(resultWeight);
            saveOrUpdate(record);
            return Result.ok();
        } else if (record.getStatus() == PojoConstants.ReplenishmentRecord.CONSENT) {
            throw new RuntimeException("已审核,请勿重复审核");
        }

        if (record.getStatus() != PojoConstants.ReplenishmentRecord.UNREVIEWED) {
            throw new RuntimeException("记录状态不正确");
        }

        JSONObject jsonObject = new JSONObject();
        if (StringUtils.isNotBlank(replenishmentRecord.getRemark())) {
            jsonObject.put("remark", replenishmentRecord.getRemark());
        }
        jsonObject.put("审核人", sysUser.getRealname());

        if (replenishmentRecord.getStatus() == PojoConstants.ReplenishmentRecord.CONSENT) {
            AuthUser authUser = authUserService.lambdaQuery().select(AuthUser::getAccount, AuthUser::getApiKey)
                    .eq(AuthUser::getApiKey, record.getApiKey()).one();
            if (ObjectUtils.isEmpty(authUser)) {
                throw new RuntimeException("账号不存在");
            }


            // if (replenishmentRecord.getOperType() == PojoConstants.ReplenishmentRecord.REPLACEMENT_ORDER) { //补单
            editeRecord(record, jsonObject, PojoConstants.ReplenishmentRecord.CONSENT);
            Weight weight = new Weight().setRemark(jsonObject.toString());
            weight.setQuantity(record.getHashrate());
            weight.setRelatedName("新增算力")
                    .setPaymentQuantity(record.getAmount());
            weight.setAccount(authUser.getAccount()).setApiKey(authUser.getApiKey())
                    .setInitDays(PojoConstants.ONE_YEAR).setLeftDays(PojoConstants.ONE_YEAR).setCreateTime(new Date())
                    .setQuantity(record.getHashrate());
            weight.setStatus(PojoConstants.Weight.ACCOUNT_PAID)
                    .setPrice(BigDecimalUtil.divide(record.getAmount(), record.getHashrate()));
            weight.setAsset(record.getAsset()).setRelatedEnName("新增算力").setUnit("TB").setRelatedId(record.getId());

            if (StringUtils.equalsAny(active, "space")) {
                weight.setServiceChargeRate(new BigDecimal(0.3)).setType(Integer.valueOf(record.getOperType()));
            } else if (10 == record.getOperType()) {
                weight.setServiceChargeRate(new BigDecimal(0.2)).setType(1);
            } else if (StringUtils.equalsAny(active, "filpool") && 1 == record.getOperType()) {
                weight.setServiceChargeRate(new BigDecimal(0.149)).setType(record.getOperType());
            } else if (1 == record.getOperType()) {
                weight.setServiceChargeRate(new BigDecimal(0.2)).setType(record.getOperType());
            } else if (7 == record.getOperType()) {
                weight.setServiceChargeRate(new BigDecimal(0.2)).setType(record.getOperType());
            } else {
                weight.setServiceChargeRate(new BigDecimal(0.149)).setType(Integer.valueOf(record.getOperType()));
                if (record.getOperType()==11){
                    weight.setPowerType(2);
                }
            }
            weightService.save(weight);
            distributionDetailService.exchangeSelfPurchase(record.getApiKey(), BigDecimalUtil.multiply(record.getHashrate(), 1000));
            /*} else if (replenishmentRecord.getOperType() == PojoConstants.ReplenishmentRecord.RECHARGE) { //充值
                editeRecord(record, remark, PojoConstants.ReplenishmentRecord.CONSENT);
                BalanceDTO balanceDTO = new BalanceDTO().setRemark(remark).setAvailable(record.getAmount())
                        .setType(PojoConstants.BalanceModifyPipeline.FILLING_MONEY).setAsset(PojoConstants.COIN_USDT)
                        .setCreateTime(new Date()).setApiKey(record.getApiKey());
                balanceService.exchange(balanceDTO);
            }*/
        } else if (replenishmentRecord.getStatus() == PojoConstants.ReplenishmentRecord.REFUSE) {
            editeRecord(record, jsonObject, PojoConstants.ReplenishmentRecord.REFUSE);
        }
        return Result.ok();
    }

    private void editeRecord(ReplenishmentRecord record, JSONObject remark, Integer status) {
        record.setStatus(status).setOperTime(new Date()).setRemark(remark.toString());
        saveOrUpdate(record);
    }
}
