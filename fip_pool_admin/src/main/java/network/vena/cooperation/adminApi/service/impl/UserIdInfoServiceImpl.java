package network.vena.cooperation.adminApi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import network.vena.cooperation.adminApi.entity.UserIdInfo;
import network.vena.cooperation.adminApi.mapper.AuthUserMapper;
import network.vena.cooperation.adminApi.mapper.UserIdInfoMapper;
import network.vena.cooperation.adminApi.service.IUserIdInfoService;
import network.vena.cooperation.util.ObjectUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.system.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Description: user_id_info
 * @Author: jeecg-boot
 * @Date: 2020-07-01
 * @Version: V1.0
 */
@Service
public class UserIdInfoServiceImpl extends ServiceImpl<UserIdInfoMapper, UserIdInfo> implements IUserIdInfoService {
    @Autowired
    private AuthUserMapper authUserMapper;

    @Override
    public Result audit(UserIdInfo userIdInfo) {
        UserIdInfo one = lambdaQuery().eq(UserIdInfo::getId, userIdInfo.getId()).one();
        if (0 != one.getAuditStatus()) {
            return Result.error("记录状态不正确");
        }
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        one.setAuditTime(new Date()).setStaffId(sysUser.getId()).setAuditStatus(userIdInfo.getAuditStatus());
        saveOrUpdate(one);
        authUserMapper.updateAuthStatus(one.getApiKey(), userIdInfo.getAuditStatus());
        return Result.ok();
    }

    @Cacheable(value = "getUserIdInfo", key = "#apiKey")
    public UserIdInfo getUserIdInfo(String apiKey) {
        UserIdInfo byApiKey = getBaseMapper().findByApiKey(apiKey);
        if (ObjectUtils.isEmpty(byApiKey)) {
            return null;
        }
        return byApiKey;

    }

}
