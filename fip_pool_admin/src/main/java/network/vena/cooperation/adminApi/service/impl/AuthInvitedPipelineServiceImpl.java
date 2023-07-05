package network.vena.cooperation.adminApi.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import network.vena.cooperation.adminApi.entity.AuthInvitedPipeline;
import network.vena.cooperation.adminApi.entity.AuthUser;
import network.vena.cooperation.adminApi.entity.BalanceModifyPipeline;
import network.vena.cooperation.adminApi.mapper.AuthInvitedPipelineMapper;
import network.vena.cooperation.adminApi.mapper.AuthUserMapper;
import network.vena.cooperation.adminApi.mapper.DistributionConfigMapper;
import network.vena.cooperation.adminApi.mapper.WeightMapper;
import network.vena.cooperation.adminApi.param.QueryParam;
import network.vena.cooperation.adminApi.service.IAuthInvitedPipelineService;
import network.vena.cooperation.adminApi.service.IAuthUserService;
import network.vena.cooperation.adminApi.service.IDistributionCheatService;
import network.vena.cooperation.adminApi.service.IDistributionConfigService;
import network.vena.cooperation.base.vo.InviteDetailPageVO;
import network.vena.cooperation.base.vo.InviteDetailVO;
import network.vena.cooperation.util.BigDecimalUtil;
import network.vena.cooperation.util.ListUtils;
import network.vena.cooperation.util.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: auth_invited_pipeline
 * @Author: jeecg-boot
 * @Date: 2020-04-13
 * @Version: V1.0
 */
@Service
public class AuthInvitedPipelineServiceImpl extends ServiceImpl<AuthInvitedPipelineMapper, AuthInvitedPipeline> implements IAuthInvitedPipelineService {


    @Autowired
    private AuthUserMapper authUserMapper;


    @Autowired
    private BalanceModifyPipelineServiceImpl balanceModifyPipelineService;

    @Autowired
    private WeightMapper weightMapper;

    @Autowired
    private DistributionConfigMapper distributionConfigMapper;

    @Override
    public InviteDetailPageVO inviteDetailByApiKey(QueryParam queryParam) {
        if (StringUtils.isNotBlank(queryParam.getAccount())) {
            queryParam.setApiKey(authUserMapper.getApiKeysByAccount(queryParam.getAccount()));
        }
        List<AuthInvitedPipeline> list = lambdaQuery().list();
        List<AuthInvitedPipeline> collect = list.stream().filter(e -> e.getInviteApiKey().equals(queryParam.getApiKey())).collect(Collectors.toList());
        InviteDetailPageVO inviteDetailPageVO = new InviteDetailPageVO();

        if (!ObjectUtils.isEmpty(collect)) {
            recursion(inviteDetailPageVO, collect, queryParam.getFixRelation(), 1, list);
        }

       /* List<String> oneApiKeys = lambdaQuery().select(AuthInvitedPipeline::getApiKey)
                .eq(AuthInvitedPipeline::getInviteApiKey, queryParam.getApiKey()).list().stream().map(AuthInvitedPipeline::getApiKey).collect(Collectors.toList());*/
        inviteDetailPageVO.setTotal(Long.valueOf(inviteDetailPageVO.getAuthInvitedPipelines().size()));
        List<AuthInvitedPipeline> authInvitedPipelines = ListUtils.ListPagingUtils(inviteDetailPageVO.getAuthInvitedPipelines(), queryParam.getPageNo(), queryParam.getPageSize());
        for (AuthInvitedPipeline authInvitedPipeline : authInvitedPipelines) {
            InviteDetailVO entity = authUserMapper.getAccount$Level$SelfPurchase$PaymentAmount(authInvitedPipeline.getApiKey());
            inviteDetailPageVO.setSelfPurchase(BigDecimalUtil.add(inviteDetailPageVO.getSelfPurchase(), entity.getSelfPurchase()));
            inviteDetailPageVO.setPaymentAmount(BigDecimalUtil.add(inviteDetailPageVO.getPaymentAmount(), entity.getPaymentAmount()));


            entity.setRelation(authInvitedPipeline.getRelation());
            entity.setApiKey(authInvitedPipeline.getApiKey());
            fullEntity(entity);

            inviteDetailPageVO.getInviteDetailVOS().add(entity);
        }
        inviteDetailPageVO.setSelfPurchase(BigDecimalUtil.format(inviteDetailPageVO.getSelfPurchase(),3));
        inviteDetailPageVO.setPaymentAmount(BigDecimalUtil.format(inviteDetailPageVO.getPaymentAmount(),3));
        return inviteDetailPageVO;
    }

    private void fullEntity(InviteDetailVO entity) {
        BigDecimal immediateBrokerage=BigDecimal.ZERO;
        BigDecimal indirectBrokerage=BigDecimal.ZERO;


        AuthInvitedPipeline one = lambdaQuery().eq(AuthInvitedPipeline::getApiKey, entity.getApiKey()).one();
        if (!ObjectUtils.isEmpty(one)) {
            List<BalanceModifyPipeline> oneBalanceModifys = balanceModifyPipelineService.lambdaQuery().eq(BalanceModifyPipeline::getApiKey, one.getInviteApiKey()).eq(BalanceModifyPipeline::getType,12) .list();
            for (BalanceModifyPipeline balanceModify : oneBalanceModifys) {
                if (StringUtils.isNotBlank(balanceModify.getRemark())) {
                    String remark = balanceModify.getRemark();
                    JSONObject jsonObject = JSONObject.parseObject(remark);
                    if (!StringUtils.isAnyBlank(jsonObject.getString("apiKey"),jsonObject.getString("round"))){
                        String apiKey = jsonObject.getString("apiKey");
                        String round = jsonObject.getString("round");
                        if (StringUtils.equals(apiKey,entity.getApiKey())) {
                            if (StringUtils.equals("1",round)) {
                                immediateBrokerage= BigDecimalUtil.add(immediateBrokerage,balanceModify.getQuantity());
                            }
                        }
                    }
                }
            }

            AuthInvitedPipeline two = lambdaQuery().eq(AuthInvitedPipeline::getApiKey, one.getInviteApiKey()).one();
            if (!ObjectUtils.isEmpty(two)) {
                List<BalanceModifyPipeline> twoBalanceModifys = balanceModifyPipelineService.lambdaQuery().eq(BalanceModifyPipeline::getApiKey, two.getInviteApiKey()).eq(BalanceModifyPipeline::getType, 12).list();
                for (BalanceModifyPipeline balanceModify : twoBalanceModifys) {
                    if (StringUtils.isNotBlank(balanceModify.getRemark())) {
                        String remark = balanceModify.getRemark();
                        JSONObject jsonObject = JSONObject.parseObject(remark);
                        if (!StringUtils.isAnyBlank(jsonObject.getString("apiKey"),jsonObject.getString("round"))){
                            String apiKey = jsonObject.getString("apiKey");
                            String round = jsonObject.getString("round");
                            if (StringUtils.equals(apiKey,entity.getApiKey())) {
                                if (StringUtils.equals("2",round)) {
                                    indirectBrokerage= BigDecimalUtil.add(indirectBrokerage,balanceModify.getQuantity());
                                }
                            }
                        }
                    }
                }
            }

        }




        entity.setImmediateBrokerage(immediateBrokerage).setIndirectBrokerage(indirectBrokerage);
    }

    @Override
    public Page<InviteDetailVO> inviteDetailByApiKeyPage(QueryParam queryParam) {
        if (StringUtils.isNotBlank(queryParam.getAccount())) {
            queryParam.setApiKey(authUserMapper.getApiKeysByAccount(queryParam.getAccount()));
        }
        Page<InviteDetailVO> page = new Page<>();
        Integer size = getBaseMapper().inviteDetailByApiKey(queryParam).size();
        PageHelper.startPage(queryParam.getPageNo(), queryParam.getPageSize());
        List<InviteDetailVO> inviteDetailVOS = getBaseMapper().inviteDetailByApiKey(queryParam);
        PageHelper.clearPage();
        for (InviteDetailVO inviteDetailVO : inviteDetailVOS) {
            inviteDetailVO.setAccount(authUserMapper.getAccount(inviteDetailVO.getApiKey()));
            BigDecimal selfPurchase = weightMapper.sumPurchaseQuantityByApiKeyFil(inviteDetailVO.getApiKey());

            BigDecimal selfPurchaseXch = weightMapper.sumPurchaseQuantityByApiKeyXch(inviteDetailVO.getApiKey());
            if (ObjectUtils.isEmpty(selfPurchase)) {
                selfPurchase = BigDecimal.ZERO;
            }
            inviteDetailVO.setSelfPurchase(BigDecimalUtil.divide(selfPurchase, 1000));
            inviteDetailVO.setSelfPurchaseXch(BigDecimalUtil.divide(selfPurchaseXch, 1000));


            Integer level = distributionConfigMapper.getLevel(inviteDetailVO.getApiKey());
           // Integer level = distributionConfigService.getLevel(inviteDetailVO.getApiKey());
            inviteDetailVO.setLevel(level);
            List<String> oneApiKeys = lambdaQuery().select(AuthInvitedPipeline::getApiKey)
                    .eq(AuthInvitedPipeline::getInviteApiKey, inviteDetailVO.getApiKey()).list().stream().map(AuthInvitedPipeline::getApiKey).collect(Collectors.toList());
            BigDecimal total = BigDecimal.ZERO;
            BigDecimal totalXch = BigDecimal.ZERO;

            if (!ObjectUtils.isEmpty(oneApiKeys)) {
                //统计fil总算力
                total = BigDecimalUtil.add(total, weightMapper.sumQuantityInApiKeysFil(oneApiKeys));
                //统计xch总算力
                totalXch = BigDecimalUtil.add(total, weightMapper.sumQuantityInApiKeysXch(oneApiKeys));
                List<String> twoApiKeys = lambdaQuery().select(AuthInvitedPipeline::getApiKey)
                        .in(AuthInvitedPipeline::getInviteApiKey, oneApiKeys).list().stream().map(AuthInvitedPipeline::getApiKey).collect(Collectors.toList());
                if (!ObjectUtils.isEmpty(twoApiKeys)) {
                    //统计fil总算力
                    total = BigDecimalUtil.add(total, weightMapper.sumQuantityInApiKeysFil(twoApiKeys));
                    //统计xch总算力
                    totalXch = BigDecimalUtil.add(totalXch, weightMapper.sumQuantityInApiKeysXch(oneApiKeys));

                }
            }
            inviteDetailVO.setTeamHashrate(BigDecimalUtil.divide(total, 1000));
            inviteDetailVO.setTeamHashrateXch(BigDecimalUtil.divide(totalXch, 1000));
            /*BigDecimal two = BigDecimal.ZERO;
            if (!ObjectUtils.isEmpty(oneAll)) {
                List<String> oneApiKeys = oneAll.stream().map(AuthInvitedPipeline::getInviteApiKey).collect(Collectors.toList());
                one = BigDecimalUtil.divide(weightMapper.sumQuantityInApiKeys(oneApiKeys), 1000);
                List<AuthInvitedPipeline> twoAll = lambdaQuery().select(AuthInvitedPipeline::getApiKey).in(AuthInvitedPipeline::getApiKey, oneApiKeys).list();
                if (!ObjectUtils.isEmpty(twoAll)) {
                    List<String> twoApiKeys = twoAll.stream().map(AuthInvitedPipeline::getInviteApiKey).collect(Collectors.toList());
                    two = BigDecimalUtil.divide(weightMapper.sumQuantityInApiKeys(twoApiKeys), 1000);
                }
            }*/

        }
        page.setTotal(size);
        page.setRecords(inviteDetailVOS);
        return page;
    }

    private BigDecimal recursion(List<String> apiKeys, BigDecimal total) {
        total = BigDecimalUtil.add(total, BigDecimalUtil.divide(weightMapper.sumQuantityInApiKeys(apiKeys), 1000));
        List<String> result = lambdaQuery().select(AuthInvitedPipeline::getApiKey)
                .in(AuthInvitedPipeline::getInviteApiKey, apiKeys).list().stream().map(AuthInvitedPipeline::getApiKey).collect(Collectors.toList());
        if (!ObjectUtils.isEmpty(result)) {
            return recursion(result, total);
        }
        return total;
    }

    private void recursion(InviteDetailPageVO inviteDetailPageVO, List<AuthInvitedPipeline> authInvitedPipelines, Integer FixRelation, Integer relation, List<AuthInvitedPipeline> list) {

        if (!ObjectUtils.isEmpty(authInvitedPipelines) && relation < 50) {
            /*List<String> result = lambdaQuery().select(AuthInvitedPipeline::getApiKey)
                    .in(AuthInvitedPipeline::getInviteApiKey, authInvitedPipelines).list().stream().map(AuthInvitedPipeline::getApiKey).children(Collectors.toList());*/
            for (AuthInvitedPipeline authInvitedPipeline : authInvitedPipelines) {
                inviteDetailPageVO.getRelations().add(relation);
                authInvitedPipeline.setRelation(relation);
            }
            List<String> apiKeys = authInvitedPipelines.stream().map(AuthInvitedPipeline::getApiKey).collect(Collectors.toList());
            List<AuthInvitedPipeline> children = list.stream().filter(e -> apiKeys.contains(e.getInviteApiKey())).collect(Collectors.toList());
            if (ObjectUtils.isEmpty(FixRelation)) {
                inviteDetailPageVO.getAuthInvitedPipelines().addAll(authInvitedPipelines);
            } else if (!ObjectUtils.isEmpty(FixRelation)) {
                if (FixRelation == relation) {
                    inviteDetailPageVO.getAuthInvitedPipelines().addAll(authInvitedPipelines);
                }
            }
            recursion(inviteDetailPageVO, children, FixRelation, relation + 1, list);

                /*for (String apikey : result) {
                    InviteDetailVO entity = authUserMapper.getAccount$Level$SelfPurchase$PaymentAmount(apikey);
                    inviteDetailPageVO.setSelfPurchase(BigDecimalUtil.add(inviteDetailPageVO.getSelfPurchase(), entity.getSelfPurchase()));
                    inviteDetailPageVO.setPaymentAmount(BigDecimalUtil.add(inviteDetailPageVO.getPaymentAmount(), entity.getPaymentAmount()));
                    inviteDetailPageVO.getRelations().add(relation);
                    entity.setRelation(relation);
                    if (ObjectUtils.isEmpty(FixRelation)) {
                        inviteDetailPageVO.getInviteDetailVOS().add(entity);
                    } else if (!ObjectUtils.isEmpty(FixRelation) && FixRelation == relation) {
                        inviteDetailPageVO.getInviteDetailVOS().add(entity);
                    }
                }*/

        }
    }

}
