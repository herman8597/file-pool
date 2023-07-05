package network.vena.cooperation.common.push;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//@Component
//@ConfigurationProperties(prefix="push")
public class PushDTO {
    private static String appId;
    private static String masterSecret;
    private static String appKey;
    private static String url;

    public static String getAppId() {
        return appId;
    }
    @Value("${push.appId}")
    public void setAppId(String appId) {
        this.appId = appId;
    }

    public static String getMasterSecret() {
        return masterSecret;
    }
    @Value("${push.masterSecret}")
    public void setMasterSecret(String masterSecret) {
        this.masterSecret = masterSecret;
    }

    public static String getAppKey() {
        return appKey;
    }
    @Value("${push.appKey}")
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public static String getUrl() {
        return url;
    }
    @Value("${push.url}")
    public void setUrl(String url) {
        this.url = url;
    }
}
