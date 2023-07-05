package io.filpool.pool.controller.admin;

import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.common.controller.BaseController;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.validator.groups.Add;
import io.filpool.framework.core.validator.groups.Update;
import io.filpool.framework.log.annotation.Module;
import io.filpool.framework.log.annotation.OperationLog;
import io.filpool.framework.log.enums.OperationLogType;
import io.filpool.pool.entity.Order;
import io.filpool.pool.entity.User;
import io.filpool.pool.excel.GoodsOrderExcel;
import io.filpool.pool.excel.PledgeExcel;
import io.filpool.pool.param.OrderPageParam;
import io.filpool.pool.request.PledgeListRequest;
import io.filpool.pool.request.SysUserPageRequest;
import io.filpool.pool.service.OrderService;
import io.filpool.pool.util.FileUtil;
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
 * 订单表 控制器
 *
 * @author filpool
 * @since 2021-03-08
 */
@Slf4j
@RestController
@RequestMapping("sys/order")
@Module("pool")
@Api(value = "订单表API", tags = {"订单表"})
public class SysOrderController extends BaseController {

    @Autowired
    private OrderService orderService;

    /**
     * 添加订单表
     */
    @PostMapping("/add")
    @OperationLog(name = "添加订单表", type = OperationLogType.ADD)
    @ApiOperation(value = "添加订单表", response = ApiResult.class)
    public ApiResult<Boolean> addOrder(@Validated(Add.class) @RequestBody Order order) throws Exception {
        boolean flag = orderService.saveOrder(order);
        return ApiResult.result(flag);
    }

    /**
     * 删除订单表
     */
    @PostMapping("/delete/{id}")
    @OperationLog(name = "删除订单表", type = OperationLogType.DELETE)
    @ApiOperation(value = "删除订单表", response = ApiResult.class)
    public ApiResult<Boolean> deleteOrder(@PathVariable("id") Long id) throws Exception {
        boolean flag = orderService.deleteOrder(id);
        return ApiResult.result(flag);
    }

    /**
     * 获取订单表详情
     */
    @GetMapping("/info/{id}")
    @OperationLog(name = "订单表详情", type = OperationLogType.INFO)
    @ApiOperation(value = "订单表详情", response = Order.class)
    public ApiResult<Order> getOrder(@PathVariable("id") Long id) throws Exception {
        Order order = orderService.getById(id);
        return ApiResult.ok(order);
    }

    /**
     * 订单表分页列表（商品订单）
     */
    @PostMapping("/getPageList")
    @OperationLog(name = "订单表分页列表", type = OperationLogType.PAGE)
    @ApiOperation(value = "订单表分页列表", response = Order.class)
    public ApiResult<Paging<Order>> getOrderPageList(@Validated @RequestBody OrderPageParam orderPageParam) throws Exception {
        Paging<Order> paging = orderService.getOrderPageList(orderPageParam);
        return ApiResult.ok(paging);
    }


    /**
     * 导出（商品订单-导出）
     */
    @PostMapping("/getOrderPageListExport")
    @OperationLog(name = "商品订单-导出", type = OperationLogType.PAGE)
    @ApiOperation(value = "商品订单-导出", response = User.class)
    public void getOrderPageListExport(@Validated @RequestBody OrderPageParam orderPageParam, HttpServletResponse response) throws Exception {
        Paging<Order> paging = orderService.getOrderPageList(orderPageParam);
        //将数据循环存到导出实体类中
        List<GoodsOrderExcel> excelPledgeList = new ArrayList<>();

        for (Order order:paging.getRecords()) {
            GoodsOrderExcel goodsOrderExcel = new GoodsOrderExcel();
            BeanUtils.copyProperties(order,goodsOrderExcel);
            excelPledgeList.add(goodsOrderExcel);
        }
        FileUtil.exportExcel(excelPledgeList,GoodsOrderExcel.class,"商品订单",response);
    }



    /**
     * 修改订单表（商品订单-编辑）
     */
    @PostMapping("/update")
    @OperationLog(name = "修改订单表", type = OperationLogType.UPDATE)
    @ApiOperation(value = "修改订单表", response = ApiResult.class)
    public ApiResult<Boolean> updateOrder(@Validated(Update.class) @RequestBody Order order) throws Exception {
        boolean flag = orderService.updateOrder(order);
        return ApiResult.result(flag);
    }

}

