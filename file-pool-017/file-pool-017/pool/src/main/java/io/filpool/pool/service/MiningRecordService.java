package io.filpool.pool.service;

import io.filpool.pool.entity.MiningRecord;
import io.filpool.pool.entity.User;
import io.filpool.pool.param.MiningRecordPageParam;
import io.filpool.framework.common.service.BaseService;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.pool.request.SysMiningRequest;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 平台挖矿记录表 服务类
 *
 * @author filpool
 * @since 2021-03-22
 */
public interface MiningRecordService extends BaseService<MiningRecord> {

    /**
     * 保存
     *
     * @param miningRecord
     * @return
     * @throws Exception
     */
    boolean saveMiningRecord(MiningRecord miningRecord) throws Exception;

    /**
     * 修改
     *
     * @param miningRecord
     * @return
     * @throws Exception
     */
    boolean updateMiningRecord(MiningRecord miningRecord) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteMiningRecord(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param miningRecordQueryParam
     * @return
     * @throws Exception
     */
    Paging<MiningRecord> getMiningRecordPageList(MiningRecordPageParam miningRecordPageParam) throws Exception;

    /**
     * 添加挖矿收益记录
     * */
    boolean addMiningRecord(SysMiningRequest request) throws Exception;

}
