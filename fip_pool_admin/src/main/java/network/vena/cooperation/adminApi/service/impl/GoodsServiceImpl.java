package network.vena.cooperation.adminApi.service.impl;

import network.vena.cooperation.adminApi.entity.Goods;
import network.vena.cooperation.adminApi.mapper.GoodsMapper;
import network.vena.cooperation.adminApi.service.IGoodsService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: goods
 * @Author: jeecg-boot
 * @Date: 2020-04-13
 * @Version: V1.0
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Override
    public void editeStatus(String id, Integer status) {
        Goods goods = lambdaQuery().eq(Goods::getId, id).one();
        goods.setStatus(status);
        saveOrUpdate(goods);
    }
}
