package com.sky.controller.admin;


import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import com.sky.service.DishService;
import com.sky.service.impl.DishServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/admin/dish")
@Api(tags = "Dish API")
public class DishController {

    @Autowired
    private DishService dishService;
    @PostMapping
    @ApiOperation("add")
    public Result add(@RequestBody DishDTO dishDTO){
        log.info("new dish");
        dishService.addwithflavor(dishDTO);
        return Result.success();
    }

}
