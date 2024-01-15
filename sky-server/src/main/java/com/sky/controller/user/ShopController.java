package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController("userShopController")
@RequestMapping("/user/shop")
@Api(tags = "shop status")
public class ShopController {

    public static final String KEY = "Shop_Status";

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * get shop status
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("get shop status")
    public Result<Integer> getstatus(){

       Integer status  = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("get shop status:{}", status == 1?"open":"close");
        return Result.success(status);
    }

}
