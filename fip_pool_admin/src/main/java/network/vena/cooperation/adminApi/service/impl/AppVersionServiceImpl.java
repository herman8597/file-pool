package network.vena.cooperation.adminApi.service.impl;

import network.vena.cooperation.adminApi.entity.AppVersion;
import network.vena.cooperation.adminApi.mapper.AppVersionMapper;
import network.vena.cooperation.adminApi.service.IAppVersionService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: app_version
 * @Author: jeecg-boot
 * @Date:   2020-05-08
 * @Version: V1.0
 */
@Service
public class AppVersionServiceImpl extends ServiceImpl<AppVersionMapper, AppVersion> implements IAppVersionService {

    @Override
    @Transactional
    public boolean save(AppVersion entity) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (1==entity.getStatus()) {
            AppVersion one = lambdaQuery().eq(AppVersion::getStatus, 1).one();
            one.setStatus(0);
            saveOrUpdate(one);
        }
        entity.setUpdateBy(sysUser.getRealname()).setUserName(sysUser.getId());
        return super.save(entity);
    }

    @Override
    @Transactional
    public boolean updateById(AppVersion entity) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (1==entity.getStatus()) {
            AppVersion one = lambdaQuery().eq(AppVersion::getStatus, 1).one();
            one.setStatus(0);
            saveOrUpdate(one);
        }
        entity.setUpdateBy(sysUser.getRealname()).setUserName(sysUser.getId());
        return super.updateById(entity);
    }
}
