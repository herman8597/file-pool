package network.vena.cooperation.websocket;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import network.vena.cooperation.common.utils.SpringUtils;
import network.vena.cooperation.websocket.entity.UserChatRecord;
import network.vena.cooperation.websocket.service.IUserChatRecordService;
import network.vena.cooperation.websocket.vo.RequestMessage;
import network.vena.cooperation.websocket.vo.ResponseMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

//@Component
@Slf4j
//@ServerEndpoint("/websocketUser/{info}")
//@Service
public class WebSocketUser {
	// 在线用户列表
	private static ConcurrentHashMap<String, WebSocketUser> onlineUsers = new ConcurrentHashMap<String, WebSocketUser>();
	// 会话
	private Session session;
	// 当前登录人id
	private String userId;
	//接收人
	private String toUserId;

	private IUserChatRecordService userChatRecordService = SpringUtils.getBean(IUserChatRecordService.class);

	/**
	 * 开启会话
	 * 
	 * @param session
	 * @param info
	 */
	@OnOpen
	public void onOpen(@PathParam("info") String info, Session session) {
		String[] arr= info.split("_");
		String userId = info;
		String toUserId = null;
		if(arr.length > 1) {
			userId = arr[0];
			toUserId = arr[1];
		}
		try {
			this.session = session;
			this.userId = userId;
			this.toUserId = toUserId;
			onlineUsers.put(userId, this);
			log.info("【websocket消息】有新的连接，user总数为:{}", onlineUsers.size());
			//sendToUserStatus();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OnClose
	public void onClose() {
		onlineUsers.remove(this.userId);
		log.info("【websocket消息】user:{},连接断开，user总数为:{}", this.userId, onlineUsers.size());
	}

	@OnMessage
	public void onMessage(String message) {
		log.info("【websocket消息】收到客户端消息:" + message);

		try {
			RequestMessage msg = JSON.parseObject(message, RequestMessage.class);
			String messageType = msg.getMessageType();
			// 1普通消息，2指定人消息，3全体人消息
			List<String> userIds = new ArrayList<String>();
			String pushType = null;
			if ("2".equals(messageType) && StringUtils.isNotEmpty(msg.getToUserIds())) {
				String[] split = StringUtils.split(msg.getToUserIds());
				//userIds = Arrays.asList(split);
				for (String id : split) {
					userIds.add(id);
				}
				userIds.add(this.userId);
			} else if ("3".equals(messageType)) {
				userIds = getAllUserIds();
			} else if("5".equals(messageType)) {
				pushType = "2";
				userIds.add(msg.getToUserIds());
			}
			sendMessageTo(userIds, msg.getContent(),pushType);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	@OnError
	public void onError(Session session, Throwable error) {
		log.info("userId:{},服务端发生了错误：{}", this.userId, error.getMessage());
		// error.printStackTrace();
	}
	
	private void sendToUserStatus() {
		if(!StringUtils.isEmpty(toUserId)) {
			//推送在线状态
			ResponseMessage res = new ResponseMessage();
			res.setMessageType("2");// 发送普通消息
			res.setOnline(this.isOnline(toUserId));
			session.getAsyncRemote().sendText(JSON.toJSONString(res));
		}
	}
	
	public boolean isOnline(String userId){
		boolean isOnline = true;
		WebSocketUser webSocket = onlineUsers.get(userId);
		if (webSocket == null || !webSocket.session.isOpen()) {
			isOnline = false;
		}
		return isOnline;
	}

	public void sendMessageTo(List<String> userIds, String message,String messageType) {
		if (CollectionUtils.isEmpty(userIds)) {
			return;
		}
		messageType = StringUtils.isEmpty(messageType)?"1":messageType;
		List<String> errorList = new ArrayList<String>();
		for (String userId : userIds) {
			WebSocketUser webSocket = onlineUsers.get(userId);
			if (!isOnline(userId)) {
				errorList.add(userId);
				continue;
			}
			ResponseMessage res = new ResponseMessage();
			res.setMessageType(messageType);// 发送普通消息
			res.setContent(message);
			webSocket.session.getAsyncRemote().sendText(JSON.toJSONString(res));
		}
		// 消息记录
		if("1".equals(messageType)) {
			List<UserChatRecord> recordList = new ArrayList<UserChatRecord>();
			for (String userId : userIds) {
				//过滤自己发的信息
				if(userId.equals(this.userId)) {
					continue;
				}
				UserChatRecord record = new UserChatRecord();
				record.setContent(message);
				record.setCreateTime(new Date());
				record.setIsRead(!errorList.contains(userId));//发送失败消息标记未读
				record.setSendUserId(this.userId);
				record.setReceiverUserId(userId);
				recordList.add(record);
			}
			if(!recordList.isEmpty()) {
				userChatRecordService.saveBatch(recordList);
			}		
		}
		
	}

	private List<String> getAllUserIds() {
		return null;
	}
}
