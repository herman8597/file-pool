package io.filpool.pool.service.impl;

import io.filpool.pool.entity.SupplementDeduct;
import io.filpool.pool.mapper.SupplementDeductMapper;
import io.filpool.pool.service.SupplementDeductService;
import io.filpool.pool.param.SupplementDeductPageParam;
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
 * 算力补单扣除记录 服务实现类
 *
 * @author filpool
 * @since 2021-04-02
 */
@Slf4j
@Service
public class SupplementDeductServiceImpl extends BaseServiceImpl<SupplementDeductMapper, SupplementDeduct> implements SupplementDeductService {

    @Autowired
    private SupplementDeductMapper supplementDeductMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveSupplementDeduct(SupplementDeduct supplementDeduct) throws Exception {
        return super.save(supplementDeduct);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateSupplementDeduct(SupplementDeduct supplementDeduct) throws Exception {
        return super.updateById(supplementDeduct);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteSupplementDeduct(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<SupplementDeduct> getSupplementDeductPageList(SupplementDeductPageParam supplementDeductPageParam) throws Exception {
        Page<SupplementDeduct> page = new PageInfo<>(supplementDeductPageParam, OrderItem.desc(getLambdaColumn(SupplementDeduct::getCreateTime)));
        LambdaQueryWrapper<SupplementDeduct> wrapper = new LambdaQueryWrapper<>();
        IPage<SupplementDeduct> iPage = supplementDeductMapper.selectPage(page, wrapper);
        return new Paging<SupplementDeduct>(iPage);
    }

}
