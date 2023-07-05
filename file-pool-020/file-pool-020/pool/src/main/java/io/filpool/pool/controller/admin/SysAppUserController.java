package io.filpool.pool.controller.admin;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.common.controller.BaseController;
import io.filpool.framework.common.exception.FILPoolException;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.validator.groups.Add;
import io.filpool.framework.core.validator.groups.Update;
import io.filpool.framework.log.annotation.Module;
import io.filpool.framework.log.annotation.OperationLog;
import io.filpool.framework.log.enums.OperationLogType;
import io.filpool.pool.entity.*;
import io.filpool.pool.excel.*;
import io.filpool.pool.mapper.PowerOrderMapper;
import io.filpool.pool.mapper.ReturnedRewardConfigMapper;
import io.filpool.pool.param.*;
import io.filpool.pool.request.AssetAccountReq;
import io.filpool.pool.request.PledgeListRequest;
import io.filpool.pool.request.SysPledgeDesc;
import io.filpool.pool.request.SysUserPageRequest;
import io.filpool.pool.service.AssetAccountService;
import io.filpool.pool.service.ReturnedRewardConfigService;
import io.filpool.pool.service.UserService;
import io.filpool.pool.service.impl.*;
import io.filpool.pool.util.AccountLogType;
import io.filpool.pool.util.BigDecimalUtil;
import io.filpool.pool.util.FileUtil;
import io.filpool.pool.vo.AssetAccountExchangeVo;
import io.filpool.pool.vo.AssetAccountVo;
import io.filpool.pool.vo.UserPowerVo;
import io.filpool.pool.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 用户表 控制器
 *
 * @author filpool
 * @since 2021-03-02
 */
@Slf4j
@RestController
@RequestMapping("sys/user")
@Module("pool")
@Api(value = "用户表API", tags = {"用户表"})
public class SysAppUserController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private AssetAccountLogServiceImpl assetAccountLogService;
    @Autowired
    private AssetAccountService assetAccountService;
    @Autowired
    private GlobalConfigServiceImpl globalConfigService;
    @Autowired
    private CurrencyServiceImpl currencyService;
    @Autowired
    private TransferRecordServiceImpl transferRecordService;
    @Autowired
    private SysUtilController sysUtilController;
    @Autowired
    private RewardConfigServiceImpl rewardConfigService;
    @Autowired
    private IncomeReleaseRecordServiceImpl incomeReleaseRecordService;
    @Autowired
    private PowerOrderMapper powerOrderMapper;
    @Autowired
    private ReturnedRewardConfigMapper returnedRewardConfigMapper;
    @Autowired
    private ReturnedRewardConfigService returnedRewardConfigService;

    /**
     * 添加或更新用户
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation("新增或者更新用户")
    public ApiResult<Boolean> addUser(@Validated(Add.class) @RequestBody User user) throws Exception {
        boolean flag;
        Integer level=999;
        if (user.getId() != null) {
            User byId = userService.getById(user.getId());
            if (byId == null) {
                return ApiResult.fail("user.not.exits");
            }
            if (user.getLevelId() != null) {
                //查询原等级
                RewardConfig rewardConfig = rewardConfigService.getBaseMapper().getConfigByPower(byId.getExperience());
                if (rewardConfig != null && !rewardConfig.getLevel().equals(user.getLevelId())) {
                    //等级不相等,重新设置经验值
                    RewardConfig newConfig = rewardConfigService.lambdaQuery().eq(RewardConfig::getLevel, user.getLevelId()).one();
                    if (newConfig == null) {
                        throw new FILPoolException("config.not-exits");
                    }
                    user.setLevelId(rewardConfig.getLevel());
                    user.setExperience(new BigDecimal(newConfig.getMinSize()));
                }
            }
                //市场等级的风控机制，根据等级设置该等级最低的经验值
            if (user.getMarketLevel() != null){
                //根据积分查询该用户原本的市场等级，如果等级没有修改则积分不变
                ReturnedRewardConfig returnedRewardConfig = returnedRewardConfigMapper.getConfigByPower(user.getCommunityExperience(),1);
                if (returnedRewardConfig==null){
                  level=0;
                }

                if (returnedRewardConfig!=null && !returnedRewardConfig.getLevel().equals(user.getMarketLevel())){
                    //等级不相等，重新设置经验值
                    ReturnedRewardConfig newMarkConfig = returnedRewardConfigService.lambdaQuery().eq(ReturnedRewardConfig::getLevel, user.getMarketLevel()).eq(ReturnedRewardConfig::getGoodsType, 1).one();
                    if (ObjectUtils.isNotEmpty(newMarkConfig)){
                        user.setCommunityExperience(new BigDecimal(newMarkConfig.getExperience()));
                    }
                }
                if (level==0 && user.getMarketLevel()!=null){
                    ReturnedRewardConfig one = returnedRewardConfigService.lambdaQuery().eq(ReturnedRewardConfig::getLevel, user.getMarketLevel()).eq(ReturnedRewardConfig::getGoodsType, 1).one();
                    user.setCommunityExperience(new BigDecimal(one.getExperience()));
                }
            }

            if (user.getServerCharge() != null && user.getServerCharge().compareTo(BigDecimal.ONE) >= 0) {
                throw new FILPoolException("技术服务费比例小于1");
            }
            if (user.getXchServerCharge() != null && user.getXchServerCharge().compareTo(BigDecimal.ONE) >= 0) {
                throw new FILPoolException("技术服务费比例小于1");
            }
            BeanUtils.copyProperties(user, byId);
            //更新用户
            flag = userService.updateUser(byId);
        } else {
            flag = userService.saveUser(user);
        }
        return ApiResult.result(flag);
    }

    /**
     * 删除用户表
     */
    @PostMapping("/delete/{id}")
    @OperationLog(name = "删除用户表", type = OperationLogType.DELETE)
    @ApiOperation(value = "删除用户表", response = ApiResult.class)
    public ApiResult<Boolean> deleteUser(@PathVariable("id") Long id) throws Exception {
        boolean flag = userService.deleteUser(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取用户表详情
     */
/*    @GetMapping("/info/{id}")
    @OperationLog(name = "用户表详情", type = OperationLogType.INFO)
    @ApiOperation(value = "用户表详情", response = User.class)
    public ApiResult<User> getUser(@PathVariable("id") Long id) throws Exception {
        User user = userService.getById(id);
        return ApiResult.ok(user);
    }*/


    /**
     * 获取用户表详情（用户列表-详情）
     */
    @GetMapping("/info/{id}")
    @OperationLog(name = "用户表详情", type = OperationLogType.INFO)
    @ApiOperation(value = "用户表详情", response = User.class)
    public ApiResult<UserVo> getUser(@PathVariable("id") Long id) throws Exception {
        //查询用户详情
        UserVo userVo = userService.queryDescById(id);
        return ApiResult.ok(userVo);
    }

    /**
     * 获取用户表资金信息（用户列表-详情-资金信息）
     */
    @PostMapping("/balanceList")
    @OperationLog(name = "用户列表-详情-资金信息", type = OperationLogType.INFO)
    @ApiOperation(value = "用户列表-详情-资金信息", response = User.class)
    public ApiResult<Paging<AssetAccount>> balanceList(@RequestBody AssetAccountReq req) throws Exception {
        //查询用户详情
        Page<AssetAccount> objectPage = new Page<>();
        List<AssetAccount> list = assetAccountService.lambdaQuery().eq(AssetAccount::getUserId, req.getUserId()).and(x -> x.eq(AssetAccount::getSymbol, "USDT").or(z -> z.eq(AssetAccount::getSymbol, "FIL")).or(z -> z.eq(AssetAccount::getSymbol, "XCH"))).list();
        for (AssetAccount assetAccount : list) {
            if (StringUtils.equals(assetAccount.getSymbol(),"FIL")) {
                assetAccount.setMinerFrozen(incomeReleaseRecordService.getBaseMapper().getFrozenIncome(req.getUserId(),assetAccount.getSymbol()));
            }else{
                assetAccount.setMinerFrozen(BigDecimal.ZERO);
            }
            BigDecimal totalAssets = assetAccount.getPledge().add(assetAccount.getAvailable()).add(assetAccount.getFrozen()).add(assetAccount.getMinerFrozen());
            assetAccount.setTotalAssets(totalAssets);
        }
        objectPage.setRecords(list).setTotal(list.size());
        return ApiResult.ok(new Paging<>(objectPage));
    }


    /**
     * 资金操作（用户列表-详情-资金操作）
     */
    @ApiOperation(value = "资金操作")
    @PostMapping(value = "/exchangeBalance")
    public ApiResult<?> exchangeBalance(@RequestBody AssetAccountExchangeVo assetAccountExchangeVo) {
        if (assetAccountExchangeVo.getType() == null || assetAccountExchangeVo.getType() == null) {
            return ApiResult.ok(null);
        }
        if (!((assetAccountExchangeVo.getType() == AccountLogType.TYPE_SYSTEM_DELETE || assetAccountExchangeVo.getType() == AccountLogType.TYPE_SYSTEM_RECHARGE) && BigDecimalUtil.greater(assetAccountExchangeVo.getOperationAmount(), BigDecimal.ZERO))) {
            return ApiResult.fail("参数有误");
        }
        if (assetAccountExchangeVo.getType() == AccountLogType.TYPE_SYSTEM_DELETE) {
            assetAccountExchangeVo.setOperationAmount(BigDecimalUtil.multiply(assetAccountExchangeVo.getOperationAmount(), -1));
            assetAccountExchangeVo.setRemark("系统扣除");
        }
        if (assetAccountExchangeVo.getType() == AccountLogType.TYPE_SYSTEM_RECHARGE) {
            assetAccountExchangeVo.setRemark("系统充值");
        }
        try {
            Boolean aBoolean = assetAccountService.exchangeAccount(assetAccountExchangeVo);
            if (aBoolean) {
                return ApiResult.ok("操作成功！");
            } else {
                return ApiResult.fail("操作失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResult.fail("操作失败");
        }
    }

    /**
     * 算力统计（用户列表-详情-算力统计）
     */
    @PostMapping("/powerStatistics")
    @OperationLog(name = "用户列表-详情-算力统计", type = OperationLogType.INFO)
    @ApiOperation(value = "用户列表-详情-算力统计", response = User.class)
    public ApiResult<Paging<UserPowerVo>> powerStatistics(@RequestBody AssetAccountReq req){
        List<UserPowerVo> userPowerVos = powerOrderMapper.sumEffectAmountTwo(req.getUserId());
        Page<UserPowerVo> objectPage = new Page<>();
        objectPage.setRecords(userPowerVos);
        objectPage.setTotal(userPowerVos.size());
        return ApiResult.ok(new Paging(objectPage));
    }

//    /**
//     * 算力操作（用户列表-详情-算力操作）
//     */
//    @PostMapping(value = "/exchangeWeight")
//    @ApiOperation(value = "算力操作")
//    public ApiResult<?> exchangeWeight(@RequestBody PowerAccountVo powerAccountVo) {
//        if (!((powerAccountVo.getType()==AccountLogType.TYPE_SYSTEM_DELETE || powerAccountVo.getType()==AccountLogType.TYPE_SYSTEM_RECHARGE) && BigDecimalUtil.greater(powerAccountVo.getAmount(),BigDecimal.ZERO))) {
//            return ApiResult.fail("参数有误");
//        }
//
//        if (powerAccountVo.getType() == AccountLogType.TYPE_SYSTEM_DELETE) {
//            powerAccountVo.setAmount(BigDecimalUtil.multiply(powerAccountVo.getAmount(),-1));
//            powerAccountVo.setRemark("系统扣除");
//        }
//        if (powerAccountVo.getType() == AccountLogType.TYPE_SYSTEM_RECHARGE) {
//            powerAccountVo.setRemark("系统充值");
//        }
//        try {
//            Boolean aBoolean = powerAccountLogService.exchangePowerAccount(powerAccountVo);
//            if (aBoolean){
//                return ApiResult.ok("操作成功！");
//            }else{
//                return ApiResult.fail("操作失败");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ApiResult.fail("操作失败");
//        }
//    }


    /**
     * 用户表分页列表（用户列表）
     */
    @PostMapping("/getPageList")
    @OperationLog(name = "用户表分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "用户表分页列表", response = User.class)
    public ApiResult<Paging<UserVo>> getUserPageList(@RequestBody SysUserPageRequest sysUserPageRequest) throws Exception {
        Paging<UserVo> userPageList = userService.getUserPageList(sysUserPageRequest);
        return ApiResult.ok(userPageList);
    }


    /**
     * 用户表分页列表（用户列表-导出）
     */
    @PostMapping("/getUserListExport")
    @OperationLog(name = "用户列表-导出", type = OperationLogType.PAGE)
    @ApiOperation(value = "用户列表-导出", response = User.class)
    public void getUserListExport(@RequestBody SysUserPageRequest sysUserPageRequest, HttpServletResponse response) throws Exception {
        Paging<UserVo> userPageList = userService.getUserPageList(sysUserPageRequest);
        //将数据循环存到导出实体类中
        List<UserExcel> excelUser = new ArrayList<>();

        for (UserVo userVo : userPageList.getRecords()) {
            String account = sysUtilController.queryAccount(userVo.getId());
            //查询上级邀请人
            String inviteUser = sysUtilController.queryInviteUser(Integer.parseInt(userVo.getId().toString()));

            UserExcel userExcel = new UserExcel();
            BeanUtils.copyProperties(userVo, userExcel);
            userExcel.setAccount(account);
            userExcel.setInviterAccount(inviteUser);
            excelUser.add(userExcel);
        }
        FileUtil.exportExcel(excelUser, UserExcel.class, "用户列表导出", response);
    }


    /**
     * 质押列表pledgeListRequest（质押列表）
     */
    @PostMapping("/getPledgePageList")
    @OperationLog(name = "质押列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "质押列表", response = AssetAccount.class)
    public ApiResult<Paging<PledgeListRequest>> getPledgePageList(@RequestBody SysUserPageRequest sysUserPageRequest) throws Exception {
        Paging<PledgeListRequest> pledgePageList = userService.getPledgePageList(sysUserPageRequest);
        return ApiResult.ok(pledgePageList);
    }


    /**
     * 导出（质押列表-导出）
     */
    @PostMapping("/getPledgePageListExport")
    @OperationLog(name = "质押列表-导出", type = OperationLogType.PAGE)
    @ApiOperation(value = "质押列表-导出", response = User.class)
    public void getPledgePageListExport(@RequestBody SysUserPageRequest sysUserPageRequest, HttpServletResponse response) throws Exception {
        Paging<PledgeListRequest> pledgePageList = userService.getPledgePageList(sysUserPageRequest);
        //将数据循环存到导出实体类中
        List<PledgeExcel> excelPledgeList = new ArrayList<>();

        for (PledgeListRequest pledgeListRequest : pledgePageList.getRecords()) {
            PledgeExcel pledgeExcel = new PledgeExcel();
            BeanUtils.copyProperties(pledgeListRequest, pledgeExcel);
            excelPledgeList.add(pledgeExcel);
        }
        FileUtil.exportExcel(excelPledgeList, PledgeExcel.class, "质押列表导出", response);
    }


    /**
     * 质押列表详情（质押列表-质押记录）
     */
    @PostMapping("/getPledgePageListByIdDesc")
    @OperationLog(name = "质押列表详情", type = OperationLogType.PAGE)
    public ApiResult<Paging<AssetAccountLog>> getPledgePageListByIdDesc(@RequestBody SysPledgeDesc sysPledgeDesc) throws Exception {
        Paging<AssetAccountLog> pledgePageList = userService.getPledgePageListByIdDesc(sysPledgeDesc);
        return ApiResult.ok(pledgePageList);
    }


    /**
     * 导出（质押记录-导出）
     */
    @PostMapping("/getPledgePageListByIdDescExport")
    @OperationLog(name = "质押详情-导出", type = OperationLogType.PAGE)
    public void getPledgePageListByIdDescExport(@RequestBody SysPledgeDesc sysPledgeDesc, HttpServletResponse response) throws Exception {
        Paging<AssetAccountLog> pledgePageList = userService.getPledgePageListByIdDesc(sysPledgeDesc);
        List<PledgeDescExcel> excelPledgeDescList = new ArrayList<>();

        //将数据循环存到导出实体类中
        for (AssetAccountLog log : pledgePageList.getRecords()) {
            PledgeDescExcel pledgeDescExcel = new PledgeDescExcel();
            BeanUtils.copyProperties(log, pledgeDescExcel);
            pledgeDescExcel.setTypeShow(log.getType() == 5 ? "GAS费" : "质押");
            excelPledgeDescList.add(pledgeDescExcel);
        }
        FileUtil.exportExcel(excelPledgeDescList, PledgeDescExcel.class, "质押詳情导出", response);
    }


    /**
     * 质押要求设置
     */
    @PostMapping("/setPledge")
    @OperationLog(name = "质押要求设置", type = OperationLogType.UPDATE)
    @ApiOperation(value = "质押要求设置", response = ApiResult.class)
    public ApiResult<Boolean> setPledge(@Validated(Update.class) @RequestBody GlobalConfig globalConfig) throws Exception {
        Boolean update = false;
        if ("pledge_require".equals(globalConfig.getConfigKey()) && globalConfig.getConfigValue() != null) {
            update = globalConfigService.lambdaUpdate().set(GlobalConfig::getConfigValue, globalConfig.getConfigValue()).eq(GlobalConfig::getConfigKey, globalConfig.getConfigKey()).update();
        }
        return ApiResult.result(update);
    }


    /**
     * 划转fil_transfer_record
     * 划转类型: 1可用转质押 2质押转可用
     */
    @PostMapping("/transfer")
    @OperationLog(name = "划转", type = OperationLogType.UPDATE)
    @ApiOperation(value = "划转", response = ApiResult.class)
    public ApiResult<Boolean> transfer(@Validated(Update.class) @RequestBody TransferRecordPageParam transferRecordPageParam) throws Exception {
        Boolean update = false;
        if (transferRecordPageParam.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new FILPoolException("transfer.amount.err");
        }
        //查询该用户的钱包信息
        AssetAccount one = assetAccountService.lambdaQuery().eq(AssetAccount::getUserId, transferRecordPageParam.getUserId()).eq(AssetAccount::getSymbol, "FIL").one();

        if (ObjectUtils.isNotEmpty(one)) {
            //1可用转质押 先对划转的金额进行修改 available可用   pledge质押
            if (transferRecordPageParam.getType() == 1) {
                //判断该用户当前可用金额是否满足划转
                if (one.getAvailable().compareTo(transferRecordPageParam.getAmount()) > 0) {
                    //增加质押
                    BigDecimal addPledge = BigDecimalUtil.add(one.getPledge(), transferRecordPageParam.getAmount());
                    //减少可用
                    BigDecimal subAvailable = BigDecimalUtil.sub(one.getAvailable(), transferRecordPageParam.getAccount());
                    update = assetAccountService.lambdaUpdate().set(AssetAccount::getAvailable, subAvailable).set(AssetAccount::getPledge, addPledge).eq(AssetAccount::getUserId, transferRecordPageParam.getUserId()).eq(AssetAccount::getSymbol, "FIL").update();
                }
            } else {
                //2质押转可用 先对划转的金额进行修改 available可用   pledge质押
                if (one.getPledge().compareTo(transferRecordPageParam.getAmount()) > 0) {
                    //增加可用
                    BigDecimal addPledge = BigDecimalUtil.add(one.getAvailable(), transferRecordPageParam.getAmount());
                    //减少质押
                    BigDecimal subAvailable = BigDecimalUtil.sub(one.getPledge(), transferRecordPageParam.getAccount());
                    update = assetAccountService.lambdaUpdate().set(AssetAccount::getAvailable, subAvailable).set(AssetAccount::getPledge, addPledge).eq(AssetAccount::getUserId, transferRecordPageParam.getUserId()).eq(AssetAccount::getSymbol, "FIL").update();
                }
            }
            //再增加一条划转的记录
            if (update) {
                TransferRecord transferRecord = new TransferRecord();
                transferRecord.setUserId(transferRecordPageParam.getUserId());
                transferRecord.setCreateTime(new Date());
                transferRecord.setOperationType(1);
                transferRecord.setType(transferRecordPageParam.getType());
                transferRecord.setAmount(transferRecordPageParam.getAmount());
                Currency fil = currencyService.lambdaQuery().eq(Currency::getSymbol, "FIL").one();
                if (ObjectUtils.isNotEmpty(fil)) {
                    transferRecord.setCurrencyId(fil.getId());
                    transferRecord.setSymbol(fil.getSymbol());
                }
                transferRecordService.save(transferRecord);
                assetAccountLogService.saveLog(one, transferRecordPageParam.getAmount(), AccountLogType.TYPE_SYSTEM_TRANSFER, "系统划转", transferRecord.getId());
            }
        }
        return ApiResult.result(update);
    }


    /**
     * 平台扣除充值记录fil_asset_account_log（平台扣除/平台充值）
     */
    @PostMapping("/platformOperation")
    @OperationLog(name = "平台扣除充值记录", type = OperationLogType.PAGE)
    @ApiOperation(value = "平台扣除充值记录", response = AssetAccount.class)
    public ApiResult<Paging<AssetAccountLog>> platformOperationList(@RequestBody AssetAccountLogPageParam assetAccountLogPageParam) throws Exception {
        Paging<AssetAccountLog> pledgePageList = userService.platformOperationList(assetAccountLogPageParam);
        return ApiResult.ok(pledgePageList);
    }


    /**
     * 导出（平台扣除/平台充值-导出）
     */
    @PostMapping("/platformOperationListExport")
    @OperationLog(name = "平台操作-导出", type = OperationLogType.PAGE)
    @ApiOperation(value = "平台操作-导出", response = User.class)
    public void platformOperationListExport(@RequestBody AssetAccountLogPageParam assetAccountLogPageParam, HttpServletResponse response) throws Exception {
        Paging<AssetAccountLog> pledgePageList = userService.platformOperationList(assetAccountLogPageParam);
        //将数据循环存到导出实体类中
        List<PlatformOperationExcel> excelPlatformOperationList = new ArrayList<>();

        for (AssetAccountLog assetAccountLog : pledgePageList.getRecords()) {
            PlatformOperationExcel platformOperationExcel = new PlatformOperationExcel();
            BeanUtils.copyProperties(assetAccountLog, platformOperationExcel);
            excelPlatformOperationList.add(platformOperationExcel);
        }
        FileUtil.exportExcel(excelPlatformOperationList, PlatformOperationExcel.class, "平台操作", response);
    }


    /**
     * (禁用账号/禁用提币/重置谷歌验证)
     *
     * @param
     * @param req
     * @return
     */
    @PostMapping(value = "/updateUserDesc")
    @OperationLog(name = "禁用账号/禁用提币/重置谷歌验证", type = OperationLogType.PAGE)
    @ApiOperation(value = "禁用账号/禁用提币/重置谷歌验证", response = User.class)
    public ApiResult<?> updateUserDesc(
            @RequestBody UserEditParam userEditParam,
            HttpServletRequest req) throws Exception {
        User user = new User();
        BeanUtils.copyProperties(userEditParam, user);
        if (userEditParam.getGoogleSecretKey() != null) {
            user.setGoogleSecretKey("");
        }

        boolean b = userService.updateById(user);
        if (b) {
            return ApiResult.ok("修改成功");
        } else {
            return ApiResult.fail("修改失败");
        }
    }


    /**
     * (技术服务费设置)
     *
     * @param
     * @param
     * @return
     */
    @PostMapping(value = "/getServiceFee")
    @OperationLog(name = "技术服务费查询", type = OperationLogType.PAGE)
    @ApiOperation(value = "技术服务费查询", response = User.class)
    public ApiResult<?> getServiceFee() throws Exception {
        GlobalConfig one = globalConfigService.lambdaQuery().eq(GlobalConfig::getId, 2).one();
        return ApiResult.ok(one);
    }


    /**
     * (技术服务费设置)
     *
     * @param
     * @param req
     * @return
     */
    @PostMapping(value = "/serviceFee")
    @OperationLog(name = "技术服务费设置", type = OperationLogType.PAGE)
    @ApiOperation(value = "技术服务费设置", response = User.class)
    public ApiResult<?> updateServiceFee(
            @RequestBody GlobalConfig globalConfig,
            HttpServletRequest req) throws Exception {
        globalConfig.setId(2L);
        boolean b = globalConfigService.updateById(globalConfig);
        if (b) {
            return ApiResult.ok("修改成功");
        } else {
            return ApiResult.fail("修改失败");
        }
    }


    /**
     * 用户基础等级配置
     */
    @PostMapping("/userBasicLevelList")
    @OperationLog(name = "用户基础等级配置", type = OperationLogType.PAGE)
    @ApiOperation(value = "用户基础等级配置", response = AssetAccount.class)
    public ApiResult<Paging<RewardConfig>> userBasicLevelList(@RequestBody RewardConfigPageParam rewardConfigPageParam) throws Exception {
        Paging<RewardConfig> rewardConfigPaging = userService.userBasicLevelList(rewardConfigPageParam);
        return ApiResult.ok(rewardConfigPaging);
    }


    /**
     * 直/间推奖励配置
     */
    @PostMapping("/rewardAllocationList")
    @OperationLog(name = "直/间推奖励配置", type = OperationLogType.PAGE)
    @ApiOperation(value = "直/间推奖励配置", response = AssetAccount.class)
    public ApiResult<Paging<RewardConfig>> rewardAllocationList(@RequestBody RewardConfigPageParam rewardConfigPageParam) throws Exception {

        Paging<RewardConfig> rewardConfigPaging = userService.rewardAllocationList(rewardConfigPageParam);
        return ApiResult.ok(rewardConfigPaging);
    }

    /**
     * 直/间推奖励配置-编辑
     */
    @PostMapping("/rewardAllocationListEdit")
    @OperationLog(name = "直/间推奖励配置-编辑", type = OperationLogType.PAGE)
    @ApiOperation(value = "直/间推奖励配置-编辑", response = AssetAccount.class)
    public ApiResult<Boolean> rewardAllocationListEdit(@RequestBody RewardConfigPageParam rewardConfigPageParam) throws Exception {
        Boolean aBoolean = userService.rewardAllocationListEdit(rewardConfigPageParam);
        return ApiResult.ok(aBoolean);
    }

}

