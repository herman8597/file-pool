package io.filpool.pool.service;

import io.filpool.pool.entity.BzzConfig;
import io.filpool.pool.param.BzzConfigPageParam;
import io.filpool.framework.common.service.BaseService;
import io.filpool.framework.core.pagination.Paging;

/**
 *  服务类
 *
 * @author filpool
 * @since 2021-06-24
 */
public interface BzzConfigService extends BaseService<BzzConfig> {

    /**
     * 保存
     *
     * @param bzzConfig
     * @return
     * @throws Exception
     */
    boolean saveBzzConfig(BzzConfig bzzConfig) throws Exception;

    /**
     * 修改
     *
     * @param bzzConfig
     * @return
     * @throws Exception
     */
    boolean updateBzzConfig(BzzConfig bzzConfig) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteBzzConfig(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param bzzConfigQueryParam
     * @return
     * @throws Exception
     */
    Paging<BzzConfig> getBzzConfigPageList(BzzConfigPageParam bzzConfigPageParam) throws Exception;

}
