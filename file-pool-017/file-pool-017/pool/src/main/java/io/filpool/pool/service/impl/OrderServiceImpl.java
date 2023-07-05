package io.filpool.pool.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import io.filpool.pool.controller.admin.SysUtilController;
import io.filpool.pool.entity.Currency;
import io.filpool.pool.entity.Goods;
import io.filpool.pool.entity.Order;
import io.filpool.pool.entity.User;
import io.filpool.pool.mapper.OrderMapper;
import io.filpool.pool.service.OrderService;
import io.filpool.pool.param.OrderPageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.filpool.framework.common.service.impl.BaseServiceImpl;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.pagination.PageInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 订单表 服务实现类
 *
 * @author filpool
 * @since 2021-03-08
 */
@Slf4j
@Service
public class OrderServiceImpl extends BaseServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private CurrencyServiceImpl currencyService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private SysUtilController sysUtilController;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveOrder(Order order) throws Exception {
        return super.save(order);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateOrder(Order order) throws Exception {
        return super.updateById(order);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteOrder(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<Order> getOrderPageList(OrderPageParam orderPageParam) throws Exception {
        Page<Order> page = new PageInfo<>(orderPageParam, OrderItem.desc(getLambdaColumn(Order::getCreateTime)));
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        if (orderPageParam.getOrderNumber()!=null){
            wrapper.eq(Order::getOrderNumber,orderPageParam.getOrderNumber());
        }
        if (orderPageParam.getAccount()!=null){
            Long userId = sysUtilController.queryUserId(orderPageParam.getAccount());
            wrapper.inSql(Order::getUserId,userId.toString());
        }
        if (orderPageParam.getGoodType()!=null){
            wrapper.eq(Order::getGoodType,orderPageParam.getGoodType());
        }
        if (orderPageParam.getStatus()!=null){
            wrapper.eq(Order::getStatus,orderPageParam.getStatus());
        }
        if (orderPageParam.getStartTime() != null) {
            wrapper.ge(Order::getCreateTime, orderPageParam.getStartTime());
        }
        if (orderPageParam.getEndTime() != null) {
            wrapper.le(Order::getCreateTime, orderPageParam.getEndTime());
        }

        IPage<Order> iPage = orderMapper.selectPage(page, wrapper);
        for (Order order:iPage.getRecords()) {
            //根据币种id查询币种名称
            String asset = sysUtilController.querySymbol(Integer.parseInt(order.getCurrencyId().toString()));
            if (asset!=null){
                order.setSymbol(asset);
            }
            //用户账号
            String account = sysUtilController.queryAccount(order.getUserId());
            if (account!=null){
                order.setAccount(account);
            }

        }
        return new Paging<Order>(iPage);
    }

}
