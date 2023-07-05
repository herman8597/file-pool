package network.vena.cooperation.common.oss;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import network.vena.cooperation.common.utils.ApiJwtUtil;
import org.jeecg.config.oss.OSSProperties;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ShopMallOSSManager {

    private static String prefix = "user-";

    private OSSClient client;

    private OSSProperties properties;

    public ShopMallOSSManager(OSSClient client, OSSProperties properties) {
        this.client = client;
        this.properties = properties;
    }

    /**
     * 上传文件
     *
     * @param request
     * @param objectName 文件名
     * @param input      文件流
     * @return
     */
    public PutObjectResult upload(HttpServletRequest request, String objectName, InputStream input) {

        boolean found = this.client.doesObjectExist(properties.getBucketName(), objectName);
        if (found) {
            return null;
        }


        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setUserMetadata(getUserMetaData(request));
        objectMetadata.setContentEncoding("UTF-8");
        objectMetadata.setLastModified(new Date());

        PutObjectRequest putObjectRequest = new PutObjectRequest(properties.getBucketName(), objectName, input);
        putObjectRequest.setMetadata(objectMetadata);

        return this.client.putObject(putObjectRequest);
    }

    /**
     * 文件下载签名
     *
     * @param request
     * @param objectName 文件名
     * @return
     */
    public URL urlSign(HttpServletRequest request, String objectName) {
        GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(properties.getBucketName(), objectName);
        urlRequest.setExpiration(getExpiration());

        return this.client.generatePresignedUrl(urlRequest);
    }

    /**
     * 构建带文件夹的文件名
     *
     * @param request
     * @param fileName
     * @param prefix
     * @return
     */
    public static String buildObjectName(HttpServletRequest request, String fileName, String prefix) {
        String userId = ApiJwtUtil.getMallUserId(request);
        fileName = prefix + "/" + userId + "/" + fileName;
        return fileName;
    }

    private Map<String, String> getUserMetaData(HttpServletRequest request) {
        Map<String, String> userMetadata = new HashMap<>();
        userMetadata.put("userId", ApiJwtUtil.getMallUserId(request));
        return userMetadata;
    }


    /**
     * 签名URL
     * @param key
     * @param httpMethod
     * @return
     */
    public URL uploadSign(String key, HttpMethod httpMethod) {
        GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(properties.getBucketName(),key,httpMethod);
        urlRequest.setExpiration(getExpiration());
        return this.client.generatePresignedUrl(urlRequest);
    }

    /**
     * 获取签名过期时间-1小时
     * @return
     */
    private Date getExpiration(){
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(60L);
        return Date.from(localDateTime.toInstant(OffsetDateTime.now().getOffset()));
    }

    /**
     * 生成文件Key
     * @return
     */
    public String generateKey() {
        return IdWorker.get32UUID();
    }
}
