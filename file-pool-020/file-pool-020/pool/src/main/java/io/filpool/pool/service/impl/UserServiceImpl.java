package io.filpool.pool.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import io.filpool.framework.common.bean.PoolUserInfo;
import io.filpool.framework.common.exception.FILPoolException;
import io.filpool.framework.jwt.JwtUtil;
import io.filpool.framework.shiro.util.SaltUtil;
import io.filpool.framework.util.*;
import io.filpool.pool.controller.admin.SysUtilController;
import io.filpool.pool.entity.*;
import io.filpool.pool.mapper.AssetAccountLogMapper;
import io.filpool.pool.mapper.ReturnedRewardConfigMapper;
import io.filpool.pool.mapper.RewardConfigMapper;
import io.filpool.pool.mapper.UserMapper;
import io.filpool.pool.param.AssetAccountLogPageParam;
import io.filpool.pool.param.RewardConfigPageParam;
import io.filpool.pool.request.*;
import io.filpool.pool.service.*;
import io.filpool.pool.param.UserPageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.filpool.framework.common.service.impl.BaseServiceImpl;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.framework.core.pagination.PageInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.filpool.pool.sms.SendEmailSMSUtil;
import io.filpool.pool.sms.SendMeiLianSMSUtil;
import io.filpool.pool.util.AccountLogType;
import io.filpool.pool.util.GoogleAuthenticatorUtil;
import io.filpool.pool.util.SecurityUtil;
import io.filpool.pool.util.VerifyCodeUtil;
import io.filpool.pool.vo.GoogleSecretVo;
import io.filpool.pool.vo.UserInfoVo;
import io.filpool.pool.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表 服务实现类
 *
 * @author filpool
 * @since 2021-03-02
 */
@Slf4j
@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private InviteRecordService inviteRecordService;

    @Autowired
    private SendMeiLianSMSUtil sendMeiLianSMSUtil;
    @Autowired
    private SendEmailSMSUtil sendEmailSMSUtil;
    @Autowired
    private VerifyCodeUtil verifyCodeUtil;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AssetAccountServiceImpl assetAccountService;

    @Autowired
    private CurrencyServiceImpl currencyService;

    @Autowired
    private TransferRecordService transferRecordService;

    @Autowired
    private GlobalConfigService globalConfigService;

    @Autowired
    private AssetAccountLogServiceImpl assetAccountLogService;

    @Autowired
    private SysUtilController sysUtilController;
    @Autowired
    private SupplementServiceImpl supplementService;
    @Autowired
    private PowerOrderServiceImpl powerOrderService;
    @Autowired
    private RewardConfigServiceImpl rewardConfigService;
    @Autowired
    private RewardConfigMapper rewardConfigMapper;
    @Autowired
    private ReturnedRewardConfigServiceImpl returnedRewardConfigService;
    @Autowired
    private ReturnedRewardConfigMapper returnedRewardConfigMapper;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveUser(User user) throws Exception {
        return super.save(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateUser(User user) throws Exception {
        return super.updateById(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteUser(Long id) throws Exception {
        return super.removeById(id);
    }

    @Override
    public Paging<User> getUserPageList(UserPageParam userPageParam) throws Exception {
        Page<User> page = new PageInfo<>(userPageParam, OrderItem.desc(getLambdaColumn(User::getCreateTime)));
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        IPage<User> iPage = userMapper.selectPage(page, wrapper);
        return new Paging<User>(iPage);
    }

    @Override
    public Paging<UserVo> getUserPageList(SysUserPageRequest request) throws Exception {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery(User.class);
        /*if (request.getId() != null) {
            wrapper.eq(User::getId, request.getId());
        }*/

        if (request.getMobile()!=null){
            Long aLong = sysUtilController.queryUserId(request.getMobile());
            wrapper.eq(User::getId,aLong);
        }
        if (request.getIsEnable() != null) {
            wrapper.eq(User::getIsEnable, request.getIsEnable());
        }
        if (request.getStartDate() != null) {
            wrapper.ge(User::getCreateTime, request.getStartDate());
        }
        if (request.getEndDate() != null) {
            wrapper.le(User::getCreateTime, request.getEndDate());
        }
      /*  if (StringUtils.isNotBlank(request.getEmail())) {
            wrapper.eq(User::getEmail, request.getEmail());
        }*/
       /* if (StringUtils.isNotBlank(request.getMobile())) {
            wrapper.eq(User::getMobile, request.getEmail());
        }*/

        wrapper.orderByDesc(User::getCreateTime);
        UserPageParam userPageParam = new UserPageParam();
        userPageParam.setPageIndex(request.getPageIndex());
        userPageParam.setPageSize(request.getPageSize());
        PageInfo<User> userPageInfo = userMapper.selectPage(new PageInfo<>(userPageParam), wrapper);
        PageInfo<UserVo> voPageInfo = new PageInfo<>(userPageParam);
        voPageInfo.setCurrent(userPageInfo.getCurrent());
        voPageInfo.setSearchCount(userPageInfo.isSearchCount());
        voPageInfo.setSize(userPageInfo.getSize());
        voPageInfo.setTotal(userPageInfo.getTotal());
        List<UserVo> vos = new ArrayList<>();
        if (!ObjectUtils.isEmpty(userPageInfo.getRecords())) {
            for (User record : userPageInfo.getRecords()) {
                UserVo vo = new UserVo();
                BeanUtils.copyProperties(record, vo);

                //根据积分查询市场等级
                ReturnedRewardConfig returnedRewardConfigList = returnedRewardConfigMapper.getConfigByPower(record.getCommunityExperience(),1);
                if (ObjectUtils.isNotEmpty(returnedRewardConfigList)){
                    vo.setMarketLevel(returnedRewardConfigList.getLevel());
                }

                //查询上级邀请人
                InviteRecord inviteRecord = inviteRecordService.lambdaQuery().eq(InviteRecord::getUserId, vo.getId()).one();
                if (inviteRecord != null) {
                    vo.setInviterAccount(getUserAccount(inviteRecord.getInviteUserId(), false));
                    vo.setInviterUserId(inviteRecord.getInviteUserId());
                }
                //查询账户算力总额
                BigDecimal totalAmount = powerOrderService.getBaseMapper().sumEffectAmount(record.getId(),"FIL");
                vo.setTotalPower(totalAmount);
                //根据经验查询等级信息
                RewardConfig rewardConfig = rewardConfigService.getBaseMapper().getConfigByPower(vo.getExperience());
                if (rewardConfig != null) {
                    vo.setLevelId(rewardConfig.getLevel());
                }
                vos.add(vo);
            }
        }
        voPageInfo.setRecords(vos);
        return new Paging<>(voPageInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public void sendRegisterCode(SendRegisterCodeRequest request) throws Exception {
        if (!AccountUtil.checkAccount(request.getAccount())) {
            throw new FILPoolException("user.account.illegal");
        }
        //校验账号
        if (!accountIsExisting(request.getAccount())) {
            throw new FILPoolException("user.account.exits");
        }
        verifyCodeUtil.checkImageCode(request.getVerifyToken(), request.getImgCode(), false);
        String smsCode = SecurityCode.getSecurityCode();
        if (AccountUtil.checkEmail(request.getAccount())) {
            boolean sendSmsCode = sendEmailSMSUtil.sendSmsCode(request.getAccount(), smsCode);
            if (!sendSmsCode) {
                throw new FILPoolException("verify.sms.email.sendFailed");
            }
            String key = Constants.VERIFY_EMAIL_KEY + request.getAccount();
            //写入redis
            redisUtil.set(key, smsCode, Constants.VERIFY_CODE_TIMEOUT);
        } else {
            if (StringUtils.isEmpty(request.getAreaCode())) {
                throw new FILPoolException("user.areaCode.err");
            }
            boolean sendSmsCode = sendMeiLianSMSUtil.sendSmsCode(request.getAreaCode(), request.getAccount(), smsCode);
            if (!sendSmsCode) {
                throw new FILPoolException("verify.sms.mobile.sendFailed");
            }
            String key = Constants.VERIFY_MOBILE_KEY + request.getAreaCode() + request.getAccount();
            //写入redis
            redisUtil.set(key, smsCode, Constants.VERIFY_CODE_TIMEOUT);
        }
    }

    public boolean accountIsExisting(String account){
        if (AccountUtil.checkEmail(account)){
            return ObjectUtils.isEmpty(getAccountByMail(account));
        }else{
            return ObjectUtils.isEmpty(ChainWrappers.lambdaQueryChain(userMapper).eq(User::getMobile,account).list());
        }
    }

    private List<User> getAccountByMail(String mail) {
        //select * from customer where email regexp '^a\\.?b\\.?c\\.?@gmail.com$'
        //如果是gmail,则使用正则匹配：去.后相同的邮箱认为是同一个邮箱
        //判断gmail的账号是否带小数点
        int eIndex = mail.lastIndexOf("@gmail.com");
        if(eIndex!=-1){
            StringBuilder builder = new StringBuilder("^");
            String substr;
            for (int i = 0; i < eIndex; i++) {
                substr = mail.substring(i, i + 1);
                if (".".equals(substr)) {
                    continue;
                }
                builder.append(substr).append("\\\\.?");
            }
            builder.append("@gmail\\\\.com$");
            builder.insert(0, "select * from fil_user where email regexp '");
            builder.append("'");
            return ChainWrappers.lambdaQueryChain(userMapper).exists(builder.toString()).list();
        }
        return ChainWrappers.lambdaQueryChain(userMapper).eq(User::getEmail, mail).list();
    }

    @Override
    public void sendMobileCode(String mobile, String areaCode) throws Exception {
        String smsCode = SecurityCode.getSecurityCode();
        if (StringUtils.isEmpty(mobile)) {
            User user = SecurityUtil.currentLogin();
            mobile = user.getMobile();
            areaCode = user.getAreaCode();
        } else {
            //需要判断是否为正确账号格式
            if (!AccountUtil.checkPhone(mobile)) {
                throw new FILPoolException("user.account.illegal");
            }
        }
        if (StringUtils.isEmpty(areaCode)) {
            throw new FILPoolException("user.areaCode.err");
        }
        boolean sendSmsCode = sendMeiLianSMSUtil.sendSmsCode(areaCode, mobile, smsCode);
        if (!sendSmsCode) {
            throw new FILPoolException("verify.sms.mobile.sendFailed");
        }
        String key = Constants.VERIFY_SECURITY_MOBILE_KEY + areaCode + mobile;
        //写入redis
        redisUtil.set(key, smsCode, Constants.VERIFY_CODE_TIMEOUT);
    }

    @Override
    public void sendEmailCode(String email) throws Exception {
        String smsCode = SecurityCode.getSecurityCode();
        if (StringUtils.isEmpty(email)) {
            User user = SecurityUtil.currentLogin();
            email = user.getEmail();
        }
//        log.info("email:{}", email);
        //需要判断是否为正确账号格式
        if (!AccountUtil.checkEmail(email)) {
            throw new FILPoolException("user.account.illegal");
        }
        boolean sendSmsCode = sendEmailSMSUtil.sendSmsCode(email, smsCode);
        if (!sendSmsCode) {
            throw new FILPoolException("verify.sms.email.sendFailed");
        }
        String key = Constants.VERIFY_SECURITY_EMAIL_KEY + email;
        //写入redis
        redisUtil.set(key, smsCode, Constants.VERIFY_CODE_TIMEOUT);
    }

    @Override
    public void sendForgetCode(ForgetSmsCodeRequest request) throws Exception {
        if (!AccountUtil.checkAccount(request.getAccount())) {
            throw new FILPoolException("user.account.illegal");
        }
        //校验账号
        User one = lambdaQuery().eq(User::getMobile, request.getAccount())
                .or().eq(User::getEmail, request.getAccount()).one();
        if (one == null) {
            throw new FILPoolException("user.account.not-exits");
        }
        String smsCode = SecurityCode.getSecurityCode();
        if (AccountUtil.checkEmail(request.getAccount())) {
            boolean sendSmsCode = sendEmailSMSUtil.sendSmsCode(request.getAccount(), smsCode);
            if (!sendSmsCode) {
                throw new FILPoolException("verify.sms.email.sendFailed");
            }
            String key = Constants.VERIFY_EMAIL_KEY + request.getAccount();
            //写入redis
            redisUtil.set(key, smsCode, Constants.VERIFY_CODE_TIMEOUT);
        } else {
            boolean sendSmsCode = sendMeiLianSMSUtil.sendSmsCode(one.getAreaCode(), request.getAccount(), smsCode);
            if (!sendSmsCode) {
                throw new FILPoolException("verify.sms.mobile.sendFailed");
            }
            String key = Constants.VERIFY_MOBILE_KEY + one.getAreaCode() + request.getAccount();
            //写入redis
            redisUtil.set(key, smsCode, Constants.VERIFY_CODE_TIMEOUT);
        }
    }

    @Override
    public String verifyMobileCode(String mobile, String areaCode, String smsCode) throws Exception {
        if (StringUtils.isEmpty(mobile)) {
            User user = SecurityUtil.currentLogin();
            mobile = user.getMobile();
            areaCode = user.getAreaCode();
        }
        verifyCodeUtil.checkSecurityMobile(areaCode, mobile, smsCode);
        String tokenKey = Constants.VERIFY_SECURITY_TOKEN + mobile;
        String token = TokenUtil.generateAccountToken();
        redisUtil.set(tokenKey, token, Constants.VERIFY_CODE_TIMEOUT);
        return token;
    }

    @Override
    public String verifyEmailCode(String email, String smsCode) throws Exception {
        if (StringUtils.isEmpty(email)) {
            User user = SecurityUtil.currentLogin();
            email = user.getEmail();
        }
        verifyCodeUtil.checkSecurityEmail(email, smsCode);
        String tokenKey = Constants.VERIFY_SECURITY_TOKEN + email;
        String token = TokenUtil.generateAccountToken();
        redisUtil.set(tokenKey, token, Constants.VERIFY_CODE_TIMEOUT);
        return token;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean register(UserRegisterRequest request) throws Exception {
        if (!AccountUtil.checkAccount(request.getAccount())) {
            throw new FILPoolException("user.account.illegal");
        }
        if (StringUtils.isEmpty(request.getPwd())) {
            throw new FILPoolException("user.passwd.not-empty");
        }
//        if (StringUtils.isEmpty(request.getPayPwd())) {
//            throw new FILPoolException("user.paypwd.not-empty");
//        }
        //校验账号
        List<User> list = ChainWrappers.lambdaQueryChain(userMapper).eq(User::getMobile, request.getAccount())
                .or().eq(User::getEmail, request.getAccount()).list();
        if (!ObjectUtils.isEmpty(list)) {
            throw new FILPoolException("user.account.exits");
        }
        //图片验证码
        verifyCodeUtil.checkImageCode(request.getVerifyToken(), request.getImgCode(), true);
        //校验验证码
        if (AccountUtil.checkEmail(request.getAccount()))
            verifyCodeUtil.checkEmailCode(request.getAccount(), request.getSmsCode());
        else {
            if (StringUtils.isEmpty(request.getAreaCode())) {
                throw new FILPoolException("user.areaCode.err");
            }
            verifyCodeUtil.checkMobileCode(request.getAreaCode(), request.getAccount(), request.getSmsCode());
        }
        //查找邀请用户
        User inviteUser = null;
        if (!StringUtils.isEmpty(request.getInviteCode())) {
            Long decode = InviteCode.instance().decode(request.getInviteCode());
            inviteUser = getById(decode);
            if (inviteUser == null) {
                throw new FILPoolException("user.invite.code.error");
            }
        }
        Date now = new Date();
        User user = new User();
        //目前只做大陆
        user.setCreateTime(now);
        if (AccountUtil.checkEmail(request.getAccount()))
            user.setEmail(request.getAccount());
        else {
            user.setMobile(request.getAccount());
            user.setAreaCode(request.getAreaCode());
        }
        String salt = SaltUtil.generateSalt();
        String encrypt = PasswordUtil.encrypt(request.getPwd(), salt);
//        String payEncrypt = PasswordUtil.encrypt(request.getPayPwd(), salt);
        user.setPassword(encrypt);
        user.setSalt(salt);
        user.setExperience(BigDecimal.ZERO);
        user.setCommunityExperience(BigDecimal.ZERO);
//        user.setPayPassword(payEncrypt);
        boolean b = this.saveUser(user);
        if (!b) {
            throw new FILPoolException("user.register.err");
        }
        //记录邀请关系
        if (inviteUser != null) {
            InviteRecord record = new InviteRecord();
            record.setCreateTime(now);
            record.setUserId(user.getId());
            record.setInviteUserId(inviteUser.getId());
            inviteRecordService.save(record);
        } else {
            InviteRecord record = new InviteRecord();
            record.setCreateTime(now);
            record.setUserId(user.getId());
            inviteRecordService.save(record);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfoVo login(String account, String pwd) throws Exception {
        if (!AccountUtil.checkAccount(account)) {
            throw new FILPoolException("user.account.illegal");
        }
        User one = ChainWrappers.lambdaQueryChain(userMapper).eq(User::getMobile, account)
                .or().eq(User::getEmail, account).one();
        if (one == null) {
            throw new FILPoolException("user.account.not-exits");
        }
        String encrypt = PasswordUtil.encrypt(pwd, one.getSalt());
        if (!StringUtils.equals(encrypt, one.getPassword())) {
            throw new FILPoolException("user.passwd.err");
        }
        if (!one.getIsEnable()) {
            throw new FILPoolException("user.not.enable");
        }
        if (StringUtils.isEmpty(one.getInviteCode())) {
            String gen = InviteCode.instance().gen(one.getId());
            one.setInviteCode(gen);
        }
        PoolUserInfo poolUserInfo = new PoolUserInfo();
        poolUserInfo.setPassword(one.getPassword());
        poolUserInfo.setUserId(one.getId());
        //校验成功，生成token,返回
        String token = JwtUtil.getToken(poolUserInfo);
        String key = Constants.TOKEN_KEY + one.getId();
        redisUtil.set(key, token, Constants.TOKEN_KEY_EXPIRE);
        one.setToken(token);
        updateUser(one);
        UserInfoVo userInfoVo = getUserInfo(one.getId());
        userInfoVo.setToken(token);
        return userInfoVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setPayPwd(String oldPwd, String newPwd) throws Exception {
        User user = SecurityUtil.currentLogin();
        user = getById(user.getId());
        //存在旧交易密码
        if (!StringUtils.isEmpty(user.getPayPassword())) {
            if (StringUtils.isEmpty(oldPwd)) {
                throw new FILPoolException("transfer.paypwd.not-empty");
            }
            //校验旧密码
            String oldEncrypt = PasswordUtil.encrypt(oldPwd, user.getSalt());
            if (!StringUtils.equals(oldEncrypt, user.getPayPassword())) {
                throw new FILPoolException("user.paypwd.err");
            }
        }
        //设置新密码
        String encrypt = PasswordUtil.encrypt(newPwd, user.getSalt());
        user.setPayPassword(encrypt);
        updateUser(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void isPayPwdRight(Long userId, String payPwd) throws Exception {
        User byId = getById(userId);
        if (byId == null) {
            throw new FILPoolException("user.not.exits");
        }
        if (StringUtils.isEmpty(byId.getPayPassword())) {
            throw new FILPoolException("user.paypwd.not-set");
        }
        String encrypt = PasswordUtil.encrypt(payPwd, byId.getSalt());
        if (!StringUtils.equals(encrypt, byId.getPayPassword())) {
            throw new FILPoolException("user.paypwd.err");
        }
        //校验通过
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateLoginPwd(String oldPwd, String newPwd) throws Exception {
        User user = SecurityUtil.currentLogin();
        user = getById(user.getId());
        String oldEncrypt = PasswordUtil.encrypt(oldPwd, user.getSalt());
        if (!StringUtils.equals(oldEncrypt, user.getPassword())) {
            throw new FILPoolException("user.passwd.err.real");
        }
        //设置新密码
        String encrypt = PasswordUtil.encrypt(newPwd, user.getSalt());
        user.setPassword(encrypt);
        updateUser(user);
        //生成新token
        PoolUserInfo poolUserInfo = new PoolUserInfo();
        poolUserInfo.setPassword(user.getPassword());
        poolUserInfo.setUserId(user.getId());
        //校验成功，生成token,返回
        String token = JwtUtil.getToken(poolUserInfo);
        String key = Constants.TOKEN_KEY + user.getId();
        redisUtil.set(key, token, Constants.TOKEN_KEY_EXPIRE);
        return token;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void forgetPWD(ForgetPwdRequest request) throws Exception {
        if (!AccountUtil.checkAccount(request.getAccount())) {
            throw new FILPoolException("user.account.illegal");
        }
        //校验账号
        User one = lambdaQuery().eq(User::getMobile, request.getAccount())
                .or().eq(User::getEmail, request.getAccount()).one();
        if (one == null) {
            throw new FILPoolException("user.account.not-exits");
        }
        if (AccountUtil.checkEmail(request.getAccount()))
            verifyCodeUtil.checkEmailCode(request.getAccount(), request.getSmsCode());
        else
            verifyCodeUtil.checkMobileCode(one.getAreaCode(), request.getAccount(), request.getSmsCode());
        //修改用户
        one.setPassword(PasswordUtil.encrypt(request.getPwd(), one.getSalt()));
        updateUser(one);
//        log.info("用户 {} 修改密码成功", one.getId());
    }

    @Override
    @Transactional
    public UserInfoVo getUserInfo(Long userId) throws Exception {
        User one = getById(userId);
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setHavePayPwd(!StringUtils.isEmpty(one.getPayPassword()));
        userInfoVo.setId(one.getId());
        userInfoVo.setInviteCode(one.getInviteCode());
        userInfoVo.setMobile(AccountUtil.desensitize(one.getMobile()));
        userInfoVo.setEmail(AccountUtil.desensitizeEmail(one.getEmail()));
        userInfoVo.setAreaCode(one.getAreaCode());
        userInfoVo.setRealNameStatus(one.getRealNameStatus());
        userInfoVo.setGoogleAuthIsOpen(one.getGoogleAuthIsOpen());
        userInfoVo.setToken(one.getToken());
        //根据经验查询等级信息
        RewardConfig rewardConfig = rewardConfigService.getBaseMapper().getConfigByPower(one.getExperience());
        if (rewardConfig != null) {
            userInfoVo.setLevelId(rewardConfig.getLevel());
        }
//        userInfoVo.setLevelId(one.getLevelId());
        userInfoVo.setExperience(one.getExperience());
        userInfoVo.setCommunityExperience(one.getCommunityExperience());
        ReturnedRewardConfig returnedRewardConfig = returnedRewardConfigService.getBaseMapper().getConfigByPowerTwo(one.getCommunityExperience());
        if (returnedRewardConfig != null) {
            userInfoVo.setCommunityLevelId(returnedRewardConfig.getLevel());
        }else{
            userInfoVo.setCommunityLevelId(0);
        }
        userInfoVo.setType(one.getType());
        return userInfoVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyMobile(String newMobile, String areaCode, String newMobileToken, String oldMobileToken) throws Exception {
        User user = SecurityUtil.currentLogin();
        if (!AccountUtil.checkPhone(newMobile)) {
            throw new FILPoolException("user.account.illegal");
        }
        //判断是否存在旧手机号码
        if (!StringUtils.isEmpty(user.getMobile())) {
            //验证旧手机token
            verifyCodeUtil.checkAccountToken(user.getMobile(), oldMobileToken);
        }
        verifyCodeUtil.checkAccountToken(newMobile, newMobileToken);
        //判断新手机号是否存在
        Integer count = lambdaQuery().eq(User::getMobile, newMobile).eq(User::getAreaCode, areaCode).count();
        if (count > 0) {
            throw new FILPoolException("user.account.exits");
        }
        //修改手机号码
        user.setMobile(newMobile);
        user.setAreaCode(areaCode);
        updateUser(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyEmail(String newEmail, String newEmailToken, String oldEmailToken) throws Exception {
        User user = SecurityUtil.currentLogin();
        if (!AccountUtil.checkEmail(newEmail)) {
            throw new FILPoolException("user.account.illegal");
        }
        //判断是否存在旧邮箱
        if (!StringUtils.isEmpty(user.getEmail())) {
            //验证旧邮箱token
            verifyCodeUtil.checkAccountToken(user.getEmail(), oldEmailToken);
        }
        verifyCodeUtil.checkAccountToken(newEmail, newEmailToken);
        //判断新邮箱是否存在
        Integer count = lambdaQuery().eq(User::getEmail, newEmail).count();
        if (count > 0) {
            throw new FILPoolException("user.account.exits");
        }
        //修改邮箱
        user.setEmail(newEmail);
        updateUser(user);
    }

    @Override
    public String getUserAccount(Long userId, Boolean isEncryption) throws Exception {
        User one = lambdaQuery().select(User::getMobile, User::getEmail).eq(User::getId, userId).one();
        if (one == null) {
            return "";
        }
        if (!StringUtils.isEmpty(one.getMobile()))
            return isEncryption ? AccountUtil.desensitize(one.getMobile()) : one.getMobile();
        else
            return isEncryption ? AccountUtil.desensitizeEmail(one.getEmail()) : one.getEmail();
    }

    @Override
    public GoogleSecretVo getGoogleSecret() throws Exception {
        User user = SecurityUtil.currentLogin();
        GoogleSecretVo vo = new GoogleSecretVo();
        String secretKey = GoogleAuthenticatorUtil.generateSecretKey();
        vo.setSecret(secretKey);
        vo.setQrCode(GoogleAuthenticatorUtil.getQRBarcodeURL(!StringUtils.isEmpty(user.getEmail()) ? user.getEmail() : user.getMobile(), secretKey));
        return vo;
    }

    @Override
    public String verifyGoogleCode(String googleCode) throws Exception {
        User user = SecurityUtil.currentLogin();
        String secretKey = user.getGoogleSecretKey();
        if (StringUtils.isEmpty(secretKey)) {
            throw new FILPoolException("google.verify.not-bind");
        }
        boolean suc = GoogleAuthenticatorUtil.authCode(googleCode, secretKey);
        if (!suc) {
            throw new FILPoolException("verify.code.error");
        }
        String tokenKey = Constants.VERIFY_SECURITY_GOOGLE_KEY + secretKey;
        String token = TokenUtil.generateAccountToken();
        redisUtil.set(tokenKey, token, Constants.VERIFY_CODE_TIMEOUT);
        return token;
    }

    @Override
    public boolean bindingGoogleAuthenticator(GoogleBindRequest request) throws Exception {
        User user = SecurityUtil.currentLogin();
        String secretKey = user.getGoogleSecretKey();
        //判断是否存在旧谷歌验证
        if (!StringUtils.isEmpty(secretKey)) {
            verifyCodeUtil.checkGoogleToken(secretKey, request.getVerityToken());
        }
        boolean suc = GoogleAuthenticatorUtil.authCode(request.getGoogleCode(), request.getGoogleSecretKey());
        if (!suc) {
            throw new FILPoolException("verify.code.error");
        }
        user.setGoogleAuthIsOpen(true);
        user.setGoogleSecretKey(request.getGoogleSecretKey());
        updateUser(user);
        return true;
    }

    //获取用户详情
    @Override
    public UserVo queryDescById(Long id) throws Exception {
        User one = userService.lambdaQuery().eq(User::getId, id).one();
        UserVo userVo =new UserVo();
        if (ObjectUtils.isNotEmpty(one)){
            BeanUtils.copyProperties(one,userVo);

            InviteRecord inviteRecord = inviteRecordService.lambdaQuery().eq(InviteRecord::getUserId, userVo.getId()).one();
            if (inviteRecord != null) {
                userVo.setInviterAccount(getUserAccount(inviteRecord.getInviteUserId(), false));
                userVo.setInviterUserId(inviteRecord.getInviteUserId());
            }
            //获取用户等级
            RewardConfig rewardConfig = rewardConfigService.getBaseMapper().getConfigByPower(userVo.getExperience());
            if (rewardConfig != null) {
                userVo.setLevelId(rewardConfig.getLevel());
            }
            //查询账户算力总额
            BigDecimal totalAmount = powerOrderService.getBaseMapper().sumEffectAmount(id,"FIL");
            userVo.setTotalPower(totalAmount);
            //存储该用户的资金信息
            List<AssetAccount> list = assetAccountService.lambdaQuery().eq(AssetAccount::getUserId,userVo.getId()).and(x -> x.eq(AssetAccount::getSymbol, "USDT").or(z -> z.eq(AssetAccount::getSymbol, "FIL"))).list();
            userVo.setAssetAccount(list);
        }
        return userVo;
    }

    @Override
    public Paging<PledgeListRequest> getPledgePageList(SysUserPageRequest sysUserPageRequest){
        LambdaQueryWrapper<User> wr = Wrappers.lambdaQuery(User.class);
        Page<User> page = new Page<>(sysUserPageRequest.getPageIndex(),sysUserPageRequest.getPageSize());

        //通过用户账号过滤数据
        if (sysUserPageRequest.getAccount()!=null){
            Long uid = sysUtilController.queryUserId(sysUserPageRequest.getAccount());
            if (uid!=null){
                wr.eq(User::getId,uid);
            }
        }
        page = page(page,wr);

        BigDecimal gasSum =BigDecimal.ZERO;

        List<PledgeListRequest> pledgeList = new ArrayList<>();
        for (User user:page.getRecords()) {
            PledgeListRequest pledgeListRequestTwo = new PledgeListRequest();
            //通过id查询该用户的质押账户总金额
            AssetAccount two = assetAccountService.lambdaQuery().eq(AssetAccount::getUserId, user.getId()).eq(AssetAccount::getSymbol,"FIL").one();

            //用户账号
            if (StringUtils.isNotBlank(user.getMobile())){
                pledgeListRequestTwo.setAccount(user.getMobile());
            }else{
                pledgeListRequestTwo.setAccount(user.getEmail());
            }
            //质押总金额
            if (ObjectUtil.isNotEmpty(two)){
                pledgeListRequestTwo.setAmount(two.getPledge());
            }else{
                pledgeListRequestTwo.setAmount(BigDecimal.ZERO);
            }
            pledgeListRequestTwo.setSymbol("FIL");
            //消费gas费总金额
            List<Supplement> list = supplementService.lambdaQuery().eq(Supplement::getUId, user.getId()).list();
            if (ObjectUtil.isNotEmpty(list)){
                gasSum = list.stream().map(Supplement::getGasPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
                 pledgeListRequestTwo.setGasSum(gasSum);
            }else{
                pledgeListRequestTwo.setGasSum(gasSum);
            }

            pledgeList.add(pledgeListRequestTwo);
        }

        Page<PledgeListRequest> objectPage = new Page<>();
        objectPage.setRecords(pledgeList);
        objectPage.setTotal(userService.list().size());

        return new Paging<>(objectPage);
    }

    @Override
    public Paging<AssetAccountLog> getPledgePageListByIdDesc(SysPledgeDesc sysPledgeDesc) {
        LambdaQueryWrapper<AssetAccountLog> wr = Wrappers.lambdaQuery(AssetAccountLog.class);
        Page<AssetAccountLog> page = new Page<>(sysPledgeDesc.getPageIndex(), sysPledgeDesc.getPageSize());
        if (sysPledgeDesc.getStartDate() != null) {
            wr.ge(AssetAccountLog::getCreateTime, sysPledgeDesc.getStartDate());
        }
        if (sysPledgeDesc.getEndDate() != null) {
            wr.le(AssetAccountLog::getCreateTime, sysPledgeDesc.getEndDate());
        }
        if (sysPledgeDesc.getAccount()!=null){
            wr.eq(AssetAccountLog::getUserId,sysUtilController.queryUserId(sysPledgeDesc.getAccount()));
        }
        List<Integer> types = new ArrayList<>();
        types.add(AccountLogType.TYPE_SYSTEM_TRANSFER);
        types.add(AccountLogType.TYPE_TRANSFER);
        types.add(AccountLogType.TYPE_GAS);
        wr.in(AssetAccountLog::getType,types);

        page = assetAccountLogService.page(page, wr);
        for (AssetAccountLog log:page.getRecords()) {
            //查询币种名称
            AssetAccount one = assetAccountService.getById(log.getAssetAccountId());
            log.setSymbol(one.getSymbol());
        }
        Page<AssetAccountLog> objectPage = new Page<>();
        objectPage.setRecords(page.getRecords());
        List<AssetAccountLog> list = assetAccountLogService.lambdaQuery().eq(AssetAccountLog::getUserId, sysUtilController.queryUserId(sysPledgeDesc.getAccount())).in(AssetAccountLog::getType, types).list();
        objectPage.setTotal(list.size());
        return new Paging<>(objectPage);
    }

    @Override
    public Paging<AssetAccountLog> platformOperationList(AssetAccountLogPageParam assetAccountLogPageParam) {
        LambdaQueryWrapper<AssetAccountLog> wr = Wrappers.lambdaQuery(AssetAccountLog.class);
        wr.orderByDesc(AssetAccountLog::getCreateTime);
        Page<AssetAccountLog> page = new Page<>(assetAccountLogPageParam.getPageIndex(), assetAccountLogPageParam.getPageSize());

        if (StringUtils.isNotBlank(assetAccountLogPageParam.getAccount())){
            Long userId = sysUtilController.queryUserId(assetAccountLogPageParam.getAccount());
            wr.eq(AssetAccountLog::getUserId,userId);
        }


        //开始结束时间过滤
        if (assetAccountLogPageParam.getStartDate() != null) {
            wr.ge(AssetAccountLog::getCreateTime, assetAccountLogPageParam.getStartDate());
        }
        if (assetAccountLogPageParam.getEndDate() != null) {
            wr.le(AssetAccountLog::getCreateTime, assetAccountLogPageParam.getEndDate());
        }
        //系统充值扣除过滤
        wr.in(AssetAccountLog::getType,3,4);

        page = assetAccountLogService.page(page, wr);

        for (AssetAccountLog assetAccountLog:page.getRecords()) {
            //单位
            AssetAccount one = assetAccountService.lambdaQuery().eq(AssetAccount::getId, Integer.parseInt(assetAccountLog.getAssetAccountId().toString())).one();
            if (ObjectUtil.isNotEmpty(one)){
                assetAccountLog.setSymbol(one.getSymbol());
            }
           /* String asset = sysUtilController.querySymbol(Integer.parseInt(assetAccountLog.getAssetAccountId().toString()));
            if (asset!=null){
                assetAccountLog.setSymbol(asset);
            }*/
            //用户账号
            String account = sysUtilController.queryAccount(assetAccountLog.getUserId());
            if (account!=null){
                assetAccountLog.setAccount(account);
            }
        }

        if (assetAccountLogPageParam.getSymbol()!=null){
            List<AssetAccountLog> collect = page.getRecords().stream().filter(e -> e.getSymbol() == assetAccountLogPageParam.getSymbol() || e.getSymbol().equals(assetAccountLogPageParam.getSymbol())).collect(Collectors.toList());
            page.setRecords(collect);
            page.setTotal(collect.size());
        }

        return new Paging<>(page);
    }

    @Override
    public BigDecimal getServiceFee(Long userId,String symbol) throws Exception {
        User one = getById(userId);
        BigDecimal fee;
        if (StringUtils.equals(symbol,"XCH")){
            fee = one.getXchServerCharge();
        }else{
            fee = one.getServerCharge();
        }
        if (fee == null || fee.compareTo(BigDecimal.ZERO) <= 0)
            return BigDecimal.ZERO;
        else
            return fee;
    }

    @Override
    public Paging<RewardConfig> userBasicLevelList(RewardConfigPageParam rewardConfigPageParam) {
        LambdaQueryWrapper<RewardConfig> wr = Wrappers.lambdaQuery(RewardConfig.class);
        wr.orderByDesc(RewardConfig::getCreateTime);
        Page<RewardConfig> page = new Page<>(rewardConfigPageParam.getPageIndex(), rewardConfigPageParam.getPageSize());
        page = rewardConfigService.page(page, wr);
        return new Paging<>(page);
    }

    @Override
    public Paging<RewardConfig> rewardAllocationList(RewardConfigPageParam rewardConfigPageParam) {
        Long pageIndex = rewardConfigPageParam.getPageIndex();
        pageIndex=(pageIndex-1)*rewardConfigPageParam.getPageSize();

        List<RewardConfig> rewardConfigs = rewardConfigMapper.rewardAllocationList(pageIndex, rewardConfigPageParam.getPageSize());
        List<RewardConfig> rewardConfigsTotal = rewardConfigMapper.rewardConfigsTotal();

        Page<RewardConfig> objectPage = new Page<>();
        objectPage.setTotal(rewardConfigsTotal.size());
        objectPage.setRecords(rewardConfigs);

        return new Paging<>(objectPage);
    }

    @Override
    public Boolean rewardAllocationListEdit(RewardConfigPageParam rewardConfigPageParam) {
        //修改云算力奖励配置
        if (rewardConfigPageParam.getGoodsType()==1){
            return rewardConfigMapper.updateOne(rewardConfigPageParam);
        }
        //修改矿机奖励配置
        if (rewardConfigPageParam.getGoodsType()==2) {
            return rewardConfigMapper.updateTwo(rewardConfigPageParam);
        }
        //修改集群奖励配置
        if (rewardConfigPageParam.getGoodsType()==3){
            return rewardConfigMapper.updateThree(rewardConfigPageParam);
        }
        //修改节点奖励配置
        if (rewardConfigPageParam.getGoodsType()==4){
            return rewardConfigMapper.updateFor(rewardConfigPageParam);
        }
        return true;
    }
}
