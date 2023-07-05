package io.filpool.pool.service.impl;

import cn.hutool.core.util.ObjectUtil;
import io.filpool.pool.controller.admin.SysUtilController;
import io.filpool.pool.entity.AreaCode;
import io.filpool.pool.entity.RealNameRecord;
import io.filpool.pool.entity.User;
import io.filpool.pool.mapper.RealNameRecordMapper;
import io.filpool.pool.service.AreaCodeService;
import io.filpool.pool.service.RealNameRecordService;
import io.filpool.pool.param.RealNameRecordPageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.filpool.framework.common.service.impl.BaseServiceImpl;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.pagination.PageInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 实名认证信息 服务实现类
 *
 * @author filpool
 * @since 2021-03-02
 */
@Slf4j
@Service
public class RealNameRecordServiceImpl extends BaseServiceImpl<RealNameRecordMapper, RealNameRecord> implements RealNameRecordService {

    @Autowired
    private RealNameRecordMapper realNameRecordMapper;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AreaCodeService areaCodeService;

    @Autowired
    private SysUtilController sysUtilController;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveRealNameRecord(RealNameRecord realNameRecord) throws Exception {
        return super.save(realNameRecord);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateRealNameRecord(RealNameRecord realNameRecord) throws Exception {
        return super.updateById(realNameRecord);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteRealNameRecord(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<RealNameRecord> getRealNameRecordPageList(RealNameRecordPageParam realNameRecordPageParam) throws Exception {
        Page<RealNameRecord> page = new PageInfo<>(realNameRecordPageParam, OrderItem.desc(getLambdaColumn(RealNameRecord::getCreateTime)));
        LambdaQueryWrapper<RealNameRecord> wrapper = new LambdaQueryWrapper<>();
        //用户账号过滤
       /* String account = realNameRecordPageParam.getAccount();
        if (account!= null && StringUtils.isNotBlank(account)) {
            wrapper.inSql(RealNameRecord::getAreaCode,"select id from fil_user where mobile== '" + account + "'");
        }*/
        if (realNameRecordPageParam.getAccount()!=null && !StringUtils.isEmpty(realNameRecordPageParam.getAccount())){
            wrapper.eq(RealNameRecord::getUserId,sysUtilController.queryUserId(realNameRecordPageParam.getAccount()));
        }

        //真实姓名过滤
        if (realNameRecordPageParam.getRealName() != null) {
            wrapper.eq(RealNameRecord::getRealName, realNameRecordPageParam.getRealName());
        }
        //开始结束时间过滤
        if (realNameRecordPageParam.getStartDate() != null) {
            wrapper.ge(RealNameRecord::getCreateTime, realNameRecordPageParam.getStartDate());
        }
        if (realNameRecordPageParam.getEndDate() != null) {
            wrapper.le(RealNameRecord::getCreateTime, realNameRecordPageParam.getEndDate());
        }
        //过滤审核状态
        if (realNameRecordPageParam.getAuthStatus()!=null){
            wrapper.eq(RealNameRecord::getAuthStatus,realNameRecordPageParam.getAuthStatus());
        }

        IPage<RealNameRecord> iPage = realNameRecordMapper.selectPage(page, wrapper);

        for (RealNameRecord realNameRecord:iPage.getRecords()) {
            //根据用户id查询用户信息
            String uAccount = sysUtilController.queryAccount(realNameRecord.getUserId());
            if (uAccount!=null){
                realNameRecord.setAccount(uAccount);
            }else{
                realNameRecord.setAccount("");
            }

            //根据地区id查询国家
//            AreaCode one1 = areaCodeService.lambdaQuery().eq(AreaCode::getId, realNameRecord.getAreaId()).one();
//            if (ObjectUtil.isNotEmpty(one1)){
//                realNameRecord.setAreaName(one1.getNameCn());
//            }
        }

        return new Paging<RealNameRecord>(iPage);
    }

}
