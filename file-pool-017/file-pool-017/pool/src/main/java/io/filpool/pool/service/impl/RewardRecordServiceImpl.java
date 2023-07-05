package io.filpool.pool.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.filpool.framework.common.exception.FILPoolException;
import io.filpool.pool.entity.*;
import io.filpool.pool.mapper.RewardRecordMapper;
import io.filpool.pool.request.PageRequest;
import io.filpool.pool.service.*;
import io.filpool.pool.param.RewardRecordPageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.filpool.framework.common.service.impl.BaseServiceImpl;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.pagination.PageInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.filpool.pool.util.AccountLogType;
import io.filpool.pool.util.SecurityUtil;
import io.filpool.pool.vo.RewardRecordVo;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单奖励记录 服务实现类
 *
 * @author filpool
 * @since 2021-03-12
 */
@Slf4j
@Service
public class RewardRecordServiceImpl extends BaseServiceImpl<RewardRecordMapper, RewardRecord> implements RewardRecordService {
    @Autowired
    private RewardRecordMapper rewardRecordMapper;
    @Autowired
    private RewardConfigServiceImpl configService;
    @Autowired
    private AssetAccountService assetService;
    @Autowired
    private AssetAccountLogService assetAccountLogService;
    @Autowired
    private InviteRecordService inviteRecordService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private PowerOrderService powerOrderService;
    @Autowired
    private SupplementService supplementService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveRewardRecord(RewardRecord rewardRecord) throws Exception {
        return super.save(rewardRecord);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateRewardRecord(RewardRecord rewardRecord) throws Exception {
        return super.updateById(rewardRecord);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteRewardRecord(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<RewardRecord> getRewardRecordPageList(RewardRecordPageParam rewardRecordPageParam) throws Exception {
        Page<RewardRecord> page = new PageInfo<>(rewardRecordPageParam, OrderItem.desc(getLambdaColumn(RewardRecord::getCreateTime)));
        LambdaQueryWrapper<RewardRecord> wrapper = new LambdaQueryWrapper<>();
        IPage<RewardRecord> iPage = rewardRecordMapper.selectPage(page, wrapper);
        return new Paging<RewardRecord>(iPage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean orderReward(Order order) throws Exception {
       //直推
        InviteRecord inviteOne = inviteRecordService.lambdaQuery().eq(InviteRecord::getUserId, order.getUserId()).one();
        User oneUser = userService.getById(inviteOne.getInviteUserId());
        //查询父级用户有没有购买过商品
        List<PowerOrder> list = powerOrderService.lambdaQuery().eq(PowerOrder::getUserId, oneUser.getId()).list();
        if (ObjectUtil.isNotEmpty(list)){
            //如果list不等于空则购买过，可以给奖励
            orderInviteReward(oneUser, true, order);
        }

        //间推
        InviteRecord inviteTowRecord = inviteRecordService.lambdaQuery().eq(InviteRecord::getUserId, inviteOne.getInviteUserId()).one();
        if (inviteTowRecord != null && inviteTowRecord.getInviteUserId() != null) {
            User twoUser = userService.getById(inviteTowRecord.getInviteUserId());
            //查询祖父级用户有没有购买过商品
            List<PowerOrder> listTwo = powerOrderService.lambdaQuery().eq(PowerOrder::getUserId, twoUser.getId()).list();
            if (ObjectUtil.isNotEmpty(listTwo)){
                //如果list不等于空则购买过，可以给奖励
                orderInviteReward(twoUser, false, order);
            }
        }
        return true;
    }

    /**
     * 订单邀请奖励
     *
     * @param isOne 是否直推
     */
    public void orderInviteReward(User user, Boolean isOne, Order order) throws Exception {
        Date now = new Date();
        //直推
        RewardConfig config = configService.getBaseMapper().getConfigByPower(user.getExperience());
        if (config == null) {
            throw new FILPoolException("config.not-exits");
        }
        BigDecimal usdtRate = BigDecimal.ZERO;
        BigDecimal powerRate = BigDecimal.ZERO;
        switch (order.getGoodType()) {
            case 1:
                //矿机
                if (isOne) {
                    usdtRate = config.getMinerOneUsdtRate();
                    powerRate = config.getMinerOnePowerRate();
                } else {
                    usdtRate = config.getMinerTwoUsdtRate();
                    powerRate = config.getMinerTwoPowerRate();
                }
                break;
            case 2:
                //增加经验值
                boolean update = userService.lambdaUpdate().eq(User::getId, user.getId())
                        .set(User::getExperience, user.getExperience().add(order.getTotalPower())).update();
                if (!update) {
                    //更新失败，抛异常回滚
                    throw new RuntimeException("更新用户经验值失败");
                }
                //云算力
                if (isOne) {
                    usdtRate = config.getPowerOneUsdtRate();
                    powerRate = config.getPowerOnePowerRate();
                } else {
                    usdtRate = config.getPowerTwoUsdtRate();
                    powerRate = config.getPowerTwoPowerRate();
                }
                break;
            case 3:
                //矿机集群
                if (isOne) {
                    usdtRate = config.getGroupOneUsdtRate();
                    powerRate = config.getGroupOnePowerRate();
                } else {
                    usdtRate = config.getGroupTwoUsdtRate();
                    powerRate = config.getGroupTwoPowerRate();
                }
                break;
        }
        if (usdtRate.compareTo(BigDecimal.ZERO) > 0 || powerRate.compareTo(BigDecimal.ZERO) > 0) {
            //需发放奖励
            BigDecimal oneUsdtReward = order.getTotalAmount().multiply(usdtRate);
            BigDecimal onePowerReward = BigDecimal.ZERO;
            if (order.getGoodType() == 2) {
                //算力奖励
                onePowerReward = order.getTotalPower().multiply(powerRate);
            }
            //奖励记录
            RewardRecord rewardRecord = new RewardRecord().setUserId(user.getId())
                    .setType(1).setInviteType(isOne ? 1 : 2).setStatus(1)
                    .setUsdtAmount(oneUsdtReward)
                    .setPowerAmount(onePowerReward)
                    .setDownUserId(order.getUserId())
                    .setRemark(isOne ? "直推购买奖励" : "间推购买奖励")
                    .setUpdateTime(now)
                    .setCreateTime(now)
                    .setOrderId(order.getId());
            saveRewardRecord(rewardRecord);
            exect(rewardRecord, order.getContractDays());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean supplementReward(Supplement supplement) throws Exception {
        InviteRecord inviteOne = inviteRecordService.lambdaQuery().eq(InviteRecord::getUserId, supplement.getUId()).one();
        User oneUser = userService.getById(inviteOne.getInviteUserId());
        supplementInviteReward(oneUser, true, supplement);
        //间推
        InviteRecord inviteTowRecord = inviteRecordService.lambdaQuery().eq(InviteRecord::getUserId, inviteOne.getInviteUserId()).one();
        if (inviteTowRecord != null && inviteTowRecord.getInviteUserId() != null) {
            User twoUser = userService.getById(inviteTowRecord.getInviteUserId());
            supplementInviteReward(twoUser, false, supplement);
        }
        return true;
    }

    /**
     * 补单邀请奖励
     *
     * @param isOne 是否直推
     */
    public void supplementInviteReward(User user, Boolean isOne, Supplement supplement) throws Exception {
        Date now = new Date();
        //增加经验值
        boolean update = userService.lambdaUpdate().eq(User::getId, user.getId())
                .set(User::getExperience, user.getExperience().add(supplement.getTbSum())).update();
        if (!update) {
            //更新失败，抛异常回滚
            throw new RuntimeException("更新用户经验值失败");
        }
        //直推
        RewardConfig config = configService.getBaseMapper().getConfigByPower(user.getExperience());
        if (config == null) {
            throw new FILPoolException("config.not-exits");
        }
        BigDecimal usdtRate = BigDecimal.ZERO;
        BigDecimal powerRate = BigDecimal.ZERO;
        switch (supplement.getType()) {
            case 1:
                //云算力
                if (isOne) {
                    usdtRate = config.getPowerOneUsdtRate();
                    powerRate = config.getPowerOnePowerRate();
                } else {
                    usdtRate = config.getPowerTwoUsdtRate();
                    powerRate = config.getPowerTwoPowerRate();
                }
                break;
            case 2:
                //矿机
                if (isOne) {
                    usdtRate = BigDecimal.ZERO;
                    powerRate = config.getMinerOnePowerRate();
                } else {
                    usdtRate = BigDecimal.ZERO;
                    powerRate = config.getMinerTwoPowerRate();
                }
                break;
            case 3:
                //矿机集群
                if (isOne) {
                    usdtRate = BigDecimal.ZERO;
                    powerRate = config.getGroupOnePowerRate();
                } else {
                    usdtRate = BigDecimal.ZERO;
                    powerRate = config.getGroupTwoPowerRate();
                }
                break;
        }
        if (usdtRate.compareTo(BigDecimal.ZERO) > 0 || powerRate.compareTo(BigDecimal.ZERO) > 0) {
            //需发放奖励
            BigDecimal usdtReward = supplement.getAmountPrice().multiply(usdtRate);
            //算力奖励
            BigDecimal powerReward = supplement.getTbSum().multiply(powerRate);
            //奖励记录
            RewardRecord rewardRecord = new RewardRecord().setUserId(user.getId())
                    .setType(2).setInviteType(isOne ? 1 : 2).setStatus(1)
                    .setUsdtAmount(usdtReward)
                    .setPowerAmount(powerReward)
                    .setDownUserId(supplement.getUId())
                    .setRemark(isOne ? "直推补单奖励" : "间推补单奖励")
                    .setUpdateTime(now)
                    .setCreateTime(now)
                    .setOrderId(supplement.getId());
            saveRewardRecord(rewardRecord);
            exect(rewardRecord, supplement.getContractDays());
        }
    }

    /**
     * 处理资产
     *
     * @param rewardRecord 奖励记录
     * @throws Exception
     */
    public void exect(RewardRecord rewardRecord, Integer contractDays) throws Exception {
        if (rewardRecord.getPowerAmount().compareTo(BigDecimal.ZERO) > 0) {
            //发放算力奖励
            boolean update = powerOrderService.addPowerOrder(rewardRecord.getUserId(), rewardRecord.getPowerAmount(), 4, rewardRecord.getId(), contractDays);
            if (!update) {
                //更新失败，抛异常回滚
                throw new RuntimeException("更新用户算力失败");
            }
        }
        if (rewardRecord.getUsdtAmount().compareTo(BigDecimal.ZERO) > 0){
            //发放usdt奖励
            assetService.checkAccount(rewardRecord.getUserId());
            AssetAccount oneAccount = assetService.lambdaQuery().eq(AssetAccount::getUserId, rewardRecord.getUserId())
                    .eq(AssetAccount::getSymbol, "USDT").one();
            oneAccount.setAvailable(oneAccount.getAvailable().add(rewardRecord.getUsdtAmount()));
            boolean update = assetService.updateAssetAccount(oneAccount);
            if (!update) {
                //更新失败，抛异常回滚
                throw new RuntimeException("更新用户余额失败");
            }
            //资产流水
            assetAccountLogService.saveLog(oneAccount, rewardRecord.getUsdtAmount(), AccountLogType.TYPE_INVITE_REWARD, rewardRecord.getInviteType() == 1 ? "直推购买奖励" : "间推购买奖励", rewardRecord.getId());
        }
    }

    @Override
    public List<RewardRecordVo> getRewardRecord(PageRequest request) throws Exception {
        User user = SecurityUtil.currentLogin();
        LambdaQueryWrapper<RewardRecord> wr = Wrappers.lambdaQuery(RewardRecord.class)
                .eq(RewardRecord::getUserId, user.getId())
                .eq(RewardRecord::getStatus, 1)
                .eq(RewardRecord::getType, 1)
                .orderByDesc(RewardRecord::getCreateTime);
        Page<RewardRecord> page = new Page<>(request.getPageIndex(), request.getPageSize());
        page = getBaseMapper().selectPage(page, wr);
        List<RewardRecordVo> voList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(page.getRecords())) {
            for (RewardRecord record : page.getRecords()) {
                RewardRecordVo vo = new RewardRecordVo();
                vo.setAccount(userService.getUserAccount(record.getDownUserId(), true));
                if (record.getType() == 1) {
                    Order order = orderService.getById(record.getOrderId());
                    BeanUtils.copyProperties(record, vo);
                    if (order != null) {
                        vo.setOrderNumber(order.getOrderNumber());
                        vo.setOrderUsdtAmount(order.getTotalAmount());
                        vo.setOrderPowerAmount(order.getTotalPower());
                    }
                } else {
                    Supplement supplement = supplementService.getById(record.getOrderId());
                    BeanUtils.copyProperties(record, vo);
                    if (supplement != null) {
                        vo.setOrderNumber(supplement.getId().toString());
                        vo.setOrderUsdtAmount(supplement.getAmountPrice());
                        vo.setOrderPowerAmount(supplement.getTbSum());
                    }
                }
                voList.add(vo);
            }
        }
        return voList;
    }
}
