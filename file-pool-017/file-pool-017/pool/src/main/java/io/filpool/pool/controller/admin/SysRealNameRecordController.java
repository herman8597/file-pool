package io.filpool.pool.controller.admin;

import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.common.controller.BaseController;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.validator.groups.Add;
import io.filpool.framework.core.validator.groups.Update;
import io.filpool.framework.log.annotation.Module;
import io.filpool.framework.log.annotation.OperationLog;
import io.filpool.framework.log.enums.OperationLogType;
import io.filpool.framework.shiro.util.JwtTokenUtil;
import io.filpool.framework.shiro.util.JwtUtil;
import io.filpool.pool.entity.RealNameRecord;
import io.filpool.pool.entity.User;
import io.filpool.pool.excel.RealNameExcel;
import io.filpool.pool.excel.UserExcel;
import io.filpool.pool.param.RealNameRecordPageParam;
import io.filpool.pool.request.SysUserPageRequest;
import io.filpool.pool.service.RealNameRecordService;
import io.filpool.pool.service.UserService;
import io.filpool.pool.util.FileUtil;
import io.filpool.pool.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 实名认证信息 控制器
 *
 * @author filpool
 * @since 2021-03-02
 */
@Slf4j
@RestController
@RequestMapping("sys/realNameRecord")
@Module("pool")
@Api(value = "实名认证信息API", tags = {"实名认证信息"})
public class SysRealNameRecordController extends BaseController {

    @Autowired
    private RealNameRecordService realNameRecordService;
    @Autowired
    private UserService userService;

    @Autowired
    private SysUtilController sysUtilController;

    /**
     * 添加实名认证信息
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/add")
    @OperationLog(name = "添加实名认证信息", type = OperationLogType.ADD)
    @ApiOperation(value = "添加实名认证信息", response = ApiResult.class)
    public ApiResult<Boolean> addRealNameRecord(@Validated(Add.class) @RequestBody RealNameRecord realNameRecord) throws Exception {
        boolean flag = realNameRecordService.saveRealNameRecord(realNameRecord);
        return ApiResult.result(flag);
    }

    /**
     * 修改实名认证信息
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/update")
    @OperationLog(name = "修改实名认证信息", type = OperationLogType.UPDATE)
    @ApiOperation(value = "修改实名认证信息", response = ApiResult.class)
    public ApiResult<Boolean> updateRealNameRecord(@Validated(Update.class) @RequestBody RealNameRecord realNameRecord) throws Exception {
        boolean flag = realNameRecordService.updateRealNameRecord(realNameRecord);
        return ApiResult.result(flag);
    }

    /**
     * 删除实名认证信息
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/delete/{id}")
    @OperationLog(name = "删除实名认证信息", type = OperationLogType.DELETE)
    @ApiOperation(value = "删除实名认证信息", response = ApiResult.class)
    public ApiResult<Boolean> deleteRealNameRecord(@PathVariable("id") Long id) throws Exception {
        boolean flag = realNameRecordService.deleteRealNameRecord(id);
        return ApiResult.result(flag);
    }

    /**
     * 实名认证信息分页列表（实名认证）
     */
    @PostMapping("/getPageList")
    @OperationLog(name = "实名认证信息分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "实名认证信息分页列表", response = RealNameRecord.class)
    public ApiResult<Paging<RealNameRecord>> getRealNameRecordPageList(@Validated @RequestBody RealNameRecordPageParam realNameRecordPageParam) throws Exception {
        Paging<RealNameRecord> paging = realNameRecordService.getRealNameRecordPageList(realNameRecordPageParam);
        return ApiResult.ok(paging);
    }

    /**
     * 实名认证信息审核（实名认证-审核）
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/approval")
    @OperationLog(name = "实名认证信息审核", type = OperationLogType.UPDATE)
    @ApiOperation(value = "实名认证信息审核", response = ApiResult.class)
    public ApiResult<Boolean> approvalNameRecord(@Validated(Update.class) @RequestBody RealNameRecord real) throws Exception {
        RealNameRecord byId = realNameRecordService.getById(real.getId());
        if (byId == null) {
            return ApiResult.fail("记录不存在");
        }
        if (byId.getAuthStatus() != 1) {
            return ApiResult.fail("该记录已审核");
        }
        if (real.getAuthStatus() == null || real.getAuthStatus() <= 1 || real.getAuthStatus() > 3) {
            return ApiResult.fail("审核参数错误");
        }
        //判断是否重复审核
        if (real.getAuthStatus() == 3) {
            Integer count = realNameRecordService.lambdaQuery().eq(RealNameRecord::getUserId, byId.getUserId()).eq(RealNameRecord::getAuthStatus, 3).count();
            if (count >= 1) {
                return ApiResult.fail("该用户已实名认证");
            }
        }
        String username = JwtUtil.getUsername(JwtTokenUtil.getToken());
        boolean update = realNameRecordService.lambdaUpdate().eq(RealNameRecord::getId, byId.getId())
                .set(RealNameRecord::getAuthStatus, real.getAuthStatus())
                .set(RealNameRecord::getAuthTime, new Date())
                .set(RealNameRecord::getAuthUserName, username)
                .set(RealNameRecord::getRemark, real.getRemark())
                .update();
        //更新状态
        userService.lambdaUpdate().eq(User::getId, byId.getUserId()).set(User::getRealNameStatus, real.getAuthStatus()).update();
        return ApiResult.ok(update);
    }

    /**
     * 获取实名认证信息详情（实名认证-详情）
     */
    @GetMapping("/info/{id}")
    @OperationLog(name = "实名认证信息详情", type = OperationLogType.INFO)
    @ApiOperation(value = "实名认证信息详情", response = RealNameRecord.class)
    public ApiResult<RealNameRecord> getRealNameRecord(@PathVariable("id") Long id) throws Exception {
        RealNameRecord realNameRecord = realNameRecordService.getById(id);
        return ApiResult.ok(realNameRecord);
    }


    /**
     * 导出（实名认证-导出）
     */
    @PostMapping("/getRealNameRecordListExport")
    @OperationLog(name = "实名认证-导出", type = OperationLogType.PAGE)
    @ApiOperation(value = "实名认证-导出", response = User.class)
    public void getRealNameRecordListExport(@Validated @RequestBody RealNameRecordPageParam realNameRecordPageParam,HttpServletResponse response) throws Exception {
        Paging<RealNameRecord> paging = realNameRecordService.getRealNameRecordPageList(realNameRecordPageParam);
        //将数据循环存到导出实体类中
        List<RealNameExcel> excelRealNameList = new ArrayList<>();

        for (RealNameRecord realNameRecord:paging.getRecords()) {
            //查询用户账号
            String account = sysUtilController.queryAccount(realNameRecord.getId());

            RealNameExcel realNameExcel = new RealNameExcel();
            BeanUtils.copyProperties(realNameRecord,realNameExcel);
            realNameExcel.setAccount(account);
            excelRealNameList.add(realNameExcel);
        }
        FileUtil.exportExcel(excelRealNameList,RealNameExcel.class,"实名认证导出",response);
    }

}

