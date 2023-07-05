package io.filpool.pool.controller.app;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import io.filpool.config.constant.CommonConstant;
import io.filpool.framework.jwt.CheckLogin;
import io.filpool.framework.util.Constants;
import io.filpool.framework.util.RedisUtil;
import io.filpool.pool.entity.ActivityTopics;
import io.filpool.pool.entity.Goods;
import io.filpool.pool.entity.Quotation;
import io.filpool.pool.service.ActivityTopicsService;
import io.filpool.pool.service.GoodsService;
import io.filpool.pool.service.QuotationService;
import io.filpool.pool.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import io.filpool.pool.param.ActivityTopicsPageParam;
import io.filpool.framework.common.controller.BaseController;
import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.common.param.IdParam;
import io.filpool.framework.log.annotation.Module;
import io.filpool.framework.log.annotation.OperationLog;
import io.filpool.framework.log.enums.OperationLogType;
import io.filpool.framework.core.validator.groups.Add;
import io.filpool.framework.core.validator.groups.Update;
import org.apache.commons.lang3.StringUtils;
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
 * 商品活动专题 控制器
 *
 * @author filpool
 * @since 2021-06-25
 */
@Slf4j
@RestController
@RequestMapping(Constants.API_PREFIX  +"/activityTopics")
@Module("pool")
@Api(value = "商品活动专题API", tags = {"商品活动专题"})
public class ActivityTopicsController extends BaseController {

    @Autowired
    private ActivityTopicsService activityTopicsService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private QuotationService quotationService;

    /**
     * 添加商品活动专题
     */
    @PostMapping("/add")
    @OperationLog(name = "添加商品活动专题", type = OperationLogType.ADD)
    @ApiOperation(value = "添加商品活动专题", response = ApiResult.class)
    public ApiResult<Boolean> addActivityTopics(@Validated(Add.class) @RequestBody ActivityTopics activityTopics) throws Exception {
        boolean flag = activityTopicsService.saveActivityTopics(activityTopics);
        return ApiResult.result(flag);
    }

    /**
     * 修改商品活动专题
     */
    @PostMapping("/update")
    @OperationLog(name = "修改商品活动专题", type = OperationLogType.UPDATE)
    @ApiOperation(value = "修改商品活动专题", response = ApiResult.class)
    public ApiResult<Boolean> updateActivityTopics(@Validated(Update.class) @RequestBody ActivityTopics activityTopics) throws Exception {
        boolean flag = activityTopicsService.updateActivityTopics(activityTopics);
        return ApiResult.result(flag);
    }

    /**
     * 删除商品活动专题
     */
    @PostMapping("/delete/{id}")
    @OperationLog(name = "删除商品活动专题", type = OperationLogType.DELETE)
    @ApiOperation(value = "删除商品活动专题", response = ApiResult.class)
    public ApiResult<Boolean> deleteActivityTopics(@PathVariable("id") Long id) throws Exception {
        boolean flag = activityTopicsService.deleteActivityTopics(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取商品活动专题详情
     */
    @GetMapping("/info/{id}")
    @OperationLog(name = "商品活动专题详情", type = OperationLogType.INFO)
    @ApiOperation(value = "商品活动专题详情", response = ActivityTopics.class)
    public ApiResult<ActivityTopics> getActivityTopics(@PathVariable("id") Long id) throws Exception {
        ActivityTopics activityTopics = activityTopicsService.getById(id);
        return ApiResult.ok(activityTopics);
    }

    /**
     * 商品活动专题分页列表
     */
    @PostMapping("/getPageList")
    @OperationLog(name = "商品活动专题分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "商品活动专题分页列表", response = ActivityTopics.class)
    public ApiResult<Paging<ActivityTopics>> getActivityTopicsPageList(@Validated @RequestBody ActivityTopicsPageParam activityTopicsPageParam) throws Exception {
        Paging<ActivityTopics> paging = activityTopicsService.getActivityTopicsPageList(activityTopicsPageParam);
        return ApiResult.ok(paging);
    }

    /***
     * app商品活动专题
     */
    @PostMapping("/getGoodsActivityTopics")
    @OperationLog(name = "商品活动专题分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "商品活动专题分页列表", response = ActivityTopics.class)
    @CheckLogin
    public ApiResult<ActivityTopics> getGoodsActivityTopics(){
        //查詢商品活動专栏
        List<ActivityTopics> list = activityTopicsService.lambdaQuery().eq(ActivityTopics::getStatus,1).list();

        //根据时间进行排序
        list.sort((t1,t2)->t2.getUpdateTime().compareTo(t1.getUpdateTime()));
        //取出最新的那个活动
        ActivityTopics activityTopics = list.get(0);
        String[] split = activityTopics.getGoodsList().split(",");
        //根据活动中的商品id查询这些商品的相信信息
        List<Goods> list1 = goodsService.lambdaQuery().in(Goods::getId, split).list();
        List<GoodsVo> goodsVos = new ArrayList<>();
        for (Goods good : list1) {
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
            goodsVos.add(vo);
        }

       /* for (Goods goods:list1) {
            GoodsVo goodsVo = new GoodsVo();
            BeanUtils.copyProperties(goods,goodsVo);
            goodsVos.add(goodsVo);
        }*/
        if (ObjectUtils.isNotEmpty(list1)){
            //将商品详细信息存到实体类中
            activityTopics.setGoodsListTwo(goodsVos);
        }
        return ApiResult.ok(activityTopics);
    }


}

