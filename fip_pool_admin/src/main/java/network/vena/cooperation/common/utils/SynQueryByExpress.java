package network.vena.cooperation.common.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


/**
 * 实时查询快递请求
 * @author Administrator
 *
 */
public class SynQueryByExpress {


	/**
	 * 实时查询请求地址
	 */
	private static final String SYNQUERY_URL = "https://poll.kuaidi100.com/poll/query.do";
	private static final String AUTONUMBER_URL = "http://www.kuaidi100.com/autonumber/auto";
	private static final String key = "JeTIpRUY5232"; //贵司的授权key
	private static final String customer = "28C81F4DDEF27D4B148A1F8AD22AAB6C"; //贵司的查询公司编号



	public static void main(String[] args) {
		String key = "JeTIpRUY5232"; //贵司的授权key
		String customer = "28C81F4DDEF27D4B148A1F8AD22AAB6C"; //贵司的查询公司编号
		String com = "zhongtong"; //快递公司编码
		String num = "75324631563483"; //快递单号
		String phone = "";				//手机号码后四位
		String from = "";				//出发地
		String to = "";					//目的地
		int resultv2 = 0;				//开启行政规划解析

		SynQueryByExpress demo = new SynQueryByExpress();
		String result = demo.synQueryData(com, num, phone, from, to, resultv2);
		System.out.println(result);
	}

	public String queryByExpress(String com, String num) {
		SynQueryByExpress express = new SynQueryByExpress();
		String phone = "";				//手机号码后四位
		String from = "";				//出发地
		String to = "";					//目的地
		int resultv2 = 0;				//开启行政规划解析
		String result = express.synQueryData(com, num, phone, from, to, resultv2);
		return result;
	}

	public String queryAutonumber(String num) {
		String result = HttpUtil.get(AUTONUMBER_URL + "?key=" + key + "&num=" + num);
		return result;
	}

	/**
	 * 实时查询快递单号
	 * @param com			快递公司编码
	 * @param num			快递单号
	 * @param phone			手机号
	 * @param from			出发地城市
	 * @param to			目的地城市
	 * @param resultv2		开通区域解析功能：0-关闭；1-开通
	 * @return
	 */
	public String synQueryData(String com, String num, String phone, String from, String to, int resultv2) {

		Map<String,String> map = new HashMap<>();
		map.put("com", com);
		map.put("num", num);
		map.put("phone", phone);
		map.put("from", from);
		map.put("to", to);
		if(1 == resultv2) {
			map.put("resultv2", "1");
		} else {
			map.put("resultv2", "0");
		}
		String param = JSON.toJSONString(map);
		Map<String, String> params = new HashMap<String, String>();
		params.put("customer", this.customer);
		String sign = MD5Utils.encode(param + this.key + this.customer);
		params.put("sign", sign);
		params.put("param", param);
		System.out.println(JSON.toJSON(params));
		return this.post(params);
	}
	
	/**
	 * 发送post请求
	 */
	public String post(Map<String, String> params) {
		StringBuffer response = new StringBuffer("");
		
		BufferedReader reader = null;
		try {
			StringBuilder builder = new StringBuilder();
			for (Map.Entry<String, String> param : params.entrySet()) {
				if (builder.length() > 0) {
					builder.append('&');
				}
				builder.append(URLEncoder.encode(param.getKey(), "UTF-8"));
				builder.append('=');
				builder.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
			}
			byte[] bytes = builder.toString().getBytes("UTF-8");

			URL url = new URL(SYNQUERY_URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(3000);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length", String.valueOf(bytes.length));
			conn.setDoOutput(true);
			conn.getOutputStream().write(bytes);

			reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			
			String line = "";
            while ((line = reader.readLine()) != null) {
            	response.append(line);
            }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != reader) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return response.toString();
	}
}

/**
 * md5加密
 */
class MD5Utils {
	private static MessageDigest mdigest = null;
	private static char digits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
	
	private static MessageDigest getMdInst() {
		if (null == mdigest) {
			try {
				mdigest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		return mdigest;
	}

	public static String encode(String s) {
		if(null == s) {
			return "";
		}
		
		try {
			byte[] bytes = s.getBytes();
			getMdInst().update(bytes);
			byte[] md = getMdInst().digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = digits[byte0 >>> 4 & 0xf];
				str[k++] = digits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
