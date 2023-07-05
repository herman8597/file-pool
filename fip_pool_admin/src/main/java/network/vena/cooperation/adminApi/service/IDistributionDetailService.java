package network.vena.cooperation.adminApi.service;

import network.vena.cooperation.adminApi.entity.DistributionDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * @Description: distribution_detail
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
public interface IDistributionDetailService extends IService<DistributionDetail> {

     Integer exchangeSelfPurchase(String apiKey, BigDecimal quantity);

}
