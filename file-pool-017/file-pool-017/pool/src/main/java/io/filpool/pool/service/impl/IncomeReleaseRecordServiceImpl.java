package io.filpool.pool.service.impl;

import io.filpool.pool.controller.admin.SysUtilController;
import io.filpool.pool.entity.IncomeReleaseRecord;
import io.filpool.pool.mapper.IncomeReleaseRecordMapper;
import io.filpool.pool.service.IncomeReleaseRecordService;
import io.filpool.pool.param.IncomeReleaseRecordPageParam;
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
 * 待释放记录表 服务实现类
 *
 * @author filpool
 * @since 2021-03-30
 */
@Slf4j
@Service
public class IncomeReleaseRecordServiceImpl extends BaseServiceImpl<IncomeReleaseRecordMapper, IncomeReleaseRecord> implements IncomeReleaseRecordService {

    @Autowired
    private IncomeReleaseRecordMapper incomeReleaseRecordMapper;

    @Autowired
    private SysUtilController sysUtilController;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveIncomeReleaseRecord(IncomeReleaseRecord incomeReleaseRecord) throws Exception {
        return super.save(incomeReleaseRecord);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateIncomeReleaseRecord(IncomeReleaseRecord incomeReleaseRecord) throws Exception {
        return super.updateById(incomeReleaseRecord);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteIncomeReleaseRecord(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<IncomeReleaseRecord> getIncomeReleaseRecordPageList(IncomeReleaseRecordPageParam incomeReleaseRecordPageParam) throws Exception {
        Page<IncomeReleaseRecord> page = new PageInfo<>(incomeReleaseRecordPageParam, OrderItem.desc(getLambdaColumn(IncomeReleaseRecord::getCreateTime)));
        LambdaQueryWrapper<IncomeReleaseRecord> wrapper = new LambdaQueryWrapper<>();
        if (incomeReleaseRecordPageParam.getAccount()!=null){
            wrapper.inSql(IncomeReleaseRecord::getUserId,"'"+sysUtilController.queryUserId(incomeReleaseRecordPageParam.getAccount())+"'");
        }
        IPage<IncomeReleaseRecord> iPage = incomeReleaseRecordMapper.selectPage(page, wrapper);
        return new Paging<IncomeReleaseRecord>(iPage);
    }

}
