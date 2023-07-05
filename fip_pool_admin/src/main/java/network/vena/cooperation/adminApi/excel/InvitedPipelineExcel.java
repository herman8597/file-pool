package network.vena.cooperation.adminApi.excel;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: fengxiyu
 * @Date: 2021/09/06/11:51
 * @Description:
 */
@Data
public class InvitedPipelineExcel {

    /**邀请者的账号*/
    @Excel(name = "邀请者的账号", width = 15)
    private String inviteApiAccount;


    /**apiKey账号*/
    @Excel(name = "被邀请者的账号", width = 15)
    private String apiAccount;


}
