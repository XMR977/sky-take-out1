package com.sky.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "sky.awsoss")
@Component
public class AWSOssProperties {

    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
}
