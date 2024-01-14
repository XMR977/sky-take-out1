package com.sky.controller.admin;


import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AWSOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/admin/common")
@Api("common API")
public class CommonController {

    @Autowired
    private AWSOssUtil awsOssUtil;

    /**
     * upload files
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation("upload")
    public Result<String> upload(MultipartFile file)  {


        File localFile = convertMultipartFileToFile(file);

        String originalFilename = file.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        String objectName = UUID.randomUUID() +substring;


        try {
          String  filePath = awsOssUtil.upload(file.getBytes(), objectName);


            return Result.success(filePath);
        } catch (IOException e) {
            log.info(MessageConstant.UPLOAD_FAILED,e);
        }

        return null;
    }

    private File convertMultipartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try {
            Files.copy(file.getInputStream(), convertedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return convertedFile;
    }

}
