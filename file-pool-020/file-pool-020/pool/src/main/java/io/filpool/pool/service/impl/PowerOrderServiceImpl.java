package io.filpool.pool.service.impl;

import io.filpool.framework.common.exception.FILPoolException;
import io.filpool.pool.entity.PowerOrder;
import io.filpool.pool.entity.RewardConfig;
import io.filpool.pool.entity.User;
import io.filpool.pool.mapper.PowerOrderMapper;
import io.filpool.pool.service.PowerOrderService;
import io.filpool.pool.param.PowerOrderPageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.filpool.framework.common.service.impl.BaseServiceImpl;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.pagination.PageInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.filpool.pool.service.UserService;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * 算力订单表 服务实现类
 *
 * @author filpool
 * @since 2021-04-01
 */
@Slf4j
@Service
public class PowerOrderServiceImpl extends BaseServiceImpl<PowerOrderMapper, PowerOrder> implements PowerOrderService {

    @Autowired
    private PowerOrderMapper powerOrderMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private RewardConfigServiceImpl configService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean savePowerOrder(PowerOrder powerOrder) throws Exception {
        return super.save(powerOrder);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updatePowerOrder(PowerOrder powerOrder) throws Exception {
        return super.updateById(powerOrder);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deletePowerOrder(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<PowerOrder> getPowerOrderPageList(PowerOrderPageParam powerOrderPageParam) throws Exception {
        Page<PowerOrder> page = new PageInfo<>(powerOrderPageParam, OrderItem.desc(getLambdaColumn(PowerOrder::getCreateTime)));
        LambdaQueryWrapper<PowerOrder> wrapper = new LambdaQueryWrapper<>();
        IPage<PowerOrder> iPage = powerOrderMapper.selectPage(page, wrapper);
        return new Paging<PowerOrder>(iPage);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addPowerOrder(Long userId, BigDecimal amount, int type, Long recordId, int contractDays,String symbol) throws Exception {
        Calendar calendar = Calendar.getInstance();
        PowerOrder order = new PowerOrder();
        order.setCreateTime(calendar.getTime());
        order.setUserId(userId);
        order.setAmount(amount);
        order.setPowerSymbol(symbol);
        order.setIsEffect(true);
        order.setRecordId(recordId);
        order.setType(type);
        //设置失效时间  购买时间第二天+contractDays
        calendar.add(Calendar.DATE, contractDays + 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        order.setInvalidTime(calendar.getTime());
//        log.info("createTime:{}  invalidTime:{}", order.getCreateTime(), order.getInvalidTime());
//        boolean update = savePowerOrder(order);
//        if (update){
//            updateUserLevel(userId);
//        }
        return savePowerOrder(order);
    }

//    @Transactional(rollbackFor = Exception.class)
//    @Override
//    public boolean updateUserLevel(Long userId) throws Exception {
//        //计算用户团队业绩
//        BigDecimal teamPower = baseMapper.sumTeamPower(userId);
//        log.info("teamPower:{}", teamPower);
//        RewardConfig rewardConfig = configService.getBaseMapper().getConfigByPower(teamPower);
//        if (rewardConfig == null) {
//            throw new FILPoolException("config.not-exits");
//        }
//        User user = userService.getById(userId);
//        //设置用户等级
//        user.setLevelId(rewardConfig.getLevel());
//        return userService.updateUser(user);
//    }
}
