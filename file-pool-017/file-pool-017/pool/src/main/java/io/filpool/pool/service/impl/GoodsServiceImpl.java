package io.filpool.pool.service.impl;

import io.filpool.framework.common.exception.FILPoolException;
import io.filpool.pool.entity.Goods;
import io.filpool.pool.entity.GoodsImage;
import io.filpool.pool.mapper.GoodsMapper;
import io.filpool.pool.service.GoodsService;
import io.filpool.pool.param.GoodsPageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.filpool.framework.common.service.impl.BaseServiceImpl;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.pagination.PageInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * 商品表 服务实现类
 *
 * @author filpool
 * @since 2021-03-08
 */
@Slf4j
@Service
public class GoodsServiceImpl extends BaseServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveGoods(Goods goods) throws Exception {
        return super.save(goods);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateGoods(Goods goods) throws Exception {
        return super.updateById(goods);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteGoods(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<Goods> getGoodsPageList(GoodsPageParam goodsPageParam) throws Exception {
        Page<Goods> page = new PageInfo<>(goodsPageParam, OrderItem.desc(getLambdaColumn(Goods::getCreateTime)));
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(goodsPageParam.getName())){
            wrapper.eq(Goods::getName,goodsPageParam.getName());
        }
        if (goodsPageParam.getType()!=null){
            wrapper.eq(Goods::getType,goodsPageParam.getType());
        }
        if (goodsPageParam.getStatus()!=null){
            wrapper.eq(Goods::getStatus,goodsPageParam.getStatus());
        }
        if (goodsPageParam.getStartTime() != null) {
            wrapper.ge(Goods::getCreateTime, goodsPageParam.getStartTime());
        }
        if (goodsPageParam.getEndTime() != null) {
            wrapper.le(Goods::getCreateTime, goodsPageParam.getEndTime());
        }
        IPage<Goods> iPage = goodsMapper.selectPage(page, wrapper);
        return new Paging<Goods>(iPage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateGood(Goods goods) throws Exception {
        Date date = new Date();
        if (goods.getId() != null){
            //修改商品
            Goods byId = getById(goods.getId());
            BeanUtils.copyProperties(goods,byId);
            goods.setUpdateTime(date);
            updateGoods(goods);
        }else{
            //新增商品
            goods.setCreateTime(date);
            goods.setUpdateTime(date);
            saveGoods(goods);
        }
        return true;
    }
/*
    @Override
    public Paging<Goods> getGoodsMinerPageList(GoodsPageParam goodsPageParam) {
        Page<Goods> page = new PageInfo<>(goodsPageParam, OrderItem.desc(getLambdaColumn(Goods::getCreateTime)));
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Goods::getType,1);
        IPage<Goods> iPage = goodsMapper.selectPage(page, wrapper);
        return new Paging<Goods>(iPage);
    }*/
}
