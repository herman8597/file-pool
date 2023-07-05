package io.filpool.pool.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.common.controller.BaseController;
import io.filpool.framework.common.exception.FILPoolException;
import io.filpool.framework.log.annotation.Module;
import io.filpool.framework.util.Constants;
import io.filpool.framework.util.RedisUtil;
import io.filpool.pool.entity.Goods;
import io.filpool.pool.entity.Quotation;
import io.filpool.pool.request.GoodDetailRequest;
import io.filpool.pool.request.GoodsPageRequest;
import io.filpool.pool.service.GoodsService;
import io.filpool.pool.service.QuotationService;
import io.filpool.pool.vo.GoodsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品表 控制器
 *
 * @author filpool
 * @since 2021-03-08
 */
@Slf4j
@RestController
@RequestMapping(Constants.API_PREFIX + "/goods")
@Module("pool")
@Api(value = "商品表API", tags = {"商品API"})
public class GoodsController extends BaseController {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private QuotationService quotationService;

    /**
     * 商品表分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "商品分页列表")
    public ApiResult<List<GoodsVo>> getGoodsPageList(@RequestBody GoodsPageRequest request) throws Exception {
        LambdaQueryWrapper<Goods> wr = Wrappers.lambdaQuery(Goods.class)
                .notIn(Goods::getStatus, 3)
                .orderByDesc(Goods::getWeight)
                .orderByDesc(Goods::getCreateTime);
        if (request.getType() != null && request.getType() != 0)
            wr.eq(Goods::getType, request.getType());
        if (request.getCurrencyId() != null && request.getCurrencyId() != 0)
            wr.eq(Goods::getCurrencyId, request.getCurrencyId());
        if (request.getIsHome() != null && request.getIsHome())
            wr.eq(Goods::getIsShowPage, 1);
        //020新需求，专题显示
        if (request.getSpecialShow()!=null && request.getCurrencyId()!=0){
            wr.eq(Goods::getSpecialShow,1);
        }
        Page<Goods> page = new Page<>(request.getPageIndex(), request.getPageSize());
        page = goodsService.getBaseMapper().selectPage(page, wr);
        List<GoodsVo> vos = new ArrayList<>();
        for (Goods good : page.getRecords()) {
            GoodsVo vo = new GoodsVo();
            BeanUtils.copyProperties(good, vo);
//            if (good.getType() == 3 && !StringUtils.isEmpty(good.getGroupIds())) {
//                List<Long> ids = Arrays.stream(good.getGroupIds().split(",")).map(Long::valueOf).collect(Collectors.toList());
//                //查询集群下的矿机列表
//                vo.setGoodSize(ids.size());
//            }
            BigDecimal income24;
            if (StringUtils.equals(good.getSymbol(),"FIL")){
                //FIL年化收益
                Object obj = redisUtil.get(Constants.FIL_24H_INCOME);
                if (!ObjectUtils.isEmpty(obj)){
                    //每T每天收益FIL
                    income24 = new BigDecimal(obj.toString());
                }else{
                    income24 = BigDecimal.ZERO;
                }
            }else{
                //XCH年化收益
                Object obj = redisUtil.get(Constants.XCH_24H_INCOME);
                if (!ObjectUtils.isEmpty(obj)){
                    income24 = new BigDecimal(obj.toString());
                }else{
                    income24 = BigDecimal.ZERO;
                }
            }
            if (income24.compareTo(BigDecimal.ZERO) > 0){
                //每天净收益
                BigDecimal everyIncome = good.getPower().multiply(income24);
                //实际成本USDT 需要转成对应单位
                BigDecimal price;
                if (good.getDiscountPrice().compareTo(BigDecimal.ZERO) > 0) {
                    price = good.getDiscountPrice();
                } else {
                    price = good.getPrice();
                }
                Quotation quotation = quotationService.lambdaQuery().eq(Quotation::getSymbol,good.getSymbol()).one();
                //实际成本usdt转化成对应币种
                price = price.divide(new BigDecimal(quotation.getPriceUsdt()),8,BigDecimal.ROUND_DOWN);
                //合约期限
                BigDecimal contractPeriod = new BigDecimal(good.getContractPeriod());
                //回本周期 = 实际投入成本/每天净收益
                BigDecimal returnDay = price.divide(everyIncome,0,BigDecimal.ROUND_HALF_UP);
                //利润 = （合约期限-回本周期）*每T收益
                BigDecimal profit = contractPeriod.subtract(returnDay).multiply(income24);
                //预计年化收益 = 利润/投入成本/合约期限*365×100%
                BigDecimal yearIncomeRate = profit.divide(price,8,BigDecimal.ROUND_DOWN).divide(contractPeriod,8,BigDecimal.ROUND_DOWN)
                        .multiply(new BigDecimal(365)).setScale(2,BigDecimal.ROUND_DOWN);
                vo.setReturnDay(returnDay.intValue());
                vo.setYearIncomeRate(yearIncomeRate);
            }else{
                vo.setReturnDay(0);
                vo.setYearIncomeRate(BigDecimal.ZERO);
            }
            vos.add(vo);
        }
        return ApiResult.ok(vos);
    }





    @PostMapping("/getGoodDetail")
    @ApiOperation(value = "商品详情")
    public ApiResult<GoodsVo> getGoodDetail(@RequestBody GoodDetailRequest request) throws Exception {
        Goods good = goodsService.lambdaQuery().eq(Goods::getId, request.getId()).one();
        if (good == null) {
            throw new FILPoolException("good.not-exits");
        }
        GoodsVo vo = new GoodsVo();
        BeanUtils.copyProperties(good, vo);
//        if (good.getType() == 3 && !StringUtils.isEmpty(good.getGroupIds())) {
//            List<Long> ids = Arrays.stream(good.getGroupIds().split(",")).map(Long::valueOf).collect(Collectors.toList());
//            //查询集群下的矿机列表
//            vo.setGoodSize(ids.size());
//        }
        BigDecimal income24;
        if (StringUtils.equals(good.getSymbol(),"FIL")){
            //FIL年化收益
            Object obj = redisUtil.get(Constants.FIL_24H_INCOME);
            if (!ObjectUtils.isEmpty(obj)){
                //每T每天收益FIL
                income24 = new BigDecimal(obj.toString());
            }else{
                income24 = BigDecimal.ZERO;
            }
        }else{
            //XCH年化收益
            Object obj = redisUtil.get(Constants.XCH_24H_INCOME);
            if (!ObjectUtils.isEmpty(obj)){
                income24 = new BigDecimal(obj.toString());
            }else{
                income24 = BigDecimal.ZERO;
            }
        }
        if (income24.compareTo(BigDecimal.ZERO) > 0){
            //每天净收益
            BigDecimal everyIncome = good.getPower().multiply(income24);
            //实际成本USDT 需要转成对应单位
            BigDecimal price;
            if (good.getDiscountPrice().compareTo(BigDecimal.ZERO) > 0) {
                price = good.getDiscountPrice();
            } else {
                price = good.getPrice();
            }
            Quotation quotation = quotationService.lambdaQuery().eq(Quotation::getSymbol,good.getSymbol()).one();
            //实际成本usdt转化成对应币种
            price = price.divide(new BigDecimal(quotation.getPriceUsdt()),8,BigDecimal.ROUND_DOWN);
            //合约期限
            BigDecimal contractPeriod = new BigDecimal(good.getContractPeriod());
            //回本周期 = 实际投入成本/每天净收益
            BigDecimal returnDay = price.divide(everyIncome,0,BigDecimal.ROUND_HALF_UP);
            //利润 = （合约期限-回本周期）*每T收益
            BigDecimal profit = contractPeriod.subtract(returnDay).multiply(income24);
            //预计年化收益 = 利润/投入成本/合约期限*365×100%
            BigDecimal yearIncomeRate = profit.divide(price,8,BigDecimal.ROUND_DOWN).divide(contractPeriod,8,BigDecimal.ROUND_DOWN)
                    .multiply(new BigDecimal(365)).setScale(2,BigDecimal.ROUND_DOWN);
            vo.setReturnDay(returnDay.intValue());
            vo.setYearIncomeRate(yearIncomeRate);
        }else{
            vo.setReturnDay(0);
            vo.setYearIncomeRate(BigDecimal.ZERO);
        }
        return ApiResult.ok(vo);
    }

    @PostMapping("/getGroupList")
    @ApiOperation("获得集群矿机列表")
    public ApiResult<List<GoodsVo>> getGroupList(@RequestBody GoodDetailRequest request) throws Exception {
        Goods good = goodsService.lambdaQuery().eq(Goods::getId, request.getId())
                .eq(Goods::getType, 3).one();
        if (good == null) {
            throw new FILPoolException("good.not-exits");
        }
        List<Long> ids = Arrays.stream(good.getGroupIds().split(",")).map(Long::valueOf).collect(Collectors.toList());
        List<GoodsVo> goods = goodsService.lambdaQuery()
//                .eq(Goods::getStatus, 1)
                .in(Goods::getId, ids)
                .orderByDesc(Goods::getCreateTime).list().stream().map(Goods -> {
                    GoodsVo childVo = new GoodsVo();
                    BeanUtils.copyProperties(Goods, childVo);
                    return childVo;
                }).collect(Collectors.toList());
        return ApiResult.ok(goods);
    }
}

