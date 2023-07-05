package io.filpool.pool.controller.admin;

import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.common.controller.BaseController;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.validator.groups.Add;
import io.filpool.framework.core.validator.groups.Update;
import io.filpool.framework.log.annotation.Module;
import io.filpool.framework.log.annotation.OperationLog;
import io.filpool.framework.log.enums.OperationLogType;
import io.filpool.pool.entity.RewardConfig;
import io.filpool.pool.param.RewardConfigPageParam;
import io.filpool.pool.service.RewardConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 奖励等级配置 控制器
 *
 * @author filpool
 * @since 2021-03-11
 */
@Slf4j
@RestController
@RequestMapping("sys/rewardConfig")
@Module("pool")
@Api(value = "奖励等级配置API", tags = {"奖励等级配置"})
public class SysRewardConfigController extends BaseController {

    @Autowired
    private RewardConfigService rewardConfigService;

    /**
     * 添加奖励等级配置
     */
    @PostMapping("/add")
    @OperationLog(name = "添加奖励等级配置", type = OperationLogType.ADD)
    @ApiOperation(value = "添加奖励等级配置", response = ApiResult.class)
    public ApiResult<Boolean> addRewardConfig(@Validated(Add.class) @RequestBody RewardConfig rewardConfig) throws Exception {
        boolean flag = rewardConfigService.saveRewardConfig(rewardConfig);
        return ApiResult.result(flag);
    }


    /**
     * 删除奖励等级配置
     */
    @PostMapping("/delete/{id}")
    @OperationLog(name = "删除奖励等级配置", type = OperationLogType.DELETE)
    @ApiOperation(value = "删除奖励等级配置", response = ApiResult.class)
    public ApiResult<Boolean> deleteRewardConfig(@PathVariable("id") Long id) throws Exception {
        boolean flag = rewardConfigService.deleteRewardConfig(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取奖励等级配置详情
     */
    @GetMapping("/info/{id}")
    @OperationLog(name = "奖励等级配置详情", type = OperationLogType.INFO)
    @ApiOperation(value = "奖励等级配置详情", response = RewardConfig.class)
    public ApiResult<RewardConfig> getRewardConfig(@PathVariable("id") Long id) throws Exception {
        RewardConfig rewardConfig = rewardConfigService.getById(id);
        return ApiResult.ok(rewardConfig);
    }

    /**
     * 奖励等级配置分页列表（邀请返佣奖励配置）
     */
    @PostMapping("/getPageList")
    @OperationLog(name = "奖励等级配置分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "奖励等级配置分页列表", response = RewardConfig.class)
    public ApiResult<Paging<RewardConfig>> getRewardConfigPageList(@Validated @RequestBody RewardConfigPageParam rewardConfigPageParam) throws Exception {
        Paging<RewardConfig> paging = rewardConfigService.getRewardConfigPageList(rewardConfigPageParam);
        return ApiResult.ok(paging);
    }

    /**
     * 修改奖励等级配置（邀请返佣奖励配置-编辑）
     */
    @PostMapping("/update")
    @OperationLog(name = "修改奖励等级配置", type = OperationLogType.UPDATE)
    @ApiOperation(value = "修改奖励等级配置", response = ApiResult.class)
    public ApiResult<Boolean> updateRewardConfig(@Validated(Update.class) @RequestBody RewardConfig rewardConfig) throws Exception {
        boolean flag = rewardConfigService.updateRewardConfig(rewardConfig);
        return ApiResult.result(flag);
    }


}

