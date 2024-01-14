package com.sky.config;


import com.sky.properties.AWSOssProperties;
import com.sky.utils.AWSOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class OssConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public AWSOssUtil awsOssUtil(AWSOssProperties awsOssProperties){
        log.info("create awsoss object:{}", awsOssProperties);
        return  new AWSOssUtil(awsOssProperties.getAccessKeyId(),
                awsOssProperties.getAccessKeySecret(),
                awsOssProperties.getBucketName());
    }
}
