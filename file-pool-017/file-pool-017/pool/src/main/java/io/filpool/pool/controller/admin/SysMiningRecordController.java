package io.filpool.pool.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import io.filpool.framework.common.exception.FILPoolException;
import io.filpool.framework.redislock.RedisLock;
import io.filpool.pool.entity.*;
import io.filpool.pool.excel.IncomeRecordExcel;
import io.filpool.pool.excel.IncomeRecordLogExcel;
import io.filpool.pool.excel.InviteExcel;
import io.filpool.pool.excel.MiningRecordExcel;
import io.filpool.pool.request.*;
import io.filpool.pool.service.CurrencyService;
import io.filpool.pool.service.IncomeReleaseLogService;
import io.filpool.pool.service.MiningRecordService;
import io.filpool.pool.service.UserService;
import io.filpool.pool.service.impl.IncomeReleaseRecordServiceImpl;
import io.filpool.pool.util.FileUtil;
import io.filpool.pool.vo.SysIncomeRecordVo;
import io.filpool.pool.vo.SysMiningTotalVo;
import lombok.extern.slf4j.Slf4j;
import io.filpool.pool.param.MiningRecordPageParam;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 平台挖矿记录表 控制器
 *
 * @author filpool
 * @since 2021-03-22
 */
@Slf4j
@RestController
@RequestMapping("sys/miningRecord")
@Module("pool")
@Api(value = "平台挖矿记录表API", tags = {"平台挖矿管理"})
public class SysMiningRecordController extends BaseController {

    @Autowired
    private MiningRecordService miningRecordService;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisLock redisLock;
    @Autowired
    private IncomeReleaseRecordServiceImpl incomeReleaseRecordService;
    @Autowired
    private IncomeReleaseLogService incomeReleaseLogService;

    /**
     * 添加平台挖矿记录表（挖矿管理-新增）
     */
    @PostMapping("/addMiningRecord")
    @OperationLog(name = "添加平台挖矿记录", type = OperationLogType.ADD)
    @ApiOperation(value = "添加平台挖矿记录", response = ApiResult.class)
    public ApiResult<Boolean> addMiningRecord(@RequestBody SysMiningRequest request) throws Exception {
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return ApiResult.fail("请输入矿池收益");
        }
        if (request.getIsAgent() != null && request.getIsAgent()) {
            if (StringUtils.isEmpty(request.getAgentAccount())) {
                return ApiResult.fail("请输入代理商账号");
            }
        }
        String lockKey = "sysaddmining";
        if (redisLock.lock(lockKey, 60)) {
            try {
                if (request.getIsAgent()) {
                    User one = userService.lambdaQuery().eq(User::getMobile, request.getAgentAccount())
                            .or().eq(User::getEmail, request.getAgentAccount()).eq(User::getType, 1).one();
                    if (one == null) {
                        return ApiResult.fail("无该代理商账号");
                    }
                }
                return ApiResult.result(miningRecordService.addMiningRecord(request));
            } finally {
                redisLock.unlock(lockKey);
            }
        } else {
            return ApiResult.fail("发放挖矿收益中,请勿操作");
        }
    }

    /**
     * 添加平台挖矿记录表（挖矿管理）
     */
    @PostMapping("/getMiningRecord")
    @ApiOperation("平台挖矿记录")
    @Transactional
    public ApiResult<Paging<MiningRecord>> getAgentRecord(@RequestBody SysMiningPageRequest request) throws Exception {
        LambdaQueryWrapper<MiningRecord> wr = Wrappers.lambdaQuery(MiningRecord.class);
        if (request.getIsAgent() != null) {
            wr.eq(MiningRecord::getType, request.getIsAgent() ? 2 : 1);
        }
        if (request.getStartTime() != null) {
            wr.ge(MiningRecord::getCreateTime, request.getStartTime());
        }
        if (request.getEndTime() != null) {
            wr.le(MiningRecord::getCreateTime, request.getEndTime());
        }
        Page<MiningRecord> page = new Page<>(request.getPageIndex(), request.getPageSize());
        page = miningRecordService.getBaseMapper().selectPage(page, wr);
        return ApiResult.ok(new Paging<>(page));
    }
    /**
     * 添加平台挖矿记录表（挖矿管理-导出）
     */
    @PostMapping("/getMiningRecordExcel")
    @ApiOperation("平台挖矿记录-导出")
    @Transactional
    public void getMiningRecordExcel(@RequestBody SysMiningPageRequest request, HttpServletResponse response) throws Exception {
        ApiResult<Paging<MiningRecord>> agentRecord = getAgentRecord(request);
        List<MiningRecord> records = agentRecord.getData().getRecords();

        //将数据循环存到导出实体类中
        List<MiningRecordExcel> excelInviteRecordList = new ArrayList<>();

        for (MiningRecord miningRecord:records) {
            MiningRecordExcel miningRecordExcel = new MiningRecordExcel();
            BeanUtils.copyProperties(miningRecord,miningRecordExcel);
            excelInviteRecordList.add(miningRecordExcel);
        }
        FileUtil.exportExcel(excelInviteRecordList,MiningRecordExcel.class,"平台挖矿记录",response);
    }

//    /**
//     * 查询用户挖矿累计收益
//     */
//    @PostMapping("/getTotalMiningList")
//    @ApiOperation("收益管理")
//    @Transactional
//    public ApiResult<Paging<SysMiningTotalVo>> getTotalMiningList(@RequestBody SysTotalMiningRequest request) throws Exception {
//        LambdaQueryWrapper<User> wr = Wrappers.lambdaQuery(User.class);
//        Paging<SysMiningTotalVo> paging = new Paging<>();
//        paging.setPageSize(request.getPageSize());
//        paging.setPageIndex(request.getPageIndex());
//        Currency currency = currencyService.lambdaQuery().eq(Currency::getSymbol,"FIL").one();
//
//        if (!StringUtils.isEmpty(request.getAccount())) {
//            wr.like(User::getMobile, request.getAccount())
//                    .like(User::getEmail, request.getAccount());
//        }
//        if (request.getIsAgent() != null) {
//            wr.eq(User::getType, request.getIsAgent() ? 1 : 0);
//        }
//        Page<User> page = new Page<>(request.getPageIndex(), request.getPageSize());
//        page = userService.getBaseMapper().selectPage(page, wr);
//        paging.setTotal(page.getTotal());
//        List<SysMiningTotalVo> voList = new ArrayList<>();
//        if (!ObjectUtils.isEmpty(page.getRecords())) {
//            for (User user : page.getRecords()) {
//                SysMiningTotalVo vo = new SysMiningTotalVo();
//                vo.setAccount(!StringUtils.isEmpty(user.getMobile()) ? user.getMobile() : user.getEmail());
//                vo.setUserId(user.getId());
//                vo.setTotalMiningAmount(incomeReleaseRecordService.getBaseMapper().getTotalIncome(user.getId()));
//                vo.setTotalReleaseAmount(incomeReleaseRecordService.getBaseMapper().getReleaseIncome(user.getId()));
//                vo.setTotalFrozenAmount(incomeReleaseRecordService.getBaseMapper().getFrozenIncome(user.getId()));
//                vo.setCurrencyId(currency.getId());
//                vo.setSymbol(currency.getSymbol());
//                voList.add(vo);
//            }
//        }
//        paging.setRecords(voList);
//        return ApiResult.ok(paging);
//    }

    /**
     * 查询用户收益记录（用户收益记录）
     */
    @PostMapping("/getIncomeRecord")
    @ApiOperation("用户收益记录")
    @Transactional
    public ApiResult<Paging<SysIncomeRecordVo>> getIncomeRecord(@RequestBody SysIncomeRecordRequest request) throws Exception {
        LambdaQueryWrapper<IncomeReleaseRecord> wr = Wrappers.lambdaQuery(IncomeReleaseRecord.class);
        Paging<SysIncomeRecordVo> paging = new Paging<>();
        if (!StringUtils.isEmpty(request.getAccount())) {
            User user = userService.lambdaQuery().eq(User::getMobile, request.getAccount())
                    .or().eq(User::getEmail, request.getAccount()).one();
            if (user != null)
                wr.eq(IncomeReleaseRecord::getUserId, user.getId());
            else
                wr.eq(IncomeReleaseRecord::getUserId, 0L);
        }
        if (request.getStartTime() != null) {
            wr.ge(IncomeReleaseRecord::getCreateTime, request.getStartTime());
        }
        if (request.getEndTime() != null) {
            wr.le(IncomeReleaseRecord::getCreateTime, request.getEndTime());
        }
        if (request.getIsAgent() != null) {
            wr.eq(IncomeReleaseRecord::getMiningType, request.getIsAgent() ? 2 : 1);
        }
        Page<IncomeReleaseRecord> page = new Page<>(request.getPageIndex(), request.getPageSize());
        page = incomeReleaseRecordService.getBaseMapper().selectPage(page, wr);
        paging.setTotal(page.getTotal());
        paging.setPageIndex(request.getPageIndex());
        paging.setPageSize(request.getPageSize());
        List<SysIncomeRecordVo> voList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(page.getRecords())) {
            for (IncomeReleaseRecord record : page.getRecords()) {
                SysIncomeRecordVo vo = new SysIncomeRecordVo();
                vo.setId(record.getId());
                vo.setUserId(record.getUserId());
                vo.setUserPower(record.getUserPower());
                vo.setFrozenAmount(record.getFrozenAmount());
                vo.setUserMiningAmount(record.getTotalAmount());
                vo.setReleaseAmount(record.getReleaseAmount());
                vo.setFirstAmount(record.getFirstAmount());
                MiningRecord miningRecord = miningRecordService.getById(record.getMiningRecordId());
                vo.setTotalMiningAmount(miningRecord!=null?miningRecord.getAmount():BigDecimal.ZERO);
                vo.setSystemPower(miningRecord!=null?miningRecord.getPower():BigDecimal.ZERO);
                vo.setAccount(userService.getUserAccount(record.getUserId(),false));
                vo.setSymbol(record.getSymbol());
                vo.setServiceFee(record.getServiceFee());
                voList.add(vo);
            }
        }
        paging.setRecords(voList);
        return ApiResult.ok(paging);
    }

    /**
     * 查询用户收益记录（用户收益记录-导出）
     */
    @PostMapping("/getIncomeRecordExcel")
    @ApiOperation("用户收益记录-导出")
    @Transactional
    public void getIncomeRecordExcel(@RequestBody SysIncomeRecordRequest request,HttpServletResponse response) throws Exception {
        ApiResult<Paging<SysIncomeRecordVo>> incomeRecord = getIncomeRecord(request);

        //将数据循环存到导出实体类中
        List<IncomeRecordExcel> excelInviteRecordList = new ArrayList<>();

        for (SysIncomeRecordVo sysIncomeRecordVo:incomeRecord.getData().getRecords()) {
            IncomeRecordExcel incomeRecordExcel = new IncomeRecordExcel();
            BeanUtils.copyProperties(sysIncomeRecordVo,incomeRecordExcel);
            excelInviteRecordList.add(incomeRecordExcel);
        }
        FileUtil.exportExcel(excelInviteRecordList,IncomeRecordExcel.class,"用户收益记录",response);

    }


    /**
     * 释放记录（用户收益记录-释放记录）
     * */
    @PostMapping("/getReleaseLog")
    @ApiOperation("释放记录")
    @Transactional
    public ApiResult<Paging<IncomeReleaseLog>> getReleaseLog(@RequestBody SysReleaseLogRequest request) throws Exception{
        if (request.getReleaseId() == null){
            throw new FILPoolException("id.not-empty");
        }
        LambdaQueryWrapper<IncomeReleaseLog> wr = Wrappers.lambdaQuery(IncomeReleaseLog.class)
                .eq(IncomeReleaseLog::getRecordId,request.getReleaseId());
        Page<IncomeReleaseLog> page = new Page<>(request.getPageIndex(),request.getPageSize());
        page = incomeReleaseLogService.getBaseMapper().selectPage(page,wr);
        return ApiResult.ok(new Paging<>(page));
    }

    /**
     * 释放记录（用户收益记录-释放记录-导出）
     * */
    @PostMapping("/getReleaseLogExcel")
    @ApiOperation("用户收益记录-释放记录-导出")
    @Transactional
    public void getReleaseLogExcel(@RequestBody SysReleaseLogRequest request,HttpServletResponse response) throws Exception{
        ApiResult<Paging<IncomeReleaseLog>> releaseLog = getReleaseLog(request);

        //将数据循环存到导出实体类中
        List<IncomeRecordLogExcel> excelInviteRecordLogList = new ArrayList<>();

        for (IncomeReleaseLog incomeReleaseLog:releaseLog.getData().getRecords()) {
            IncomeRecordLogExcel incomeRecordLogExcel = new IncomeRecordLogExcel();
            BeanUtils.copyProperties(incomeReleaseLog,incomeRecordLogExcel);
            excelInviteRecordLogList.add(incomeRecordLogExcel);
        }
        FileUtil.exportExcel(excelInviteRecordLogList,IncomeRecordExcel.class,"释放记录",response);
    }


}

