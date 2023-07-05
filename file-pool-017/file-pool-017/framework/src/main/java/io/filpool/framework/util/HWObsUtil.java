package io.filpool.framework.util;

import com.obs.services.ObsClient;
import com.obs.services.model.PutObjectResult;
import io.filpool.framework.config.hwc.HWCConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 华为云obs
 */
@Component
public class HWObsUtil {
    @Autowired
    private HWCConfig config;
    private ObsClient obsClient;

    //lazycat
    //GNROSJSECZEXAFQRHIEF
    public String uploadFile(File file, String uploadPath) {
        PutObjectResult result = getClient().putObject(config.getBucketname(), uploadPath, file);
        return result.getObjectUrl();
    }

    public String uploadInputStream(InputStream is, String uploadPath) {
        PutObjectResult result = getClient().putObject(config.getBucketname(), uploadPath, is);
        return result.getObjectUrl();
    }

    /**
     * MultipartFile 转 File
     *
     * @param file
     * @throws Exception
     */
    public File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    //获取流文件
    private void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObsClient getClient() {
        if (obsClient == null) {
            obsClient = new ObsClient(config.getAk(), config.getSk(), config.getEndPoint());
        }
        return obsClient;
    }
}
