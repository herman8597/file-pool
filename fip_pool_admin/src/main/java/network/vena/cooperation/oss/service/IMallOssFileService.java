package network.vena.cooperation.oss.service;

import network.vena.cooperation.oss.entity.MallOssFile;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 文件资源管理
 * @Author: jeecg-boot
 * @Date: 2019-11-13
 * @Version: V1.0
 */
public interface IMallOssFileService extends IService<MallOssFile> {

    /**
     * 查询文件名
     *
     * @param mallOssFileId
     * @return
     */
    MallOssFile getNameById(String mallOssFileId);

    /**
     * 上传图片
     * @param request
     * @param multipartFile
     * @param ossFile
     * @param media
     */
    void uploadMedia(HttpServletRequest request, MultipartFile multipartFile, MallOssFile ossFile, String media);

}
