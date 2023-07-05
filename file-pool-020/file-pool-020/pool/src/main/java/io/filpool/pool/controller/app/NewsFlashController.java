package io.filpool.pool.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.common.exception.FILPoolException;
import io.filpool.framework.util.Constants;
import io.filpool.framework.util.RedisUtil;
import io.filpool.pool.model.NewsFlashVo;
import io.filpool.pool.request.PageRequest;
import io.filpool.pool.util.ListUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Api(tags = {"快讯接口"})
@Slf4j
@RequestMapping(Constants.API_PREFIX + "/newsFlash")
public class NewsFlashController {
    @Autowired
    private RedisUtil redisUtil;

    private OkHttpClient okHttpClient = null;
    //获取快讯的接口
    private static final String url = "http://api.coindog.com/live/list?limit=200";
    private final Gson gson = new Gson();
    private static final String CACHE_KEY = "NewsFlashCache";

    @PostMapping("news")
    @ApiOperation("快讯")
    public ApiResult<List<NewsFlashVo.ListDTO.LivesDTO>> news(@RequestBody PageRequest request) throws Exception {
        try {
            List<NewsFlashVo.ListDTO.LivesDTO> collect = null;
            if (redisUtil.exists(CACHE_KEY)) {
                collect = (List<NewsFlashVo.ListDTO.LivesDTO>) redisUtil.get(CACHE_KEY);
            } else {
                Request build = new Request.Builder().url(url).get().build();
                String json = getOkHttpClient().newCall(build).execute().body().string();
                Type type = new TypeToken<NewsFlashVo>() {
                }.getType();
                NewsFlashVo vo = gson.fromJson(json, type);
                List<NewsFlashVo.ListDTO.LivesDTO> lives = new ArrayList<>();
                for (NewsFlashVo.ListDTO dto : vo.getList()) {
                    String date = dto.getDate();
                    for (NewsFlashVo.ListDTO.LivesDTO life : dto.getLives()) {
                        life.setDate(date);
                        lives.add(life);
                    }
                }
                collect = lives.stream().sorted(Comparator.comparing(NewsFlashVo.ListDTO.LivesDTO::getDate).reversed()).collect(Collectors.toList());
                //保存到redis
                redisUtil.set(CACHE_KEY, collect, 300L);
            }
            //分页
            List<NewsFlashVo.ListDTO.LivesDTO> list = ListUtils.pageBySubList(collect, request.getPageSize().intValue(), request.getPageIndex().intValue());
            return ApiResult.ok(list);
        } catch (Exception e) {
            log.error("获取快讯出错", e);
            throw new FILPoolException("newsflash.error");
        }
    }


    private OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        return okHttpClient;
    }
}
