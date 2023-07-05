package io.filpool.pool.service;

import io.filpool.pool.entity.Goods;
import io.filpool.pool.param.GoodsPageParam;
import io.filpool.framework.common.service.BaseService;
import io.filpool.framework.core.pagination.Paging;

/**
 * 商品表 服务类
 *
 * @author filpool
 * @since 2021-03-08
 */
public interface GoodsService extends BaseService<Goods> {

    /**
     * 保存
     *
     * @param goods
     * @return
     * @throws Exception
     */
    boolean saveGoods(Goods goods) throws Exception;

    /**
     * 修改
     *
     * @param goods
     * @return
     * @throws Exception
     */
    boolean updateGoods(Goods goods) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteGoods(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param goodsQueryParam
     * @return
     * @throws Exception
     */
    Paging<Goods> getGoodsPageList(GoodsPageParam goodsPageParam) throws Exception;

    boolean saveOrUpdateGood(Goods goods) throws Exception;

//    Paging<Goods> getGoodsMinerPageList(GoodsPageParam goodsPageParam);
}
