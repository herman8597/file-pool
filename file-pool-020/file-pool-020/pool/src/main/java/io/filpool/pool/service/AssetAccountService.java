package io.filpool.pool.service;

import io.filpool.framework.common.exception.FILPoolException;
import io.filpool.pool.entity.AssetAccount;
import io.filpool.pool.entity.AssetAccountLog;
import io.filpool.pool.param.AssetAccountPageParam;
import io.filpool.framework.common.service.BaseService;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.pool.vo.AssetAccountExchangeVo;
import io.filpool.pool.vo.AssetAccountVo;

/**
 * 账户资产表 服务类
 *
 * @author filpool
 * @since 2021-03-10
 */
public interface AssetAccountService extends BaseService<AssetAccount> {

    /**
     * 保存
     *
     * @param assetAccount
     * @return
     * @throws Exception
     */
    boolean saveAssetAccount(AssetAccount assetAccount) throws Exception;

    /**
     * 修改
     *
     * @param assetAccount
     * @return
     * @throws Exception
     */
    boolean updateAssetAccount(AssetAccount assetAccount) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteAssetAccount(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param assetAccountQueryParam
     * @return
     * @throws Exception
     */
    Paging<AssetAccount> getAssetAccountPageList(AssetAccountPageParam assetAccountPageParam) throws Exception;

    /**
     * 生成账户
     * @return
     * @throws Exception
     * @param userId
     */
    Boolean checkAccount(Long userId) throws Exception;

    /**
     * 获得单项资产
     * */
    AssetAccountVo.AccountVo getAccount(Long currencyId) throws Exception;

    /**
     * 获得资产列表
     * */
    AssetAccountVo getAccountAssets(Long userId) throws Exception;

    /**
     * 修改资产
     *
     * @param assetAccountLog*/
    Boolean exchangeAccount(AssetAccountExchangeVo assetAccountLog) throws FILPoolException;
}
