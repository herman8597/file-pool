package io.filpool.pool.service.impl;

import io.filpool.pool.entity.Banner;
import io.filpool.pool.entity.Supplement;
import io.filpool.pool.mapper.BannerMapper;
import io.filpool.pool.service.BannerService;
import io.filpool.pool.param.BannerPageParam;
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
 * banner 服务实现类
 *
 * @author filpool
 * @since 2021-03-04
 */
@Slf4j
@Service
public class BannerServiceImpl extends BaseServiceImpl<BannerMapper, Banner> implements BannerService {

    @Autowired
    private BannerMapper bannerMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveBanner(Banner banner) throws Exception {
        return super.save(banner);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateBanner(Banner banner) throws Exception {
        return super.updateById(banner);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteBanner(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<Banner> getBannerPageList(BannerPageParam bannerPageParam) throws Exception {
        Page<Banner> page = new PageInfo<>(bannerPageParam, OrderItem.desc(getLambdaColumn(Banner::getCreateTime)));
        LambdaQueryWrapper<Banner> wrapper = new LambdaQueryWrapper<>();
        if (bannerPageParam.getTitle()!=null){
            wrapper.eq(Banner::getTitle,bannerPageParam.getTitle());
        }
        if (bannerPageParam.getLanguage()!=null){
            wrapper.eq(Banner::getLanguage,bannerPageParam.getLanguage());
        }
        if (bannerPageParam.getStartDate() != null) {
            wrapper.ge(Banner::getCreateTime, bannerPageParam.getStartDate());
        }
        if (bannerPageParam.getEndDate() != null) {
            wrapper.le(Banner::getCreateTime, bannerPageParam.getEndDate());
        }
        IPage<Banner> iPage = bannerMapper.selectPage(page, wrapper);

        return new Paging<Banner>(iPage);
    }

}
