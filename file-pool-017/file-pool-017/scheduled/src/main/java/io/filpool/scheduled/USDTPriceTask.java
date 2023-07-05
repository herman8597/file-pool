package io.filpool.scheduled;

import com.google.gson.Gson;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import io.filpool.framework.util.Constants;
import io.filpool.framework.util.RedisUtil;
import io.filpool.scheduled.model.UsdtPriceVo;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class USDTPriceTask {
    private Gson gson = new Gson();
    private OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(20000L, TimeUnit.MILLISECONDS)
            .readTimeout(20000L, TimeUnit.MILLISECONDS).build();

    @Autowired
    private RedisUtil redisUtil;

    @XxlJob("USDTPriceTask")
    public void getUsdtPrice() throws Exception {
        Request request = new Request.Builder().url(Constants.COIN_PRICE_URL_URL).get().build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String json = response.body().string();
            //解析json
            UsdtPriceVo priceVo = gson.fromJson(json, UsdtPriceVo.class);
            if (priceVo != null && priceVo.getData() != null) {
                for (UsdtPriceVo.DataBean dataBean : priceVo.getData()) {
                    if (StringUtils.equals("USDT", dataBean.getName())) {
                        //是USDT
                        redisUtil.set(Constants.USDT_PRICE_KEY, dataBean.getCurrent_price().stripTrailingZeros().toPlainString());
                        log.info("当前USDT价格为 {}", dataBean.getCurrent_price());
                        break;
                    }
                }
            }
            XxlJobHelper.handleSuccess();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
