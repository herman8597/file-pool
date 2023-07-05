package io.filpool.pool.service;

import io.filpool.pool.entity.Order;
import io.filpool.pool.param.OrderPageParam;
import io.filpool.framework.common.service.BaseService;
import io.filpool.framework.core.pagination.Paging;

/**
 * 订单表 服务类
 *
 * @author filpool
 * @since 2021-03-08
 */
public interface OrderService extends BaseService<Order> {

    /**
     * 保存
     *
     * @param order
     * @return
     * @throws Exception
     */
    boolean saveOrder(Order order) throws Exception;

    /**
     * 修改
     *
     * @param order
     * @return
     * @throws Exception
     */
    boolean updateOrder(Order order) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteOrder(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param orderQueryParam
     * @return
     * @throws Exception
     */
    Paging<Order> getOrderPageList(OrderPageParam orderPageParam) throws Exception;

}
