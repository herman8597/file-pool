package io.filpool.pool.service.impl;

import io.filpool.pool.entity.AreaCode;
import io.filpool.pool.mapper.AreaCodeMapper;
import io.filpool.pool.service.AreaCodeService;
import io.filpool.pool.param.AreaCodePageParam;
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
 * 国家地区表 服务实现类
 *
 * @author filpool
 * @since 2021-03-02
 */
@Slf4j
@Service
public class AreaCodeServiceImpl extends BaseServiceImpl<AreaCodeMapper, AreaCode> implements AreaCodeService {

    @Autowired
    private AreaCodeMapper areaCodeMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveAreaCode(AreaCode areaCode) throws Exception {
        return super.save(areaCode);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateAreaCode(AreaCode areaCode) throws Exception {
        return super.updateById(areaCode);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteAreaCode(Long id) throws Exception {
        return super.removeById(id);
    }

//    @Override
//    public Paging<AreaCode> getAreaCodePageList(AreaCodePageParam areaCodePageParam) throws Exception {
//        Page<AreaCode> page = new PageInfo<>(areaCodePageParam, OrderItem.desc(getLambdaColumn(AreaCode::getCreateTime)));
//        LambdaQueryWrapper<AreaCode> wrapper = new LambdaQueryWrapper<>();
//        IPage<AreaCode> iPage = areaCodeMapper.selectPage(page, wrapper);
//        return new Paging<AreaCode>(iPage);
//    }

}
