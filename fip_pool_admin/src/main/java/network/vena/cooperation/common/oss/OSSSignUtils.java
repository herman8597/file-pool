package network.vena.cooperation.common.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import lombok.Setter;
import org.jeecg.config.oss.OSSProperties;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;

public class OSSSignUtils {

    @Setter
    private OSSClient client;

    @Setter
    private OSSProperties properties;

    public URL sign(String objectName) {

        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(60L);
        Date date = Date.from(localDateTime.toInstant(OffsetDateTime.now().getOffset()));

        GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(properties.getBucketName(), objectName);
        urlRequest.setExpiration(date);

        return this.client.generatePresignedUrl(urlRequest);
    }
}
