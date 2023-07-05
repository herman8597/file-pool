package io.filpool.scheduled;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.google.gson.Gson;
import com.xxl.job.core.handler.annotation.XxlJob;
import io.filpool.framework.util.Constants;
import io.filpool.framework.util.RedisUtil;
import io.filpool.scheduled.model.ChiaIncomeVo;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/***
 * 奇亚24小时挖矿收益
 */
@Component
@Slf4j
public class ChiaDayIncomeTask {

    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new Gson();
    @Autowired
    private RedisUtil redisUtil;

    @XxlJob("chiaDayIncomeTask")
    public void getDayIncome() throws Exception{
        Request request = new Request.Builder().url("https://api2.chiaexplorer.com/chart/xchTibDay?period=1w").get().build();
        try {
            Response execute = client.newCall(request).execute();
            String json = execute.body().string();
            ChiaIncomeVo incomeVo = gson.fromJson(json,ChiaIncomeVo.class);
            if (ObjectUtils.isNotEmpty(incomeVo.getData())){
                redisUtil.set(Constants.XCH_24H_INCOME, incomeVo.getData().get(0), 60L*60*24);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getDayIncome(String a) throws Exception{
        Request request = new Request.Builder().url("https://api2.chiaexplorer.com/chart/xchTibDay?period=1w").get().build();
        Response execute = client.newCall(request).execute();
        String json = execute.body().string();
        ChiaIncomeVo incomeVo = gson.fromJson(json,ChiaIncomeVo.class);
//            if (ObjectUtils.isNotEmpty(incomeVo.getData())){
//                redisUtil.set(Constants.XCH_24H_INCOME, incomeVo.getData().get(0), 60L*60*24);
//            }
        return incomeVo.getData().get(0);
    }

}
