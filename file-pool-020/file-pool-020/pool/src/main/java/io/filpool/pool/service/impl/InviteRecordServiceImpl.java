package io.filpool.pool.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.filpool.pool.controller.admin.SysUtilController;
import io.filpool.pool.entity.InviteRecord;
import io.filpool.pool.entity.User;
import io.filpool.pool.mapper.InviteRecordMapper;
import io.filpool.pool.mapper.RewardRecordMapper;
import io.filpool.pool.request.InviteRecordRequest;
import io.filpool.pool.service.InviteRecordService;
import io.filpool.pool.param.InviteRecordPageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.filpool.framework.common.service.impl.BaseServiceImpl;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.pagination.PageInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.filpool.pool.service.UserService;
import io.filpool.pool.vo.RewardDescVo;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 邀请关系表 服务实现类
 *
 * @author filpool
 * @since 2021-03-02
 */
@Slf4j
@Service
public class InviteRecordServiceImpl extends BaseServiceImpl<InviteRecordMapper, InviteRecord> implements InviteRecordService {

    @Autowired
    private InviteRecordMapper inviteRecordMapper;

    @Autowired
    private SysUtilController sysUtilController;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RewardRecordMapper rewardRecordMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveInviteRecord(InviteRecord inviteRecord) throws Exception {
        return super.save(inviteRecord);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateInviteRecord(InviteRecord inviteRecord) throws Exception {
        return super.updateById(inviteRecord);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteInviteRecord(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<InviteRecord> getInviteRecordPageList(InviteRecordPageParam inviteRecordPageParam) throws Exception {
        Page<InviteRecord> page = new PageInfo<>(inviteRecordPageParam, OrderItem.desc(getLambdaColumn(InviteRecord::getCreateTime)));
        LambdaQueryWrapper<InviteRecord> wrapper = new LambdaQueryWrapper<>();
        //根据用户账号查询出用户id并过滤
        if (inviteRecordPageParam.getAccount()!=null){
            wrapper.eq(InviteRecord::getUserId,sysUtilController.queryUserId(inviteRecordPageParam.getAccount()));
        }
        IPage<InviteRecord> iPage = inviteRecordMapper.selectPage(page, wrapper);
        //初始化
        Integer DirectPush =0;
        Integer Interposition =0;
        BigDecimal fyUsdt =BigDecimal.ZERO;
        BigDecimal fyTb =BigDecimal.ZERO;

        for (InviteRecord inviteRecord:iPage.getRecords()) {
            //用户账号
            if (inviteRecord.getUserId()!=null){
                String account = sysUtilController.queryAccount(inviteRecord.getUserId());
                inviteRecord.setAccount(account);
            }

            //邀请人账号
            if (inviteRecord.getInviteUserId()!=null){
                String account = sysUtilController.queryAccount(inviteRecord.getInviteUserId());
                inviteRecord.setSuperiorAccount(account);
            }

            User one = userService.lambdaQuery().eq(User::getId, inviteRecord.getUserId()).one();
            if (ObjectUtils.isNotEmpty(one)){
                inviteRecord.setInviteCode(one.getInviteCode());
            }
            //统计直推用户个数
             DirectPush = inviteRecordMapper.countInviteOneNumber(inviteRecord.getUserId());
            inviteRecord.setDirectPush(DirectPush);

            //统计间推用户个数
             Interposition = inviteRecordMapper.countInviteTowNumber(inviteRecord.getUserId());
             inviteRecord.setInterposition(Interposition);

             //返佣金额奖励（USDT）
             fyUsdt = rewardRecordMapper.sumTotalAmount(inviteRecord.getUserId());
             inviteRecord.setFyUsdt(fyUsdt);

            //返佣算力奖励（TB）FIL
             fyTb = rewardRecordMapper.sumTotalPower(inviteRecord.getUserId(),"FIL");
            inviteRecord.setFyTbFil(fyTb);

            //返佣算力奖励（TB）XCH
            BigDecimal fyTbXch = rewardRecordMapper.sumTotalPower(inviteRecord.getUserId(),"XCH");
            inviteRecord.setFyTbXch(fyTbXch);

            //BZZ返佣接奖励（节点）
            BigDecimal fyTbBzz = rewardRecordMapper.sumTotalPower(inviteRecord.getUserId(),"BZZ");
            inviteRecord.setFyTbBzz(fyTbBzz);
        }
        return new Paging<InviteRecord>(iPage);
    }

    @Override
    public Paging<RewardDescVo> getInviteRecordDesc(InviteRecordPageParam inviteRecordPageParam) {
        if (inviteRecordPageParam.getGoodType()!=null){
            if (inviteRecordPageParam.getGoodType()<=3){
                inviteRecordPageParam.setType(1);
            }
        }

        if (inviteRecordPageParam.getGoodType()!=null){
            if (inviteRecordPageParam.getGoodType()>3){
                if (inviteRecordPageParam.getGoodType()==4){
                    inviteRecordPageParam.setGoodType(1);
                }
                if (inviteRecordPageParam.getGoodType()==5){
                    inviteRecordPageParam.setGoodType(2);
                }
                if (inviteRecordPageParam.getGoodType()==6){
                    inviteRecordPageParam.setGoodType(3);
                }
                inviteRecordPageParam.setType(2);
            }
        }

        List<RewardDescVo> rewardDescVos = inviteRecordMapper.queryRewardDescFour(inviteRecordPageParam);
        for (RewardDescVo rewardDescVo:rewardDescVos) {
            if (rewardDescVo.getType()==2){
                if (rewardDescVo.getGoodType()==1){
                    //4：云算力补单返佣
                    rewardDescVo.setGoodType(4);
                }
                if (rewardDescVo.getGoodType()==2){
                    //5:矿机补单返佣
                    rewardDescVo.setGoodType(5);
                }
                if (rewardDescVo.getGoodType()==3){
                    //集群补单返佣
                    rewardDescVo.setGoodType(6);
                }
            }
        }

        IPage<RewardDescVo> iPage = new Page<>();
        //查询总共有多少条记录
        List<RewardDescVo> queryRewardDescFourTotal = inviteRecordMapper.queryRewardDescFourTotal(inviteRecordPageParam);
        iPage.setTotal(queryRewardDescFourTotal.size());
        iPage.setRecords(rewardDescVos);
        return new Paging<RewardDescVo>(iPage);
    }

    @Override
    public Paging<InviteRecord> getInviteRecordDescPageList(InviteRecordRequest inviteRecordRequest) {
        Long pageIndex = inviteRecordRequest.getPageIndex();
        Long pageSize = inviteRecordRequest.getPageSize();
        inviteRecordRequest.setPageIndex((pageIndex-1)*pageSize);

        //查询直推间推的用户
        List<InviteRecord> inviteRecords = inviteRecordMapper.queryInviteRecord(inviteRecordRequest);
        BigDecimal fyUsdt =BigDecimal.ZERO;
        BigDecimal fyTb =BigDecimal.ZERO;
        for (InviteRecord inviteRecord:inviteRecords) {
            if (inviteRecord.getUserId()!=null){
                String account = sysUtilController.queryAccount(inviteRecord.getUserId());
                inviteRecord.setAccount(account);
            }
            //返佣金额奖励（USDT）
            fyUsdt = rewardRecordMapper.sumSuperiorTotalAmount(inviteRecord.getUserId(),inviteRecordRequest.getId());
            inviteRecord.setFyUsdt(fyUsdt);

            //返佣算力奖励（TB）
            fyTb = rewardRecordMapper.sumSuperiorTotalPower(inviteRecord.getUserId(),inviteRecordRequest.getId());
            inviteRecord.setFyTbFil(fyTb);

            //返佣算力奖励（TB）XCH
            BigDecimal fyTbXch = rewardRecordMapper.sumTotalPower(inviteRecord.getUserId(),"XCH");
            inviteRecord.setFyTbXch(fyTbXch);

            //返佣算力奖励（TB）BZZ
            BigDecimal fyTbBzz = rewardRecordMapper.sumTotalPower(inviteRecord.getUserId(),"BZZ");
            inviteRecord.setFyTbBzz(fyTbBzz);

        }
        Page<InviteRecord> objectPage = new Page<>();

        objectPage.setTotal(inviteRecords.size());
        objectPage.setRecords(inviteRecords);
        return new Paging<>(objectPage);
    }

}
