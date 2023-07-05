package io.filpool.pool.service;

import io.filpool.pool.entity.GlobalConfig;
import io.filpool.pool.param.GlobalConfigPageParam;
import io.filpool.framework.common.service.BaseService;
import io.filpool.framework.core.pagination.Paging;

/**
 * 全局配置 服务类
 *
 * @author filpool
 * @since 2021-03-11
 */
public interface GlobalConfigService extends BaseService<GlobalConfig> {

    /**
     * 保存
     *
     * @param globalConfig
     * @return
     * @throws Exception
     */
    boolean saveGlobalConfig(GlobalConfig globalConfig) throws Exception;

    /**
     * 修改
     *
     * @param globalConfig
     * @return
     * @throws Exception
     */
    boolean updateGlobalConfig(GlobalConfig globalConfig) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteGlobalConfig(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param globalConfigQueryParam
     * @return
     * @throws Exception
     */
    Paging<GlobalConfig> getGlobalConfigPageList(GlobalConfigPageParam globalConfigPageParam) throws Exception;

}
