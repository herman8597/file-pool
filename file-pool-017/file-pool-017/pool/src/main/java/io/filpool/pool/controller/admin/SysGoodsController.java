package io.filpool.pool.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.common.controller.BaseController;
import io.filpool.framework.common.exception.FILPoolException;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.log.annotation.Module;
import io.filpool.framework.log.annotation.OperationLog;
import io.filpool.framework.log.enums.OperationLogType;
import io.filpool.pool.entity.Goods;
import io.filpool.pool.param.GoodsPageParam;
import io.filpool.pool.service.GoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 添加商品表（商品列表-新增或编辑）
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation("添加或编辑商品")
    public ApiResult<Boolean> saveOrUpdate(@RequestBody Goods goods) throws Exception {
        if (StringUtils.isEmpty(goods.getName())){
            throw new FILPoolException("good.name.not-empty");
        }
        if (goods.getType() == null || goods.getType() <= 0){
            throw new FILPoolException("good.type.not-empty");
        }
        if (goods.getContractPeriod() == null || goods.getContractPeriod()<=0){
            throw new FILPoolException("good.period.not-empty");
        }
        if (StringUtils.isEmpty(goods.getImages())){
            throw new FILPoolException("good.images.not-empty");
        }
        boolean flag = goodsService.saveOrUpdateGood(goods);
        return ApiResult.result(flag);
    }
/*
    *//**
     * 查询矿机列表
     *//*
    @PostMapping("/getPageList")
    @OperationLog(name = "查询矿机列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "查询矿机列表", response = Goods.class)
    public ApiResult<Paging<Goods>> getGoodsMinerPageList(@Validated @RequestBody GoodsPageParam goodsPageParam) throws Exception {
        Paging<Goods> goodsMinerPageList = goodsService.getGoodsMinerPageList(goodsPageParam);
        return ApiResult.ok(goodsMinerPageList);
    }*/



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
        return ApiResult.ok(paging);
    }

}

