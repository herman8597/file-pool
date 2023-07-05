package io.filpool.pool.service;

import io.filpool.pool.entity.Order;
import io.filpool.pool.entity.RewardRecord;
import io.filpool.pool.entity.Supplement;
import io.filpool.pool.param.RewardRecordPageParam;
import io.filpool.framework.common.service.BaseService;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.pool.request.PageRequest;
import io.filpool.pool.vo.RewardRecordVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单奖励记录 服务类
 *
 * @author filpool
 * @since 2021-03-12
 */
public interface RewardRecordService extends BaseService<RewardRecord> {

    /**
     * 保存
     *
     * @param rewardRecord
     * @return
     * @throws Exception
     */
    boolean saveRewardRecord(RewardRecord rewardRecord) throws Exception;

    /**
     * 修改
     *
     * @param rewardRecord
     * @return
     * @throws Exception
     */
    boolean updateRewardRecord(RewardRecord rewardRecord) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteRewardRecord(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param rewardRecordQueryParam
     * @return
     * @throws Exception
     */
    Paging<RewardRecord> getRewardRecordPageList(RewardRecordPageParam rewardRecordPageParam) throws Exception;

    /**
     * 订单奖励
     */
    boolean orderReward(Order order) throws Exception;

    /**
     * 补单奖励
     * */
    boolean supplementReward(Supplement supplement) throws Exception;

    /**
     * 奖励记录
     * */
    List<RewardRecordVo> getRewardRecord(PageRequest request) throws Exception;

    /***
     * 社区奖励
     */
    void communityRewards(Order order, BigDecimal expPlus,Integer rewardType) throws Exception;
}
