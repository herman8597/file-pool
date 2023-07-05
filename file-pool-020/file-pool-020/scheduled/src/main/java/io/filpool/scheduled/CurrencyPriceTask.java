package io.filpool.scheduled;

import com.google.gson.Gson;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import io.filpool.framework.util.Constants;
import io.filpool.framework.util.DateUtil;
import io.filpool.framework.util.RedisUtil;
import io.filpool.pool.entity.Currency;
import io.filpool.pool.entity.Quotation;
import io.filpool.pool.service.CurrencyService;
import io.filpool.pool.service.QuotationService;
import io.filpool.pool.vo.CurrencyPriceDetailVo;
import io.filpool.scheduled.model.CurrencyPriceVo;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 币种价格定时任务
 */
@Component
@Slf4j
public class CurrencyPriceTask {
    @Autowired
    private QuotationService quotationService;
    @Autowired
    private RedisUtil redisUtil;
    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new Gson();

    @XxlJob("currencyPriceTask")
    @Transactional(rollbackFor = Exception.class)
    public void getPrice() throws Exception {
        List<Quotation> list = quotationService.list();
        if (list != null) {
            Date date = new Date();
            for (Quotation quotation : list) {
                if (!quotation.getSymbol().equals("USDT")) {
                    Request request = new Request.Builder().url(Constants.COIN_PRICE_URL + quotation.getSymbol()).get().build();
                    try {
                        Response execute = client.newCall(request).execute();
                        String json = execute.body().string();
                        CurrencyPriceVo priceVo = gson.fromJson(json, CurrencyPriceVo.class);
                        if (priceVo.getCode() == 200) {
                            for (CurrencyPriceVo.CoinlistBean bean : priceVo.getCoinlist()) {
                                if (bean.getSymbol().equals("FIL") || bean.getSymbol().equals("XCH")) {
                                    CurrencyPriceDetailVo vo = new CurrencyPriceDetailVo();
                                    vo.setPercent(bean.getChange_percent().toPlainString());
                                    vo.setMarketValue(bean.getMarket_value());
                                    vo.setRank(bean.getRank_no());
                                    vo.setPrice(bean.getPrice().toPlainString());
                                    //保留币种详情
                                    String ket = bean.getSymbol().equals("FIL") ? Constants.CURRENCY_FIL_KEY : Constants.CURRENCY_XCH_KEY;
                                    redisUtil.set(ket, vo);
                                }
                                quotation.setPercentChange(bean.getChange_percent().toPlainString());
                                quotation.setPriceUsdt(bean.getPrice().stripTrailingZeros().toPlainString());
                                quotation.setUpdateTime(date);
                                quotationService.updateQuotation(quotation);
//                                redisUtil.set(Constants.CURRENCY_PRICE_KEY + quotation.getSymbol(), bean.getPrice().stripTrailingZeros().toPlainString());
//                                redisUtil.set(Constants.CURRENCY_PERCENT_KEY + quotation.getSymbol(), bean.getChange_percent().stripTrailingZeros().toPlainString());
//                                log.info("{} 价格为：{}", quotation.getSymbol(), bean.getPrice());
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        XxlJobHelper.handleSuccess();
    }
}
