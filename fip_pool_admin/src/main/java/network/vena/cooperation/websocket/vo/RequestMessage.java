package network.vena.cooperation.websocket.vo;

import lombok.Data;

/**
 * 接收消息
 * @author Administrator
 *
 */
@Data
public class RequestMessage {

	/**
	 * 发送类型，1普通消息，2指定人消息，3全体人消息,4查询在线人数
	 */
	private String messageType;
	
	/**
	 * 消息内容
	 */
	private String content;
	
	/**
	 * 发送指定人
	 */
	private String toUserIds;
	
	
}
