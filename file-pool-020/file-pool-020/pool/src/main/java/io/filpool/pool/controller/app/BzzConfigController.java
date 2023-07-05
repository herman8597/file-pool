package io.filpool.pool.controller.app;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.common.controller.BaseController;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.validator.groups.Add;
import io.filpool.framework.core.validator.groups.Update;
import io.filpool.framework.jwt.CheckLogin;
import io.filpool.framework.log.annotation.Module;
import io.filpool.framework.log.annotation.OperationLog;
import io.filpool.framework.log.enums.OperationLogType;
import io.filpool.framework.util.Constants;
import io.filpool.framework.util.RedisUtil;
import io.filpool.pool.entity.BzzConfig;
import io.filpool.pool.entity.FilVo;
import io.filpool.pool.entity.Order;
import io.filpool.pool.mapper.PowerOrderMapper;
import io.filpool.pool.param.BzzConfigPageParam;
import io.filpool.pool.service.BzzConfigService;
import io.filpool.pool.service.OrderService;
import io.filpool.pool.vo.NodeDataVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 *  控制器
 *
 * @author filpool
 * @since 2021-06-23
 */
@Slf4j
@RestController
@RequestMapping(Constants.API_PREFIX +"/bzzConfig")
@Module("pool")
@Api(value = "API", tags = {""})
public class BzzConfigController extends BaseController {

    @Autowired
    private BzzConfigService bzzConfigService;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PowerOrderMapper powerOrderMapper;

    /**
     * 添加
     */
    @PostMapping("/add")
    @OperationLog(name = "添加", type = OperationLogType.ADD)
    @ApiOperation(value = "添加", response = ApiResult.class)
    public ApiResult<Boolean> addBzzConfig(@Validated(Add.class) @RequestBody BzzConfig bzzConfig) throws Exception {
        boolean flag = bzzConfigService.saveBzzConfig(bzzConfig);
        return ApiResult.result(flag);
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @OperationLog(name = "修改", type = OperationLogType.UPDATE)
    @ApiOperation(value = "修改", response = ApiResult.class)
    public ApiResult<Boolean> updateBzzConfig(@Validated(Update.class) @RequestBody BzzConfig bzzConfig) throws Exception {
        boolean flag = bzzConfigService.updateBzzConfig(bzzConfig);
        return ApiResult.result(flag);
    }

    /**
     * 删除
     */
    @PostMapping("/delete/{id}")
    @OperationLog(name = "删除", type = OperationLogType.DELETE)
    @ApiOperation(value = "删除", response = ApiResult.class)
    public ApiResult<Boolean> deleteBzzConfig(@PathVariable("id") Long id) throws Exception {
        boolean flag = bzzConfigService.deleteBzzConfig(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取详情
     */
    @GetMapping("/info/{id}")
    @OperationLog(name = "详情", type = OperationLogType.INFO)
    @ApiOperation(value = "详情", response = BzzConfig.class)
    public ApiResult<BzzConfig> getBzzConfig(@PathVariable("id") Long id) throws Exception {
        BzzConfig bzzConfig = bzzConfigService.getById(id);
        return ApiResult.ok(bzzConfig);
    }

    /**
     * 分页列表
     */
    @PostMapping("/getPageList")
    @OperationLog(name = "分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "分页列表", response = BzzConfig.class)
    public ApiResult<Paging<BzzConfig>> getBzzConfigPageList(@Validated @RequestBody BzzConfigPageParam bzzConfigPageParam) throws Exception {
        Paging<BzzConfig> paging = bzzConfigService.getBzzConfigPageList(bzzConfigPageParam);
        return ApiResult.ok(paging);
    }

    /***
     * 币种数据
     */
    @PostMapping("/getCurrencyData")
    @CheckLogin
    public ApiResult<NodeDataVo> getCurrencyData() throws Exception {
        NodeDataVo getCurrencyData = (NodeDataVo) redisUtil.get("getCurrencyData");
        if (ObjectUtils.isNotEmpty(getCurrencyData)){
            return ApiResult.ok(getCurrencyData);
        }
        //企亚有效算力单T产量（FIL/TiB）
        Object o1 = redisUtil.get(Constants.XCH_24H_INCOME);
        String dayIncome =o1.toString();
        dayIncome = dayIncome.replaceAll("E-", "0");
//        String dayIncome = new ChiaDayIncomeTaskTwo().getDayIncome();

        //企亚矿池总算力
//        String supply = new MarketServiceTwo().getXchDesc().getSupply();
//        int supply = orderService.lambdaQuery().eq(Order::getPowerSymbol, "XCH").eq(Order::getStatus,2).list().stream().mapToInt(Order::getQuantity).sum();
        BigDecimal supply = powerOrderMapper.sumSystemPowerBySymbol("XCH");

        //获取bzz矿池节点总数
//        long beenodesAll = new MarketServiceTwo().getBzzDesc().getBeenodesAll();
//        int beenodesAll = orderService.lambdaQuery().eq(Order::getPowerSymbol, "BZZ").eq(Order::getStatus,2).list().stream().mapToInt(Order::getQuantity).sum();
        BigDecimal beenodesAll = powerOrderMapper.sumSystemPowerBySymbol("BZZ");
        //获取后台设置的节点数据
        List<BzzConfig> list = bzzConfigService.lambdaQuery().list();
        BzzConfig bzzConfig =null;
        if (ObjectUtils.isNotEmpty(list)){
            list.sort((t1, t2) -> t2.getUpdateTime().compareTo(t1.getUpdateTime()));
            bzzConfig=list.get(0);
        }

        //获取fil币的数据信息
//        OverviewVo overviewVo = marketService.getMainNeiInfo();
        Object o = redisUtil.get(Constants.FIL_INFO);

        System.out.println(o);
        FilVo filVo = JSON.parseObject(o.toString(), FilVo.class);
        System.out.println(filVo);


        int totalQualityAdjPower = orderService.lambdaQuery().eq(Order::getPowerSymbol, "FIL").eq(Order::getStatus,2).list().stream().mapToInt(Order::getQuantity).sum();


        //将上面数据封存实体类中
        NodeDataVo nodeDataVo = new NodeDataVo();
        nodeDataVo.setBeenodesAll(beenodesAll);
        nodeDataVo.setDayIncome(dayIncome);
        if (bzzConfig!=null){
            nodeDataVo.setExchangeToday(bzzConfig.getExchangeToday());
            nodeDataVo.setIssuedToday(bzzConfig.getIssuedToday());
        }
        nodeDataVo.setSupply(supply+"");
        nodeDataVo.setTotalQualityAdjPower(totalQualityAdjPower+"");
        nodeDataVo.setDailyCoinsMined(filVo.getOverview().getDailyCoinsMined());
        Long expireTime=60L;
        redisUtil.remove("getCurrencyData");
        redisUtil.set("getCurrencyData",nodeDataVo,expireTime);
        return ApiResult.ok(nodeDataVo);
    }

}

