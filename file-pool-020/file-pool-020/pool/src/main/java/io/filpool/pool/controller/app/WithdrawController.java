package io.filpool.pool.controller.app;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.common.controller.BaseController;
import io.filpool.framework.jwt.CheckLogin;
import io.filpool.framework.util.Constants;
import io.filpool.pool.request.WithdrawRequest;
import io.filpool.pool.service.WithdrawRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.API_PREFIX + "/withdraw")
@Api(value = "提现控制器", tags = {"提现相关API"})
@ApiSupport(order = 2)
public class WithdrawController extends BaseController {

    @Autowired
    private WithdrawRecordService withdrawRecordService;

    @PostMapping("submit")
    @ApiOperation("发起提现申请，返回id")
    @CheckLogin
    public ApiResult<Long> withdraw(@RequestBody WithdrawRequest request) throws Exception {
        Long id = withdrawRecordService.withdraw(request.getAmount(), request.getCurrencyId(), request.getAddress(), request.getPayPwd(),request.getChain(),request.getGoogleCode());
        return ApiResult.ok(id);
    }


}
