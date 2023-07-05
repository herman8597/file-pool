package network.vena.cooperation.oss.service.impl;

import network.vena.cooperation.common.oss.ShopMallOSSManager;
import network.vena.cooperation.oss.entity.MallOssFile;
import network.vena.cooperation.oss.mapper.MallOssFileMapper;
import network.vena.cooperation.oss.service.IMallOssFileService;
import org.jeecg.config.oss.OSSProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

/**
 * @Description: 文件资源管理
 * @Author: jeecg-boot
 * @Date: 2019-11-13
 * @Version: V1.0
 */
@Service
public class MallOssFileServiceImpl extends ServiceImpl<MallOssFileMapper, MallOssFile> implements IMallOssFileService {

    @Autowired
    private OSSProperties properties;

    @Autowired
    private ShopMallOSSManager shopMallOSSManager;

    @Override
    public MallOssFile getNameById(String mallOssFileId) {
        return lambdaQuery().select(
                MallOssFile::getId,
                MallOssFile::getFileName
        )
                .eq(MallOssFile::getId, mallOssFileId)
                .one();
    }

    @Override
    public void uploadMedia(HttpServletRequest request, MultipartFile multipartFile, MallOssFile ossFile, String media) {
        String fileName = multipartFile.getOriginalFilename();
        // 重新构建文件名
        fileName = ShopMallOSSManager.buildObjectName(request, fileName, media);
        try {
            shopMallOSSManager.upload(request, fileName, multipartFile.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ossFile.setFileName(fileName);
        ossFile.setCreateTime(new Date());
        ossFile.setFileUrl("https://" + properties.getBucketName() + "." + properties.getEndpoint() + "/" + fileName);
        this.save(ossFile);
    }
}
