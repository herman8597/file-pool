package io.filpool.pool.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.filpool.framework.util.AccountUtil;
import io.filpool.pool.controller.admin.SysUtilController;
import io.filpool.pool.entity.RealNameRecord;
import io.filpool.pool.entity.RechargeRecord;
import io.filpool.pool.mapper.RechargeRecordMapper;
import io.filpool.pool.request.RechargeRecordRequest;
import io.filpool.pool.service.RechargeRecordService;
import io.filpool.pool.param.RechargeRecordPageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.filpool.framework.common.service.impl.BaseServiceImpl;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.pagination.PageInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 充值记录 服务实现类
 *
 * @author filpool
 * @since 2021-03-10
 */
@Slf4j
@Service
public class RechargeRecordServiceImpl extends BaseServiceImpl<RechargeRecordMapper, RechargeRecord> implements RechargeRecordService {

    @Autowired
    private RechargeRecordMapper rechargeRecordMapper;

    @Autowired
    private SysUtilController sysUtilController;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveRechargeRecord(RechargeRecord rechargeRecord) throws Exception {
        return super.save(rechargeRecord);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateRechargeRecord(RechargeRecord rechargeRecord) throws Exception {
        return super.updateById(rechargeRecord);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteRechargeRecord(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<RechargeRecord> getRechargeRecordPageList(RechargeRecordPageParam rechargeRecordPageParam) throws Exception {
        Page<RechargeRecord> page = new PageInfo<>(rechargeRecordPageParam, OrderItem.desc(getLambdaColumn(RechargeRecord::getCreateTime)));
        LambdaQueryWrapper<RechargeRecord> wrapper = new LambdaQueryWrapper<>();
        IPage<RechargeRecord> iPage = rechargeRecordMapper.selectPage(page, wrapper);
        return new Paging<RechargeRecord>(iPage);
    }

    @Override
    public Paging<RechargeRecord> getChargeMoneyPageList(RechargeRecordRequest rechargeRecordRequest) {
        Page<RechargeRecord> page = new PageInfo<>(rechargeRecordRequest, OrderItem.desc(getLambdaColumn(RechargeRecord::getCreateTime)));
        LambdaQueryWrapper<RechargeRecord> wrapper = new LambdaQueryWrapper<>();
        if (rechargeRecordRequest.getAccount()!=null){
            wrapper.eq(RechargeRecord::getUserId,sysUtilController.queryUserId(rechargeRecordRequest.getAccount()));
        }
        if (rechargeRecordRequest.getSymbol()!=null){
            wrapper.eq(RechargeRecord::getSymbol,rechargeRecordRequest.getSymbol());
        }
        if (rechargeRecordRequest.getFromAddress()!=null){
            wrapper.eq(RechargeRecord::getFromAddress,rechargeRecordRequest.getFromAddress());
        }
        if (rechargeRecordRequest.getStartDate() != null) {
            wrapper.ge(RechargeRecord::getCreateTime, rechargeRecordRequest.getStartDate());
        }
        if (rechargeRecordRequest.getEndDate() != null) {
            wrapper.le(RechargeRecord::getCreateTime, rechargeRecordRequest.getEndDate());
        }

        IPage<RechargeRecord> iPage = rechargeRecordMapper.selectPage(page, wrapper);
        for (RechargeRecord rechargeRecord:iPage.getRecords()) {
            //用户账号
            String account = sysUtilController.queryAccount(rechargeRecord.getUserId());
            if (account!=null){
                rechargeRecord.setAccount(sysUtilController.queryAccount(rechargeRecord.getUserId()));
            }
        }
        return new Paging<RechargeRecord>(iPage);
    }
}
