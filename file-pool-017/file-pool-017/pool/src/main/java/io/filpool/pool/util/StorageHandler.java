package io.filpool.pool.util;

import com.aliyun.oss.OSSClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;


/**
 * Created by Can_z on 2017/5/18.
 */
public class StorageHandler {

    public void FileStreamUpload(String FileKey, MultipartFile file, String bucketName,
                                 String accessKeyId, String accessKeySecret, String endpoint) throws IOException {
        InputStream fileInputStream = file.getInputStream();
        inputStreamUpload(FileKey, fileInputStream, bucketName, accessKeyId, accessKeySecret, endpoint);
    }

    public void inputStreamUpload(String FileKey, InputStream inputStream, String bucketName,
                                 String accessKeyId, String accessKeySecret, String endpoint) throws IOException {

        // 创建OSSClient实例
        if (FileKey.startsWith("/")){
            FileKey = FileKey.substring(1);
        }

        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        ossClient.putObject(bucketName, FileKey, inputStream);
        // 关闭client
        ossClient.shutdown();

    }

    public static void main(String s[]){
        String asdasd = "abcdefg";
        System.out.println(asdasd.substring(1));
    }
}
