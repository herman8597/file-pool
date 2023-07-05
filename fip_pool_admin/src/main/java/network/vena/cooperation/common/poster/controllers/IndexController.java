package network.vena.cooperation.common.poster.controllers;

import network.vena.cooperation.common.oss.ShopMallOSSManager;
import network.vena.cooperation.common.poster.contracts.Data;
import network.vena.cooperation.common.poster.contracts.Result;
import network.vena.cooperation.common.poster.drawable.Poster;
import network.vena.cooperation.common.poster.kernal.BlankResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Api(value = "海报生成", tags = "海报生成")
@Slf4j
@RestController
@RequestMapping("/api/poster")
public class IndexController {

    @Autowired
    private Data data;

    @Autowired
    private ShopMallOSSManager shopMallOSSManager;

    /**
     * 画图返回
     *
     * @param poster
     * @return Result
     */
    @ApiOperation(value = "生成海报")
    @PostMapping("/inline")
    public Result drawAndReturn(@RequestBody Poster poster, HttpServletRequest request) throws IOException {

        Result result = data.find(poster.key());

        // TODO 上线取消注释
//        if (result != null) {
//            log.warn("读取缓存");
//            return result;
//        }

        log.warn("新创建");
        File file = poster.draw();

        String fileName = ShopMallOSSManager.buildObjectName(request, file.getName(), "image");

        InputStream inputStream = new FileInputStream(file);

        shopMallOSSManager.upload(request, fileName, inputStream);

        BlankResult blankResult = new BlankResult(Result.SUCCESSFUL, "上传成功");

        URL url = shopMallOSSManager.urlSign(request, fileName);

        Map<String, String> map = new HashMap<>();
        map.put("url", url.toString());
        blankResult.setData(map);
        data.save(poster.key(), blankResult);

        return blankResult;

    }


    /**
     * 画图保存到本地
     *
     * @param poster
     * @return Result
     */
    @ApiOperation(value = "生成海报")
    @PostMapping("/local")
    public Result drawAndSaveLocal(@RequestBody Poster poster) throws IOException {

        Result result = data.find(poster.key());

        if (result != null) {
            log.warn("读取缓存");
            return result;
        }

        log.warn("新创建");
        File file = poster.draw();

        File newFile = new File(poster.getLocalPath());
        if (!newFile.exists()) {
            newFile.mkdirs();
        }

        InputStream is = new FileInputStream(file);

        String path = newFile.getPath() + File.separator + new Random().nextInt() + ".png";
        OutputStream os = new FileOutputStream(path);

        byte[] bs = new byte[1024];
        int len;
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        os.flush();
        os.close();
        is.close();

        BlankResult blankResult = new BlankResult(Result.SUCCESSFUL, "上传成功");
        Map<String, String> map = new HashMap<>();
        map.put("url", path);
        blankResult.setData(map);
        data.save(poster.key(), blankResult);

        return blankResult;

    }


}
