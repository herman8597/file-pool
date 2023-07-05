package io.filpool.pool.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.filpool.framework.common.api.ApiResult;
import io.filpool.framework.common.exception.FILPoolException;
import io.filpool.pool.entity.Address;
import io.filpool.pool.entity.RechargeRecord;
import io.filpool.pool.model.WithdrawVo;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 云钱包帮助类
 */
@Component
@Slf4j
public class CloudApiRpc {
    @Value("${cloud.url}")
    private String url;

    private OkHttpClient okHttpClient = null;
    private static final MediaType JSON = MediaType.parse("application/json");

    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    /**
     * 创建钱包
     */
    public Address generateWallet(String symbol) throws Exception {
        HashMap<String, Object> params = new HashMap<>();
        params.put("symbol", symbol);
        Request request = getRequest("generateWallet", gson.toJson(params));
        Response response = getOkHttpClient().newCall(request).execute();
        String json = response.body().string();
        log.info("generateWallet json :{}",json);
        ApiResult<Address> result = gson.fromJson(json, new TypeToken<ApiResult<Address>>() {
        }.getType());
        if (result.isSuccess())
            return result.getData();
        else
            throw new FILPoolException("illegal.params");
    }

    /**
     * 转账
     */
    public ApiResult<WithdrawVo> transfer(String fromAddress, String secret, String toAddress, String symbol, BigDecimal amount, Long recordId) throws Exception {
        HashMap<String, Object> params = new HashMap<>();
        params.put("address", fromAddress);
        params.put("secret", secret);
        params.put("toAddress", toAddress);
        params.put("symbol", symbol);
        params.put("amount", amount);
        params.put("pid", recordId);
        Request request = getRequest("transfer", gson.toJson(params));
        Response response = getOkHttpClient().newCall(request).execute();
        String json = response.body().string();
        log.info("json:{}", json);
        return gson.fromJson(json, new TypeToken<ApiResult<WithdrawVo>>() {
        }.getType());
//        if (result.getCode() == ApiResult.ok().getCode())
//            return result.getData().getTransHash();
//        else
//            return "";
    }

    /**
     * 获取地址对应最新交易记录
     */
    public List<RechargeRecord> getNewRecharge(String address, String secret, Date time) throws Exception {
        HashMap<String, Object> params = new HashMap<>();
        params.put("address", address);
        params.put("secret", secret);
        params.put("lastSyncTime", time.getTime());
        String json1 = gson.toJson(params);
//        log.info(json1);
        Request request = getRequest("getNewRecharge", json1);
        Response response = getOkHttpClient().newCall(request).execute();
        String json = response.body().string();
        log.info("json:{}", json);
        ApiResult<List<RechargeRecord>> result = gson.fromJson(json, new TypeToken<ApiResult<List<RechargeRecord>>>() {
        }.getType());
        if (result.isSuccess())
            return result.getData();
        else
            return null;
    }


    public String getRecordByPid(Long recordId) throws Exception {
        HashMap<String, Object> params = new HashMap<>();
        params.put("pid", recordId);
        Request request = getRequest("getWithdrawLogByPid", gson.toJson(params));
        Response response = getOkHttpClient().newCall(request).execute();
        String json = response.body().string();
        log.info("json:{}", json);
        ApiResult<RechargeRecord> result = gson.fromJson(json, new TypeToken<ApiResult<List<RechargeRecord>>>() {
        }.getType());
        if (result.isSuccess())
            return result.getData().getTransHash();
        else
            return null;
    }

    private OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder().connectTimeout(20000L, TimeUnit.MILLISECONDS)
                    .readTimeout(20000L, TimeUnit.MILLISECONDS).build();
        }
        return okHttpClient;
    }

    private Request getRequest(String path, String bodyJson) {
        RequestBody body = RequestBody.create(JSON, bodyJson);
//        log.info("generateWallet url :{} body:{}",url+"generateWallet",bodyJson);
        return new Request.Builder()
                .url(url + path)
                .post(body)
                .addHeader("content-type", "application/json")
                .build();
    }
}
