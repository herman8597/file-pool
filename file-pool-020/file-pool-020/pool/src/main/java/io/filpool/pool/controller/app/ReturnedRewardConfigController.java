package io.filpool.pool.controller.app;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.filpool.pool.entity.CommunityRewardLog;
import io.filpool.pool.entity.ReturnedRewardConfig;
import io.filpool.pool.service.ReturnedRewardConfigService;
import lombok.extern.slf4j.Slf4j;
import io.filpool.pool.param.ReturnedRewardConfigPageParam;
import io.filpool.framework.common.controller.BaseController;
import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.common.param.IdParam;
import io.filpool.framework.log.annotation.Module;
import io.filpool.framework.log.annotation.OperationLog;
import io.filpool.framework.log.enums.OperationLogType;
import io.filpool.framework.core.validator.groups.Add;
import io.filpool.framework.core.validator.groups.Update;
import org.springframework.validation.annotation.Validated;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 020社区奖励 控制器
 *
 * @author filpool
 * @since 2021-05-27
 */
@Slf4j
@RestController
@RequestMapping("/returnedRewardConfig")
@Module("pool")
@Api(value = "020社区奖励API", tags = {"020社区奖励"})
public class ReturnedRewardConfigController extends BaseController {

    @Autowired
    private ReturnedRewardConfigService returnedRewardConfigService;

    /**
     * 添加020社区奖励
     */
    @PostMapping("/add")
    @OperationLog(name = "添加020社区奖励", type = OperationLogType.ADD)
    @ApiOperation(value = "添加020社区奖励", response = ApiResult.class)
    public ApiResult<Boolean> addReturnedRewardConfig(@Validated(Add.class) @RequestBody ReturnedRewardConfig returnedRewardConfig) throws Exception {
        boolean flag = returnedRewardConfigService.saveReturnedRewardConfig(returnedRewardConfig);
        return ApiResult.result(flag);
    }

    /**
     * 修改020社区奖励
     */
    @PostMapping("/update")
    @OperationLog(name = "修改020社区奖励", type = OperationLogType.UPDATE)
    @ApiOperation(value = "修改020社区奖励", response = ApiResult.class)
    public ApiResult<Boolean> updateReturnedRewardConfig(@Validated(Update.class) @RequestBody ReturnedRewardConfig returnedRewardConfig) throws Exception {
        boolean flag = returnedRewardConfigService.updateReturnedRewardConfig(returnedRewardConfig);
        return ApiResult.result(flag);
    }

    /**
     * 删除020社区奖励
     */
    @PostMapping("/delete/{id}")
    @OperationLog(name = "删除020社区奖励", type = OperationLogType.DELETE)
    @ApiOperation(value = "删除020社区奖励", response = ApiResult.class)
    public ApiResult<Boolean> deleteReturnedRewardConfig(@PathVariable("id") Long id) throws Exception {
        boolean flag = returnedRewardConfigService.deleteReturnedRewardConfig(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取020社区奖励详情
     */
    @GetMapping("/info/{id}")
    @OperationLog(name = "020社区奖励详情", type = OperationLogType.INFO)
    @ApiOperation(value = "020社区奖励详情", response = ReturnedRewardConfig.class)
    public ApiResult<ReturnedRewardConfig> getReturnedRewardConfig(@PathVariable("id") Long id) throws Exception {
        ReturnedRewardConfig returnedRewardConfig = returnedRewardConfigService.getById(id);
        return ApiResult.ok(returnedRewardConfig);
    }

    /**
     * 020社区奖励分页列表
     */
    @PostMapping("/getPageList")
    @OperationLog(name = "020社区奖励分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "020社区奖励分页列表", response = ReturnedRewardConfig.class)
    public ApiResult<Paging<ReturnedRewardConfig>> getReturnedRewardConfigPageList(@Validated @RequestBody ReturnedRewardConfigPageParam returnedRewardConfigPageParam) throws Exception {
        Paging<ReturnedRewardConfig> paging = returnedRewardConfigService.getReturnedRewardConfigPageList(returnedRewardConfigPageParam);
        return ApiResult.ok(paging);
    }

}

