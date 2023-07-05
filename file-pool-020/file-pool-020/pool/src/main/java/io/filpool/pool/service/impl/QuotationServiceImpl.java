package io.filpool.pool.service.impl;

import io.filpool.pool.entity.Quotation;
import io.filpool.pool.mapper.QuotationMapper;
import io.filpool.pool.service.QuotationService;
import io.filpool.pool.param.QuotationPageParam;
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
 * 行情信息 服务实现类
 *
 * @author filpool
 * @since 2021-04-14
 */
@Slf4j
@Service
public class QuotationServiceImpl extends BaseServiceImpl<QuotationMapper, Quotation> implements QuotationService {

    @Autowired
    private QuotationMapper quotationMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveQuotation(Quotation quotation) throws Exception {
        return super.save(quotation);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateQuotation(Quotation quotation) throws Exception {
        return super.updateById(quotation);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteQuotation(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<Quotation> getQuotationPageList(QuotationPageParam quotationPageParam) throws Exception {
        Page<Quotation> page = new PageInfo<>(quotationPageParam, OrderItem.desc(getLambdaColumn(Quotation::getCreateTime)));
        LambdaQueryWrapper<Quotation> wrapper = new LambdaQueryWrapper<>();
        IPage<Quotation> iPage = quotationMapper.selectPage(page, wrapper);
        return new Paging<Quotation>(iPage);
    }

}
