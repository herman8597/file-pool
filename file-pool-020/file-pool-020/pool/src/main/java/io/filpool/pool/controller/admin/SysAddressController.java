package io.filpool.pool.controller.admin;

import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.common.controller.BaseController;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.validator.groups.Add;
import io.filpool.framework.core.validator.groups.Update;
import io.filpool.framework.log.annotation.Module;
import io.filpool.framework.log.annotation.OperationLog;
import io.filpool.framework.log.enums.OperationLogType;
import io.filpool.pool.entity.Address;
import io.filpool.pool.param.AddressPageParam;
import io.filpool.pool.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 地址表 控制器
 *
 * @author filpool
 * @since 2021-03-08
 */
@Slf4j
@RestController
@RequestMapping("sys/address")
@Module("pool")
@Api(value = "地址表API", tags = {"地址表"})
public class SysAddressController extends BaseController {

    @Autowired
    @Lazy
    private AddressService addressService;

    /**
     * 添加地址表
     */
    @PostMapping("/add")
    @OperationLog(name = "添加地址表", type = OperationLogType.ADD)
    @ApiOperation(value = "添加地址表", response = ApiResult.class)
    public ApiResult<Boolean> addAddress(@Validated(Add.class) @RequestBody Address address) throws Exception {
        boolean flag = addressService.saveAddress(address);
        return ApiResult.result(flag);
    }

    /**
     * 修改地址表
     */
    @PostMapping("/update")
    @OperationLog(name = "修改地址表", type = OperationLogType.UPDATE)
    @ApiOperation(value = "修改地址表", response = ApiResult.class)
    public ApiResult<Boolean> updateAddress(@Validated(Update.class) @RequestBody Address address) throws Exception {
        boolean flag = addressService.updateAddress(address);
        return ApiResult.result(flag);
    }

    /**
     * 删除地址表
     */
    @PostMapping("/delete/{id}")
    @OperationLog(name = "删除地址表", type = OperationLogType.DELETE)
    @ApiOperation(value = "删除地址表", response = ApiResult.class)
    public ApiResult<Boolean> deleteAddress(@PathVariable("id") Long id) throws Exception {
        boolean flag = addressService.deleteAddress(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取地址表详情
     */
    @GetMapping("/info/{id}")
    @OperationLog(name = "地址表详情", type = OperationLogType.INFO)
    @ApiOperation(value = "地址表详情", response = Address.class)
    public ApiResult<Address> getAddress(@PathVariable("id") Long id) throws Exception {
        Address address = addressService.getById(id);
        return ApiResult.ok(address);
    }

    /**
     * 地址表分页列表
     */
    @PostMapping("/getPageList")
    @OperationLog(name = "地址表分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "地址表分页列表", response = Address.class)
    public ApiResult<Paging<Address>> getAddressPageList(@Validated @RequestBody AddressPageParam addressPageParam) throws Exception {
        Paging<Address> paging = addressService.getAddressPageList(addressPageParam);
        return ApiResult.ok(paging);
    }

}

