package io.filpool.pool.service;

import io.filpool.pool.entity.PowerOrder;
import io.filpool.pool.param.PowerOrderPageParam;
import io.filpool.framework.common.service.BaseService;
import io.filpool.framework.core.pagination.Paging;

import java.math.BigDecimal;

/**
 * 算力订单表 服务类
 *
 * @author filpool
 * @since 2021-04-01
 */
public interface PowerOrderService extends BaseService<PowerOrder> {

    /**
     * 保存
     *
     * @param powerOrder
     * @return
     * @throws Exception
     */
    boolean savePowerOrder(PowerOrder powerOrder) throws Exception;

    /**
     * 修改
     *
     * @param powerOrder
     * @return
     * @throws Exception
     */
    boolean updatePowerOrder(PowerOrder powerOrder) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deletePowerOrder(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param powerOrderQueryParam
     * @return
     * @throws Exception
     */
    Paging<PowerOrder> getPowerOrderPageList(PowerOrderPageParam powerOrderPageParam) throws Exception;


    /**
     * 添加算力订单
     * @param amount 算力数量
     * @param userId 用户id
     * @param type 订单类型
     * @param contractDays 订单有效天数
     * */
    boolean addPowerOrder(Long userId, BigDecimal amount, int type, Long recordId,int contractDays) throws Exception;

//    boolean updateUserLevel(Long userId) throws Exception;
}
