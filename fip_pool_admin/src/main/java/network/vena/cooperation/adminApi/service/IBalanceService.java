package network.vena.cooperation.adminApi.service;

import network.vena.cooperation.adminApi.dto.BalanceDTO;
import network.vena.cooperation.adminApi.entity.Balance;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: balance
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
public interface IBalanceService extends IService<Balance> {

      Boolean exchange(BalanceDTO balanceDTO);

}
