package io.filpool.pool.service;

import io.filpool.pool.entity.InviteRecord;
import io.filpool.pool.param.InviteRecordPageParam;
import io.filpool.framework.common.service.BaseService;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.pool.request.InviteRecordRequest;
import io.filpool.pool.vo.RewardDescVo;

/**
 * 邀请关系表 服务类
 *
 * @author filpool
 * @since 2021-03-02
 */
public interface InviteRecordService extends BaseService<InviteRecord> {

    /**
     * 保存
     *
     * @param inviteRecord
     * @return
     * @throws Exception
     */
    boolean saveInviteRecord(InviteRecord inviteRecord) throws Exception;

    /**
     * 修改
     *
     * @param inviteRecord
     * @return
     * @throws Exception
     */
    boolean updateInviteRecord(InviteRecord inviteRecord) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteInviteRecord(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param inviteRecordQueryParam
     * @return
     * @throws Exception
     */
    Paging<InviteRecord> getInviteRecordPageList(InviteRecordPageParam inviteRecordPageParam) throws Exception;

    /**
     * 获取分页对象
     *
     * @param inviteRecordQueryParam
     * @return
     * @throws Exception
     */
    Paging<RewardDescVo> getInviteRecordDesc(InviteRecordPageParam inviteRecordPageParam);

    Paging<InviteRecord> getInviteRecordDescPageList(InviteRecordRequest inviteRecordRequest);


}
