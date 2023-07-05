package io.filpool.pool.controller.app;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.filpool.config.constant.CommonConstant;
import io.filpool.config.constant.CommonRedisKey;
import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.common.controller.BaseController;
import io.filpool.framework.common.exception.FILPoolException;
import io.filpool.framework.log.annotation.Module;
import io.filpool.framework.util.Constants;
import io.filpool.framework.util.RedisUtil;
import io.filpool.framework.util.UUIDUtil;
import io.filpool.framework.util.VerificationCode;
import io.filpool.pool.request.VerifyImageCodeParam;
import io.filpool.pool.vo.ImageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

@RestController
@Api(value = "图片验证码", tags = {"图片验证码控制器"})
@RequestMapping(Constants.API_PREFIX + "/image")
@Module("pool")
@ApiSupport(order =999)
public class ImageController extends BaseController {
    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/getBase64Image")
    @ResponseBody
    @ApiOperation(value = "获取图片Base64验证码")
    public ApiResult<ImageVo> getCode() throws Exception {
        VerificationCode verificationCode = new VerificationCode();
        BufferedImage image = verificationCode.getImage();
        String code = verificationCode.getText();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, CommonConstant.JPEG, outputStream);
        // 将图片转换成base64字符串
        String base64 = Base64.getEncoder().encodeToString(outputStream.toByteArray());
        // 生成当前验证码会话token
        String verifyToken = UUIDUtil.getUuid();
        ImageVo vo = new ImageVo();
        vo.setImage(CommonConstant.BASE64_PREFIX + base64);
        vo.setVerifyToken(verifyToken);
        // 缓存到Redis
//        redisTemplate.opsForValue().set(String.format(CommonRedisKey.VERIFY_CODE, verifyToken), code, 5, TimeUnit.MINUTES);
        redisUtil.set(String.format(CommonRedisKey.VERIFY_CODE, verifyToken), code, Constants.VERIFY_CODE_TIMEOUT);
        return ApiResult.ok(vo);
    }

    @PostMapping("/verifyImageCode")
    @ResponseBody
    @ApiOperation(value = "校验验证码")
    public ApiResult<Boolean> verifyImageCode(@RequestBody VerifyImageCodeParam param) throws Exception {
        String key = String.format(CommonRedisKey.VERIFY_CODE, param.getVerifyToken());
        if (redisUtil.exists(key)) {
            String o = (String) redisUtil.get(key);
            if (StringUtils.equals(o.toLowerCase(), param.getCode().toLowerCase())) {
                return ApiResult.ok(true);
            }
        }
        throw new FILPoolException("verify.code.error");
    }
}
