package io.filpool.pool.service;

import io.filpool.pool.entity.AppVersion;
import io.filpool.pool.param.AppVersionPageParam;
import io.filpool.framework.common.service.BaseService;
import io.filpool.framework.core.pagination.Paging;

/**
 * 版本更新表 服务类
 *
 * @author filpool
 * @since 2021-03-30
 */
public interface AppVersionService extends BaseService<AppVersion> {

    /**
     * 保存
     *
     * @param appVersion
     * @return
     * @throws Exception
     */
    boolean saveAppVersion(AppVersion appVersion) throws Exception;

    /**
     * 修改
     *
     * @param appVersion
     * @return
     * @throws Exception
     */
    boolean updateAppVersion(AppVersion appVersion) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteAppVersion(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param appVersionQueryParam
     * @return
     * @throws Exception
     */
    Paging<AppVersion> getAppVersionPageList(AppVersionPageParam appVersionPageParam) throws Exception;


    AppVersion getNewestVersion(AppVersionPageParam appVersionPageParam);
}
