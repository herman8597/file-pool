package io.filpool.pool.service;

import io.filpool.pool.entity.AssetAccount;
import io.filpool.pool.entity.AssetAccountLog;
import io.filpool.pool.param.AssetAccountLogPageParam;
import io.filpool.framework.common.service.BaseService;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.pool.request.AssetRecordsRequest;
import io.filpool.pool.vo.AssetRecordVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 账户资产变化表 服务类
 *
 * @author filpool
 * @since 2021-03-10
 */
public interface AssetAccountLogService extends BaseService<AssetAccountLog> {

    /**
     * 保存
     *
     * @param assetAccountLog
     * @return
     * @throws Exception
     */
    boolean saveAssetAccountLog(AssetAccountLog assetAccountLog) throws Exception;

    /**
     * 修改
     *
     * @param assetAccountLog
     * @return
     * @throws Exception
     */
    boolean updateAssetAccountLog(AssetAccountLog assetAccountLog) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteAssetAccountLog(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param assetAccountLogQueryParam
     * @return
     * @throws Exception
     */
    Paging<AssetAccountLog> getAssetAccountLogPageList(AssetAccountLogPageParam assetAccountLogPageParam) throws Exception;

    //保存日志
    Long saveLog(AssetAccount account, BigDecimal amount, Integer type, String remark,Long recordId) throws Exception;

    List<AssetRecordVo> getAssetRecords(AssetRecordsRequest request) throws Exception;
}
