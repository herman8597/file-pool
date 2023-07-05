package io.filpool.scheduled.websocket;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import io.filpool.framework.util.Constants;
import io.filpool.framework.util.RedisUtil;
import io.filpool.framework.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Author scott
 * @Date 2019/11/29 9:41
 * @Description: 此注解相当于设置访问URL
 */
@Component
@Slf4j
@ServerEndpoint("/getFilInfo") //此注解相当于设置访问URL
public class WebSocketFilInfo {


    private Session session;

    private String userId;

    private static CopyOnWriteArraySet<WebSocketFilInfo> webSocketTransactions = new CopyOnWriteArraySet<>();

    private static Map<String, Session> sessionPool = new HashMap<String, Session>();

    private RedisUtil redisUtil;

    private void init() {
        redisUtil =  SpringContextUtil.getBean(RedisUtil.class);
    }

    @OnOpen
    //public void onOpen(Session session, @PathParam(value = "token") String token) throws Exception {
    public void onOpen(Session session) throws Exception {
        if (ObjectUtils.isEmpty(session.getId())) {
            return;
        }

        init();
        /*String userId = validateToken(token);
        if (ObjectUtils.isEmpty(userId)) {
            return;
        }*/
        this.userId = session.getId();
//        log.info("开启链接_{}", userId);
        this.session = session;
        //this.token = token;
        webSocketTransactions.add(this);
        sessionPool.put(this.userId, session);
//        log.info("【币价行情】有新的连接，总数为:" + webSocketTransactions.size());

        Object obj = redisUtil.get(Constants.FIL_INFO);
        if (!ObjectUtils.isEmpty(obj))
            session.getAsyncRemote().sendText(obj.toString());
    }



    @OnClose
    public void onClose() {
        try {
            webSocketTransactions.remove(this);
            //redisUtil.releaseLock(CommonRedisKey.LOCK_ORDER + userId);
//            log.info("【币价行情】连接断开，总数为:" + webSocketTransactions.size());
        } catch (Exception e) {
            //log.error(e.getMessage());
        }
    }


    @OnMessage
    public void onMessage(String message) throws Exception {
        session.getAsyncRemote().sendText("pong");
    }


    // 此为广播消息
    public void sendAllMessage(String message) {
//        log.info("【币价行情】广播消息:" + message);
        for (WebSocketFilInfo webSocketTransaction : webSocketTransactions) {
            try {
                if (webSocketTransaction.session.isOpen()) {
                    webSocketTransaction.session.getAsyncRemote().sendText(message);
                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
    }

    // 此为单点消息
    public void sendOneMessage(Map<String, String> bizMap) throws Exception {
        Session session = sessionPool.get(bizMap.get("userId"));
        if (session != null && session.isOpen()) {
            session.getAsyncRemote().sendText(JSON.toJSONString(bizMap));
            session.close();
        } else {
            //如果长连接断开， 则商家需到交易查询查该笔交易（自动查询最新交易），商家可重新打印小票
            //webHttpUtil.postBySign()
            //建议发起融云推送
        }
    }

    // 此为单点消息(多人)
    public void sendMoreMessage(String[] userIds, String message) {
        for (String userId : userIds) {
            Session session = sessionPool.get(userId);
            if (session != null && session.isOpen()) {
                try {
//                    log.info("【币价行情】 单点消息:" + message);
                    session.getAsyncRemote().sendText(message);
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }
        }

    }


}
