package io.filpool.pool.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.filpool.framework.common.exception.FILPoolException;
import io.filpool.pool.entity.*;
import io.filpool.pool.mapper.PowerOrderMapper;
import io.filpool.pool.mapper.ReturnedRewardConfigMapper;
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
import io.filpool.pool.util.BigDecimalUtil;
import io.filpool.pool.util.SecurityUtil;
import io.filpool.pool.vo.RewardRecordVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;

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
    @Autowired
    private PowerOrderMapper powerOrderMapper;
    @Autowired
    private ReturnedRewardConfigService returnedRewardConfigService;
    @Autowired
    private ReturnedRewardConfigMapper returnedRewardConfigMapper;
    @Autowired
    private CommunityRewardLogService communityRewardLogService;
    @Autowired
    private AssetAccountService assetAccountService;

    @Autowired
    private CurrencyConfigService currencyConfigService;

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
        InviteRecord inviteOne = inviteRecordService.lambdaQuery().eq(InviteRecord::getUserId, order.getUserId()).one();
        User oneUser = userService.getById(inviteOne.getInviteUserId());
        orderInviteReward(oneUser, true, order);
        //间推
        InviteRecord inviteTowRecord = inviteRecordService.lambdaQuery().eq(InviteRecord::getUserId, inviteOne.getInviteUserId()).one();
        if (inviteTowRecord != null && inviteTowRecord.getInviteUserId() != null) {
            User twoUser = userService.getById(inviteTowRecord.getInviteUserId());
            orderInviteReward(twoUser, false, order);
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
            case 4:
                //节点
            case 2:
                //增加经验值
                CurrencyConfig expRateConfig = currencyConfigService.lambdaQuery().eq(CurrencyConfig::getCurrencyId, order.getPowerCurrencyId())
                        .eq(CurrencyConfig::getType, 1).one();
                //经验值比例
                BigDecimal exp = order.getTotalPower().multiply(BigDecimal.ONE.divide(expRateConfig.getAmount(), 8, BigDecimal.ROUND_DOWN));
                boolean update = userService.lambdaUpdate().eq(User::getId, user.getId())
                        .set(User::getExperience, user.getExperience().add(exp)).update();
                if (!update) {
                    //更新失败，抛异常回滚
                    throw new RuntimeException("更新用户经验值失败");
                }
                //云算力
                if (isOne) {
                    usdtRate = order.getGoodType() == 2 ? config.getPowerOneUsdtRate() : config.getBzzOneUsdtRate();
                    powerRate = order.getGoodType() == 2 ? config.getPowerOnePowerRate() : config.getBzzOnePowerRate();
                } else {
                    usdtRate = order.getGoodType() == 2 ? config.getPowerTwoUsdtRate() : config.getBzzTwoUsdtRate();
                    powerRate = order.getGoodType() == 2 ? config.getPowerTwoPowerRate() : config.getBzzTwoPowerRate();
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
            //购买算力跟Bzz都会奖励算力
            if (order.getGoodType() == 2 || order.getGoodType() == 4) {
                //算力奖励
                onePowerReward = order.getTotalPower().multiply(powerRate);
            }
            //奖励记录
            RewardRecord rewardRecord = new RewardRecord().setUserId(user.getId())
                    .setType(1).setInviteType(isOne ? 1 : 2).setStatus(1)
                    .setUsdtAmount(oneUsdtReward)
                    .setPowerAmount(onePowerReward)
                    .setPowerSymbol(order.getPowerSymbol())
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
        CurrencyConfig expRateConfig = currencyConfigService.lambdaQuery().eq(CurrencyConfig::getCurrencyId,supplement.getPowerCurrencyId())
                .eq(CurrencyConfig::getType,1).one();
        //经验值比例
        BigDecimal exp = supplement.getTbSum().multiply(BigDecimal.ONE.divide(expRateConfig.getAmount(),8,BigDecimal.ROUND_DOWN));
        boolean update = userService.lambdaUpdate().eq(User::getId, user.getId())
                .set(User::getExperience, user.getExperience().add(exp)).update();
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
            case 4:
            case 1:
                //云算力
                if (isOne) {
                    usdtRate = supplement.getType() == 2 ? config.getPowerOneUsdtRate() : config.getBzzOneUsdtRate();
                    powerRate = supplement.getType() == 2 ? config.getPowerOnePowerRate() : config.getBzzOnePowerRate();
                } else {
                    usdtRate = supplement.getType() == 2 ? config.getPowerTwoUsdtRate() : config.getBzzTwoUsdtRate();
                    powerRate = supplement.getType() == 2 ? config.getPowerTwoPowerRate() : config.getBzzTwoPowerRate();
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
                    .setPowerSymbol(supplement.getPowerSymbol())
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
            boolean update = powerOrderService.addPowerOrder(rewardRecord.getUserId(), rewardRecord.getPowerAmount(), 4, rewardRecord.getId(), contractDays,rewardRecord.getPowerSymbol());
            if (!update) {
                //更新失败，抛异常回滚
                throw new RuntimeException("更新用户算力失败");
            }
        }
        if (rewardRecord.getUsdtAmount().compareTo(BigDecimal.ZERO) > 0) {
            //发放usdt奖励
            assetService.checkAccount(rewardRecord.getUserId());
            boolean update = assetService.lambdaUpdate().eq(AssetAccount::getUserId, rewardRecord.getUserId())
                    .eq(AssetAccount::getSymbol, "USDT")
                    .setSql("available = available + " + rewardRecord.getUsdtAmount().stripTrailingZeros().toPlainString())
                    .update();
            if (!update) {
                //更新失败，抛异常回滚
                throw new RuntimeException("更新用户余额失败");
            }
            AssetAccount oneAccount = assetService.lambdaQuery().eq(AssetAccount::getUserId, rewardRecord.getUserId())
                    .eq(AssetAccount::getSymbol, "USDT").one();
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
                        vo.setOrderType(order.getGoodType());
                    }
                } else {
                    Supplement supplement = supplementService.getById(record.getOrderId());
                    BeanUtils.copyProperties(record, vo);
                    if (supplement != null) {
                        vo.setOrderNumber(supplement.getId().toString());
                        vo.setOrderUsdtAmount(supplement.getAmountPrice());
                        vo.setOrderPowerAmount(supplement.getTbSum());
                        //补单商品类型相反
                        vo.setOrderType(supplement.getType()==1?2:1);
                    }
                }
                voList.add(vo);
            }
        }
        return voList;
    }

    /***
     * 社区奖励
     * @param order    订单信息
     * @param expPlus  经验值
     * @param rewardTypeTwo  奖励类型：1:购买，2：补单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void communityRewards(Order order,BigDecimal expPlus,Integer rewardTypeTwo) throws Exception {

        //初始化平级奖励（key:用户等级，value:平级奖励次数，当value==0的时候则该用户等级不能享受平级奖励）
        Map<Integer, Integer> LevelRewardMap = memberReward();

        //记录usdt给过的极差奖励集合
//        List<BigDecimal> usdtRangeList = new ArrayList();
        BigDecimal usdtRangeList=BigDecimal.ZERO;

        //记录算力给过的极差奖励集合
//        List<BigDecimal> powerRangeList = new ArrayList();
        BigDecimal powerRangeList = BigDecimal.ZERO;

        //邀请记录表中的所有数据信息
        List<InviteRecord> list = inviteRecordService.list();

        //用户存储祖宗用户的集合
        List<Map<Long, ReturnedRewardConfig>> ancestorUsersList = new ArrayList<>();

        Map<Long, Long> apiKeyMap = new HashMap<>();
        for (InviteRecord inviteRecord : list) {
            //将用户id和邀请人用户id存到集合中
            apiKeyMap.put(inviteRecord.getUserId(), inviteRecord.getInviteUserId());
        }
        //查询出该用户的所有祖宗（key:祖宗id，value:祖宗的社区奖励配置信息）
        ancestorUsersList = ancestorUsers(order.getUserId(), apiKeyMap, ancestorUsersList, order.getGoodType());

        //如果祖宗集合不为空，则开始进行如下奖励（极差、平级）
        if (ObjectUtil.isNotEmpty(ancestorUsersList)) {
            Integer index = 0;//迭代次数
            Integer level = 0;//上个用户的等级
            Long downUserId=null;
            for (Map<Long, ReturnedRewardConfig> item : ancestorUsersList) {
                index++;
                //初始化usdt极差奖励
                BigDecimal usdtRangeReward = BigDecimal.ZERO;
                //初始化算力极差奖励
                BigDecimal powerRangeReward = BigDecimal.ZERO;
                //初始化usdt平级奖励金额
                BigDecimal usdtLevelReward = BigDecimal.ZERO;
                //初始化power平级奖励金额
                BigDecimal powerLevelReward = BigDecimal.ZERO;
                //初始化usdt极差奖励比例
                BigDecimal usdtRangeRatio = BigDecimal.ZERO;
                //初始化power极差奖励比例
                BigDecimal powerRangeRatio = BigDecimal.ZERO;
                //初始化奖励类型(20极差，21平级)
                Integer rewardType = null;

                for (Map.Entry<Long, ReturnedRewardConfig> entry : item.entrySet()) {
                    //祖宗id
                    Long key = entry.getKey();
                    //祖宗的社区奖励配置信息
                    ReturnedRewardConfig value = entry.getValue();

                    //蒋朋曰：购买人的所有上级都获取与购买人相等的经验值
                    User user = userService.lambdaQuery().eq(User::getId, key).one();
                    user.setCommunityExperience(user.getCommunityExperience().add(expPlus));
                    userService.updateUser(user);
                    rewardType=20;

                    //第一个上级用户直接给奖励
                    if (index == 1) {
                        if (ObjectUtils.isEmpty(value)) {
                            continue;
                        }
                        //u极差奖励金额
                        usdtRangeReward = order.getTotalAmount().multiply(value.getUsdtRangeReward());
                        usdtRangeList=value.getUsdtRangeReward();

                        //购买矿机不奖励算力
                        if (order.getGoodType() != 1) {
                            //power极差奖励金额
                            powerRangeReward = new BigDecimal(order.getQuantity()).multiply(value.getPowerRangeReward());
                            powerRangeList=value.getPowerRangeReward();
                        }
                    } else {
                        //从第二个上级开始判断是给极差还是平级
                        if (ObjectUtils.isEmpty(value)) {
                            continue;
                        }
                        //获取当前用户等级
                        Integer currentLevel = value.getLevel();
                        if (currentLevel < level) {
                            //如果当前等级小于上一个用户等级，则跳出当前循环不给奖励
                            continue;
                        }
                        //如果当前等级与上一个用户等级相等，则给平级奖励
                        if (currentLevel == level && LevelRewardMap.get(currentLevel) != 0) {
                            //u平级奖励金额
                            usdtLevelReward = order.getTotalAmount().multiply(value.getUsdtLevelReward());
                            //购买矿机不奖励算力
                            if (order.getGoodType()!=1){
                                //power平级奖励金额
                                powerLevelReward = new BigDecimal(order.getQuantity()).multiply(value.getPowerLevelReward());
                            }

                            //将该等级的平级奖励map改成0，0后该等级就不能再进行平级奖励了
                            if (usdtLevelReward.compareTo(BigDecimal.ZERO) > 0 || powerLevelReward.compareTo(BigDecimal.ZERO) > 0) {
                                //如果奖励金额大于0则修改
                                LevelRewardMap.put(currentLevel, 0);
                            }
                            rewardType=AccountLogType.TYPE_REWARD_LEVEL_SAME;
                        }
                        //如果当前等级>上一个用户等级，则给极差奖励
                        if (currentLevel > level) {
                            if (value.getUsdtRangeReward().compareTo(usdtRangeList) > 0) {
                                //u极差奖励=订单u金额*（自身极差比例-之前极差累计之和）
//                                usdtRangeReward = order.getTotalAmount().multiply(BigDecimalUtil.sub(value.getUsdtRangeReward(), usdtRangeRatio));
                                usdtRangeReward = order.getTotalAmount().multiply(BigDecimalUtil.sub(value.getUsdtRangeReward(),usdtRangeList));
                                //将当前用户的极差比例存到usdt已发放的极差奖励中
                                usdtRangeList=value.getUsdtRangeReward();
                            }

                            //购买矿机不奖励算力
                            if (order.getGoodType()!=1){
                                //如果存储算力极差的集合中不为空，则将以前所有发过的极差比例相加
                                if (value.getPowerRangeReward().compareTo(powerRangeList) > 0) {
                                    //算力极差奖励=订单算力*（自身算力比例-之前算力累计之和）
                                    powerRangeReward = new BigDecimal(order.getQuantity()).multiply(BigDecimalUtil.sub(value.getPowerRangeReward(), powerRangeList));
                                    //将当前用户的极差比例存到usdt已发放的极差奖励中
                                    powerRangeList = value.getPowerRangeReward();
                                }
                            }
                            rewardType=AccountLogType.TYPE_REWARD_LEVEL_DIFF;
                        }
                    }

                    //存储奖励信息
                    if (usdtRangeReward.compareTo(BigDecimal.ZERO)>0 || usdtLevelReward.compareTo(BigDecimal.ZERO)>0
                            || powerRangeReward.compareTo(BigDecimal.ZERO)>0 || powerLevelReward.compareTo(BigDecimal.ZERO)>0){

                        //社区(市场)奖励记录表
                        CommunityRewardLog communityRewardLog = new CommunityRewardLog();
                        communityRewardLog.setBuyerUserId(order.getUserId());
                        communityRewardLog.setRewardUserId(key);
                        communityRewardLog.setRewardUserLevel(value.getLevel());
                        if (index==1){
                            //第一个人拿奖励的人的下级则是购买人
                            User one = userService.lambdaQuery().eq(User::getId, order.getUserId()).one();
                            //根据积分查询购买人的社区等级
                            ReturnedRewardConfig returnedRewardConfigList = returnedRewardConfigMapper.getConfigByPower(one.getCommunityExperience(), order.getGoodType());

                            communityRewardLog.setDownUserId(order.getUserId());
                            if (ObjectUtils.isNotEmpty(returnedRewardConfigList)){
                                communityRewardLog.setDownUserLevel(returnedRewardConfigList.getLevel());
                            }else{
                                communityRewardLog.setDownUserLevel(0);
                            }
                        }else{
                            communityRewardLog.setDownUserId(downUserId);
                            communityRewardLog.setDownUserLevel(level);
                        }
                        communityRewardLog.setUsdtRangeReward(usdtRangeReward);
                        communityRewardLog.setUsdtLevelLeward(usdtLevelReward);
                        communityRewardLog.setPowerSymbol(order.getPowerSymbol());
                        communityRewardLog.setPowerLevelReward(powerLevelReward);
                        communityRewardLog.setPowerRangeReward(powerRangeReward);
                        communityRewardLog.setCreateTime(new Date());
                        communityRewardLog.setUpdateTime(new Date());
                        communityRewardLog.setOrderNumber(order.getOrderNumber());
                        communityRewardLog.setRewardType(rewardTypeTwo);
                        communityRewardLogService.save(communityRewardLog);

                        Calendar calendar = Calendar.getInstance();
                        //设置失效时间  购买时间第二天+contractDays
                        calendar.add(Calendar.DATE, order.getContractDays()+ 1);
                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                        calendar.set(Calendar.MINUTE, 0);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);

                        //算力订单表,购买矿机不需要存记录
                        if (order.getGoodType()!=1){
                            PowerOrder powerOrder = new PowerOrder();
                            powerOrder.setUserId(key);
                            powerOrder.setType(6);
                            powerOrder.setRecordId(communityRewardLog.getId());
                            powerOrder.setIsEffect(true);
                            powerOrder.setCreateTime(new Date());
                            powerOrder.setInvalidTime(calendar.getTime());
                            powerOrder.setPowerSymbol(order.getPowerSymbol());
                            if (powerRangeReward.compareTo(BigDecimal.ZERO)>0){
                                powerOrder.setAmount(powerRangeReward);
                            }else{
                                powerOrder.setAmount(powerLevelReward);
                            }
                            powerOrderService.save(powerOrder);
                        }

                        //修改用户资产表中的资产信息
                        AssetAccount one = assetAccountService.lambdaQuery().eq(AssetAccount::getUserId, key).eq(AssetAccount::getSymbol,"USDT").one();
                        one.setUpdateTime(new Date());
                        if (usdtRangeReward.compareTo(BigDecimal.ZERO)>0){
                            one.setAvailable(one.getAvailable().add(usdtRangeReward));
                        }else{
                            one.setAvailable(one.getAvailable().add(usdtLevelReward));
                        }
                        assetAccountService.saveOrUpdate(one);

                        //账户资产变化表
                        AssetAccountLog assetAccountLog = new AssetAccountLog();
                        assetAccountLog.setCreateTime(new Date());
                        if (usdtRangeReward.compareTo(BigDecimal.ZERO)>0){
                            assetAccountLog.setOperationAmount(usdtRangeReward);
                            assetAccountLog.setAvailable(one.getAvailable());
                            assetAccountLog.setRemark("社区USDT极差奖励");
                        }else{
                            assetAccountLog.setOperationAmount(usdtLevelReward);
                            assetAccountLog.setAvailable(one.getAvailable());
                            assetAccountLog.setRemark("社区USDT平级奖励");
                        }
                        assetAccountLog.setFrozen(one.getFrozen());
                        assetAccountLog.setUserId(one.getUserId());
                        assetAccountLog.setAssetAccountId(one.getId());
                        assetAccountLog.setType(rewardType);
                        assetAccountLog.setPledge(one.getPledge());
                        assetAccountLog.setRecordId(communityRewardLog.getId());
                        assetAccountLogService.saveOrUpdate(assetAccountLog);
                    }
                    //存储上一个用户的id和社区等级
                    downUserId=key;
                    level = value.getLevel();
                }
            }
        }
        return;
    }

    public void exect(RewardRecord rewardRecord,String type) throws Exception {
        if (rewardRecord.getUsdtAmount().compareTo(BigDecimal.ZERO) > 0){
            //发放usdt奖励
            assetService.checkAccount(rewardRecord.getUserId());
            //查询用户资产
            AssetAccount oneAccount = assetService.lambdaQuery().eq(AssetAccount::getUserId, rewardRecord.getUserId())
                    .eq(AssetAccount::getSymbol, "USDT").one();
            //将u奖励存到资产中
            oneAccount.setAvailable(oneAccount.getAvailable().add(rewardRecord.getUsdtAmount()));
            boolean update = assetService.updateAssetAccount(oneAccount);
            if (!update) {
                //更新失败，抛异常回滚
                throw new RuntimeException("更新用户余额失败");
            }
            //资产流水
            assetAccountLogService.saveLog(oneAccount, rewardRecord.getUsdtAmount(), Integer.parseInt(type), rewardRecord.getRemark(), rewardRecord.getId());
        }
    }


    //查询某个用户的所有祖宗
    public List ancestorUsers(Long apikey, Map<Long, Long> apiKeyMap, List<Map<Long, ReturnedRewardConfig>> ancestorUsersList, Integer goodsType) {
        Long v = apiKeyMap.get(apikey);
        if (ObjectUtil.isNotEmpty(v)) {
            //查询用户积分
            User one = userService.lambdaQuery().eq(User::getId, v).one();
            //根据积分查询用户等级
            ReturnedRewardConfig returnedRewardConfigList = returnedRewardConfigMapper.getConfigByPower(one.getCommunityExperience(), goodsType);
            Map<Long, ReturnedRewardConfig> userLevel$Algebra = new HashMap<>();
            //将用户id和用户等级存到map集合中
            if (ObjectUtil.isNotEmpty(returnedRewardConfigList)) {
                userLevel$Algebra.put(one.getId(), returnedRewardConfigList);
            } else {
                userLevel$Algebra.put(one.getId(), null);
            }
            ancestorUsersList.add(userLevel$Algebra);
            return ancestorUsers(v, apiKeyMap, ancestorUsersList, goodsType);
        }
        return ancestorUsersList;
    }


    //对每种会员类型的平级奖励进行初始化
    public Map<Integer, Integer> memberReward() {
        Map<Integer, Integer> memberMap = new HashMap<>();
        memberMap.put(1, 1);
        memberMap.put(2, 1);
        memberMap.put(3, 1);
        memberMap.put(4, 1);
        memberMap.put(5, 1);
        return memberMap;
    }
}
