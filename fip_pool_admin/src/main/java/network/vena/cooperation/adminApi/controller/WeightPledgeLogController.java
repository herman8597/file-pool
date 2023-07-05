package network.vena.cooperation.adminApi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import network.vena.cooperation.adminApi.entity.AuthUser;
import network.vena.cooperation.adminApi.entity.DictInfo;
import network.vena.cooperation.adminApi.entity.WeightPledgeLog;
import network.vena.cooperation.adminApi.service.IAuthUserService;
import network.vena.cooperation.adminApi.service.IDictInfoService;
import network.vena.cooperation.adminApi.service.IWeightPledgeLogService;
import network.vena.cooperation.adminApi.vo.WeightPledgeVo;
import network.vena.cooperation.common.constant.DictConstant;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.SqlInjectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@RestController
@RequestMapping("/adminApi/weightPledgeLog")
@Slf4j
@Api(tags = {"算力质押API"})
public class WeightPledgeLogController extends JeecgController<WeightPledgeLog, IWeightPledgeLogService> {
    @Autowired
    private IWeightPledgeLogService weightPledgeLogService;
    @Autowired
    private IDictInfoService dictInfoService;
    @Autowired
    private IAuthUserService authUserService;


    @AutoLog("算力购买质押列表")
    @GetMapping("list")
    @ApiOperation("算力购买质押列表")
    public Result<IPage<WeightPledgeLog>> list(WeightPledgeLog pledgeLog,
                                               @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                               HttpServletRequest req) throws Exception {
        String account = pledgeLog.getAccount();
        if (!StringUtils.isEmpty(account)) {
            pledgeLog.setAccount(null);
        }
        QueryWrapper<WeightPledgeLog> wr = QueryGenerator.initQueryWrapper(pledgeLog, req.getParameterMap());
        if (!StringUtils.isEmpty(account)) {
            SqlInjectionUtil.filterContent(account);
            wr.inSql("api_key", " SELECT api_key FROM auth_user WHERE account='" + account + "'");
        }
        Page<WeightPledgeLog> page = new Page<>(pageNo, pageSize);
        IPage<WeightPledgeLog> pageList = weightPledgeLogService.page(page, wr);
        for (WeightPledgeLog record : pageList.getRecords()) {
            AuthUser user = authUserService.lambdaQuery().eq(AuthUser::getApiKey, record.getApiKey()).one();
            record.setAccount(StringUtils.isEmpty(user.getPhone()) ? user.getEmail() : user.getPhone());
        }
        Result<IPage<WeightPledgeLog>> r = new Result<>();
        r.setSuccess(true);
        r.setCode(CommonConstant.SC_OK_200);
        r.setMessage("成功");
        r.setResult(pageList);
        return r;
    }


    @GetMapping("setting")
    @AutoLog("获取算力质押设置")
    @ApiOperation("获取算力质押设置")
    public Result<WeightPledgeVo> setting() throws Exception {
        DictInfo pledge = dictInfoService.lambdaQuery().eq(DictInfo::getDictKey, DictConstant.PLEDGE_FIL_AMOUNT).one();
        DictInfo gas = dictInfoService.lambdaQuery().eq(DictInfo::getDictKey, DictConstant.PLEDGE_FIL_GAS).one();
        WeightPledgeVo vo = new WeightPledgeVo();
        vo.setPledgeAmount(new BigDecimal(pledge.getValue())).setGas(new BigDecimal(gas.getValue()));
        Result<WeightPledgeVo> r = new Result<>();
        r.setSuccess(true);
        r.setCode(CommonConstant.SC_OK_200);
        r.setMessage("成功");
        r.setResult(vo);
        return r;
    }

    @PostMapping("updateSetting")
    @AutoLog("更新算力质押设置")
    @ApiOperation("更新算力质押设置")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> updateSetting(@RequestBody WeightPledgeVo vo) throws Exception {
        if (vo.getPledgeAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return Result.error("质押币数量不能小于0");
        }
        if (vo.getGas().compareTo(BigDecimal.ZERO) <= 0) {
            return Result.error("GAS币数量不能小于0");
        }
        DictInfo pledge = dictInfoService.lambdaQuery().eq(DictInfo::getDictKey, DictConstant.PLEDGE_FIL_AMOUNT).one();
        DictInfo gas = dictInfoService.lambdaQuery().eq(DictInfo::getDictKey, DictConstant.PLEDGE_FIL_GAS).one();
        pledge.setValue(vo.getPledgeAmount().stripTrailingZeros().toPlainString());
        gas.setValue(vo.getGas().stripTrailingZeros().toPlainString());
        dictInfoService.updateById(pledge);
        dictInfoService.updateById(gas);
        return Result.ok(true);
    }
}
