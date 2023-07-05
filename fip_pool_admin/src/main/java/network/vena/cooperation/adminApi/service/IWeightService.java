package network.vena.cooperation.adminApi.service;

import network.vena.cooperation.adminApi.dto.WeightDTO;
import network.vena.cooperation.adminApi.entity.Balance;
import network.vena.cooperation.adminApi.entity.Weight;
import com.baomidou.mybatisplus.extension.service.IService;
import network.vena.cooperation.adminApi.param.BaseParam;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Description: weight
 * @Author: jeecg-boot
 * @Date:   2020-04-13
 * @Version: V1.0
 */
public interface IWeightService extends IService<Weight> {
    void exchangeWeight(WeightDTO weightDTO);

    void edit(Weight weight);

}
