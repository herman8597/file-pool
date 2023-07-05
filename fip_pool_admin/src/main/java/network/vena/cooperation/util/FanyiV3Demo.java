package network.vena.cooperation.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import network.vena.cooperation.adminApi.entity.Goods;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class FanyiV3Demo {

    private static Logger logger = LoggerFactory.getLogger(FanyiV3Demo.class);

    private static final String YOUDAO_URL = "https://openapi.youdao.com/api";

    //自己的测试翻译
//    private static final String APP_KEY = "10b5928838226180";
//    private static final String APP_SECRET = "tTnQb5uORlizGpMA9Mg9kyTDCvoJi33K";

    //004的正式翻译
    private static final String APP_KEY = "7e39971eed08dd3a";
    private static final String APP_SECRET = "EmW17HlsKuO3livVnQ2m95ouBpnlmqKc";

    //翻译的方法
    public static <T> T fanyi(Object obj) throws IOException {
        //String q = new Gson().toJson(obj);
        String q = JSONObject.toJSONString(obj);
        Map<String,String> params = new HashMap<String,String>();
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("from", "zh-CHS");
        params.put("to", "en");
        params.put("signType", "v3");
        String curtime = String.valueOf(System.currentTimeMillis() / 1000);
        params.put("curtime", curtime);
        String signStr = APP_KEY + truncate(q) + salt + curtime + APP_SECRET;
        String sign = getDigest(signStr);
        params.put("appKey", APP_KEY);
        params.put("q", q);
        params.put("salt", salt);
        params.put("sign", sign);
        params.put("vocabId","您的用户词表ID");
        /** 处理结果 */
        String content = requestForHttp(YOUDAO_URL, params);
        HashMap parseMap = JSON.parseObject(content, HashMap.class);
        List studentList1 =null;
        String contentEn =null;
        Map<String, Object> resMap = new LinkedHashMap<>();
        Object o =null;
        if ((List) parseMap.get("web")!=null){
            //如果翻译的是单个字符串
            studentList1 = (List) parseMap.get("web");
            Map map = (Map) studentList1.get(0);
            List list = (List) map.get("value");
            contentEn = list.get(0).toString();
            resMap.put("contentEn",contentEn);
        }else{
            studentList1 = (List) parseMap.get("translation");
            Gson gson = new Gson();
//            resMap = gson.fromJson(studentList1.get(0).toString(), resMap.getClass());
            o = studentList1.get(0);
        }
        return (T) o;
    }



    //接收map进行翻译的方法
    public static Map<String,Object> fanyis(Map<String,Object> map1) throws IOException {

        String q = JSONObject.toJSONString(map1);

        Map<String,String> params = new HashMap<String,String>();
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("from", "zh-CHS");
        params.put("to", "en");
        params.put("signType", "v3");
        String curtime = String.valueOf(System.currentTimeMillis() / 1000);
        params.put("curtime", curtime);
        String signStr = APP_KEY + truncate(q) + salt + curtime + APP_SECRET;
        String sign = getDigest(signStr);
        params.put("appKey", APP_KEY);
        params.put("q", q);
        params.put("salt", salt);
        params.put("sign", sign);
        params.put("vocabId","您的用户词表ID");
        /** 处理结果 */
        String content = requestForHttp(YOUDAO_URL, params);
        HashMap parseMap = JSON.parseObject(content, HashMap.class);
        List studentList1 =null;
        String contentEn =null;
        Map<String, Object> resMap = new LinkedHashMap<>();
        Object o =null;
        if ((List) parseMap.get("web")!=null){
            //如果翻译的是单个字符串
            studentList1 = (List) parseMap.get("web");
            Map map = (Map) studentList1.get(0);
            List list = (List) map.get("value");
            contentEn = list.get(0).toString();
            resMap.put("contentEn",contentEn);
        }else{
            //传的是集合
            studentList1 = (List) parseMap.get("translation");
            Gson gson = new Gson();
            resMap = gson.fromJson(studentList1.get(0).toString(), resMap.getClass());
        }
        return resMap;
    }


 /*   public static <T> T main(String[] args) throws IOException {
        Map<String,String> params = new HashMap<String,String>();
        String q = "你好";
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("from", "zh-CHS");
        params.put("to", "ja");
        params.put("signType", "v3");
        String curtime = String.valueOf(System.currentTimeMillis() / 1000);
        params.put("curtime", curtime);
        String signStr = APP_KEY + truncate(q) + salt + curtime + APP_SECRET;
        String sign = getDigest(signStr);
        params.put("appKey", APP_KEY);
        params.put("q", q);
        params.put("salt", salt);
        params.put("sign", sign);
        params.put("vocabId","您的用户词表ID");
        *//** 处理结果 *//*
        requestForHttp(YOUDAO_URL, params);
    }*/

    public static String requestForHttp(String url,Map<String,String> params) throws IOException {

        /** 创建HttpClient */
        CloseableHttpClient httpClient = HttpClients.createDefault();

        /** httpPost */
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
        Iterator<Map.Entry<String,String>> it = params.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String,String> en = it.next();
            String key = en.getKey();
            String value = en.getValue();
            paramsList.add(new BasicNameValuePair(key,value));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(paramsList,"UTF-8"));
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        try{
            Header[] contentType = httpResponse.getHeaders("Content-Type");
            logger.info("Content-Type:" + contentType[0].getValue());
            if("audio/mp3".equals(contentType[0].getValue())){
                //如果响应是wav
                HttpEntity httpEntity = httpResponse.getEntity();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                httpResponse.getEntity().writeTo(baos);
                byte[] result = baos.toByteArray();
                EntityUtils.consume(httpEntity);
                if(result != null){//合成成功
                    String file = "合成的音频存储路径"+System.currentTimeMillis() + ".mp3";
                    byte2File(result,file);
                }
            }else{
                /** 响应不是音频流，直接显示结果 */
                HttpEntity httpEntity = httpResponse.getEntity();
                String json = EntityUtils.toString(httpEntity,"UTF-8");
                EntityUtils.consume(httpEntity);
                logger.info(json);
                System.out.println(json);
                return json;
            }
        }finally {
            try{
                if(httpResponse!=null){
                    httpResponse.close();
                }
            }catch(IOException e){
                logger.info("## release resouce error ##" + e);
            }
        }
        return null;
    }

    /**
     * 生成加密字段
     */
    public static String getDigest(String string) {
        if (string == null) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        byte[] btInput = string.getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest mdInst = MessageDigest.getInstance("SHA-256");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     *
     * @param result 音频字节流
     * @param file 存储路径
     */
    private static void byte2File(byte[] result, String file) {
        File audioFile = new File(file);
        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(audioFile);
            fos.write(result);

        }catch (Exception e){
            logger.info(e.toString());
        }finally {
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static String truncate(String q) {
        if (q == null) {
            return null;
        }
        int len = q.length();
        String result;
        return len <= 20 ? q : (q.substring(0, 10) + len + q.substring(len - 10, len));
    }

    public static void main(String[] args) throws IOException {
        Goods goods = new Goods();
        goods.setEnSlogan("中华人民共和国");
        goods.setEnName("美利坚合众国");
//        goods.setEnHighlight("大不列颠");
        Object fanyi = fanyi(goods);
        Goods goods1 = JSONObject.parseObject(fanyi.toString().replaceAll("\" ","\"").replaceAll(" \"","\""), Goods.class);
        System.out.println(goods1);
        System.out.println(goods1);
    }

}
