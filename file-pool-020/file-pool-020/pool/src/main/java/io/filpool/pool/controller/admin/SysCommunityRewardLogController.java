package io.filpool.pool.controller.admin;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.common.controller.BaseController;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.validator.groups.Add;
import io.filpool.framework.core.validator.groups.Update;
import io.filpool.framework.log.annotation.Module;
import io.filpool.framework.log.annotation.OperationLog;
import io.filpool.framework.log.enums.OperationLogType;
import io.filpool.pool.entity.*;
import io.filpool.pool.excel.CommunityRewardLogDescExcel;
import io.filpool.pool.excel.MiningRecordExcel;
import io.filpool.pool.mapper.ReturnedRewardConfigMapper;
import io.filpool.pool.param.CommunityRewardLogPageParam;
import io.filpool.pool.param.ReturnedRewardConfigPageParam;
import io.filpool.pool.request.SysCommunityRewardLogDescRequest;
import io.filpool.pool.request.SysCommunityRewardLogRequest;
import io.filpool.pool.service.CommunityRewardLogService;
import io.filpool.pool.service.InviteRecordService;
import io.filpool.pool.service.ReturnedRewardConfigService;
import io.filpool.pool.service.UserService;
import io.filpool.pool.util.FileUtil;
import io.filpool.pool.vo.CommunityRewardLogVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 社区(市场)奖励记录表 控制器
 *
 * @author filpool
 * @since 2021-06-01
 */
@Slf4j
@RestController
@RequestMapping("/sys/communityRewardLog")
@Module("pool")
@Api(value = "社区(市场)奖励记录表API", tags = {"社区(市场)奖励记录表"})
public class SysCommunityRewardLogController extends BaseController {

    @Autowired
    private CommunityRewardLogService communityRewardLogService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReturnedRewardConfigMapper returnedRewardConfigMapper;

    @Autowired
    private InviteRecordService inviteRecordService;

    @Autowired
    private SysUtilController sysUtilController;

    @Autowired
    private ReturnedRewardConfigService returnedRewardConfigService;

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
     * 社区(市场)奖励记录表分页列表(后台)
     */
    @PostMapping("/getPageList")
    @OperationLog(name = "社区(市场)奖励记录表分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "社区(市场)奖励记录表分页列表", response = CommunityRewardLog.class)
    public ApiResult<Paging<CommunityRewardLogVo>> getCommunityRewardLogPageList(@Validated @RequestBody SysCommunityRewardLogRequest req){
        List<CommunityRewardLog> list1 = communityRewardLogService.lambdaQuery().list();
        //过滤出社区奖励表中拿过奖励的所有用户id
        Set<Long> setUserId = new LinkedHashSet<>();
        for (CommunityRewardLog communityRewardLog:list1) {
            setUserId.add(communityRewardLog.getRewardUserId());
        }

        List<CommunityRewardLogVo> communityRewardLogList = new ArrayList<>();

        //循环拿过奖励的用户id
        setUserId.stream().forEach(x->{
            //获取用户信息(用户手机号码)
            User one = userService.lambdaQuery().eq(User::getId, x).one();
            //用户等级
            ReturnedRewardConfig configByPower = returnedRewardConfigMapper.getConfigByPowerTwo(one.getCommunityExperience());
            //上级邀请人
            InviteRecord invite = inviteRecordService.lambdaQuery().eq(InviteRecord::getUserId, x).one();
            //上级邀请人的手机号
            String account = sysUtilController.queryAccount(invite.getInviteUserId());
            //团队人数
            List<Long> longs = sysUtilController.subordinateUser(x);

            List<CommunityRewardLog> list = communityRewardLogService.lambdaQuery().eq(CommunityRewardLog::getRewardUserId, x).list();
            BigDecimal rewardUsdt=BigDecimal.ZERO;
            BigDecimal powerFilReward=BigDecimal.ZERO;
            BigDecimal powerXchReward=BigDecimal.ZERO;
            BigDecimal powerBzzReward=BigDecimal.ZERO;
            if (ObjectUtils.isNotEmpty(list)){
                //累计总收益U
                BigDecimal usdtLevel = list.stream().map(CommunityRewardLog::getUsdtLevelLeward).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal usdtRange = list.stream().map(CommunityRewardLog::getUsdtRangeReward).reduce(BigDecimal.ZERO, BigDecimal::add);
                rewardUsdt=usdtLevel.add(usdtRange);

                //Fil累计总收益
                BigDecimal powerLevelFilReward = list.stream().filter(z->z.getPowerSymbol().equals("FIL")).map(CommunityRewardLog::getPowerLevelReward).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal powerRangeFilReward = list.stream().filter(z->z.getPowerSymbol().equals("FIL")).map(CommunityRewardLog::getPowerRangeReward).reduce(BigDecimal.ZERO, BigDecimal::add);
                powerFilReward=powerLevelFilReward.add(powerRangeFilReward);

                //XCH累计总收益
                BigDecimal powerLevelXchReward = list.stream().filter(z->z.getPowerSymbol().equals("XCH")).map(CommunityRewardLog::getPowerLevelReward).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal powerRangeXchReward = list.stream().filter(z->z.getPowerSymbol().equals("XCH")).map(CommunityRewardLog::getPowerRangeReward).reduce(BigDecimal.ZERO, BigDecimal::add);
                powerXchReward=powerLevelXchReward.add(powerRangeXchReward);

                //BZZ累计总收益
                BigDecimal powerLevelBzzReward = list.stream().filter(z->z.getPowerSymbol().equals("BZZ")).map(CommunityRewardLog::getPowerLevelReward).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal powerRangeBzzReward = list.stream().filter(z->z.getPowerSymbol().equals("BZZ")).map(CommunityRewardLog::getPowerRangeReward).reduce(BigDecimal.ZERO, BigDecimal::add);
                powerBzzReward=powerLevelBzzReward.add(powerRangeBzzReward);

            }

            CommunityRewardLogVo communityRewardLogVo = new CommunityRewardLogVo();
            communityRewardLogVo.setAccount(sysUtilController.queryAccount(one.getId()));
            if (ObjectUtils.isNotEmpty(configByPower)){
                communityRewardLogVo.setLevel(configByPower.getLevel());
            }else{
                communityRewardLogVo.setLevel(0);
            }
            communityRewardLogVo.setSuperiorAccount(account);
            communityRewardLogVo.setTeamSize(longs.size());
            communityRewardLogVo.setRewardUsdt(rewardUsdt);
            communityRewardLogVo.setRewardFil(powerFilReward);
            communityRewardLogVo.setRewardXch(powerXchReward);
            communityRewardLogVo.setRewardBzz(powerBzzReward);

            communityRewardLogList.add(communityRewardLogVo);
        });

        //条件过滤
        List<CommunityRewardLogVo>  collect =null;
        if (req.getAccount()!=null){
             collect = communityRewardLogList.stream().filter(x -> x.getAccount().equals(req.getAccount())).collect(Collectors.toList());
        }else{
            collect =communityRewardLogList;
        }
        //手动分页
         collect = collect.stream().skip((req.getPageIndex() - 1) * req.getPageSize()).limit(req.getPageSize()).collect(Collectors.toList());

        Page<CommunityRewardLogVo> page = new Page<>();
        if (req.getAccount()!=null){
            page.setTotal(collect.size());
        }else{
            page.setTotal(setUserId.size());
        }
        page.setRecords(collect);

        return ApiResult.ok(new Paging<>(page));
    }




    /**
     * 社区(市场)奖励记录表分页列表-导出(后台)
     */
    @PostMapping("/getPageListExport")
    @OperationLog(name = "社区(市场)奖励记录表分页列表-导出", type = OperationLogType.PAGE)
    @ApiOperation(value = "社区(市场)奖励记录表分页列表-导出", response = CommunityRewardLog.class)
    public void getCommunityRewardLogPageListExport(@Validated @RequestBody SysCommunityRewardLogRequest req, HttpServletResponse response) throws Exception {
        ApiResult<Paging<CommunityRewardLogVo>> communityRewardLogPageList = this.getCommunityRewardLogPageList(req);
        FileUtil.exportExcel(communityRewardLogPageList.getData().getRecords(), CommunityRewardLogVo.class,"市场奖励列表",response);
    }




    /**
     * 获取社区(市场)奖励记录表详情
     */
    @PostMapping("/getCommunityRewardLogDesc")
    @OperationLog(name = "社区(市场)奖励记录表详情")
    @ApiOperation(value = "社区(市场)奖励记录表详情", response = CommunityRewardLog.class)
    public ApiResult<Paging<CommunityRewardLog>> getCommunityRewardLogDesc(@RequestBody SysCommunityRewardLogDescRequest req){
        //根据手机号查询用户id
        Long userId= sysUtilController.queryUserId(req.getAccount());
        //根据用户id查询它伞下有哪些人
//        List<Long> subordinateUser = sysUtilController.subordinateUser(userId);
        //存储内容的集合
        List<CommunityRewardLog> returnList = new ArrayList<>();

        //循环伞下用户id
        List<CommunityRewardLog> list = communityRewardLogService.lambdaQuery().eq(CommunityRewardLog::getRewardUserId, userId).orderByDesc(CommunityRewardLog::getCreateTime).list();
        //查询三个用户的手机号
        for (CommunityRewardLog communityRewardLog:list) {
            communityRewardLog.setBuyerUserAccount(sysUtilController.queryAccount(communityRewardLog.getBuyerUserId()));
            communityRewardLog.setDownUserAccount(sysUtilController.queryAccount(communityRewardLog.getDownUserId()));
            communityRewardLog.setRewardUserAccount(sysUtilController.queryAccount(communityRewardLog.getRewardUserId()));

            if (communityRewardLog.getPowerSymbol().equals("XCH")){
                communityRewardLog.setPowerRangeXchReward(communityRewardLog.getPowerRangeReward());
                communityRewardLog.setPowerRangeReward(BigDecimal.ZERO);
            }else{
                communityRewardLog.setPowerRangeXchReward(BigDecimal.ZERO);
            }
        }
        returnList.addAll(list);

        //过滤条件
        List<CommunityRewardLog> collect =null;
        if (req.getSearchAccount()!=null){
            collect = returnList.stream().filter(x -> x.getBuyerUserAccount().equals(req.getSearchAccount())).collect(Collectors.toList());
        }else{
            collect = returnList;
        }
        //分页
         collect = collect.stream().skip((req.getPageIndex() - 1) * req.getPageSize()).limit(req.getPageSize()).collect(Collectors.toList());

        Page<CommunityRewardLog> page = new Page<>();
        page.setRecords(collect);
        if (req.getSearchAccount()!=null){
            page.setTotal(collect.size());
        }else{
            page.setTotal(returnList.size());
        }
        return ApiResult.ok(new Paging<>(page));
    }



    /**
     * 获取社区(市场)奖励记录表详情-导出
     */
    @PostMapping("/getCommunityRewardLogDescExport")
    @OperationLog(name = "社区(市场)奖励记录表详情-导出")
    @ApiOperation(value = "社区(市场)奖励记录表详情-导出", response = CommunityRewardLog.class)
    public void getCommunityRewardLogDescExport(@RequestBody SysCommunityRewardLogDescRequest req,HttpServletResponse response) throws Exception {
        ApiResult<Paging<CommunityRewardLog>> communityRewardLogDesc = this.getCommunityRewardLogDesc(req);

        //将数据循环存到导出实体类中
        List<CommunityRewardLogDescExcel> excelInviteRecordList = new ArrayList<>();

        for (CommunityRewardLog communityRewardLog:communityRewardLogDesc.getData().getRecords()) {
            CommunityRewardLogDescExcel communityRewardLogDescExcel = new CommunityRewardLogDescExcel();
            BeanUtils.copyProperties(communityRewardLog,communityRewardLogDescExcel);

            communityRewardLogDescExcel.setUsdtRangeReward(communityRewardLog.getUsdtRangeReward().stripTrailingZeros().toPlainString());
            communityRewardLogDescExcel.setUsdtLevelLeward(communityRewardLog.getUsdtLevelLeward().stripTrailingZeros().toPlainString());
            communityRewardLogDescExcel.setPowerRangeReward(communityRewardLog.getPowerRangeReward().stripTrailingZeros().toPlainString());
            communityRewardLogDescExcel.setPowerLevelReward(communityRewardLog.getPowerLevelReward().stripTrailingZeros().toPlainString());

            excelInviteRecordList.add(communityRewardLogDescExcel);
        }
        FileUtil.exportExcel(excelInviteRecordList, CommunityRewardLogDescExcel.class,"市场奖励详情",response);

    }


}

