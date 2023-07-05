package io.filpool.pool.controller.admin;

import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.common.controller.BaseController;
import io.filpool.framework.common.exception.FILPoolException;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.log.annotation.Module;
import io.filpool.framework.log.annotation.OperationLog;
import io.filpool.framework.log.enums.OperationLogType;
import io.filpool.pool.entity.Currency;
import io.filpool.pool.entity.Goods;
import io.filpool.pool.entity.InviteRecord;
import io.filpool.pool.excel.GoodsExcel;
import io.filpool.pool.excel.InviteExcel;
import io.filpool.pool.param.GoodsPageParam;
import io.filpool.pool.service.CurrencyService;
import io.filpool.pool.service.GoodsService;
import io.filpool.pool.util.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品表 控制器
 *
 * @author filpool
 * @since 2021-03-08
 */
@Slf4j
@RestController
@RequestMapping("sys/goods")
@Module("pool")
@Api(value = "商品表API", tags = {"商品表"})
public class SysGoodsController extends BaseController {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private CurrencyService currencyService;

    /**
     * 添加商品表（商品列表-新增或编辑）
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation("添加或编辑商品")
    public ApiResult<Boolean> saveOrUpdate(@RequestBody Goods goods) throws Exception {
        if (StringUtils.isEmpty(goods.getName())) {
            throw new FILPoolException("good.name.not-empty");
        }
        if (goods.getType() == null || goods.getType() <= 0) {
            throw new FILPoolException("good.type.not-empty");
        }
        if (goods.getContractPeriod() == null || goods.getContractPeriod() <= 0) {
            throw new FILPoolException("good.period.not-empty");
        }
        if (StringUtils.isEmpty(goods.getImages())) {
            throw new FILPoolException("good.images.not-empty");
        }
        if (StringUtils.isEmpty(goods.getCurrencyId())){
            throw new FILPoolException("good.currency.not-empty");
        }
        if (goods.getPower() == null) {
            throw new FILPoolException("good.power.not-empty");
        }
        Currency currency = currencyService.getById(goods.getCurrencyId());
        if (currency == null) {
            throw new FILPoolException("transfer.currency.non-exits");
        }
        goods.setSymbol(currency.getSymbol());
        if (goods.getTbPledge() == null){
            goods.setTbPledge(BigDecimal.ZERO);
        }
        if (goods.getTbGas() == null){
            goods.setTbGas(BigDecimal.ZERO);
        }
        boolean flag = goodsService.saveOrUpdateGood(goods);
        return ApiResult.result(flag);
    }

    /**
     * 删除商品表
     */
    @PostMapping("/delete/{id}")
    @OperationLog(name = "删除商品表", type = OperationLogType.DELETE)
    @ApiOperation(value = "删除商品表", response = ApiResult.class)
    public ApiResult<Boolean> deleteGoods(@PathVariable("id") Long id) throws Exception {
        boolean flag = goodsService.deleteGoods(id);
        return ApiResult.result(flag);
    }


    /**
     * 查询矿机列表
     */
    @PostMapping("/getGoodsMinerPageList")
    @OperationLog(name = "查询矿机列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "查询矿机列表", response = Goods.class)
    public ApiResult<Paging<Goods>> getGoodsMinerPageList(@Validated @RequestBody GoodsPageParam goodsPageParam) throws Exception {
        Paging<Goods> goodsMinerPageList = goodsService.getGoodsMinerPageList(goodsPageParam);
        return ApiResult.ok(goodsMinerPageList);
    }


    /**
     * 获取商品表详情
     */
    @GetMapping("/info/{id}")
    @OperationLog(name = "商品表详情", type = OperationLogType.INFO)
    @ApiOperation(value = "商品表详情", response = Goods.class)
    public ApiResult<Goods> getGoods(@PathVariable("id") Long id) throws Exception {
        Goods goods = goodsService.getById(id);
        return ApiResult.ok(goods);
    }

    /**
     * 商品表分页列表（商品列表）
     */
    @PostMapping("/getPageList")
    @OperationLog(name = "商品表分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "商品表分页列表", response = Goods.class)
    public ApiResult<Paging<Goods>> getGoodsPageList(@Validated @RequestBody GoodsPageParam goodsPageParam) throws Exception {
        Paging<Goods> paging = goodsService.getGoodsPageList(goodsPageParam);
        for (Goods goods:paging.getRecords()) {
            Currency one = currencyService.lambdaQuery().eq(Currency::getId, goods.getCurrencyId()).one();
            goods.setAsset(one.getSymbol());
        }
        return ApiResult.ok(paging);
    }

    /**
     * 商品表分页列表（商品列表-导出）
     */
    @PostMapping("/goodsListExcel")
    @OperationLog(name = "商品列表-导出", type = OperationLogType.PAGE)
    @ApiOperation(value = "商品列表-导出", response = Goods.class)
    public ApiResult<Paging<Goods>> goodsListExcel(@Validated @RequestBody GoodsPageParam goodsPageParam, HttpServletResponse response) throws Exception {
        Paging<Goods> paging = goodsService.getGoodsPageList(goodsPageParam);

        //将数据循环存到导出实体类中
        List<GoodsExcel> excelGoodsList = new ArrayList<>();

        for (Goods goods : paging.getRecords()) {
            GoodsExcel goodsExcel = new GoodsExcel();
            BeanUtils.copyProperties(goods, goodsExcel);
            goodsExcel.setQuantitySum(goods.getQuantity() + goods.getSoldQuantity());

            excelGoodsList.add(goodsExcel);

        }
        FileUtil.exportExcel(excelGoodsList, GoodsExcel.class, "商品列表", response);

        return ApiResult.ok(paging);
    }


}

