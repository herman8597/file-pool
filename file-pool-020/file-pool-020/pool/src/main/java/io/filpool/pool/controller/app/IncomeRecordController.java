package io.filpool.pool.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.jwt.CheckLogin;
import io.filpool.framework.log.annotation.Module;
import io.filpool.framework.util.Constants;
import io.filpool.framework.util.DateUtil;
import io.filpool.pool.entity.*;
import io.filpool.pool.request.PageRequest;
import io.filpool.pool.request.SymbolPageRequest;
import io.filpool.pool.request.SymbolRequest;
import io.filpool.pool.service.*;
import io.filpool.pool.service.impl.IncomeReleaseRecordServiceImpl;
import io.filpool.pool.util.SecurityUtil;
import io.filpool.pool.vo.IncomeInfoVo;
import io.filpool.pool.vo.IncomeRecordVo;
import io.filpool.pool.vo.TodayIncomeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 收益记录
 *
 * @author filpool
 * @since 2021-03-24
 */
@Slf4j
@RestController
@RequestMapping(Constants.API_PREFIX + "/incomeRecord")
@Module("pool")
@Api(value = "挖矿收益API", tags = {"挖矿收益相关"})
public class IncomeRecordController {
    @Autowired
    private IncomeReleaseRecordServiceImpl incomeReleaseRecordService;
    @Autowired
    private IncomeLogService incomeLogService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private AssetAccountService accountService;


    @PostMapping("info")
    @CheckLogin
    @ApiOperation("挖矿收益详情")
    public ApiResult<List<IncomeInfoVo>> getIncomeInfo() throws Exception {
        User user = SecurityUtil.currentLogin();
        List<Currency> currencyList = currencyService.getMinerCurrency();
        List<IncomeInfoVo> voList = new ArrayList<>();
        for (Currency currency : currencyList) {
            IncomeInfoVo infoVo = new IncomeInfoVo();
            infoVo.setSymbol(currency.getSymbol());
            infoVo.setTotalAmount(incomeReleaseRecordService.getBaseMapper().getTotalIncome(user.getId(), currency.getSymbol()));
            infoVo.setTotalFrozenAmount(incomeReleaseRecordService.getBaseMapper().getFrozenIncome(user.getId(), currency.getSymbol()));
            infoVo.setTotalReleaseAmount(incomeReleaseRecordService.getBaseMapper().getReleaseIncome(user.getId(), currency.getSymbol()));
            voList.add(infoVo);
        }
        return ApiResult.ok(voList);
    }


    @PostMapping("getPageList")
    @CheckLogin
    @ApiOperation("挖矿收益列表")
    public ApiResult<List<IncomeRecordVo>> getPageList(@RequestBody SymbolPageRequest request) throws Exception {
        User user = SecurityUtil.currentLogin();
        //查询挖矿收益相关记录
        LambdaQueryWrapper<IncomeLog> wr = Wrappers.lambdaQuery(IncomeLog.class)
                .eq(IncomeLog::getUserId, user.getId())
                .orderByDesc(IncomeLog::getCreateTime);
        String symbol = StringUtils.isBlank(request.getSymbol()) ? "FIL" : request.getSymbol();
        wr.eq(IncomeLog::getPowerSymbol, symbol);
        Page<IncomeLog> page = new Page<>(request.getPageIndex(), request.getPageSize());
        page = incomeLogService.getBaseMapper().selectPage(page, wr);
        List<IncomeRecordVo> voList = page.getRecords().stream().map(log -> {
            IncomeRecordVo vo = new IncomeRecordVo();
            vo.setType(log.getType());
            vo.setCreateTime(log.getCreateTime());
            vo.setAmount(log.getAmount());
            return vo;
        }).collect(Collectors.toList());
        return ApiResult.ok(voList);
    }

    @PostMapping("todayIncome")
    @CheckLogin
    @ApiOperation("用户今日收益")
    public ApiResult<TodayIncomeVo> getTodayIncome(@RequestBody SymbolRequest request) throws Exception {
        Date todayStart = DateUtil.getTodayStart();
        User user = SecurityUtil.currentLogin();
        TodayIncomeVo infoVo = new TodayIncomeVo();
        String symbol = StringUtils.isBlank(request.getSymbol()) ? "FIL" : request.getSymbol();
        AssetAccount account = accountService.lambdaQuery().eq(AssetAccount::getSymbol, symbol)
                .eq(AssetAccount::getUserId, user.getId())
                .one();
        infoVo.setTotalAmount(incomeReleaseRecordService.getBaseMapper().getTotalIncome(user.getId(), symbol));
        infoVo.setTodayIncomeAmount(incomeReleaseRecordService.getBaseMapper().getTodayIncome(user.getId(), todayStart, symbol));
        if (account == null) {
            infoVo.setTodayReleaseAmount(BigDecimal.ZERO);
        } else {
            infoVo.setTodayReleaseAmount(incomeReleaseRecordService.getBaseMapper().getTodayRelease(user.getId(), todayStart, account.getId()));
        }
        return ApiResult.ok(infoVo);
    }
}
