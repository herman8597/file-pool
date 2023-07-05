package io.filpool.pool.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.common.controller.BaseController;
import io.filpool.framework.common.exception.FILPoolException;
import io.filpool.framework.log.annotation.Module;
import io.filpool.framework.util.Constants;
import io.filpool.pool.entity.Goods;
import io.filpool.pool.request.GoodDetailRequest;
import io.filpool.pool.request.GoodsPageRequest;
import io.filpool.pool.service.GoodsService;
import io.filpool.pool.vo.GoodsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 商品表分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "商品分页列表", response = Goods.class)
    public ApiResult<List<GoodsVo>> getGoodsPageList(@RequestBody GoodsPageRequest request) throws Exception {
        LambdaQueryWrapper<Goods> wr = Wrappers.lambdaQuery(Goods.class)
//                .eq(Goods::getStatus, 1)
                .orderByDesc(Goods::getCreateTime);
        if (request.getType() != null && request.getType() != 0)
            wr.eq(Goods::getType, request.getType());
        if (request.getCurrencyId() != null && request.getCurrencyId() != 0)
            wr.eq(Goods::getCurrencyId, request.getCurrencyId());
        Page<Goods> page = new Page<>(request.getPageIndex(), request.getPageSize());
        page = goodsService.getBaseMapper().selectPage(page, wr);
        List<GoodsVo> vos = new ArrayList<>();
        for (Goods good : page.getRecords()) {
            GoodsVo vo = new GoodsVo();
            BeanUtils.copyProperties(good, vo);
            if (good.getType() == 3 && !StringUtils.isEmpty(good.getGroupIds())) {
                List<Long> ids = Arrays.stream(good.getGroupIds().split(",")).map(Long::valueOf).collect(Collectors.toList());
                //查询集群下的矿机列表
                vo.setGoodSize(ids.size());
            }
            vos.add(vo);
        }
        return ApiResult.ok(vos);
    }


    @PostMapping("/getGoodDetail")
    @ApiOperation(value = "商品详情", response = Goods.class)
    public ApiResult<GoodsVo> getGoodDetail(@RequestBody GoodDetailRequest request) throws Exception {
        Goods good = goodsService.lambdaQuery().eq(Goods::getId, request.getId()).one();
        if (good == null) {
            throw new FILPoolException("good.not-exits");
        }
        GoodsVo vo = new GoodsVo();
        BeanUtils.copyProperties(good, vo);
        if (good.getType() == 3 && !StringUtils.isEmpty(good.getGroupIds())) {
            List<Long> ids = Arrays.stream(good.getGroupIds().split(",")).map(Long::valueOf).collect(Collectors.toList());
            //查询集群下的矿机列表
            vo.setGoodSize(ids.size());
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

