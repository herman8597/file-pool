package io.filpool.pool.controller.admin;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.filpool.framework.util.AccountUtil;
import io.filpool.pool.entity.Currency;
import io.filpool.pool.entity.InviteRecord;
import io.filpool.pool.entity.ReturnedRewardConfig;
import io.filpool.pool.entity.User;
import io.filpool.pool.mapper.ReturnedRewardConfigMapper;
import io.filpool.pool.service.InviteRecordService;
import io.filpool.pool.service.impl.CurrencyServiceImpl;
import io.filpool.pool.service.impl.UserServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SysUtilController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private CurrencyServiceImpl currencyService;

    @Autowired
    private InviteRecordService inviteRecordService;

    @Autowired
    private ReturnedRewardConfigMapper returnedRewardConfigMapper;

    /**
     *
     * 根据用户id查询用户的账号
     */
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
    public Long queryUserId(String account){
        if (AccountUtil.checkEmail(account)){
            User one = userService.lambdaQuery().eq(User::getEmail,account).one();
            if (ObjectUtil.isNotEmpty(one)){
                return one.getId();
            }else{
                return 0L;
            }
        }else{
            User one = userService.lambdaQuery().eq(User::getMobile,account).one();
            if (ObjectUtil.isNotEmpty(one)){
                return one.getId();
            }else{
                return 0L;
            }
        }
    }

    /***
     * 根据用户id查询用户市场登记
     */
    public Integer markLevel(Long userId){
        //查询用户积分
        User one = userService.lambdaQuery().eq(User::getId, userId).one();
        //根据积分查询用户等级
        if (ObjectUtils.isNotEmpty(one)){
            ReturnedRewardConfig returnedRewardConfigList = returnedRewardConfigMapper.getConfigByPower(one.getCommunityExperience(), 2);
            //如果查到了，该怎么返回怎么返回，没查到给0
            if (ObjectUtils.isNotEmpty(returnedRewardConfigList)){
                return returnedRewardConfigList.getLevel();
            }else{
                return 0;
            }
        }else{
            return 0;
        }
    }



    /**
     *
     * 根据币种id查询币种名称
     */
    public String querySymbol(Integer cid){
        Currency one = currencyService.lambdaQuery().eq(Currency::getId, cid).one();
        if (ObjectUtils.isNotEmpty(one)){
            return one.getSymbol();
        }
        return null;
    }

    /**
     *
     * 根据币种名称查询币种id
     */
    public Long querySymbol(String symbol){
        if ("USDT".equals(symbol) || symbol=="USDT"){
            return 1L;
        }else{
            Currency one = currencyService.lambdaQuery().eq(Currency::getSymbol, symbol).one();
            return one.getId();
        }
    }

    /**
     *
     * 查询上级邀请人
     */
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



    /**
     *
     * 查询该用户伞下用户信息（无限递归查询下级）
     */
    public List<Long> subordinateUser(Long uid){
        Map<Long, Long> apiKeyMap = new HashMap<>();

        //邀请记录表中的所有数据信息
        List<InviteRecord> list = inviteRecordService.list();

        for (InviteRecord inviteRecord:list) {
            apiKeyMap.put(inviteRecord.getUserId(),inviteRecord.getInviteUserId());
        }
        //收集所有下级的userid
        List<Long> apikeyList= new ArrayList<>();

        List<Long> strings = TestThree(apiKeyMap, uid, apikeyList);
        System.out.println(strings);
        return strings;
    }


    public List<Long> TestThree(Map<Long, Long> apiKeyMap, Long uid, List<Long> apikeyList){
        Map<Long, Long> hMap = new HashMap<>();
        for (Map.Entry<Long, Long> entry:apiKeyMap.entrySet()){
            if (uid.equals(entry.getValue())){
                //存储子子孙孙，孙孙子子
                apikeyList.add(entry.getKey());
                //存储直接下级(key=上级，value=下级)
                hMap.put(entry.getValue(),entry.getKey());
                if (ObjectUtil.isNotEmpty(hMap)){
                    for (Map.Entry<Long, Long> entryTwo:hMap.entrySet()){
                        TestThree(apiKeyMap,entryTwo.getValue(),apikeyList);
                    }
                }
                else{
                    return apikeyList;
                }
            }
        }
        return apikeyList;
    }

    /**
     *
     *查询该用户与祖宗之间的距离
     * 用户id:userId
     * 祖宗id:ancestorsId
     */
    public Integer contextLoads(Long userId,Long ancestorsId){
        Map<Long, Long> apiKeyMap = new HashMap<>();
        //邀请记录表中的所有数据信息
        List<InviteRecord> list = inviteRecordService.list();

        for (InviteRecord inviteRecord:list) {
            apiKeyMap.put(inviteRecord.getUserId(),inviteRecord.getInviteUserId());
        }
        //收集所有下级的userid
        List<Long> apikeyList= new ArrayList<>();
        //初始化该用户与祖宗用户之间的距离
        Integer distance=1;

        return test(userId,ancestorsId,apiKeyMap,distance);

    }

    public Integer test(Long userId,Long ancestorsId,Map<Long, Long> apiKeyMap,Integer distance){
        Long aLong = apiKeyMap.get(userId);
        if (ObjectUtil.isNotEmpty(aLong)){
            if (aLong==ancestorsId || aLong.equals(ancestorsId)){
                return distance;
            }
            distance++;
            return test(aLong,ancestorsId,apiKeyMap,distance);
        }else{
            return distance;
        }
    }


    /**
     *
     *查询该用户上面的第一个合伙人的用户信息
     * 用户id:userId
     */
    public User cooperativePartner(Long userId){
        Map<Long, Long> apiKeyMap = new HashMap<>();
        //邀请记录表中的所有数据信息
        List<InviteRecord> list = inviteRecordService.list();

        for (InviteRecord inviteRecord:list) {
            apiKeyMap.put(inviteRecord.getUserId(),inviteRecord.getInviteUserId());
        }
        return cooperativePartnereTwo(userId,apiKeyMap);
    }

    public User cooperativePartnereTwo(Long userId,Map<Long, Long> apiKeyMap){
        Long aLong = apiKeyMap.get(userId);
        if (ObjectUtil.isNotEmpty(aLong)){
            //根据上级用户id查询用户详细信息
            User one = userService.lambdaQuery().eq(User::getId, aLong).one();
            if (ObjectUtil.isNotEmpty(one)){
                //判断该用户是否是合作伙伴
                if (one.getType()==1){
                    return one;
                }
            }
            return cooperativePartnereTwo(aLong,apiKeyMap);
        }else{
            return null;
        }
    }


}
