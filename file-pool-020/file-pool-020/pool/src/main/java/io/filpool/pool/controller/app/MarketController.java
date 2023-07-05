package io.filpool.pool.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
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
import io.filpool.pool.vo.NodeDataVo;
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
import java.util.ArrayList;
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
    @Autowired
    private BzzConfigController bzzConfigController;

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
            if (!StringUtils.isEmpty(vo.getPrice())) {
                vo.setPriceCNY(getUSDTPriceCNY().multiply(new BigDecimal(vo.getPrice())).setScale(3, BigDecimal.ROUND_DOWN));
            } else {
                vo.setPriceCNY(BigDecimal.ZERO);
            }
            return vo;
        }).collect(Collectors.toList());
        return ApiResult.ok(vos);
    }

    @PostMapping("filPrice")
    @ApiOperation("FIL和XCH最新币价")
    public ApiResult<List<CurrencyPriceDetailVo>> filPrice() throws Exception {
        List<Quotation> currencyList = quotationService.lambdaQuery().eq(Quotation::getSymbol, "FIL")
                .or().eq(Quotation::getSymbol, "XCH")
                .list();
        if (ObjectUtils.isEmpty(currencyList)) {
            throw new FILPoolException("transfer.currency.non-exits");
        }
        List<CurrencyPriceDetailVo> voList = new ArrayList<>();

//        vo.setCurrencyId(currency.getId());
        for (Quotation currency : currencyList) {
            CurrencyPriceDetailVo vo = new CurrencyPriceDetailVo();

            vo.setImage(currency.getImg());
            vo.setSymbol(currency.getSymbol());
            String key = currency.getSymbol().equals("FIL")?Constants.CURRENCY_FIL_KEY : Constants.CURRENCY_XCH_KEY;
            if (redisUtil.exists(key)) {
                CurrencyPriceDetailVo detailVo = (CurrencyPriceDetailVo) redisUtil.get(key);
                BeanUtils.copyProperties(detailVo, vo);
                if (!StringUtils.isEmpty(vo.getPrice())) {
                    vo.setPriceCNY(getUSDTPriceCNY().multiply(new BigDecimal(vo.getPrice())).setScale(3, BigDecimal.ROUND_DOWN));
                } else {
                    vo.setPriceCNY(BigDecimal.ZERO);
                }
//            vo.setCurrencyId(currency.getId());
                vo.setImage(currency.getImg());
                vo.setSymbol(currency.getSymbol());
            }
            voList.add(vo);
        }
        return ApiResult.ok(voList);
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
