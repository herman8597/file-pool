package io.filpool.pool.controller.admin;

import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.common.controller.BaseController;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.validator.groups.Add;
import io.filpool.framework.core.validator.groups.Update;
import io.filpool.framework.log.annotation.Module;
import io.filpool.framework.log.annotation.OperationLog;
import io.filpool.framework.log.enums.OperationLogType;
import io.filpool.pool.entity.InviteRecord;
import io.filpool.pool.excel.InviteExcel;
import io.filpool.pool.excel.InviteRecordDescExcel;
import io.filpool.pool.excel.UserExcel;
import io.filpool.pool.param.InviteRecordPageParam;
import io.filpool.pool.request.InviteRecordRequest;
import io.filpool.pool.service.InviteRecordService;
import io.filpool.pool.util.FileUtil;
import io.filpool.pool.vo.RewardDescVo;
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
 * 邀请关系表 控制器
 *
 * @author filpool
 * @since 2021-03-02
 */
@Slf4j
@RestController
@RequestMapping("/sys/inviteRecord")
@Module("pool")
@Api(value = "邀请关系表API", tags = {"邀请关系表"})
public class SysInviteRecordController extends BaseController {

    @Autowired
    private InviteRecordService inviteRecordService;

    /**
     * 添加邀请关系表
     */
    @PostMapping("/add")
    @OperationLog(name = "添加邀请关系表", type = OperationLogType.ADD)
    @ApiOperation(value = "添加邀请关系表", response = ApiResult.class)
    public ApiResult<Boolean> addInviteRecord(@Validated(Add.class) @RequestBody InviteRecord inviteRecord) throws Exception {
        boolean flag = inviteRecordService.saveInviteRecord(inviteRecord);
        return ApiResult.result(flag);
    }

    /**
     * 修改邀请关系表
     */
    @PostMapping("/update")
    @OperationLog(name = "修改邀请关系表", type = OperationLogType.UPDATE)
    @ApiOperation(value = "修改邀请关系表", response = ApiResult.class)
    public ApiResult<Boolean> updateInviteRecord(@Validated(Update.class) @RequestBody InviteRecord inviteRecord) throws Exception {
        boolean flag = inviteRecordService.updateInviteRecord(inviteRecord);
        return ApiResult.result(flag);
    }

    /**
     * 删除邀请关系表
     */
    @PostMapping("/delete/{id}")
    @OperationLog(name = "删除邀请关系表", type = OperationLogType.DELETE)
    @ApiOperation(value = "删除邀请关系表", response = ApiResult.class)
    public ApiResult<Boolean> deleteInviteRecord(@PathVariable("id") Long id) throws Exception {
        boolean flag = inviteRecordService.deleteInviteRecord(id);
        return ApiResult.result(flag);
    }


    /**
     * 邀请返佣奖励（邀请返佣奖励）
     */
    @PostMapping("/getPageList")
    @OperationLog(name = "邀请关系表分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "邀请关系表分页列表", response = InviteRecord.class)
    public ApiResult<Paging<InviteRecord>> getInviteRecordPageList(@Validated @RequestBody InviteRecordPageParam inviteRecordPageParam) throws Exception {
        Paging<InviteRecord> paging = inviteRecordService.getInviteRecordPageList(inviteRecordPageParam);
        return ApiResult.ok(paging);
    }

    /**
     * 邀请返佣奖励（邀请返佣奖励-导出）
     */
    @PostMapping("/getPageListExcel")
    @OperationLog(name = "邀请关系表分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "邀请关系表分页列表", response = InviteRecord.class)
    public void getInviteRecordPageList(@Validated @RequestBody InviteRecordPageParam inviteRecordPageParam, HttpServletResponse response) throws Exception {
        Paging<InviteRecord> paging = inviteRecordService.getInviteRecordPageList(inviteRecordPageParam);

        //将数据循环存到导出实体类中
        List<InviteExcel> excelInviteRecordList = new ArrayList<>();

        for (InviteRecord inviteRecord:paging.getRecords()) {
            InviteExcel inviteExcel = new InviteExcel();
            BeanUtils.copyProperties(inviteRecord,inviteExcel);
            excelInviteRecordList.add(inviteExcel);
        }
        FileUtil.exportExcel(excelInviteRecordList,InviteExcel.class,"邀请奖励返佣",response);
    }



    /**
     * 邀请返佣奖励-邀请记录（邀请返佣奖励-邀请记录）
     */
    @PostMapping("/getInviteRecordDescPageList")
    @OperationLog(name = "邀请返佣奖励-邀请记录", type = OperationLogType.INFO)
    @ApiOperation(value = "邀请返佣奖励-邀请记录", response = InviteRecord.class)
    public ApiResult<Paging<InviteRecord>> getInviteRecordDescPageList(@RequestBody InviteRecordRequest inviteRecordRequest) throws Exception {
        Paging<InviteRecord> inviteRecordDescPageList = inviteRecordService.getInviteRecordDescPageList(inviteRecordRequest);
        return ApiResult.ok(inviteRecordDescPageList);
    }

    /**
     * 邀请返佣奖励-邀请记录（邀请返佣奖励-邀请记录-导出）
     */
    @PostMapping("/inviteRecordDescPageListExcel")
    @OperationLog(name = "邀请返佣奖励-邀请记录-导出", type = OperationLogType.INFO)
    @ApiOperation(value = "邀请返佣奖励-邀请记录-导出", response = InviteRecord.class)
    public void inviteRecordDescPageListExcel(@RequestBody InviteRecordRequest inviteRecordRequest,HttpServletResponse response) throws Exception {
        Paging<InviteRecord> inviteRecordDescPageList = inviteRecordService.getInviteRecordDescPageList(inviteRecordRequest);

        //将数据循环存到导出实体类中
        List<InviteRecordDescExcel> excelInviteRecordList = new ArrayList<>();

        for (InviteRecord inviteRecord:inviteRecordDescPageList.getRecords()) {
            InviteRecordDescExcel inviteRecordDescExcel = new InviteRecordDescExcel();
            BeanUtils.copyProperties(inviteRecord,inviteRecordDescExcel);
            excelInviteRecordList.add(inviteRecordDescExcel);
        }
        FileUtil.exportExcel(excelInviteRecordList,InviteRecordDescExcel.class,"邀请记录",response);

    }

    /**
     * 邀请返佣奖励详情（邀请返佣奖励-邀请记录-奖励详情）
     */
    @PostMapping("/info")
    @OperationLog(name = "邀请返佣奖励-邀请记录-奖励详情", type = OperationLogType.INFO)
    @ApiOperation(value = "邀请返佣奖励-邀请记录-奖励详情", response = InviteRecord.class)
    public ApiResult<Paging<RewardDescVo>> getInviteRecord(@Validated @RequestBody InviteRecordPageParam inviteRecordPageParam) throws Exception {
        Paging<RewardDescVo> inviteRecordDesc = inviteRecordService.getInviteRecordDesc(inviteRecordPageParam);
        return ApiResult.ok(inviteRecordDesc);
    }

    /**
     * 邀请返佣奖励详情（邀请返佣奖励-邀请记录-奖励详情-导出）
     */
    @PostMapping("/infoExcel")
    @OperationLog(name = "邀请返佣奖励-邀请记录-奖励详情-导出", type = OperationLogType.INFO)
    @ApiOperation(value = "邀请返佣奖励-邀请记录-奖励详情-导出", response = InviteRecord.class)
    public void infoExcel(@Validated @RequestBody InviteRecordPageParam inviteRecordPageParam,HttpServletResponse response) throws Exception {
        Paging<RewardDescVo> inviteRecordDesc = inviteRecordService.getInviteRecordDesc(inviteRecordPageParam);
        FileUtil.exportExcel(inviteRecordDesc.getRecords(),RewardDescVo.class,"奖励详情",response);
    }


}

