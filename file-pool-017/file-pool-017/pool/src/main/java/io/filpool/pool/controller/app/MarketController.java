package io.filpool.pool.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.common.exception.FILPoolException;
import io.filpool.framework.log.annotation.Module;
import io.filpool.framework.util.Constants;
import io.filpool.framework.util.RedisUtil;
import io.filpool.pool.entity.Currency;
import io.filpool.pool.entity.Quotation;
import io.filpool.pool.request.PageRequest;
import io.filpool.pool.service.CurrencyService;
import io.filpool.pool.service.QuotationService;
import io.filpool.pool.vo.CurrencyPriceDetailVo;
import io.filpool.pool.vo.CurrencyPriceListVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(Constants.API_PREFIX + "/market")
@Module("pool")
@Api(value = "行情", tags = {"行情API"})
public class MarketController {
    @Autowired
    private QuotationService quotationService;
    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("currencyList")
    @ApiOperation("行情列表")
    public ApiResult<List<CurrencyPriceListVo>> currencyList(@RequestBody PageRequest pageRequest) throws Exception {
        LambdaQueryWrapper<Quotation> wr = Wrappers.lambdaQuery(Quotation.class)
                .notIn(Quotation::getSymbol, "USDT", "FIL(TEST)");
        Page<Quotation> page = new Page<>(pageRequest.getPageIndex(), pageRequest.getPageSize());
        page = quotationService.getBaseMapper().selectPage(page, wr);
        List<CurrencyPriceListVo> vos = page.getRecords().stream().map(quotation -> {
//            BigDecimal price = getCurrencyUsdtPrice(quotation.getSymbol());
            CurrencyPriceListVo vo = new CurrencyPriceListVo();
//            vo.setCurrencyId(currency.getId());
            vo.setSymbol(quotation.getSymbol());
            vo.setPercent(quotation.getPercentChange());
            vo.setPrice(quotation.getPriceUsdt());
            vo.setImage(quotation.getImg());
            if (!StringUtils.isEmpty(vo.getPrice())){
                vo.setPriceCNY(getUSDTPriceCNY().multiply(new BigDecimal(vo.getPrice())).setScale(3, BigDecimal.ROUND_DOWN));
            }
            else{
                vo.setPriceCNY(BigDecimal.ZERO);
            }
            return vo;
        }).collect(Collectors.toList());
        return ApiResult.ok(vos);
    }

    @PostMapping("filPrice")
    @ApiOperation("FIL最新币价")
    public ApiResult<CurrencyPriceDetailVo> filPrice() throws Exception {
        Quotation currency = quotationService.lambdaQuery().eq(Quotation::getSymbol, "FIL").one();
        if (currency == null) {
            throw new FILPoolException("transfer.currency.non-exits");
        }
        CurrencyPriceDetailVo vo = new CurrencyPriceDetailVo();
//        vo.setCurrencyId(currency.getId());
        vo.setImage(currency.getImg());
        vo.setSymbol(currency.getSymbol());
        if (redisUtil.exists(Constants.CURRENCY_FIL_KEY)) {
            CurrencyPriceDetailVo detailVo = (CurrencyPriceDetailVo) redisUtil.get(Constants.CURRENCY_FIL_KEY);
            BeanUtils.copyProperties(detailVo, vo);
            if (!StringUtils.isEmpty(vo.getPrice())){
                vo.setPriceCNY(getUSDTPriceCNY().multiply(new BigDecimal(vo.getPrice())).setScale(3, BigDecimal.ROUND_DOWN));
            }
            else{
                vo.setPriceCNY(BigDecimal.ZERO);
            }
//            vo.setCurrencyId(currency.getId());
            vo.setImage(currency.getImg());
            vo.setSymbol(currency.getSymbol());
            return ApiResult.ok(vo);
        }
        return ApiResult.ok(vo);
    }

    public BigDecimal getUSDTPriceCNY() {
        Object o = redisUtil.get(Constants.USDT_PRICE_KEY);
        if (o == null) {
            return BigDecimal.valueOf(7);
        }
        return new BigDecimal((String) o);
    }

//    public BigDecimal getCurrencyUsdtPrice(String symbol) {
//        if (symbol.equals("USDT")) {
//            return BigDecimal.ONE;
//        }
//        Object o = redisUtil.get(Constants.CURRENCY_PRICE_KEY + symbol);
//        if (o == null) {
//            return BigDecimal.ZERO;
//        }
//        return new BigDecimal((String) o);
//    }

//    public BigDecimal getCurrencyChangePercent(String symbol) {
//        if (symbol.equals("USDT")) {
//            return BigDecimal.ONE;
//        }
//        Object o = redisUtil.get(Constants.CURRENCY_PERCENT_KEY + symbol);
//        if (o == null) {
//            return BigDecimal.ZERO;
//        }
//        return new BigDecimal((String) o);
//    }
}
