package network.vena.cooperation.oss.api;

import cn.hutool.core.util.ObjectUtil;
import com.aliyun.oss.OSSClient;
import network.vena.cooperation.common.oss.ShopMallOSSManager;
import network.vena.cooperation.common.utils.ApiJwtUtil;
import network.vena.cooperation.oss.entity.MallOssFile;
import network.vena.cooperation.oss.service.IMallOssFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.config.oss.OSSProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;

/**
 * @Description: 文件资源管理API
 * @Author: jeecg-boot
 * @Date: 2019-11-13
 * @Version: V1.0
 */

@Api(value = "文件资源管理", tags = "文件资源管理")
@Slf4j
@RestController
@RequestMapping("/api/oss/mallOssFile")
public class MallOssFileApi {

    @Autowired
    private IMallOssFileService mallOssFileService;

    @Autowired
    private ShopMallOSSManager shopMallOSSManager;

    @Autowired
    private OSSProperties properties;

    @Autowired
    private OSSClient ossClient;

    @ApiOperation(value = "文件上传", notes = "图片上传并返回数据库实体,media=image/video/audio")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "图片上传")
    })
    @PostMapping("/upload/{media}")
    public Result imageUpload(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request, @PathVariable("media") String media) {
        MallOssFile ossFile = new MallOssFile();
        ossFile.setUserId(ApiJwtUtil.getMallUserId(request));
        mallOssFileService.uploadMedia(request, multipartFile, ossFile, media);
        return Result.ok(ossFile);
    }

    @ApiOperation(value = "获取OSS文件签名路径", notes = "获取OSS文件签名路径")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mallOssFileId", value = "文件表ID")
    })
    @GetMapping("/urlSign")
    public Result urlSign(HttpServletRequest request, String mallOssFileId) {
        MallOssFile mallOssFile = mallOssFileService.getNameById(mallOssFileId);
        if (ObjectUtil.isNull(mallOssFile)) {
            throw new RuntimeException("文件不存在");
        }

        Result<String> result = new Result<>();

        URL url = shopMallOSSManager.urlSign(request, mallOssFile.getFileName());
        result.setResult(url.toString());
        result.setMessage("签名成功");
        result.setSuccess(true);
        return result;
    }

}
