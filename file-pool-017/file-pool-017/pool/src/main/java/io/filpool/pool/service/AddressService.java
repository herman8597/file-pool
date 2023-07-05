package io.filpool.pool.service;

import io.filpool.pool.entity.Address;
import io.filpool.pool.param.AddressPageParam;
import io.filpool.framework.common.service.BaseService;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.pool.vo.RechargeAddrVo;

/**
 * 地址表 服务类
 *
 * @author filpool
 * @since 2021-03-08
 */
public interface AddressService extends BaseService<Address> {

    /**
     * 保存
     *
     * @param address
     * @return
     * @throws Exception
     */
    boolean saveAddress(Address address) throws Exception;

    /**
     * 修改
     *
     * @param address
     * @return
     * @throws Exception
     */
    boolean updateAddress(Address address) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteAddress(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param addressQueryParam
     * @return
     * @throws Exception
     */
    Paging<Address> getAddressPageList(AddressPageParam addressPageParam) throws Exception;

    RechargeAddrVo getUserRechargeAddr(String symbol,String chain) throws Exception;

    Boolean checkAddress(Long userId,String series) throws Exception;
}
