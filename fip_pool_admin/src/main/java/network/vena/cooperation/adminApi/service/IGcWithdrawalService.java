package network.vena.cooperation.adminApi.service;

import network.vena.cooperation.adminApi.entity.Balance;
import network.vena.cooperation.adminApi.entity.GcWithdrawal;
import com.baomidou.mybatisplus.extension.service.IService;
import network.vena.cooperation.adminApi.param.BaseParam;

import java.util.List;

/**
 * @Description: gc_withdrawal
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
public interface IGcWithdrawalService extends IService<GcWithdrawal> {

    String  audit(GcWithdrawal gcWithdrawal);

    List<Balance> getWithdrawBalance();
}
