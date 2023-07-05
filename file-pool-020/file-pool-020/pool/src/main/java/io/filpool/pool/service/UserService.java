package io.filpool.pool.service;

import io.filpool.pool.entity.*;
import io.filpool.pool.param.AssetAccountLogPageParam;
import io.filpool.pool.param.RewardConfigPageParam;
import io.filpool.pool.param.UserPageParam;
import io.filpool.framework.common.service.BaseService;
import io.filpool.framework.core.pagination.Paging;
import io.filpool.pool.request.*;
import io.filpool.pool.vo.GoogleSecretVo;
import io.filpool.pool.vo.UserInfoVo;
import io.filpool.pool.vo.UserVo;

import java.math.BigDecimal;

/**
 * 用户表 服务类
 *
 * @author filpool
 * @since 2021-03-02
 */
public interface UserService extends BaseService<User> {

    /**
     * 保存
     *
     * @param user
     * @return
     * @throws Exception
     */
    boolean saveUser(User user) throws Exception;

    /**
     * 修改
     *
     * @param user
     * @return
     * @throws Exception
     */
    boolean updateUser(User user) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteUser(Long id) throws Exception;


    /**
     * 获取分页对象
     *
     * @param userPageParam
     * @return
     * @throws Exception
     */
    Paging<User> getUserPageList(UserPageParam userPageParam) throws Exception;

    Paging<UserVo> getUserPageList(SysUserPageRequest request) throws Exception;

    //发送验证码相关接口
    void sendRegisterCode(SendRegisterCodeRequest request) throws Exception;

    void sendMobileCode(String mobile,String areaCode) throws Exception;

    void sendEmailCode(String email) throws Exception;

    void sendForgetCode(ForgetSmsCodeRequest request) throws Exception;

    String verifyMobileCode(String mobile,String areaCode,String smsCode) throws Exception;

    String verifyEmailCode(String email,String smsCode) throws Exception;

    Boolean register(UserRegisterRequest request) throws Exception;

    UserInfoVo login(String account,String pwd) throws Exception;

    void setPayPwd(String oldPwd,String newPwd) throws Exception;

    void isPayPwdRight(Long userId,String payPwd) throws Exception;

    String updateLoginPwd(String oldPwd,String newPwd) throws Exception;

    void forgetPWD(ForgetPwdRequest request) throws Exception;

    //获取用户信息
    UserInfoVo getUserInfo(Long userId) throws Exception;

    void modifyMobile(String newMobile,String areaCode, String newMobileToken, String oldMobileToken) throws Exception;

    void modifyEmail(String newEmail, String newEmailToken, String oldEmailToken) throws Exception;

    String getUserAccount(Long userId,Boolean isEncryption) throws Exception;

    GoogleSecretVo getGoogleSecret() throws Exception;

    String verifyGoogleCode(String googleCode) throws Exception;

    boolean bindingGoogleAuthenticator(GoogleBindRequest request) throws Exception;

    //获取用户详情
    UserVo queryDescById(Long id) throws Exception;

    //质押列表
    Paging<PledgeListRequest> getPledgePageList(SysUserPageRequest sysUserPageRequest) throws Exception;

    //质押详情
    Paging<AssetAccountLog> getPledgePageListByIdDesc(SysPledgeDesc sysPledgeDesc);

    //平台操作记录
    Paging<AssetAccountLog> platformOperationList(AssetAccountLogPageParam assetAccountLogPageParam);

    BigDecimal getServiceFee(Long userId,String symbol) throws Exception;

    Paging<RewardConfig> userBasicLevelList(RewardConfigPageParam rewardConfigPageParam);

    Paging<RewardConfig> rewardAllocationList(RewardConfigPageParam rewardConfigPageParam);

    Boolean rewardAllocationListEdit(RewardConfigPageParam rewardConfigPageParam);
}
