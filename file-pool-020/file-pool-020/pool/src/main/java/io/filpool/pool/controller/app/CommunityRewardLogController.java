package io.filpool.pool.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.common.controller.BaseController;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.validator.groups.Add;
import io.filpool.framework.core.validator.groups.Update;
import io.filpool.framework.log.annotation.Module;
import io.filpool.framework.log.annotation.OperationLog;
import io.filpool.framework.log.enums.OperationLogType;
import io.filpool.framework.util.Constants;
import io.filpool.pool.controller.admin.SysUtilController;
import io.filpool.pool.entity.CommunityRewardLog;
import io.filpool.pool.entity.Order;
import io.filpool.pool.entity.Supplement;
import io.filpool.pool.mapper.CommunityRewardLogMapper;
import io.filpool.pool.request.TopCommunityRewardRequest;
import io.filpool.pool.service.CommunityRewardLogService;
import io.filpool.pool.service.SupplementDeductService;
import io.filpool.pool.service.impl.OrderServiceImpl;
import io.filpool.pool.service.impl.SupplementServiceImpl;
import io.filpool.pool.service.impl.UserServiceImpl;
import io.filpool.pool.vo.TopCommunityRewardLogVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 社区(市场)奖励记录表 控制器
 *
 * @author filpool
 * @since 2021-06-01
 */
@Slf4j
@RestController
@RequestMapping(Constants.API_PREFIX +"/communityRewardLog")
@Module("pool")
@Api(value = "社区(市场)奖励记录表API", tags = {"社区(市场)奖励记录表"})
public class CommunityRewardLogController extends BaseController {

    @Autowired
    private CommunityRewardLogService communityRewardLogService;

    @Autowired
    private SysUtilController sysUtilController;

    @Autowired
    private CommunityRewardLogMapper communityRewardLogMapper;

    @Autowired
    private SupplementServiceImpl supplementService;

    @Autowired
    private OrderServiceImpl orderService;
    @Autowired
    private UserServiceImpl userService;

    /**
     * 添加社区(市场)奖励记录表
     */
    @PostMapping("/add")
    @OperationLog(name = "添加社区(市场)奖励记录表", type = OperationLogType.ADD)
    @ApiOperation(value = "添加社区(市场)奖励记录表", response = ApiResult.class)
    public ApiResult<Boolean> addCommunityRewardLog(@Validated(Add.class) @RequestBody CommunityRewardLog communityRewardLog) throws Exception {
        boolean flag = communityRewardLogService.saveCommunityRewardLog(communityRewardLog);
        return ApiResult.result(flag);
    }

    /**
     * 修改社区(市场)奖励记录表
     */
    @PostMapping("/update")
    @OperationLog(name = "修改社区(市场)奖励记录表", type = OperationLogType.UPDATE)
    @ApiOperation(value = "修改社区(市场)奖励记录表", response = ApiResult.class)
    public ApiResult<Boolean> updateCommunityRewardLog(@Validated(Update.class) @RequestBody CommunityRewardLog communityRewardLog) throws Exception {
        boolean flag = communityRewardLogService.updateCommunityRewardLog(communityRewardLog);
        return ApiResult.result(flag);
    }

    /**
     * 删除社区(市场)奖励记录表
     */
    @PostMapping("/delete/{id}")
    @OperationLog(name = "删除社区(市场)奖励记录表", type = OperationLogType.DELETE)
    @ApiOperation(value = "删除社区(市场)奖励记录表", response = ApiResult.class)
    public ApiResult<Boolean> deleteCommunityRewardLog(@PathVariable("id") Long id) throws Exception {
        boolean flag = communityRewardLogService.deleteCommunityRewardLog(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取社区(市场)奖励记录表详情
     */
    @GetMapping("/info/{id}")
    @OperationLog(name = "社区(市场)奖励记录表详情", type = OperationLogType.INFO)
    @ApiOperation(value = "社区(市场)奖励记录表详情", response = CommunityRewardLog.class)
    public ApiResult<CommunityRewardLog> getCommunityRewardLog(@PathVariable("id") Long id) throws Exception {
        CommunityRewardLog communityRewardLog = communityRewardLogService.getById(id);
        return ApiResult.ok(communityRewardLog);
    }

    /**
     * 社区(市场)奖励记录表分页列表
     */
    @PostMapping("/getPageList")
    @OperationLog(name = "社区(市场)奖励记录表分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "社区(市场)奖励记录表分页列表", response = CommunityRewardLog.class)
    public ApiResult<Paging<CommunityRewardLog>> getCommunityRewardLogPageList(@Validated @RequestBody TopCommunityRewardRequest topCommunityRewardRequest) throws Exception {
        Page<CommunityRewardLog> page = new Page<>(topCommunityRewardRequest.getPageIndex(), topCommunityRewardRequest.getPageSize());
        LambdaQueryWrapper<CommunityRewardLog> wr = Wrappers.lambdaQuery(CommunityRewardLog.class);
        wr.eq(CommunityRewardLog::getRewardUserId,topCommunityRewardRequest.getId());
        wr.orderByDesc(CommunityRewardLog::getCreateTime);
        Page<CommunityRewardLog> communityRewardLogPage = communityRewardLogMapper.selectPage(page, wr);

        for (CommunityRewardLog communityRewardLog:communityRewardLogPage.getRecords()) {
            communityRewardLog.setDownUserAccount(userService.getUserAccount(communityRewardLog.getBuyerUserId(),true));
            communityRewardLog.setUsdtReward(communityRewardLog.getUsdtRangeReward().add(communityRewardLog.getUsdtLevelLeward()));
            communityRewardLog.setPowerReward(communityRewardLog.getPowerLevelReward().add(communityRewardLog.getPowerRangeReward()));
            communityRewardLog.setRewardUserLevel(sysUtilController.markLevel(communityRewardLog.getBuyerUserId()));

            //如果是补单的，就去查询它是云算力补单还是矿机补单
            if (communityRewardLog.getRewardType()==2){
                Supplement one = supplementService.lambdaQuery().eq(Supplement::getId, communityRewardLog.getOrderNumber()).one();
                //3补单云算力 4补单矿机
                communityRewardLog.setRewardType(one.getType()==1?3:4);
            }
            if (communityRewardLog.getRewardType()==1){
                Order one = orderService.lambdaQuery().eq(Order::getOrderNumber, communityRewardLog.getOrderNumber()).one();
                //1购买矿机，2购买云算力
                communityRewardLog.setRewardType(one.getGoodType());
            }
        }
        return ApiResult.ok(new Paging<>(communityRewardLogPage));
    }


    /***
     * app社区奖励顶部接口
     */
    @PostMapping("/topCommunityReward")
    @OperationLog(name = "app社区奖励顶部接口")
    @ApiOperation(value = "app社区奖励顶部接口")
    public ApiResult<TopCommunityRewardLogVo> topCommunityReward(@Validated @RequestBody TopCommunityRewardRequest topCommunityRewardRequest) throws Exception {

        //查询昨天的收入信息记录
        List<CommunityRewardLog> topCommunityRewardList =communityRewardLogMapper.topCommunityReward(topCommunityRewardRequest.getId());
        //查询该用户所有收入记录
        List<CommunityRewardLog> listAll = communityRewardLogService.lambdaQuery().eq(CommunityRewardLog::getRewardUserId, topCommunityRewardRequest.getId()).list();

        BigDecimal yesterdayRewardUsdt=BigDecimal.ZERO;
        BigDecimal yesterdayRewardFil=BigDecimal.ZERO;
        BigDecimal yesterdayRewardXch=BigDecimal.ZERO;
        BigDecimal yesterdayRewardBzz=BigDecimal.ZERO;
        BigDecimal rewardUsdt=BigDecimal.ZERO;
        BigDecimal rewardFil=BigDecimal.ZERO;
        BigDecimal rewardXch=BigDecimal.ZERO;
        BigDecimal rewardBzz=BigDecimal.ZERO;

        if (ObjectUtils.isNotEmpty(topCommunityRewardList)){
            //昨日收益（USDT）
            BigDecimal usdtLevel = topCommunityRewardList.stream().map(CommunityRewardLog::getUsdtLevelLeward).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal usdtRange = topCommunityRewardList.stream().map(CommunityRewardLog::getUsdtRangeReward).reduce(BigDecimal.ZERO, BigDecimal::add);
             yesterdayRewardUsdt=usdtLevel.add(usdtRange);
            //FIL昨日收益（T）
            BigDecimal powerLevelFilReward = topCommunityRewardList.stream().filter(z->z.getPowerSymbol().equals("FIL")).map(CommunityRewardLog::getPowerLevelReward).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal powerRangeFilReward = topCommunityRewardList.stream().filter(z->z.getPowerSymbol().equals("FIL")).map(CommunityRewardLog::getPowerRangeReward).reduce(BigDecimal.ZERO, BigDecimal::add);
             yesterdayRewardFil=powerLevelFilReward.add(powerRangeFilReward);
            //XCH昨日收益（T）
            BigDecimal powerLevelXchReward = topCommunityRewardList.stream().filter(z->z.getPowerSymbol().equals("XCH")).map(CommunityRewardLog::getPowerLevelReward).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal powerRangeXchReward = topCommunityRewardList.stream().filter(z->z.getPowerSymbol().equals("XCH")).map(CommunityRewardLog::getPowerRangeReward).reduce(BigDecimal.ZERO, BigDecimal::add);
             yesterdayRewardXch=powerLevelXchReward.add(powerRangeXchReward);
           //BZZ昨日收益（节点）
            BigDecimal powerLevelBzzReward = topCommunityRewardList.stream().filter(z->z.getPowerSymbol().equals("BZZ")).map(CommunityRewardLog::getPowerLevelReward).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal powerRangeBzzReward = topCommunityRewardList.stream().filter(z->z.getPowerSymbol().equals("BZZ")).map(CommunityRewardLog::getPowerRangeReward).reduce(BigDecimal.ZERO, BigDecimal::add);
            yesterdayRewardBzz=powerLevelBzzReward.add(powerRangeBzzReward);

        }
        if (ObjectUtils.isNotEmpty(listAll)){
            //累计总收益（USDT）
            BigDecimal usdtLevel = listAll.stream().map(CommunityRewardLog::getUsdtLevelLeward).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal usdtRange = listAll.stream().map(CommunityRewardLog::getUsdtRangeReward).reduce(BigDecimal.ZERO, BigDecimal::add);
             rewardUsdt=usdtLevel.add(usdtRange);
            //FIL累计总收益（T）
            BigDecimal powerLevelFilReward = listAll.stream().filter(z->z.getPowerSymbol().equals("FIL")).map(CommunityRewardLog::getPowerLevelReward).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal powerRangeFilReward = listAll.stream().filter(z->z.getPowerSymbol().equals("FIL")).map(CommunityRewardLog::getPowerRangeReward).reduce(BigDecimal.ZERO, BigDecimal::add);
             rewardFil=powerLevelFilReward.add(powerRangeFilReward);
            //XCH累计总收益（T）
            BigDecimal powerLevelXchReward = listAll.stream().filter(z->z.getPowerSymbol().equals("XCH")).map(CommunityRewardLog::getPowerLevelReward).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal powerRangeXchReward = listAll.stream().filter(z->z.getPowerSymbol().equals("XCH")).map(CommunityRewardLog::getPowerRangeReward).reduce(BigDecimal.ZERO, BigDecimal::add);
             rewardXch=powerLevelXchReward.add(powerRangeXchReward);
            //BZZ累计总收益（节点）
            BigDecimal powerLevelBzzReward = listAll.stream().filter(z->z.getPowerSymbol().equals("BZZ")).map(CommunityRewardLog::getPowerLevelReward).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal powerRangeBzzReward = listAll.stream().filter(z->z.getPowerSymbol().equals("BZZ")).map(CommunityRewardLog::getPowerRangeReward).reduce(BigDecimal.ZERO, BigDecimal::add);
            rewardBzz=powerLevelBzzReward.add(powerRangeBzzReward);
        }
        TopCommunityRewardLogVo tcrlv = new TopCommunityRewardLogVo();
        tcrlv.setRewardFil(rewardFil);
        tcrlv.setRewardUsdt(rewardUsdt);
        tcrlv.setRewardXch(rewardXch);
        tcrlv.setYesterdayRewardFil(yesterdayRewardFil);
        tcrlv.setYesterdayRewardUsdt(yesterdayRewardUsdt);
        tcrlv.setYesterdayRewardXch(yesterdayRewardXch);
        tcrlv.setYesterdayRewardBzz(yesterdayRewardBzz);
        tcrlv.setRewardBzz(rewardBzz);
        return ApiResult.ok(tcrlv);
    }




}

