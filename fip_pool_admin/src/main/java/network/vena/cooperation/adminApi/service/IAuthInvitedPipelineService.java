package network.vena.cooperation.adminApi.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import network.vena.cooperation.adminApi.entity.AuthInvitedPipeline;
import com.baomidou.mybatisplus.extension.service.IService;
import network.vena.cooperation.adminApi.param.QueryParam;
import network.vena.cooperation.base.vo.InviteDetailPageVO;
import network.vena.cooperation.base.vo.InviteDetailVO;

import java.util.List;

/**
 * @Description: auth_invited_pipeline
 * @Author: jeecg-boot
 * @Date: 2020-04-13
 * @Version: V1.0
 */
public interface IAuthInvitedPipelineService extends IService<AuthInvitedPipeline> {


    InviteDetailPageVO inviteDetailByApiKey(QueryParam queryParam);

    Page<InviteDetailVO> inviteDetailByApiKeyPage(QueryParam queryParam);


}
