package network.vena.cooperation.adminApi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import network.vena.cooperation.adminApi.entity.UserIdInfo;
import org.jeecg.common.api.vo.Result;

/**
 * @Description: user_id_info
 * @Author: jeecg-boot
 * @Date:   2020-07-01
 * @Version: V1.0
 */
public interface IUserIdInfoService extends IService<UserIdInfo> {

    Result audit(UserIdInfo userIdInfo);
}
