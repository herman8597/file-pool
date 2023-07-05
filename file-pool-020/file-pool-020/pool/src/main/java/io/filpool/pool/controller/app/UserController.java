package io.filpool.pool.controller.app;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.filpool.framework.common.exception.FILPoolException;
import io.filpool.framework.jwt.CheckLogin;
import io.filpool.framework.util.AccountUtil;
import io.filpool.framework.util.Constants;
import io.filpool.framework.util.RedisUtil;
import io.filpool.pool.entity.AreaCode;
import io.filpool.pool.entity.User;
import io.filpool.pool.request.*;
import io.filpool.pool.service.AreaCodeService;
import io.filpool.pool.service.UserService;
import io.filpool.pool.service.impl.UserServiceImpl;
import io.filpool.pool.util.SecurityUtil;
import io.filpool.pool.vo.AreaCodeVo;
import io.filpool.pool.vo.GoogleSecretVo;
import io.filpool.pool.vo.UserInfoVo;
import lombok.extern.slf4j.Slf4j;
import io.filpool.framework.common.controller.BaseController;
import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.log.annotation.Module;
import org.springframework.beans.BeanUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户表 控制器
 *
 * @author filpool
 * @since 2021-03-02
 */
@Slf4j
@RestController
@RequestMapping(Constants.API_PREFIX + "/user")
@Module("pool")
@Api(value = "用户表API", tags = {"矿池用户控制器"})
@ApiSupport(order = 999)
public class UserController extends BaseController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AreaCodeService areaCodeService;

    @Autowired
    private RedisUtil redisUtil;


    @PostMapping("getAreaCodes")
    @ApiOperation("获取国家地区码")
    public ApiResult<List<AreaCodeVo>> getAreaCodes() throws Exception {
        String key = "area-code-cache";
        List<AreaCode> result;
        if (redisUtil.exists(key)) {
            result = (List<AreaCode>) redisUtil.get(key);
        } else {
            result = areaCodeService.list();
            redisUtil.set(key, result, 60L);
        }
        Locale locale = LocaleContextHolder.getLocale();
        List<AreaCodeVo> vos = result.stream().map(new Function<AreaCode, AreaCodeVo>() {
            @Override
            public AreaCodeVo apply(AreaCode areaCode) {
                AreaCodeVo vo = new AreaCodeVo();
                BeanUtils.copyProperties(areaCode, vo);
                if (locale == Locale.SIMPLIFIED_CHINESE) {
                    vo.setName(areaCode.getNameCn());
                } else if (locale == Locale.TRADITIONAL_CHINESE) {
                    vo.setName(areaCode.getNameFan());
                } else {
                    vo.setName(areaCode.getNameEn());
                }
                return vo;
            }
        }).collect(Collectors.toList());
        return ApiResult.ok(vos);
    }

    @PostMapping("register")
    @ApiOperation("用户注册")
    public ApiResult<Boolean> register(@RequestBody UserRegisterRequest request) throws Exception {
        userService.register(request);
        return ApiResult.ok(true);
    }

    @PostMapping("sendRegisterCode")
    @ApiOperation("发送注册验证码")
    public ApiResult<Boolean> sendRegisterCode(@RequestBody SendRegisterCodeRequest request) throws Exception {
        userService.sendRegisterCode(request);
        return ApiResult.ok(true);
    }

    @PostMapping("login")
    @ApiOperation("用户登录")
    public ApiResult<UserInfoVo> login(@RequestBody LoginRequest request) throws Exception {
        UserInfoVo login = userService.login(request.getAccount(), request.getPwd());
        return ApiResult.ok(login);
    }

    @PostMapping("sendMobileCode")
    @ApiOperation("发送手机验证码")
    @CheckLogin
    public ApiResult<Boolean> sendMobileCode(@RequestBody SmsMobileCodeRequest request) throws Exception {
        userService.sendMobileCode(request.getMobile(), request.getAreaCode());
        return ApiResult.ok(true);
    }

    @PostMapping("sendEmailCode")
    @ApiOperation("发送邮箱验证码")
    @CheckLogin
    public ApiResult<Boolean> sendEmailCode(@RequestBody SmsEmailCodeRequest request) throws Exception {
        userService.sendEmailCode(request.getEmail());
        return ApiResult.ok(true);
    }

    @PostMapping("verifyMobileCode")
    @ApiOperation("校验手机验证码,返回验证TOKEN")
    @CheckLogin
    public ApiResult<String> verifyMobileCode(@RequestBody VerifyMobileRequest request) throws Exception {
        String token = userService.verifyMobileCode(request.getMobile(), request.getAreaCode(),request.getMobileCode());
        return ApiResult.ok(token);
    }

    @PostMapping("verifyEmailCode")
    @ApiOperation("校验邮箱验证码,返回验证TOKEN")
    @CheckLogin
    public ApiResult<String> verifyEmailCode(@RequestBody VerifyEmailRequest request) throws Exception {
        String token = userService.verifyEmailCode(request.getEmail(), request.getEmailCode());
        return ApiResult.ok(token);
    }

    @PostMapping("sendForgetCode")
    @ApiOperation("发送忘记密码验证码")
    public ApiResult<Boolean> sendForgetCode(@RequestBody ForgetSmsCodeRequest request) throws Exception {
        userService.sendForgetCode(request);
        return ApiResult.ok(true);
    }

    @PostMapping("forgetPWD")
    @ApiOperation("忘记密码")
    public ApiResult<Boolean> forgetPWD(@RequestBody ForgetPwdRequest request) throws Exception {
        userService.forgetPWD(request);
        return ApiResult.ok(true);
    }

    @PostMapping("updateLoginPwd")
    @ApiOperation("修改登录密码,返回token")
    @CheckLogin
    public ApiResult<String> updateLoginPwd(@RequestBody UpdatePwdRequest request) throws Exception {
        String token = userService.updateLoginPwd(request.getOldPwd(), request.getNewPwd());
        return ApiResult.ok(token);
    }

    @PostMapping("updatePawPwd")
    @ApiOperation("修改支付密码")
    @CheckLogin
    public ApiResult<Boolean> updatePayPwd(@RequestBody UpdatePwdRequest request) throws Exception {
        userService.setPayPwd(request.getOldPwd(), request.getNewPwd());
        return ApiResult.ok(true);
    }

    @PostMapping("getUserInfo")
    @ApiOperation("获取用户资料")
    @CheckLogin
    public ApiResult<UserInfoVo> getUserInfo() throws Exception {
        Long id = SecurityUtil.currentLogin().getId();
        return ApiResult.ok(userService.getUserInfo(id));
    }

    @PostMapping("modifyMobile")
    @ApiOperation("修改绑定手机号码")
    @CheckLogin
    public ApiResult<Boolean> modifyMobile(@RequestBody ModifyMobileRequest request) throws Exception {
        userService.modifyMobile(request.getNewMobile(), request.getAreaCode(), request.getNewMobileToken(), request.getOldMobileToken());
        return ApiResult.ok(true);
    }

    @PostMapping("modifyEmail")
    @ApiOperation("修改绑定邮箱")
    @CheckLogin
    public ApiResult<Boolean> modifyEmail(@RequestBody ModifyEmailRequest request) throws Exception {
        userService.modifyEmail(request.getNewEmail(), request.getNewEmailToken(), request.getOldEmailToken());
        return ApiResult.ok(true);
    }

    @PostMapping("generateGoogleSecretKey")
    @ApiOperation("生成谷歌验证密钥和二维码")
    @CheckLogin
    public ApiResult<GoogleSecretVo> achieveGoogleSecretKey() throws Exception{
        GoogleSecretVo secretVo = userService.getGoogleSecret();
        return ApiResult.ok(secretVo);
    }

    @PostMapping("bindingGoogleAuthenticator")
    @ApiOperation("绑定/换绑谷歌验证")
    @CheckLogin
    public ApiResult<Boolean> bindingGoogleAuthenticator(@RequestBody GoogleBindRequest request) throws Exception{
        userService.bindingGoogleAuthenticator(request);
        return ApiResult.ok(true);
    }

    @PostMapping("verifyGoogleCode")
    @ApiOperation("校验谷歌验证码")
    @CheckLogin
    public ApiResult<String> verifyGoogleCode(@RequestBody VerifyGoogleRequest request) throws Exception{
        return ApiResult.ok(userService.verifyGoogleCode(request.getGoogleCode()));
    }

    @PostMapping("checkAccount")
    @ApiOperation("检验账户是否注册")
    public ApiResult<Boolean> checkAccount(@RequestBody CheckAccountRequest request) throws Exception{
        if (!AccountUtil.checkAccount(request.getAccount())) {
            throw new FILPoolException("user.account.illegal");
        }
        //校验账号
        if (!userService.accountIsExisting(request.getAccount())) {
            throw new FILPoolException("user.account.exits");
        }
        return ApiResult.ok(true);
    }

}

