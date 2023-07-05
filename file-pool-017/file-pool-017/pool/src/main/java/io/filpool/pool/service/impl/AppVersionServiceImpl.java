package io.filpool.pool.service.impl;

import io.filpool.pool.entity.AppVersion;
import io.filpool.pool.mapper.AppVersionMapper;
import io.filpool.pool.service.AppVersionService;
import io.filpool.pool.param.AppVersionPageParam;
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

import java.util.List;

/**
 * 版本更新表 服务实现类
 *
 * @author filpool
 * @since 2021-03-30
 */
@Slf4j
@Service
public class AppVersionServiceImpl extends BaseServiceImpl<AppVersionMapper, AppVersion> implements AppVersionService {

    @Autowired
    private AppVersionMapper appVersionMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveAppVersion(AppVersion appVersion) throws Exception {
        return super.save(appVersion);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateAppVersion(AppVersion appVersion) throws Exception {
        return super.updateById(appVersion);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteAppVersion(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<AppVersion> getAppVersionPageList(AppVersionPageParam appVersionPageParam) throws Exception {
        Page<AppVersion> page = new PageInfo<>(appVersionPageParam, OrderItem.desc(getLambdaColumn(AppVersion::getCreateTime)));
        LambdaQueryWrapper<AppVersion> wrapper = new LambdaQueryWrapper<>();
        if (appVersionPageParam.getVersionCode()!=null){
            wrapper.eq(AppVersion::getVersionCode,appVersionPageParam.getVersionCode());
        }
        if (appVersionPageParam.getVersionType()!=null){
            wrapper.eq(AppVersion::getVersionType,appVersionPageParam.getVersionType());
        }
        if (appVersionPageParam.getUpdateType()!=null){
            wrapper.eq(AppVersion::getUpdateType,appVersionPageParam.getUpdateType());
        }
        IPage<AppVersion> iPage = appVersionMapper.selectPage(page, wrapper);
        return new Paging<AppVersion>(iPage);
    }

    @Override
    public AppVersion getNewestVersion(AppVersionPageParam appVersionPageParam) {
        AppVersion newestVersion = appVersionMapper.getNewestVersion(appVersionPageParam.getVersionType());
        return newestVersion;
    }

}
