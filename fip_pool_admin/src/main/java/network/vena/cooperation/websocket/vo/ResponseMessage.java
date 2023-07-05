package network.vena.cooperation.websocket.vo;

import lombok.Data;

import java.util.List;

/**
 * 回应消息
 * @author Administrator
 *
 */
@Data
public class ResponseMessage {

	/**
	 * 1普通消息， 2在线名单，2用户上线，3用户下线
	 */
	private String messageType;
	

	/**
	 * 是否已读
	 */
	private boolean isRead = false;
	
	/**
	 * 消息内容
	 */
	private String content;
	
	/**
	 * 关注的消息
	 */
	private boolean focusFlag;
	
	private boolean isOnline;
	
	private List<String> onlineUsers;
}
