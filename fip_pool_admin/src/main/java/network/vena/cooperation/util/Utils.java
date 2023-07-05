package network.vena.cooperation.util;

import cn.hutool.core.util.StrUtil;

public class Utils {
	public static String anonymouPhone(String phone) {
		if(StrUtil.isEmpty(phone)) {
			return phone;
		}
		int length = phone.length();
		if(length>7) {
			return phone.substring(0,3)+"****"+phone.substring(length-4);
		}
		return phone;
	}
}
