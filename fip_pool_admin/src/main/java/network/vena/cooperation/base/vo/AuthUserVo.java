package network.vena.cooperation.base.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import network.vena.cooperation.adminApi.entity.Balance;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: auth_user
 * @Author: jeecg-boot
 * @Date: 2020-04-13
 * @Version: V1.0
 */
@Data
public class AuthUserVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(type = IdType.ID_WORKER_STR)
    private Integer id;
    /**
     * 手机号
     */
    @Excel(name = "手机号", width = 15)
    private String phone;
    /**
     * 邮箱
     */
    @Excel(name = "邮箱", width = 15)
    private String email;
    /**
     * 昵称
     */
    @Excel(name = "昵称", width = 15)
    private String nickname;
    /**
     * 用户来源:OFFICIAL=官方节点
     */
    @Excel(name = "用户来源:OFFICIAL=官方节点", width = 15)
    private String nodeSource;

    @Excel(name = "账号", width = 15)
    private String account;
    @Excel(name = "邀请码", width = 15)
    private String invitationCode;
    @Excel(name = "邀请人", width = 15)
    private String inviter;
    @Excel(name = "createTime", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private Integer level;

    @Excel(name = "唯一api_key", width = 15)
    private String apiKey;
    /**
     * 0=解锁，1=已锁定
     */
    @Excel(name = "0=解锁，1=已锁定", width = 15)
    private Integer blocked;
    @Excel(name = "头像", width = 15)
    private String image;
    @Excel(name = "谷歌code", width = 15)
    private String gaSecret;


    private AwardVo awardVO;

    private List<Balance> balances;

    ArrayList<HashrateVo> hashrateVos;
}
