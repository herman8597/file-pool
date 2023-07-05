package io.filpool.scheduled.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.filpool.framework.util.Constants;
import io.filpool.framework.util.RedisUtil;
import io.filpool.framework.util.WebHttpUtil;
import io.filpool.scheduled.model.*;
import io.filpool.scheduled.websocket.WebSocketFilInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

@Service
public class MarketService {
    private static Gson gson = new Gson();
    @Autowired
    private WebSocketFilInfo webSocketFilInfo;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private WebHttpUtil webHttpUtil;

    private final String NODE_CODE = "f0137019";
    private OkHttpClient okHttpClient = null;


    public void sendFilInfo() throws IOException {

        String strPriceVo = webHttpUtil.get("https://dncapi.bqiapp.com/api/search/websearch?page=1&pagesize=1&code=FIL", String.class);
        Type strPriceVoType = new TypeToken<PriceVo>() {
        }.getType();
        PriceVo priceVo = gson.fromJson(strPriceVo, strPriceVoType);

        String strExchangeVo = webHttpUtil.get("https://dncapi.bqiapp.com/api/home/global?webp=1", String.class);

        Type strExchangeVoType = new TypeToken<ExchangeVo>() {
        }.getType();
        ExchangeVo exchangeVo = gson.fromJson(strExchangeVo, strExchangeVoType);

        FilVo filVo = new FilVo();

        PriceVo.CoinlistBean source = null;
        if (ObjectUtils.isEmpty(priceVo.getCoinlist().get(0))) {
            source = priceVo.getCoinlistX().get(0);
        } else {
            source = priceVo.getCoinlist().get(0);
        }
        BeanUtils.copyProperties(source, filVo);
        BeanUtils.copyProperties(exchangeVo.getData(), filVo);
        BeanUtils.copyProperties(priceVo.getCoinlist().get(0), filVo);
        BeanUtils.copyProperties(exchangeVo.getData(), filVo);
//        FilFoxWormVo foxInfo = getFilFoxInfo();

        try {
            //获取排名信息  设置进去
            FilAddressVo filInfo = getFilInfo(NODE_CODE);
            OverviewVo mainNeiInfo = getMainNeiInfo();
            int blockRank = getBlockRank(NODE_CODE);
            int powerGorwthRank = getPowerGorwthRank(NODE_CODE);
            filInfo.getMiner().setBlockRank(blockRank);
            filInfo.getMiner().setPowerGorwthRank(powerGorwthRank);
            filVo.setFilAddress(filInfo);
            filVo.setOverview(mainNeiInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        redisUtil.set(Constants.FIL_INFO, JSONObject.toJSONString(filVo), 300L);
        webSocketFilInfo.sendAllMessage(JSONObject.toJSONString(filVo));
    }

    /**
     * 获取Fil节点信息
     *
     * @return
     * @throws Exception
     */
    public FilAddressVo getFilInfo(String code) throws Exception {
        //首页信息：https://filfox.info/api/v1/overview
        //地址信息：https://filfox.info/api/v1/address/f02456
        //算力增速排名列表：https://filfox.info/api/v1/miner/list/power-growth?pageSize=100&page=0&duration=24h
        //出块排名列表：https://filfox.info/api/v1/miner/list/blocks?pageSize=100&page=0&duration=24h
        //挖矿统计： https://filfox.info/api/v1/address/f024563/mining-stats?duration=24h
        Request request = new Request.Builder().url("https://filfox.info/api/v1/address/"+code).get().build();
        Response execute = getClient().newCall(request).execute();
        String json = execute.body().string();
        FilAddressVo addressVo = gson.fromJson(json, FilAddressVo.class);
        return addressVo;
    }

    public OverviewVo getMainNeiInfo() throws Exception {
        Request request = new Request.Builder().url("https://filfox.info/api/v1/overview").get().build();
        String json = getClient().newCall(request).execute().body().string();
        OverviewVo vo = gson.fromJson(json, OverviewVo.class);
        return vo;
    }

    /**
     * 获取出块排名
     *
     * @return
     */
    public int getBlockRank(String code) throws Exception {
        int page = 0;
        int limit = 15;
        while (page <= limit) {
            Request build = new Request.Builder().url("https://filfox.info/api/v1/miner/list/blocks?pageSize=100&page="+page+"&duration=24h").get().build();
            String json = getClient().newCall(build).execute().body().string();

            ListVo<BlocksVo> vo = gson.fromJson(json, new TypeToken<ListVo<BlocksVo>>() {
            }.getType());
            for (int i = 0; i < vo.getMiners().size(); i++) {
                BlocksVo vo1 = vo.getMiners().get(i);
                if (vo1.getAddress().equals(code)) {
                    return (page * 100) + i + 1;
                }
            }
            page++;
        }

        return 0;
    }

    /**
     * 获取算力增速排名
     *
     * @return
     */
    public int getPowerGorwthRank(String code) throws Exception {
        int page = 0;
        int limit = 15;
        while (page <+ limit) {
            Request build = new Request.Builder().url("https://filfox.info/api/v1/miner/list/power-growth?pageSize=100&page="+page+"&duration=24h").get().build();
            String json = getClient().newCall(build).execute().body().string();

            ListVo<PowerGorwthVo> vo = gson.fromJson(json, new TypeToken<ListVo<PowerGorwthVo>>() {
            }.getType());
            for (int i = 0; i < vo.getMiners().size(); i++) {
                PowerGorwthVo vo1 = vo.getMiners().get(i);
                if (vo1.getAddress().equals(code)) {
                    return  (page * 100) + i + 1;
                }
            }
            page++;
        }
        return 0;
    }

    /**
     * 获取请求Client
     *
     * @return
     */
    private OkHttpClient getClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .build();
        }
        return okHttpClient;
    }

}
