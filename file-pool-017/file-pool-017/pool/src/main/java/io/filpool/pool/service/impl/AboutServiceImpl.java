package io.filpool.pool.service.impl;

import io.filpool.pool.entity.About;
import io.filpool.pool.entity.Supplement;
import io.filpool.pool.mapper.AboutMapper;
import io.filpool.pool.service.AboutService;
import io.filpool.pool.param.AboutPageParam;
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
 * 平台信息表 服务实现类
 *
 * @author filpool
 * @since 2021-03-02
 */
@Slf4j
@Service
public class AboutServiceImpl extends BaseServiceImpl<AboutMapper, About> implements AboutService {

    @Autowired
    private AboutMapper aboutMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveAbout(About about) throws Exception {
        return super.save(about);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateAbout(About about) throws Exception {
        return super.updateById(about);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteAbout(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<About> getAboutPageList(AboutPageParam aboutPageParam) throws Exception {
        Page<About> page = new PageInfo<>(aboutPageParam, OrderItem.desc(getLambdaColumn(About::getCreateTime)));
        LambdaQueryWrapper<About> wrapper = new LambdaQueryWrapper<>();
        if (aboutPageParam.getTitle()!=null){
            wrapper.eq(About::getTitle,aboutPageParam.getTitle());
        }
        if (aboutPageParam.getStartDate() != null) {
            wrapper.ge(About::getCreateTime, aboutPageParam.getStartDate());
        }
        if (aboutPageParam.getEndDate() != null) {
            wrapper.le(About::getCreateTime, aboutPageParam.getEndDate());
        }
        IPage<About> iPage = aboutMapper.selectPage(page, wrapper);
        return new Paging<About>(iPage);
    }

}
