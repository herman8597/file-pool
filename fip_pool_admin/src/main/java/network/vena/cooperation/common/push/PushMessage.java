package network.vena.cooperation.common.push;

import com.gexin.fastjson.JSON;
import com.gexin.rp.sdk.base.IBatch;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.notify.Notify;
import com.gexin.rp.sdk.dto.GtReq;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.AbstractTemplate;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.gexin.rp.sdk.template.style.Style0;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PushMessage {

    private static String appId;
    private static String masterSecret;
    private static String appKey;
    private static String url;
    private static IGtPush push = null;

    static {
         appId = PushDTO.getAppId();
         masterSecret = PushDTO.getMasterSecret();
         appKey = PushDTO.getAppKey();
         url = PushDTO.getUrl();
         push = new IGtPush(url, appKey, masterSecret);
    }



    /*
     *
     * 推送消息（透传内容）
     *
     * @param cids 用户的cid
     * @param title 标签
     * @param content 内容
     * @param url 跳转页
     * @author : llj
     * @date : 2019/12/10 18:25
     */
    public static void sendMsgByAll(String title,String content, String url){
        System.out.println("发" + url + appId);
        Map<String,String> map  = new HashMap<>();
        map.put("title", title);
        map.put("content", content);
        map.put("url", url);
        String msg = JSON.toJSONString(map);
        TransmissionTemplate template = getTransmissionTemplate(msg);
        AppMessage message = getAppMessage(template);
        IPushResult ret = push.pushMessageToApp(message);
        System.out.println(ret.getResponse());
    }

    /*
     *
     * 推送消息（透传内容）
     *
     * @param cids 用户的cid
     * @param title 标签
     * @param content 内容
     * @param url 跳转页
     * @author : llj
     * @date : 2019/12/10 18:25
     */
    public  void sendMsg(List<String> cids,String title,String content, String url){
        Map<String,String> map  = new HashMap<>();
        map.put("title", title);
        map.put("content", content);
        map.put("url", url);
        sendMsg(cids, JSON.toJSONString(map));
    }

    /*
    *
     * 推送消息（透传内容）
     *
     * @param cids 用户的cid
     * @param msg 推送内容
     * @return :
     * @author : llj
     * @date : 2019/12/10 18:24
     */
    public  void sendMsg(List<String> cids,String msg){
        IBatch batch = push.getBatch();
        try {
            for (String cid: cids) {

                TransmissionTemplate template = getTransmissionTemplate(msg);

                // 单推消息类型
                SingleMessage message = getSingleMessage(template);

                // 设置推送目标，填入appid和clientId
                Target target = new Target();
                target.setAppId(appId);
                target.setClientId(cid);
                batch.add(message, target);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            batch.submit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * 选择透传内容
    *
    * @return :
    * @author : llj
    * @date : 2019/12/10 18:08
    */
    private  void transmissionTemplate(){

        TransmissionTemplate template = getTransmissionTemplate("内容");

        // 单推消息类型
        SingleMessage message = getSingleMessage(template);
        Target target = new Target();
        target.setAppId(appId);
        target.setClientId("f695f74c4c1eceb2b11fe1a4ef7a49eb");
        // target.setAlias(ALIAS); //别名需要提前绑定

        // STEP6：执行推送
        //		IPushResult ret = push.pushMessageToApp(message);

		IPushResult ret = null;
		try {
			ret = push.pushMessageToSingle(message, target);
		} catch (RequestException e) {
			e.printStackTrace();
			ret = push.pushMessageToSingle(message, target, e.getRequestId());
		}
		if (ret != null) {
			System.out.println(ret.getResponse().toString());
		} else {
			System.out.println("服务器响应异常");
		}
    }


    /*
    *
     选择通知模板
     *
     * @param  1
     * @return :
     * @author : llj
     * @date : 2019/12/10 18:08
     */
    private  void notificationTemplate() {

		Style0 style = new Style0();
		// STEP2：设置推送标题、推送内容
		style.setTitle("请输入通知栏标题");
		style.setText("请输入通知栏内容");
		style.setLogo("push.png");  // 设置推送图标
		// STEP3：设置响铃、震动等推送效果
		style.setRing(true);  // 设置响铃
		style.setVibrate(true);  // 设置震动


		// STEP4：选择通知模板
		NotificationTemplate template = new NotificationTemplate();
		template.setAppId(appId);
		template.setAppkey(appKey);
		template.setStyle(style);


		// STEP5：定义"AppMessage"类型消息对象,设置推送消息有效期等推送参数
		List<String> appIds = new ArrayList<String>();
		appIds.add(appId);
		AppMessage message = getAppMessage(template);

		// STEP6：执行推送
		IPushResult ret = push.pushMessageToApp(message);
		System.out.println(ret.getResponse().toString());
	}

    private static TransmissionTemplate getTransmissionTemplate(String msg){
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setTransmissionContent(msg);
        // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
        template.setTransmissionType(2);
        return template;
    }

   /*
     *
     * 对指定应用的所有（或符合筛选条件的）用户群发推送消息。有定时、定速功能。
     *
     * @param template 1
     * @return: com.gexin.rp.sdk.base.impl.AppMessage
     * @author : llj
     * @date : 2019/12/10 18:01
     */
    private static   AppMessage getAppMessage(AbstractTemplate template) {
        //  STEP5：定义"AppMessage"类型消息对象,设置推送消息有效期等推送参数
        List<String> appIds = new ArrayList<String>();
        appIds.add(appId);
        AppMessage message = new AppMessage();
        message.setData(template);
        message.setAppIdList(appIds);
        message.setOffline(true);
        message.setOfflineExpireTime(1000 * 600);  // 时间单位为毫秒
        return  message;
    }


    /*
    *
     * 向单个用户发送消息，可根据cid或别名指定用户。
     *
     * @param template 1
     * @return: com.gexin.rp.sdk.base.impl.SingleMessage
     * @author : llj
     * @date : 2019/12/10 17:56
     */
    private  SingleMessage getSingleMessage(AbstractTemplate template) {
        SingleMessage message = new SingleMessage();
        message.setData(template);
        // 设置消息离线，并设置离线时间
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(72 * 3600 * 1000);
        message.setPriority(1);
        message.setPushNetWorkType(0); // 判断客户端是否wifi环境下推送。1为仅在wifi环境下推送，0为不限制网络环境，默认不限
        return message;
    }

    /*
    *
     * 设置第三方通知
     *
     * @param template 1
     * @param title 2
     * @param content 3
     * @author : llj
     * @date : 2019/12/10 17:56
     */
    private  void setNotify(TransmissionTemplate template, String title, String content) {
        Notify notify = new Notify();
		notify.setTitle(title);
		notify.setContent(content);
		notify.setIntent("intent:#Intent;action=android.intent.action.oppopush;launchFlags=0x14000000;component=com.yxkj.shopmallapp/io.dcloud.PandoraEntry;S.UP-OL-SU=true;S.title=" + title +";S.content="+content+";S.payload=test;end");
		notify.setType(GtReq.NotifyInfo.Type._intent);
//      notify.setUrl("https://dev.getui.com/");
//      notify.setType(GtReq.NotifyInfo.Type._url);
		template.set3rdNotifyInfo(notify);
    }




}
