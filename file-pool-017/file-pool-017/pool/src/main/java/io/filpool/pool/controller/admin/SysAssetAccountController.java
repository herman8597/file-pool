package io.filpool.pool.controller.admin;

import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.common.controller.BaseController;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.validator.groups.Add;
import io.filpool.framework.core.validator.groups.Update;
import io.filpool.framework.log.annotation.Module;
import io.filpool.framework.log.annotation.OperationLog;
import io.filpool.framework.log.enums.OperationLogType;
import io.filpool.pool.entity.AssetAccount;
import io.filpool.pool.entity.RechargeRecord;
import io.filpool.pool.entity.User;
import io.filpool.pool.excel.RechargeExcel;
import io.filpool.pool.excel.UserExcel;
import io.filpool.pool.param.AssetAccountPageParam;
import io.filpool.pool.request.RechargeRecordRequest;
import io.filpool.pool.request.SysUserPageRequest;
import io.filpool.pool.service.AssetAccountService;
import io.filpool.pool.service.RechargeRecordService;
import io.filpool.pool.service.TransferRecordService;
import io.filpool.pool.util.FileUtil;
import io.filpool.pool.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 账户资产表 控制器
 *
 * @author filpool
 * @since 2021-03-08
 */
@Slf4j
@RestController
@RequestMapping("sys/assetAccount")
@Module("pool")
@Api(value = "账户资产表API", tags = {"账户资产表"})
public class SysAssetAccountController extends BaseController {

    @Autowired
    private AssetAccountService assetAccountService;

    @Autowired
    private TransferRecordService transferRecordService;

    @Autowired
    private RechargeRecordService rechargeRecordService;

    /**
     * 添加账户资产表
     */
    @PostMapping("/add")
    @OperationLog(name = "添加账户资产表", type = OperationLogType.ADD)
    @ApiOperation(value = "添加账户资产表", response = ApiResult.class)
    public ApiResult<Boolean> addAssetAccount(@Validated(Add.class) @RequestBody AssetAccount assetAccount) throws Exception {
        boolean flag = assetAccountService.saveAssetAccount(assetAccount);
        return ApiResult.result(flag);
    }

    /**
     * 修改账户资产表
     */
    @PostMapping("/update")
    @OperationLog(name = "修改账户资产表", type = OperationLogType.UPDATE)
    @ApiOperation(value = "修改账户资产表", response = ApiResult.class)
    public ApiResult<Boolean> updateAssetAccount(@Validated(Update.class) @RequestBody AssetAccount assetAccount) throws Exception {
        boolean flag = assetAccountService.updateAssetAccount(assetAccount);
        return ApiResult.result(flag);
    }

    /**
     * 删除账户资产表
     */
    @PostMapping("/delete/{id}")
    @OperationLog(name = "删除账户资产表", type = OperationLogType.DELETE)
    @ApiOperation(value = "删除账户资产表", response = ApiResult.class)
    public ApiResult<Boolean> deleteAssetAccount(@PathVariable("id") Long id) throws Exception {
        boolean flag = assetAccountService.deleteAssetAccount(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取账户资产表详情
     */
    @GetMapping("/info/{id}")
    @OperationLog(name = "账户资产表详情", type = OperationLogType.INFO)
    @ApiOperation(value = "账户资产表详情", response = AssetAccount.class)
    public ApiResult<AssetAccount> getAssetAccount(@PathVariable("id") Long id) throws Exception {
        AssetAccount assetAccount = assetAccountService.getById(id);
        return ApiResult.ok(assetAccount);
    }

    /**
     * 账户资产表分页列表
     */
    @PostMapping("/getPageList")
    @OperationLog(name = "账户资产表分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "账户资产表分页列表", response = AssetAccount.class)
    public ApiResult<Paging<AssetAccount>> getAssetAccountPageList(@Validated @RequestBody AssetAccountPageParam assetAccountPageParam) throws Exception {
        Paging<AssetAccount> paging = assetAccountService.getAssetAccountPageList(assetAccountPageParam);
        return ApiResult.ok(paging);
    }


    /**
     * 质押列表fil_transfer_record
     */
/*    @PostMapping("/getPledgePageList")
    @OperationLog(name = "质押列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "质押列表", response = AssetAccount.class)
    public ApiResult<Paging<TransferRecord>> getPledgePageList(@Validated @RequestBody TransferRecordPageParam transferRecordPageParam) throws Exception {
        Paging<TransferRecord> paging = transferRecordService.getAssetAccountPageList(transferRecordPageParam);
        return ApiResult.ok(paging);
    }*/


    /**
     *
     * 充币订单（充币订单）
     */
    @PostMapping("/getChargeMoneyPageList")
    @OperationLog(name = "充币订单", type = OperationLogType.PAGE)
    @ApiOperation(value = "充币订单", response = RechargeRecord.class)
    public Paging<RechargeRecord> getChargeMoneyPageList(@Validated @RequestBody RechargeRecordRequest rechargeRecordRequest){
        Paging<RechargeRecord> paging = rechargeRecordService.getChargeMoneyPageList(rechargeRecordRequest);
        return paging;
    }


    /**
     * 充币订单（充币订单-导出）
     */
    @PostMapping("/getChargeMoneyExport")
    @OperationLog(name = "充币订单-导出", type = OperationLogType.PAGE)
    @ApiOperation(value = "充币订单-导出", response = User.class)
    public void getChargeMoneyExport(@Validated @RequestBody RechargeRecordRequest rechargeRecordRequest,HttpServletResponse response) throws Exception {
        Paging<RechargeRecord> paging = rechargeRecordService.getChargeMoneyPageList(rechargeRecordRequest);
        //将数据循环存到导出实体类中
        List<RechargeExcel> rechargeExcelList = new ArrayList<>();

        for (RechargeRecord rechargeRecord:paging.getRecords()) {
            RechargeExcel rechargeExcel = new RechargeExcel();
            BeanUtils.copyProperties(rechargeRecord,rechargeExcel);
            rechargeExcelList.add(rechargeExcel);
        }
        FileUtil.exportExcel(rechargeExcelList,RechargeRecord.class,"充币订单导出",response);
    }



}

