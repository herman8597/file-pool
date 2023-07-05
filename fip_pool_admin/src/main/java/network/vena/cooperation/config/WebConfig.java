package network.vena.cooperation.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import network.vena.cooperation.common.oss.ShopMallOSSManager;
import org.jeecg.config.oss.OSSProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

@Configuration
public class WebConfig {

    @Autowired
    protected OSSProperties properties;

    @Bean
    public ShopMallOSSManager shopMallOSSManager(OSSClient ossClient, OSSProperties properties) {
        return new ShopMallOSSManager(ossClient, properties);
    }

    @Bean
    @ConditionalOnMissingBean(name = "ossClient")
    public OSSClient ossClient() {
        DefaultCredentialProvider provider = new DefaultCredentialProvider(properties.getAccessKey(), properties.getSecretKey());
        ClientConfiguration config = new ClientConfiguration();
        return new OSSClient(properties.getEndpoint(), provider, config);
    }




    /**
     * 设置fastJson为默认的JSON解析器
     *
     * @return
     */
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        // 1.定义一个converters转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        // 2.添加fastjson的配置信息，比如: 是否需要格式化返回的json数据
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");

        // 3.在converter中添加配置信息
        fastConverter.setFastJsonConfig(fastJsonConfig);
        // 4.将converter赋值给HttpMessageConverter
        HttpMessageConverter<?> converter = fastConverter;
        // 5.返回HttpMessageConverters对象
        return new HttpMessageConverters(converter);
    }
}
