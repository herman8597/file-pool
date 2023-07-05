package io.filpool.pool.service;

import io.filpool.pool.entity.AreaCode;
import io.filpool.pool.param.AreaCodePageParam;
import io.filpool.framework.common.service.BaseService;
import io.filpool.framework.core.pagination.Paging;

/**
 * 国家地区表 服务类
 *
 * @author filpool
 * @since 2021-03-02
 */
public interface AreaCodeService extends BaseService<AreaCode> {

    /**
     * 保存
     *
     * @param areaCode
     * @return
     * @throws Exception
     */
    boolean saveAreaCode(AreaCode areaCode) throws Exception;

    /**
     * 修改
     *
     * @param areaCode
     * @return
     * @throws Exception
     */
    boolean updateAreaCode(AreaCode areaCode) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteAreaCode(Long id) throws Exception;


//    /**
//     * 获取分页对象
//     *
//     * @param areaCodeQueryParam
//     * @return
//     * @throws Exception
//     */
//    Paging<AreaCode> getAreaCodePageList(AreaCodePageParam areaCodePageParam) throws Exception;

}
