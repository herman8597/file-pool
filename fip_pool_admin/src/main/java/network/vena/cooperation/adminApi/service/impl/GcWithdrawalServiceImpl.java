package network.vena.cooperation.adminApi.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bytecode.Throw;
import network.vena.cooperation.adminApi.dto.ApiResult;
import network.vena.cooperation.adminApi.entity.*;
import network.vena.cooperation.adminApi.mapper.GcWithdrawalMapper;
import network.vena.cooperation.adminApi.param.BaseParam;
import network.vena.cooperation.adminApi.service.IGcWithdrawalService;
import network.vena.cooperation.base.vo.TransLogQueryVo;
import network.vena.cooperation.common.constant.PojoConstants;
import network.vena.cooperation.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.hibernate.type.PostgresUUIDType;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: gc_withdrawal
 * @Author: jeecg-boot
 * @Date: 2020-04-13
 * @Version: V1.0
 */
@Service
@Slf4j
public class GcWithdrawalServiceImpl extends ServiceImpl<GcWithdrawalMapper, GcWithdrawal> implements IGcWithdrawalService {

    private static Gson gson = new Gson();

    @Autowired
    private GcDepositAddressServiceImpl gcDepositAddressService;

/*    @Value("${deposit.wallet.host}")
    private String host;*/

    @Autowired
    private WebHttpUtil webHttpUtil;

    @Autowired
    private BalanceServiceImpl balanceService;

    @Autowired
    private BalanceModifyPipelineServiceImpl balanceModifyPipelineService;

    @Autowired
    private GcAssetServiceImpl gcAssetService;

    @Override
    @Transactional
    public synchronized String audit(GcWithdrawal baseParam) {
        //    @Select("select * from gc_withdrawal where pid = #{pid} and status = 'pending' and system_status = 'WAITING_MANAGER_CHECK'")
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        GcWithdrawal gcWithdrawal = lambdaQuery()
                .eq(GcWithdrawal::getPid, baseParam.getPid()).eq(GcWithdrawal::getStatus, PojoConstants.GcWithdrawal.PENDING)
                .eq(GcWithdrawal::getSystemStatus, PojoConstants.GcWithdrawal.WAITING_MANAGER_CHECK).one();
        if (ObjectUtils.isEmpty(gcWithdrawal)) {
            throw new RuntimeException("请检查记录状态");
        }
        if (baseParam.getPass() == 1) {
           /* if (!withdraw(gcWithdrawal)) {
                throw new RuntimeException("提币提交上链服务确认失败");
            }*/
            //@Update("update gc_withdrawal set system_status = 'WAITING_WALLET_HANDLING', approve_time = #{date}, operator = #{operator} where pid = #{pid} and system_status='WAITING_MANAGER_CHECK'")
            gcWithdrawal.setSystemStatus(PojoConstants.GcWithdrawal.WAITING_WALLET_HANDLING).setApproveTime(new Date())
                    .setOperator(sysUser.getRealname()).setRemark(baseParam.getRemark());
            try {
                if (!updateById(gcWithdrawal)) {
                    log.error("审核失败_{}", gcWithdrawal);
                    return "审核失败";
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("审核失败_{}", gcWithdrawal);
                return "审核失败";
            }
        } else {
            Balance balance = balanceService.lambdaQuery()
                    .eq(Balance::getApiKey, gcWithdrawal.getApiKey()).eq(Balance::getAsset, gcWithdrawal.getAsset()).one();
            BigDecimal sub = BigDecimalUtil.sub(balance.getFrozen(), gcWithdrawal.getAmount()).subtract(gcWithdrawal.getFee());
            balance.setFrozen(sub); //扣除冻结
            balance.setAvailable(BigDecimalUtil.add(balance.getAvailable(), gcWithdrawal.getAmount()).add(gcWithdrawal.getFee())); //增加可用余额
            balance.setUpdateTime(new Date());
            UpdateWrapper<Balance> balanceUpdateWrapper = new UpdateWrapper<>();
            balanceUpdateWrapper.eq("asset", balance.getAsset()).eq("api_key", balance.getApiKey());
            if (!balanceService.update(balance, balanceUpdateWrapper)) {
                log.error("提现approve拒绝还款失败_{}", balanceService);
                throw new RuntimeException("更新余额失败");
            }
            //@Update("update gc_withdrawal set status = 'refuse', system_status = 'MANAGER_REFUSED', finished_time = #{date}, operator = #{operator} where pid = #{pid}")
            gcWithdrawal.setStatus(PojoConstants.GcWithdrawal.REFUSE).setOperator(sysUser.getRealname())
                    .setRemark(baseParam.getRemark()).setSystemStatus(PojoConstants.GcWithdrawal.MANAGER_REFUSED).setFinishedTime(new Date());
            if (!updateById(gcWithdrawal)) {
                log.error("提币approve拒绝失败_{}", gcWithdrawal);
                throw new RuntimeException("提币approve拒绝失败");
            }
            QueryWrapper<BalanceModifyPipeline> balanceModifyPipelineQueryWrapper = new QueryWrapper<>();
            balanceModifyPipelineQueryWrapper.select("id,api_key,asset,quantity,`type`,create_time,update_time,`status`,remark,service_charge,charge_asset,operator,pid,`from`,`to`");
            balanceModifyPipelineQueryWrapper.eq("pid", gcWithdrawal.getPid());
            BalanceModifyPipeline balanceModifyPipeline = balanceModifyPipelineService.getOne(balanceModifyPipelineQueryWrapper);
            /*BalanceModifyPipeline balanceModifyPipeline = balanceModifyPipelineService.lambdaQuery()
                    .eq(BalanceModifyPipeline::getPid, gcWithdrawal.getPid()).one();*/
            //@Update("update balance_modify_pipeline set status = 2, update_time = #{date}, operator = #{operator},type=4 where pid = #{pid}")
            balanceModifyPipeline.setStatus(PojoConstants.BalanceModifyPipeline.REJECT).setUpdateTime(new Date())
                    .setOperator(sysUser.getRealname()).setType(PojoConstants.BalanceModifyPipeline.WITHDRAW).setRemark(baseParam.getRemark());
            if (!balanceModifyPipelineService.updateById(balanceModifyPipeline)) {
                log.error("提币approve拒绝失败_{}", balanceModifyPipeline);
                throw new RuntimeException("提币approve拒绝失败");
            }
        }
        return "审核成功";
    }

    @Override
    public List<Balance> getWithdrawBalance() {
        List<String> assets = gcAssetService.lambdaQuery().select(GcAsset::getAsset).eq(GcAsset::getWithdraw,1).eq(GcAsset::getLocked,0).list()
                .stream().map(GcAsset::getAsset).collect(Collectors.toList());
        ArrayList<Balance> balances = new ArrayList<>();
        for (String asset : assets) {
            String strResult = webHttpUtil.post( "http://10.0.0.115:8888/api/wallet/getWithdrawBalance?symbol=" + asset, null, String.class);
            if (StringUtils.isNotBlank(strResult)) {
                Type type = new TypeToken<ApiResult<BigDecimal>>() {
                }.getType();
                ApiResult<BigDecimal> apiResult = gson.fromJson(strResult, type);
                if (apiResult.getCode() == 200 && !ObjectUtils.isEmpty(apiResult.getData())) {
                    Balance balance = new Balance();
                    balance.setAsset(asset).setAvailable(apiResult.getData());
                    balances.add(balance);
                }
            }
        }
        return balances;
    }

    private boolean withdraw(GcWithdrawal gcWithdrawal) {
        String name = CoinType.valueBySymbol(gcWithdrawal.getAsset()).getSeries().getName();
        GcDepositAddress gcDepositAddress = gcDepositAddressService.lambdaQuery()
                .eq(GcDepositAddress::getApiKey, gcWithdrawal.getApiKey()).eq(GcDepositAddress::getAsset, name).one();

        if (ObjectUtils.isEmpty(gcDepositAddress)) {
            log.error("用户管理地址有误_{}", gcDepositAddress);
            throw new RuntimeException("用户管理地址有误");
        }
        //DepositAddressDO gcDepositAddress = depositMapper.getGcDepositAddress(withdrawalDO.getApiKey(),name);


        //String url = host + "/withdraw";
        String url =  "/api/wallet/transfer";
        // String reqId = withdrawalDO.getPid();
        JSONObject param = new JSONObject();
        //param.put("pid", withdrawalDO.getPid());
        param.put("toAddress", gcWithdrawal.getAddress());
        param.put("symbol", gcWithdrawal.getAsset());
        param.put("amount", gcWithdrawal.getAmount());
        param.put("address", gcDepositAddress.getAddress());
        param.put("secret", gcDepositAddress.getSecret());
        param.put("secret", gcWithdrawal.getPid());

        // param.put("block_chain", withdrawalDO.getBlockChain());

        /*String data = param.toJSONString();
        HttpHeaders headers = DepositTask.getHeader(data, reqId, userName, secret);
        String response = RestTemplateHandler.postWithHeader(url, param, headers, null);
        JSONObject res = JSONObject.parseObject(response);*/

        ApiResult result = webHttpUtil.post(url, param, ApiResult.class);
        log.info("提现申请提交至钱包,响应_{}", result);
        if (0 == result.getCode()) {
            TransLogQueryVo transLogQueryVo = gson.fromJson(result.getData().toString(), TransLogQueryVo.class);
            if ("1".equals(transLogQueryVo.getType())) {
                return true;
            }
            return false;
        } else {
            return false;
        }

        //return ("00".equals(res.getString("response_code")));
    }
}
