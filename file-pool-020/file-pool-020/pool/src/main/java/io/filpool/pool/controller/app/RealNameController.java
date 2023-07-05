package io.filpool.pool.controller.app;

import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.common.exception.FILPoolException;
import io.filpool.framework.jwt.CheckLogin;
import io.filpool.framework.util.Constants;
import io.filpool.pool.entity.AreaCode;
import io.filpool.pool.entity.RealNameRecord;
import io.filpool.pool.entity.User;
import io.filpool.pool.request.CommitRealNameRequest;
import io.filpool.pool.service.AreaCodeService;
import io.filpool.pool.service.RealNameRecordService;
import io.filpool.pool.service.UserService;
import io.filpool.pool.util.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Locale;

@RestController
@Slf4j
@Api(tags = {"实名认证控制器"})
@RequestMapping(Constants.API_PREFIX + "/realName")
public class RealNameController {
    @Autowired
    private RealNameRecordService realNameRecordService;
    @Autowired
    private UserService userService;
    @Autowired
    private AreaCodeService areaCodeService;

    @ApiOperation("提交实名认证")
    @PostMapping("commit")
    @CheckLogin
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<Boolean> commit(@RequestBody CommitRealNameRequest request) throws Exception {
        User user = SecurityUtil.currentLogin();
        if (user.getRealNameStatus() != null && user.getRealNameStatus() == 3) {
            throw new FILPoolException("user.realName.auth");
        }
        Integer count = realNameRecordService.lambdaQuery().eq(RealNameRecord::getUserId, user.getId())
                .eq(RealNameRecord::getAuthStatus, 1).count();
        if (count > 0) {
            throw new FILPoolException("user.realName.have.commit");
        }
        Integer cardCount = realNameRecordService.lambdaQuery().eq(RealNameRecord::getCardNumber, request.getCardNumber())
                .eq(RealNameRecord::getAuthStatus, 3).count();
        if (cardCount>0){
            throw new FILPoolException("user.realName.card.have.auth");
        }
        if (StringUtils.isEmpty(request.getRealName()) || StringUtils.isEmpty(request.getCardFront())
                //|| StringUtils.isEmpty(request.getCardHolding())
                || StringUtils.isEmpty(request.getCardSide())
                || StringUtils.isEmpty(request.getCardNumber())) {
            throw new FILPoolException("illegal.params");
        }
//        AreaCode areaCode = areaCodeService.lambdaQuery().eq(AreaCode::getCode, request.getAreaCode()).one();
//        if (areaCode == null) {
//            throw new FILPoolException("user.areaCode.err");
//        }
        //插入数据库
        RealNameRecord record = new RealNameRecord();
        record.setCreateTime(new Date());
        record.setUserId(user.getId());
        record.setRealName(request.getRealName());
        record.setAuthStatus(1);
        record.setAreaCode(request.getAreaCode());
        record.setCardFront(request.getCardFront());
        record.setCardNumber(request.getCardNumber());
        record.setCardSide(request.getCardSide());
        record.setAreaName(request.getAreaName());
//        record.setCardHolding(request.getCardHolding());
        realNameRecordService.saveRealNameRecord(record);
        boolean update = userService.lambdaUpdate().eq(User::getId, user.getId()).set(User::getRealNameStatus, 1).update();
        return ApiResult.ok(update);
    }

    @ApiOperation("获取最近的实名认证详情")
    @PostMapping("getDetail")
    @CheckLogin
    public ApiResult<RealNameRecord> getDetail() throws Exception {
        User user = SecurityUtil.currentLogin();
        List<RealNameRecord> records = realNameRecordService.lambdaQuery().eq(RealNameRecord::getUserId, user.getId()).orderByDesc(RealNameRecord::getCreateTime).list();
        if (ObjectUtils.isEmpty(records)) {
            throw new FILPoolException("user.realName.notCommit");
        }
        RealNameRecord vo = records.get(0);
//        Locale locale = LocaleContextHolder.getLocale();
//        AreaCode area = areaCodeService.getById(vo.getAreaId());
        vo.setAccount(StringUtils.isEmpty(user.getMobile()) ? user.getEmail() : user.getMobile());
//        if (locale == Locale.SIMPLIFIED_CHINESE) {
//            vo.setAreaName(area.getNameCn());
//        } else if (locale == Locale.TRADITIONAL_CHINESE) {
//            vo.setAreaName(area.getNameFan());
//        } else {
//            vo.setAreaName(area.getNameEn());
//        }
        return ApiResult.ok(vo);
    }
}
