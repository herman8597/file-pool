package io.filpool.pool.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.filpool.framework.jwt.CheckLogin;
import io.filpool.framework.util.Constants;
import io.filpool.pool.entity.Currency;
import io.filpool.pool.entity.PowerOrder;
import io.filpool.pool.entity.Supplement;
import io.filpool.pool.entity.User;
import io.filpool.pool.request.PageRequest;
import io.filpool.pool.request.SymbolPageRequest;
import io.filpool.pool.request.SymbolRequest;
import io.filpool.pool.service.CurrencyService;
import io.filpool.pool.service.PowerOrderService;
import io.filpool.pool.service.SupplementService;
import io.filpool.pool.service.impl.PowerOrderServiceImpl;
import io.filpool.pool.util.SecurityUtil;
import io.filpool.pool.vo.IncomeInfoVo;
import io.filpool.pool.vo.PowerOrderVo;
import io.filpool.pool.vo.TotalPowerVo;
import lombok.extern.slf4j.Slf4j;
import io.filpool.pool.param.PowerOrderPageParam;
import io.filpool.framework.common.controller.BaseController;
import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.common.param.IdParam;
import io.filpool.framework.log.annotation.Module;
import io.filpool.framework.log.annotation.OperationLog;
import io.filpool.framework.log.enums.OperationLogType;
import io.filpool.framework.core.validator.groups.Add;
import io.filpool.framework.core.validator.groups.Update;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 算力订单表 控制器
 *
 * @author filpool
 * @since 2021-04-01
 */
@Slf4j
@RestController
@RequestMapping(Constants.API_PREFIX + "/powerManager")
@Module("pool")
@Api(value = "算力管理API", tags = {"算力管理"})
public class PowerOrderController extends BaseController {

    @Autowired
    private PowerOrderServiceImpl powerOrderService;
    @Autowired
    private SupplementService supplementService;
    @Autowired
    private CurrencyService currencyService;

    /**
     * 获得用户有效总算力
     */
    @PostMapping("getTotalPower")
    @ApiOperation("获取有效总算力")
    @CheckLogin
    public ApiResult<List<TotalPowerVo>> getTotalPower() throws Exception {
        User user = SecurityUtil.currentLogin();
//        String symbol = StringUtils.isBlank(request.getSymbol()) ? "FIL" : request.getSymbol();
        List<Currency> currencyList = currencyService.getMinerCurrency();
        List<TotalPowerVo> voList = new ArrayList<>();
        for (Currency currency : currencyList){
            TotalPowerVo vo = new TotalPowerVo();
            vo.setSymbol(currency.getSymbol());
            vo.setTotalPower(powerOrderService.getBaseMapper().sumEffectAmount(user.getId(), currency.getSymbol()));
            voList.add(vo);
        }
        return ApiResult.ok(voList);
    }

    /**
     * 算力订单表分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "算力记录")
    @CheckLogin
    public ApiResult<List<PowerOrderVo>> getPowerOrderPageList(@Validated @RequestBody SymbolPageRequest request) throws Exception {
        User user = SecurityUtil.currentLogin();
        LambdaQueryWrapper<PowerOrder> wr = Wrappers.lambdaQuery(PowerOrder.class)
                .eq(PowerOrder::getUserId, user.getId()).orderByDesc(PowerOrder::getCreateTime);
        String symbol = StringUtils.isBlank(request.getSymbol()) ? "FIL" : request.getSymbol();
        wr.eq(PowerOrder::getPowerSymbol,symbol);
        Page<PowerOrder> page = new Page<>(request.getPageIndex(), request.getPageSize());
        page = powerOrderService.getBaseMapper().selectPage(page, wr);
        List<PowerOrderVo> voList = new ArrayList<>();
        for (PowerOrder order : page.getRecords()) {
            PowerOrderVo vo = new PowerOrderVo();
            BeanUtils.copyProperties(order, vo);
            if (order.getType() == 3 || order.getType() == 2 || order.getType() == 5) {
                //补单gas费及质押金额
                Supplement supplement = supplementService.getById(order.getRecordId());
                vo.setGasAmount(supplement.getGasPrice());
                vo.setPledgeAmount(supplement.getPledgePrice());
            } else {
                vo.setGasAmount(BigDecimal.ZERO);
                vo.setPledgeAmount(BigDecimal.ZERO);
            }
            voList.add(vo);
        }
        return ApiResult.ok(voList);
    }

}

