package com.sky.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.sky.constant.MessageConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.File;

@Slf4j
@AllArgsConstructor
@Data
public class AWSOssUtil {
    
    private  String accessKeyId;
    private  String accessKeySecret;
    private  String bucketName;


    public String upload(byte[] bytes, String objectName) {


            Region region = Region.US_EAST_1;
            AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKeyId,
                    accessKeySecret);
            S3Client s3Client = S3Client.builder()
                    .region(region)
                    .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                    .build();

            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectName)
                    .acl("public-read")
                    .contentType("image/*")
                    .build();

//        CreateMultipartUploadRequest objectRequest = CreateMultipartUploadRequest.builder()
//                .bucket(bucketName)
//                .key(objectName)
//                .build();


            try {

                // 创建PutObject请求。
                s3Client.putObject(objectRequest, RequestBody.fromBytes(bytes));

            } catch (OSSException oe) {
                System.out.println("Caught an OSSException, which means your request made it to OSS, "
                        + "but was rejected with an error response for some reason.");
                System.out.println("Error Message:" + oe.getErrorMessage());
                System.out.println("Error Code:" + oe.getErrorCode());
                System.out.println("Request ID:" + oe.getRequestId());
                System.out.println("Host ID:" + oe.getHostId());
            } catch (ClientException ce) {
                System.out.println("Caught an ClientException, which means the client encountered "
                        + "a serious internal problem while trying to communicate with OSS, "
                        + "such as not being able to access the network.");
                System.out.println("Error Message:" + ce.getMessage());
            } finally {
                if (s3Client != null) {
                    s3Client.close();
                }
            }




        //return file path as string(ie https://skytakeout.s3.amazonaws.com/IMG_1265.jpg)
        StringBuilder stringBuilder = new StringBuilder("https://");
        stringBuilder
                .append(bucketName)
                .append(".")
                .append("s3")
                .append(".")
                .append("amazonaws.com")
                .append("/")
                .append(objectName);

        log.info("File Path:{}", stringBuilder.toString());

        return stringBuilder.toString();
    }

}
