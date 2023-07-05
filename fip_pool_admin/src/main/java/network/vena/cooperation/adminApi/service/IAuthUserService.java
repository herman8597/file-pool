package network.vena.cooperation.adminApi.service;

import network.vena.cooperation.adminApi.entity.AuthUser;
import com.baomidou.mybatisplus.extension.service.IService;
import network.vena.cooperation.adminApi.param.AuthUserParam;
import network.vena.cooperation.base.vo.AuthUserEditVo;
import network.vena.cooperation.base.vo.AuthUserVo;

import java.math.BigDecimal;

/**
 * @Description: auth_user
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
public interface IAuthUserService extends IService<AuthUser> {

    AuthUserEditVo getEditVo(String id);

    AuthUserVo getAuthUserDetail(String id);

    void edit(AuthUserParam authUserParam);

    BigDecimal getAuthUserDetailTwo(String id);
}
