package io.filpool.pool.controller.admin;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.common.controller.BaseController;
import io.filpool.framework.common.exception.FILPoolException;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.validator.groups.Add;
import io.filpool.framework.core.validator.groups.Update;
import io.filpool.framework.log.annotation.Module;
import io.filpool.framework.log.annotation.OperationLog;
import io.filpool.framework.log.enums.OperationLogType;
import io.filpool.pool.entity.Currency;
import io.filpool.pool.entity.Supplement;
import io.filpool.pool.entity.SupplementDeduct;
import io.filpool.pool.excel.SupplementDeductExcel;
import io.filpool.pool.excel.SupplementExcel;
import io.filpool.pool.excel.UserExcel;
import io.filpool.pool.param.SupplementDeductPageParam;
import io.filpool.pool.param.SupplementPageParam;
import io.filpool.pool.request.SysDeductSupRequest;
import io.filpool.pool.request.SysSupplementAuthRequest;
import io.filpool.pool.service.CurrencyService;
import io.filpool.pool.service.SupplementService;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 算力补单 控制器
 *
 * @author filpool
 * @since 2021-03-29
 */
@Slf4j
@RestController
@RequestMapping("/sys/supplement")
@Module("pool")
@Api(value = "算力补单API", tags = {"算力补单"})
public class SysSupplementController extends BaseController {

    @Autowired
    private SupplementService supplementService;

    @Autowired
    private SysUtilController sysUtilController;
    @Autowired
    private CurrencyService currencyService;


//    /**
//     * 修改算力补单
//     */
//    @PostMapping("/update")
//    @OperationLog(name = "修改算力补单", type = OperationLogType.UPDATE)
//    @ApiOperation(value = "修改算力补单", response = ApiResult.class)
//    public ApiResult<Boolean> updateSupplement(@Validated(Update.class) @RequestBody Supplement supplement) throws Exception {
//        boolean flag = supplementService.updateSupplement(supplement);
//        return ApiResult.result(flag);
//    }

//    /**
//     * 删除算力补单
//     */
//    @PostMapping("/delete/{id}")
//    @OperationLog(name = "删除算力补单", type = OperationLogType.DELETE)
//    @ApiOperation(value = "删除算力补单", response = ApiResult.class)
//    public ApiResult<Boolean> deleteSupplement(@PathVariable("id") Long id) throws Exception {
//        boolean flag = supplementService.deleteSupplement(id);
//        return ApiResult.result(flag);
//    }

    /**
     * 算力补单分页列表（算力补单）
     */
    @PostMapping("/getPageList")
    @OperationLog(name = "算力补单分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "算力补单分页列表", response = Supplement.class)
    public ApiResult<Paging<Supplement>> getSupplementPageList(@Validated @RequestBody SupplementPageParam supplementPageParam) throws Exception {
        Paging<Supplement> paging = supplementService.getSupplementPageList(supplementPageParam);
        return ApiResult.ok(paging);
    }


    /**
     * 算力补单分页列表（算力补单-导出）
     */
    @PostMapping("/supplementExcel")
    @OperationLog(name = "算力补单-导出", type = OperationLogType.PAGE)
    @ApiOperation(value = "算力补单-导出", response = Supplement.class)
    public void supplementExcel(@Validated @RequestBody SupplementPageParam supplementPageParam, HttpServletResponse response) throws Exception {
        Paging<Supplement> paging = supplementService.getSupplementPageList(supplementPageParam);

        //将数据循环存到导出实体类中
        List<SupplementExcel> excelSupplementList = new ArrayList<>();

        for (Supplement supplement : paging.getRecords()) {
            SupplementExcel supplementExcel = new SupplementExcel();
            BeanUtils.copyProperties(supplement, supplementExcel);
            excelSupplementList.add(supplementExcel);
        }
        FileUtil.exportExcel(excelSupplementList, SupplementExcel.class, "算力补单导出", response);

    }


    /**
     * 添加算力补单（算力补单-新增）
     */
    @PostMapping("/add")
    @OperationLog(name = "添加算力补单", type = OperationLogType.ADD)
    @ApiOperation(value = "添加算力补单", response = ApiResult.class)
    public ApiResult<Boolean> addSupplement(@Validated(Add.class) @RequestBody Supplement supplement) throws Exception {
        if (supplement.getAssetName() != null) {
            Long aLong = sysUtilController.querySymbol(supplement.getAssetName());
            supplement.setAssetId(aLong);
        }
        if (supplement.getAccount() != null) {
            Long aLong = sysUtilController.queryUserId(supplement.getAccount());
            supplement.setUId(aLong);
        }
        if (supplement.getPowerSymbol() == null) {
            return ApiResult.fail("请选择算力币种");
        }
        if (supplement.getPowerCurrencyId() == null) {
            Currency currency = currencyService.lambdaQuery().eq(Currency::getSymbol, supplement.getPowerSymbol())
                    .eq(Currency::getMiningStatus, true).one();
            supplement.setPowerCurrencyId(currency.getId());
        }
        if (supplement.getPledgePrice() == null) {
            supplement.setPledgePrice(BigDecimal.ZERO);
        }
        if (supplement.getGasPrice() == null) {
            supplement.setGasPrice(BigDecimal.ZERO);
        }
        boolean flag = supplementService.saveSupplement(supplement);
        return ApiResult.result(flag);



    }

    /**
     * 算力补单审核（算力补单-审核）
     */
    @PostMapping("/audit")
    @OperationLog(name = "算力补单审核", type = OperationLogType.UPDATE)
    @ApiOperation(value = "算力补单审核", response = ApiResult.class)
    public ApiResult<Boolean> audit(@RequestBody Supplement request) throws Exception {
        boolean flag = supplementService.audit(request);
        return ApiResult.result(flag);
    }


    /**
     * 获取算力补单详情（算力补单-详情）
     */
    @GetMapping("/info/{id}")
    @OperationLog(name = "算力补单详情", type = OperationLogType.INFO)
    @ApiOperation(value = "算力补单详情", response = Supplement.class)
    public ApiResult<Supplement> getSupplement(@PathVariable("id") Long id) throws Exception {
        Supplement supplement = supplementService.getById(id);
        if (ObjectUtils.isNotEmpty(supplement)) {
            String account = sysUtilController.queryAccount(Long.parseLong(supplement.getUId().toString()));
            supplement.setAccount(account);
            String asset = sysUtilController.querySymbol(Integer.parseInt(supplement.getAssetId().toString()));
            supplement.setAssetName(asset);
        }
        return ApiResult.ok(supplement);
    }


    /**
     * 算力扣除补单列表（算力扣除补单）
     */
    @PostMapping("/queryPageList")
    @OperationLog(name = "算力扣除补单列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "算力扣除补单列表", response = Supplement.class)
    public ApiResult<Paging<SupplementDeduct>> getSupplementPageList(@Validated @RequestBody SupplementDeductPageParam supplementDeductPageParam) throws Exception {
        Paging<SupplementDeduct> paging = supplementService.getSupplementDeductPageList(supplementDeductPageParam);
        return ApiResult.ok(paging);
    }


    /**
     * 算力扣除补单列表（算力扣除补单-导出）
     */
    @PostMapping("/supplementDeductExcel")
    @OperationLog(name = "算力扣除补单-导出", type = OperationLogType.PAGE)
    @ApiOperation(value = "算力扣除补单-导出", response = Supplement.class)
    public void supplementDeductExcel(@Validated @RequestBody SupplementDeductPageParam supplementDeductPageParam, HttpServletResponse response) throws Exception {
        Paging<SupplementDeduct> paging = supplementService.getSupplementDeductPageList(supplementDeductPageParam);

        //将数据循环存到导出实体类中
        List<SupplementDeductExcel> excelSupplementList = new ArrayList<>();

        for (SupplementDeduct supplementDeduct : paging.getRecords()) {
            SupplementDeductExcel supplementDeductExcel = new SupplementDeductExcel();
            BeanUtils.copyProperties(supplementDeduct, supplementDeductExcel);
            excelSupplementList.add(supplementDeductExcel);
        }
        FileUtil.exportExcel(excelSupplementList, SupplementDeductExcel.class, "算力补单扣除导出", response);

    }


    /**
     * 算力补单扣除（算力扣除补单-新增）
     */
    @PostMapping("/deduct")
    @OperationLog(name = "算力补单扣除", type = OperationLogType.INFO)
    @ApiOperation("算力补单扣除")
    public ApiResult<Boolean> deductSupplement(@RequestBody SysDeductSupRequest request) throws Exception {
        return ApiResult.result(supplementService.deduct(request));
    }
}

