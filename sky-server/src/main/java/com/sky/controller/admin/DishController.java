package com.sky.controller.admin;


import com.github.pagehelper.Page;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import com.sky.service.DishService;
import com.sky.service.impl.DishServiceImpl;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/page")
    @ApiOperation("page query")
    public Result<PageResult> dishQuery(DishPageQueryDTO dishPageQueryDTO){
        log.info("page query started");
        PageResult p = dishService.pagequery(dishPageQueryDTO);

        return Result.success(p);
    }


    @DeleteMapping
    @ApiOperation("delete dish")
    public  Result deletedish(@RequestParam List<Long> ids){
        log.info("delete dish:{}",ids);
        dishService.deletedish(ids);

        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("find dish by id")
    public Result<DishVO> findbyid (@PathVariable Long id){
        log.info("find dish by id:{}", id);
        DishVO dishVO = dishService.getbyid(id);
        return Result.success(dishVO);
    }

    @PutMapping
    @ApiOperation("update dish")
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("update dish:{}",dishDTO.getName());
        dishService.update(dishDTO);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("list")
    public Result<List<Dish>> list(Long categoryId){
        log.info("List dish by categoryid");
        List<Dish> dish = dishService.list(categoryId);
        return Result.success(dish);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("start")
    public Result start(@PathVariable Integer status, Long id){
        log.info("start dish");
        dishService.start(status,id);
        return Result.success();
    }
}
