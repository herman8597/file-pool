package network.vena.cooperation.websocket.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@TableName("mall_user_chat_record")
public class UserChatRecord {

	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	/**
	 * 发送人
	 */
	@TableField("send_user_id")
	private String sendUserId;
	/**
	 * 接收人
	 */
	@TableField("receiver_user_id")
	private String receiverUserId;
	/**
	 * 聊天内容
	 */
	@TableField("content")
	private String content;
	/**
	 * 聊天时间
	 */
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@TableField("create_time")
	private Date createTime;
	/**
	 * 是否已读
	 */
	@TableField("is_read")
	private Boolean isRead;
}
