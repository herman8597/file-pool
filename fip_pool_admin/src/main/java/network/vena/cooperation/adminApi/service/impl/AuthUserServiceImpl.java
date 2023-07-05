package network.vena.cooperation.adminApi.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import network.vena.cooperation.adminApi.entity.*;
import network.vena.cooperation.adminApi.mapper.*;
import network.vena.cooperation.adminApi.param.AuthUserParam;
import network.vena.cooperation.adminApi.service.IAuthInvitationCodeService;
import network.vena.cooperation.adminApi.service.IAuthUserService;
import network.vena.cooperation.adminApi.service.IBalanceService;
import network.vena.cooperation.adminApi.vo.BalanceModifyPipelineRemarkVo;
import network.vena.cooperation.base.vo.AuthUserEditVo;
import network.vena.cooperation.base.vo.AuthUserVo;
import network.vena.cooperation.base.vo.AwardVo;
import network.vena.cooperation.base.vo.HashrateVo;
import network.vena.cooperation.util.BeanUtils;
import network.vena.cooperation.util.BigDecimalUtil;
import network.vena.cooperation.util.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: auth_user
 * @Author: jeecg-boot
 * @Date: 2020-04-13
 * @Version: V1.0
 */
@Service
public class AuthUserServiceImpl extends ServiceImpl<AuthUserMapper, AuthUser> implements IAuthUserService {

    @Autowired
    private DistributionDetailServiceImpl distributionDetailService;

    @Autowired
    private DistributionConfigServiceImpl distributionConfigService;

    @Autowired
    private IAuthInvitationCodeService iAuthInvitationCodeService;


    @Autowired
    private BalanceModifyPipelineMapper balanceModifyPipelineMapper;

    @Autowired
    private IBalanceService balanceService;

    @Autowired
    private WeightMapper weightMapper;

    @Autowired
    private DistributionCheatServiceImpl distributionCheatService;

    @Autowired
    private AuthInvitedPipelineServiceImpl authInvitedPipelineService;

    @Autowired
    private OperatingIncomeServiceImpl operatingIncomeService;

    @Value("${spring.profiles.active}")
    private String active;

    @Override
    public AuthUserEditVo getEditVo(String id) {
        QueryWrapper<AuthUser> authUserQueryWrapper = new QueryWrapper<>();
        authUserQueryWrapper.select("phone,ga_secret,email,api_key,account,create_time,node_source,`lock`");
        authUserQueryWrapper.eq("id", id);
/*        AuthUser authUser = lambdaQuery()
                .select(AuthUser::getPhone, AuthUser::getEmail, AuthUser::getApiKey,AuthUser::getAccount,AuthUser::getCreateTime,AuthUser::getNodeSource,AuthUser::getLock)
                .eq(AuthUser::getId, id).one();*/
        AuthUser authUser = getOne(authUserQueryWrapper);
        AuthUserEditVo authUserEditVo = new AuthUserEditVo();
        BeanUtils.copyProperties(authUser, authUserEditVo);
        Integer levle = distributionConfigService.getLevel(authUser.getApiKey());
        if (ObjectUtils.isEmpty(levle)) {
            authUserEditVo.setLevel(-1);
        }
        authUserEditVo.setLevel(levle);
        AuthInvitationCode authInvitationCode = iAuthInvitationCodeService.lambdaQuery()
                .select(AuthInvitationCode::getInvitationCode)
                .eq(AuthInvitationCode::getApiKey, authUser.getApiKey()).one();
        authUserEditVo.setInvitationCode(authInvitationCode.getInvitationCode());
        String invite = authInvitedPipelineService.getBaseMapper().getInviter(authUser.getApiKey());
        authUserEditVo.setInviter(invite);
        return authUserEditVo;
    }

    @Override
    public BigDecimal getAuthUserDetailTwo(String id) {
        AuthUserEditVo editVo = getEditVo(id);
        String apiKey = editVo.getApiKey();
        BigDecimal bigDecimal = weightMapper.sumQuantityTwo(apiKey);
        return bigDecimal;
    }

    @Override
    public AuthUserVo getAuthUserDetail(String id) {

        AuthUserEditVo editVo = getEditVo(id);
        AuthUserVo authUserVo = new AuthUserVo();
        BeanUtils.copyProperties(editVo, authUserVo);

        //--

        //List<Weight> list = weightService.lambdaQuery().eq(Weight::getApiKey, editVo.getApiKey()).eq(Weight::getType, 8).eq(Weight::getStatus, 1).list();

        BigDecimal one = BigDecimal.ZERO;
        BigDecimal two = BigDecimal.ZERO;

        /*List<String> oneApiKeys = authInvitedPipelineService.lambdaQuery().select(AuthInvitedPipeline::getApiKey)
                .eq(AuthInvitedPipeline::getInviteApiKey, authUserVo.getApiKey()).list().stream().map(AuthInvitedPipeline::getApiKey).collect(Collectors.toList());

        if (!ObjectUtils.isEmpty(oneApiKeys)) {
            one = BigDecimalUtil.divide(weightMapper.sumQuantityInApiKeys(oneApiKeys), 1000);
            List<String> twoApiKeys = authInvitedPipelineService.lambdaQuery().select(AuthInvitedPipeline::getApiKey).in(AuthInvitedPipeline::getInviteApiKey, oneApiKeys).list()
                    .stream().map(AuthInvitedPipeline::getApiKey).collect(Collectors.toList());
            if (!ObjectUtils.isEmpty(twoApiKeys)) {
                two = BigDecimalUtil.divide(weightMapper.sumQuantityInApiKeys(twoApiKeys), 1000);
            }
        }*/
        List<BalanceModifyPipeline> rebateRecord = balanceModifyPipelineMapper.getRebateRecord(authUserVo.getApiKey());

        for (BalanceModifyPipeline balanceModifyPipeline : rebateRecord) {
            if (!ObjectUtils.isEmpty(balanceModifyPipeline.getRemark())) {
                BalanceModifyPipelineRemarkVo balanceModifyPipelineRemarkVo = JSONObject.parseObject(balanceModifyPipeline.getRemark(), BalanceModifyPipelineRemarkVo.class);
                BigDecimal divide = BigDecimalUtil.divide(balanceModifyPipelineRemarkVo.getOrderQuantity(), 1000);
                if (StringUtils.equals(balanceModifyPipelineRemarkVo.getRound(), "1")) {
                    one = BigDecimalUtil.add(one, divide);
                } else if (StringUtils.equals(balanceModifyPipelineRemarkVo.getRound(), "2")) {
                    two = BigDecimalUtil.add(two, divide);
                }
            }
        }





        /*for (Weight weight : list) {
            String remark = weight.getRemark();
                JSONObject jsonObject = JSONObject.parseObject(remark);
            Object roundobj = jsonObject.get("round");
            if (!ObjectUtils.isEmpty(roundobj)) {
                String round = roundobj.toString();
                if ("1".equals(round)) {
                    if (weight.getUnit().equals("GB")) {
                        weight.setQuantity(BigDecimalUtil.divide(weight.getQuantity(), 1000));
                    }
                    one = BigDecimalUtil.add(one, weight.getQuantity());
                } else if ("2".equals(round)) {
                    if (weight.getUnit().equals("GB")) {
                        weight.setQuantity(BigDecimalUtil.divide(weight.getQuantity(), 1000));
                    }
                    two = BigDecimalUtil.add(two, weight.getQuantity());
                }
            }
        }*/
        //查询用户的运营奖励记录
        List<OperatingIncome> list = operatingIncomeService.lambdaQuery()
                .orderByDesc(OperatingIncome::getCreateTime)
                .eq(OperatingIncome::getApiKey, authUserVo.getApiKey()).list();

        AwardVo awardVO = new AwardVo();

        if (ObjectUtil.isNotEmpty(list)){
            awardVO.setChildrenPurchase(list.get(0).getTotal());
        }else{
            awardVO.setChildrenPurchase(BigDecimal.ZERO);
        }

//        awardVO.setChildrenPurchase(one);
        awardVO.setGrandchildrenPurchase(two);
        if (StringUtils.equalsAny(active, "dev", "hulihui","hulihuitest")) {
            getAllInviteCount(Arrays.asList(authUserVo.getApiKey()), awardVO);
        } else {
            Integer invitedCumulative = authInvitedPipelineService.getBaseMapper().getInvitedCumulative(editVo.getApiKey());
            awardVO.setHashrateTotal(BigDecimalUtil.add(one, two));
            awardVO.setTotalCount(invitedCumulative);
        }


        BigDecimal award = balanceModifyPipelineMapper.sumRewardsDistribution(editVo.getApiKey());
        if (!ObjectUtils.isEmpty(award)) {
            awardVO.setAward(award);
        }
        authUserVo.setAwardVO(awardVO);

        //distribution
        BigDecimal distribution = balanceModifyPipelineMapper.sumRewardsDistribution(editVo.getApiKey());
        awardVO.setDistribution(distribution);
        //----

        List<Balance> balances = balanceService.lambdaQuery().eq(Balance::getApiKey, editVo.getApiKey()).list();
        for (Balance balance : balances) {
            BigDecimal add = BigDecimalUtil.add(balance.getFrozen(), balance.getAvailable());
            BigDecimal format = BigDecimalUtil.format(add, 3);
            balance.setTotal(format);

        }
        authUserVo.setBalances(balances);
        //--
        List<Weight> weights = weightMapper.listValidHashrate(editVo.getApiKey(), new Date());


        Set<Integer> activity = Set.of(2, 5, 6); //活动奖励 type 6
        Set<Integer> rests = Set.of(4, 8); //推广奖励 type 8

        for (Weight weightDAO : weights) {
            if (activity.contains(weightDAO.getType())) {
                weightDAO.setType(6);
            } else if (rests.contains(weightDAO.getType())) {
                weightDAO.setType(8);
            } else if (BigDecimalUtil.equal(weightDAO.getServiceChargeRate(), 0.2)) {
                weightDAO.setType(10);
            }
        }


        Map<Integer, List<Weight>> type$map = weights.stream().collect(Collectors.groupingBy(Weight::getType));

        ArrayList<HashrateVo> hashrateVos = new ArrayList<>();
        type$map.forEach((k, v) -> {
            BigDecimal reduce = v.stream().filter(x->x.getPowerType()==1).map(e -> {
                if ("GB".equals(e.getUnit())) {
                    BigDecimal divide = BigDecimalUtil.divide(e.getQuantity(), 1000);
                    return divide;
                } else {
                    return e.getQuantity();
                }
            }).reduce(BigDecimal.ZERO, BigDecimal::add);
            HashrateVo hashrateVO = new HashrateVo().setType(k).setHashrate(BigDecimalUtil.format(reduce, 3));
            hashrateVos.add(hashrateVO);
        });
        authUserVo.setHashrateVos(hashrateVos);
        return authUserVo;
    }



    private AwardVo getAllInviteCount(List<String> apiKeys, AwardVo awardVo) {
        List<AuthInvitedPipeline> list = authInvitedPipelineService.lambdaQuery().in(AuthInvitedPipeline::getInviteApiKey, apiKeys).list();
        if (!ObjectUtils.isEmpty(list)) {
            List<String> childrenApiKeys = list.stream().map(AuthInvitedPipeline::getApiKey).collect(Collectors.toList());
            awardVo.setTotalCount(awardVo.getTotalCount() + childrenApiKeys.size());
            BigDecimal add = BigDecimalUtil.add(BigDecimalUtil.divide(weightMapper.sumQuantityInApiKeys(childrenApiKeys), 1000), awardVo.getHashrateTotal());
            awardVo.setHashrateTotal(add);
            return getAllInviteCount(childrenApiKeys, awardVo);
        }
        return awardVo;
    }

    @Override
    @Transactional
    @CacheEvict(value = "getByApiKey", key = "#authUserParam.apiKey")
    public void edit(AuthUserParam authUserParam) {
        DistributionDetail distributionDetail = distributionDetailService.lambdaQuery().eq(DistributionDetail::getApiKey, authUserParam.getApiKey()).one();
        if (!ObjectUtils.isEmpty(distributionDetail)) {
            distributionDetail.setLevel(authUserParam.getLevel());
            if (!ObjectUtils.isEmpty(authUserParam.getMarketLevel())) {
                distributionDetail.setMarketLevel(authUserParam.getMarketLevel());
            }
            if (!ObjectUtils.isEmpty(authUserParam.getOperatingLevel())) {
                distributionDetail.setOperatingLevel(authUserParam.getOperatingLevel());
            }
            distributionDetailService.saveOrUpdate(distributionDetail);
        }
        AuthUser authUser = lambdaQuery().eq(AuthUser::getApiKey, authUserParam.getApiKey()).one();

        DistributionCheat distributionCheat = distributionCheatService.lambdaQuery().eq(DistributionCheat::getApiKey, authUser.getApiKey()).one();
        if (ObjectUtils.isEmpty(distributionCheat)) {
            distributionCheat = new DistributionCheat().setApiKey(authUser.getApiKey()).setUpdateTime(new Date());
        }
        distributionCheat.setLevelDirect(Integer.valueOf(authUserParam.getLevel()));
        distributionCheatService.saveOrUpdate(distributionCheat);
        if (!ObjectUtils.isEmpty(authUser)) {
            authUser.setLock(authUserParam.getLock());
            saveOrUpdate(authUser);
        }
    }


    @CacheEvict(value = "getAuthUserByCache", key = "#apiKey")
    public AuthUser getAuthUserByCache(String apiKey) {
        AuthUser authUser = lambdaQuery().eq(AuthUser::getApiKey, apiKey).one();
        if (ObjectUtils.isEmpty(authUser)) {
            return null;
        }
        return authUser;
    }

}
