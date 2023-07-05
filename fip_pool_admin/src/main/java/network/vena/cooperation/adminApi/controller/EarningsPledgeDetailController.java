package network.vena.cooperation.adminApi.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.druid.sql.visitor.functions.If;
import network.vena.cooperation.adminApi.dto.BalanceDTO;
import network.vena.cooperation.adminApi.entity.AuthUser;
import network.vena.cooperation.adminApi.entity.DictInfo;
import network.vena.cooperation.adminApi.entity.PledgeLog;
import network.vena.cooperation.adminApi.service.impl.*;
import network.vena.cooperation.adminApi.vo.PledgeEditVo;
import network.vena.cooperation.common.constant.PojoConstants;
import network.vena.cooperation.util.BigDecimalUtil;
import network.vena.cooperation.util.DateUtils;
import network.vena.cooperation.util.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.SqlInjectionUtil;
import network.vena.cooperation.adminApi.entity.EarningsPledgeDetail;
import network.vena.cooperation.adminApi.service.IEarningsPledgeDetailService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description: earnings_pledge_detail
 * @Author: jeecg-boot
 * @Date: 2020-11-30
 * @Version: V1.0
 */
@RestController
@RequestMapping("/adminApi/earningsPledgeDetail")
@Slf4j
public class EarningsPledgeDetailController extends JeecgController<EarningsPledgeDetail, IEarningsPledgeDetailService> {
    @Autowired
    private EarningsPledgeDetailServiceImpl earningsPledgeDetailService;
    @Autowired
    private AuthUserServiceImpl authUserService;
    @Autowired
    private DictInfoServiceImpl dictInfoService;

    /**
     * 分页列表查询
     *
     * @param earningsPledgeDetail
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<?> queryPageList(EarningsPledgeDetail earningsPledgeDetail,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        IPage<EarningsPledgeDetail> pageList = getEarningsPledgeDetailIPage(earningsPledgeDetail, pageNo, pageSize, req);
        return Result.ok(pageList);
    }

    private IPage<EarningsPledgeDetail> getEarningsPledgeDetailIPage(EarningsPledgeDetail earningsPledgeDetail, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        String accountParam = earningsPledgeDetail.getAccount();
        if (StringUtils.isNotBlank(accountParam)) {
            earningsPledgeDetail.setAccount(null);
        }
        QueryWrapper<EarningsPledgeDetail> queryWrapper = QueryGenerator.initQueryWrapper(earningsPledgeDetail, req.getParameterMap());
        if (StringUtils.isNotBlank(accountParam)) {
            SqlInjectionUtil.filterContent(accountParam);
            queryWrapper.inSql("api_key", "SELECT api_key FROM auth_user WHERE account= '" + accountParam + "'");
        }
        Page<EarningsPledgeDetail> page = new Page<EarningsPledgeDetail>(pageNo, pageSize);
        IPage<EarningsPledgeDetail> result = earningsPledgeDetailService.page(page, queryWrapper);
        for (EarningsPledgeDetail record : result.getRecords()) {
            AuthUser authUserByCache = authUserService.getAuthUserByCache(record.getApiKey());
            if (ObjectUtils.isNotEmpty(authUserByCache)) {
                record.setAccount(authUserByCache.getAccount());
            }
        }
        return result;
    }

    /**
     * 添加
     *
     * @param earningsPledgeDetail
     * @return
     */
 /*   @PostMapping(value = "/add")
    public Result<?> add(@RequestBody EarningsPledgeDetail earningsPledgeDetail) {
        earningsPledgeDetailService.save(earningsPledgeDetail);
        return Result.ok("添加成功！");
    }*/
    @PostMapping(value = "/changePledge")
    @Transactional
    public Result<?> changePledge(@RequestBody PledgeEditVo pledgeEditVo) {
        DictInfo mining_pledge_limit = dictInfoService.lambdaQuery().eq(DictInfo::getDictKey, "mining_pledge_limit").one();
        mining_pledge_limit.setValue(pledgeEditVo.getMiningPledgeLimit().toPlainString());
        dictInfoService.saveOrUpdate(mining_pledge_limit);

        DictInfo order_pledge_time_begin = dictInfoService.lambdaQuery().eq(DictInfo::getDictKey, "order_pledge_time_begin").one();
        order_pledge_time_begin.setValue(DateUtils.dateToString(pledgeEditVo.getBeginTime(), DateUtils.DATE_TIME_FORMAT));
        dictInfoService.saveOrUpdate(order_pledge_time_begin);

        DictInfo order_pledge_time_end = dictInfoService.lambdaQuery().eq(DictInfo::getDictKey, "order_pledge_time_end").one();
        order_pledge_time_end.setValue(DateUtils.dateToString(pledgeEditVo.getEndTime(), DateUtils.DATE_TIME_FORMAT));
        dictInfoService.saveOrUpdate(order_pledge_time_end);

        DictInfo earnings_pledge_ratio = dictInfoService.lambdaQuery().eq(DictInfo::getDictKey, "earnings_pledge_ratio").one();
        earnings_pledge_ratio.setValue(pledgeEditVo.getPledgeAmount().toPlainString());
        dictInfoService.saveOrUpdate(earnings_pledge_ratio);

        earningsPledgeDetailService.getBaseMapper().updateMiningPledgeLimit(pledgeEditVo.getMiningPledgeLimit());

        return Result.ok("修改成功！");
    }

    @GetMapping(value = "/getChangePledge")
    public Result<?> getChangePledge() {
        DictInfo mining_pledge_limit = dictInfoService.lambdaQuery().eq(DictInfo::getDictKey, "mining_pledge_limit").one();
        if (ObjectUtils.isEmpty(mining_pledge_limit)) {
            mining_pledge_limit = new DictInfo().setValue("100").setUpdateTime(new Date()).setDictCode("1").setDictKey("mining_pledge_limit").setDictExplain("挖矿质押上限");
            dictInfoService.save(mining_pledge_limit);
        }
        DictInfo order_pledge_time_begin = dictInfoService.lambdaQuery().eq(DictInfo::getDictKey, "order_pledge_time_begin").one();
        if (ObjectUtils.isEmpty(order_pledge_time_begin)) {
            order_pledge_time_begin = new DictInfo().setValue(DateUtils.dateToString(new Date())).setUpdateTime(new Date()).setDictCode("1").setDictKey("order_pledge_time_begin").setDictExplain("订单赠送开始时间");
            dictInfoService.save(order_pledge_time_begin);
        }
        DictInfo order_pledge_time_end = dictInfoService.lambdaQuery().eq(DictInfo::getDictKey, "order_pledge_time_end").one();
        if (ObjectUtils.isEmpty(order_pledge_time_end)) {
            order_pledge_time_end = new DictInfo().setValue(DateUtils.dateToString(new Date())).setUpdateTime(new Date()).setDictCode("1").setDictKey("order_pledge_time_end").setDictExplain("订单赠送结束时间");
            dictInfoService.save(order_pledge_time_end);
        }
        DictInfo earnings_pledge_ratio = dictInfoService.lambdaQuery().eq(DictInfo::getDictKey, "earnings_pledge_ratio").one();
        if (ObjectUtils.isEmpty(earnings_pledge_ratio)) {
            earnings_pledge_ratio = new DictInfo().setValue(DateUtils.dateToString(new Date())).setUpdateTime(new Date()).setDictCode("1").setDictKey("earnings_pledge_ratio").setDictExplain("新订单每T奖励");
            dictInfoService.save(earnings_pledge_ratio);
        }
        PledgeEditVo pledgeEditVo = new PledgeEditVo().setPledgeAmount(new BigDecimal(earnings_pledge_ratio.getValue())).setBeginTime(DateUtils.stringToDate(order_pledge_time_begin.getValue())).setEndTime(DateUtils.stringToDate(order_pledge_time_end.getValue())).setMiningPledgeLimit(new BigDecimal(mining_pledge_limit.getValue()));
        return Result.ok(pledgeEditVo);
    }


    /**
     * 编辑
     *
     * @param earningsPledgeDetail
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody EarningsPledgeDetail earningsPledgeDetail) {
        earningsPledgeDetailService.updateById(earningsPledgeDetail);
        return Result.ok("编辑成功!");
    }

    @PutMapping(value = "/changePledge")
    @Transactional
    public synchronized Result<?> changePledge(@RequestBody EarningsPledgeDetail earningsPledgeDetail) {
        EarningsPledgeDetail resutl = earningsPledgeDetailService.lambdaQuery().eq(EarningsPledgeDetail::getApiKey, earningsPledgeDetail.getApiKey()).one();
        BalanceDTO balanceDTO = new BalanceDTO().setApiKey(earningsPledgeDetail.getApiKey()).setAsset(PojoConstants.COIN_FIL).setCreateTime(new Date());

        PledgeLog pledgeLog = new PledgeLog().setApiKey(earningsPledgeDetail.getApiKey()).setAsset(PojoConstants.COIN_FIL).setCreateTime(new Date()).setPid(resutl.getId().toString()).setPledgeType(4).setRemark("人工操作订单赠送质押");
        if (!earningsPledgeDetail.getFlag()) {
            balanceDTO.setAvailable(earningsPledgeDetail.getAmount());
            earningsPledgeDetail.setAmount(earningsPledgeDetail.getAmount().negate());
        }
        pledgeLog.setAmount(earningsPledgeDetail.getAmount());


        if (1 == earningsPledgeDetail.getOperType()) {
            resutl.setAwardPledge(BigDecimalUtil.add(earningsPledgeDetail.getAmount(), resutl.getAwardPledge()));
            balanceDTO.setFrozen(earningsPledgeDetail.getAmount()).setType(41).setRemark("人工操作订单赠送质押");

        } else if (2 == earningsPledgeDetail.getOperType()) {
            balanceDTO.setFrozen(earningsPledgeDetail.getAwardPledge()).setType(42).setRemark("人工操作竞赛质押数量");
            resutl.setCompetitionPledge(BigDecimalUtil.add(resutl.getCompetitionPledge(), earningsPledgeDetail.getAmount()));

        } else if (3 == earningsPledgeDetail.getOperType()) {
            balanceDTO.setFrozen(earningsPledgeDetail.getAwardPledge()).setType(43).setRemark("人工操作挖矿质押");
            resutl.setMiningPledge(BigDecimalUtil.add(resutl.getMiningPledge(), earningsPledgeDetail.getAmount()));

        }

        if (!balanceService.exchange(balanceDTO)) {
            throw new RuntimeException("操作失败");
        }

        pledgeLogService.save(pledgeLog);
        earningsPledgeDetailService.saveOrUpdate(resutl);
        return Result.ok("操作成功!");
    }

    @Autowired
    private PledgeLogServiceImpl pledgeLogService;

    @Autowired
    private BalanceServiceImpl balanceService;


    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        earningsPledgeDetailService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.earningsPledgeDetailService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        EarningsPledgeDetail earningsPledgeDetail = earningsPledgeDetailService.getById(id);
        if (earningsPledgeDetail == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(earningsPledgeDetail);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param earningsPledgeDetail
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, EarningsPledgeDetail earningsPledgeDetail) {
        return super.exportXls(request, earningsPledgeDetail, EarningsPledgeDetail.class, "earnings_pledge_detail");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, EarningsPledgeDetail.class);
    }

}
