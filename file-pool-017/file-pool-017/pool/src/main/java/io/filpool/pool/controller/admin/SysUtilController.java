package io.filpool.pool.controller.admin;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.filpool.framework.util.AccountUtil;
import io.filpool.pool.entity.Currency;
import io.filpool.pool.entity.InviteRecord;
import io.filpool.pool.entity.User;
import io.filpool.pool.service.InviteRecordService;
import io.filpool.pool.service.impl.CurrencyServiceImpl;
import io.filpool.pool.service.impl.UserServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "/sys/util")
@Api(value = "后台公共控制器", tags = {"后台常用接口"})
public class SysUtilController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private CurrencyServiceImpl currencyService;

    @Autowired
    private InviteRecordService inviteRecordService;

    /**
     *
     * 根据用户id查询用户的账号
     */
    @RequestMapping("queryAccount")
    public String queryAccount(Long uid){
        User one = userService.lambdaQuery().eq(User::getId, uid).one();
        if (ObjectUtils.isNotEmpty(one)){
            if (one.getMobile()!=null && StringUtils.isNotBlank(one.getMobile())){
                return one.getMobile();
            }else{
                return one.getEmail();
            }
        }
        return null;
    }

    /**
     *
     * 根据用户的账号查询用户id
     */
    @RequestMapping("queryUserId")
    public Long queryUserId(String account){
        if (AccountUtil.checkEmail(account)){
            User one = userService.lambdaQuery().eq(User::getEmail,account).one();
            return one.getId();
        }else{
            User one = userService.lambdaQuery().eq(User::getMobile,account).one();
            return one.getId();
        }
    }

    /**
     *
     * 根据币种id查询币种名称
     */
    @RequestMapping("querySymbol")
    public String querySymbol(Integer cid){
        Currency one = currencyService.lambdaQuery().eq(Currency::getId, cid).one();
        if (ObjectUtils.isNotEmpty(one)){
            return one.getSymbol();
        }
        return null;
    }

    /**
     *
     * 查询上级邀请人
     */
    @RequestMapping("queryInviteUser")
    public String queryInviteUser(Integer uid){
        InviteRecord one = inviteRecordService.lambdaQuery().eq(InviteRecord::getUserId, uid).one();
        if (ObjectUtil.isNotEmpty(one)){
            //上级邀请人id
            Long inviteUserId = one.getInviteUserId();
            //根据上级邀请人id查询邀请人的账号
            String account = queryAccount(inviteUserId);
            return account;
        }
        return null;
    }

}
