package io.filpool.pool.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.common.controller.BaseController;
import io.filpool.framework.common.exception.FILPoolException;
import io.filpool.framework.jwt.CheckLogin;
import io.filpool.framework.log.annotation.Module;
import io.filpool.framework.util.AliyunOssUtil;
import io.filpool.framework.util.Constants;
import io.filpool.framework.util.HWObsUtil;
import io.filpool.framework.util.UUIDUtil;
import io.filpool.pool.entity.InviteRecord;
import io.filpool.pool.entity.RewardRecord;
import io.filpool.pool.entity.User;
import io.filpool.pool.request.PageRequest;
import io.filpool.pool.service.UserService;
import io.filpool.pool.service.impl.InviteRecordServiceImpl;
import io.filpool.pool.service.impl.PowerOrderServiceImpl;
import io.filpool.pool.service.impl.RewardRecordServiceImpl;
import io.filpool.pool.util.CreateImage;
import io.filpool.pool.util.SecurityUtil;
import io.filpool.pool.util.StorageHandler;
import io.filpool.pool.vo.InviteInfoVo;
import io.filpool.pool.vo.InviteRecordVo;
import io.filpool.pool.vo.RewardRecordVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 邀请关系表 控制器
 *
 * @author filpool
 * @since 2021-03-02
 */
@Slf4j
@RestController
@RequestMapping(Constants.API_PREFIX + "/inviteRecord")
@Module("pool")
@Api(value = "邀请关系表API", tags = {"邀请相关API"})
public class InviteController extends BaseController {
    @Value("${upload.active}")
    private Integer active;
    @Autowired
    private AliyunOssUtil ossUtil;
    @Autowired
    private HWObsUtil obsUtil;
    @Autowired
    private InviteRecordServiceImpl inviteRecordService;
    @Autowired
    private RewardRecordServiceImpl rewardRecordService;
    @Autowired
    private PowerOrderServiceImpl powerOrderService;
    @Autowired
    private UserService userService;
    @Value("${spring-boot-plus.server-ip}")
    private String host;


    @PostMapping("inviteRecord")
    @ApiOperation("邀请好友列表")
    @CheckLogin
    public ApiResult<List<InviteRecordVo>> inviteRecord(@RequestBody PageRequest request) throws Exception {
        User user = SecurityUtil.currentLogin();
        List<Long> ids = inviteRecordService.lambdaQuery().select(InviteRecord::getUserId)
                .eq(InviteRecord::getInviteUserId, user.getId()).list().stream().map(InviteRecord::getUserId)
                .collect(Collectors.toList());
        ids.add(user.getId());
        LambdaQueryWrapper<InviteRecord> wr = Wrappers.lambdaQuery(InviteRecord.class).orderByDesc(InviteRecord::getCreateTime);
        Page<InviteRecord> page = new Page<>(request.getPageIndex(), request.getPageSize());
        wr.in(InviteRecord::getInviteUserId, ids);
        page = inviteRecordService.getBaseMapper().selectPage(page, wr);
        List<InviteRecordVo> vos = new ArrayList<>();
        for (InviteRecord record : page.getRecords()) {
            InviteRecordVo vo = new InviteRecordVo();
            BeanUtils.copyProperties(record, vo);
            vo.setUserAccount(userService.getUserAccount(vo.getUserId(), true));
            vo.setType(record.getInviteUserId().longValue() == user.getId() ? 1 : 2);
            vos.add(vo);
        }
        return ApiResult.ok(vos);
    }

    @PostMapping("info")
    @CheckLogin
    @ApiOperation("邀请好友详情")
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<InviteInfoVo> info() throws Exception {
        User user = SecurityUtil.currentLogin();
        InviteInfoVo vo = new InviteInfoVo();
        vo.setTotalInviteCount(inviteRecordService.getBaseMapper().countInviteTotalNumber(user.getId()));
        //累计推广数量=自购业绩+邀请用户 即团队业绩
        vo.setTotalPromotionAmount(powerOrderService.getBaseMapper().sumTeamPower(user.getId()));
        //购买人数
        vo.setTotalBuyCount(inviteRecordService.getBaseMapper().countBuyNumber(user.getId()));
        vo.setTotalRewardAmount(rewardRecordService.getBaseMapper().sumTotalAmount(user.getId()));
        vo.setTotalRewardPower(rewardRecordService.getBaseMapper().sumTotalPower(user.getId()));
        vo.setInviteCode(user.getInviteCode());
//        String url = host.contains("http")?host:"http://"+host;
        vo.setInviteUrl("http://m.jwocc.net/#/registerPage/"+ user.getInviteCode());
        if (StringUtils.isBlank(user.getInvitePoster())) {
            //生成海报
            String path = null;
            ByteArrayOutputStream out = CreateImage.saveImage(vo.getInviteUrl());
            InputStream is = new ByteArrayInputStream(out.toByteArray());
            String fileName = user.getId() + "-" + System.currentTimeMillis() + ".jpg";
            String uploadPath = "filpool" + "/invite/" + fileName;
            try {
                if (active == 1) {
                    path = ossUtil.uploadInputStream(is, fileName, uploadPath);
                } else {
                    path = obsUtil.uploadInputStream(is, uploadPath);
                }
            } catch (Exception e) {
                throw new FILPoolException("upload.file.failed");
            }
            user.setInvitePoster(path);
            userService.updateUser(user);
            vo.setInvitePoster(path);
        } else {
            vo.setInvitePoster(user.getInvitePoster());
        }
        return ApiResult.ok(vo);
    }

    @PostMapping("rewardRecord")
    @CheckLogin
    @ApiOperation("返佣奖励记录")
    public ApiResult<List<RewardRecordVo>> getRewardRecord(@RequestBody PageRequest request) throws Exception {
        return ApiResult.ok(rewardRecordService.getRewardRecord(request));
    }
}

