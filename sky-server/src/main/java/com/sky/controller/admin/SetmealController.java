package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "setmeal API")
@Slf4j
@RequestMapping("/admin/setmeal")
public class SetmealController {

    @Autowired
    public SetmealService setmealService;
    @GetMapping("/page")
    @ApiOperation("page query")
    public Result<PageResult> pagequery(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("Page query on setmeal");
        PageResult pageResult = setmealService.query(setmealPageQueryDTO);

        return Result.success(pageResult);
    }

    @PostMapping
    @ApiOperation("add setmeal")
    public Result add(@RequestBody SetmealDTO setmealDTO){
        log.info("Insert new setmeal");
        setmealService.insert(setmealDTO);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("getbyid")
    public Result<SetmealVO> getbyid(@PathVariable Long id){
        log.info("Get by id:{}",id);
        SetmealVO setmealVO = setmealService.getbyid(id);
        return Result.success(setmealVO);
    }

    @DeleteMapping
    @ApiOperation("DELETE SETMEAL")
    public Result delete(@RequestParam List<Long> ids){
        log.info("Delete setmeal id;{}", ids);
        setmealService.delete(ids);
        return  Result.success();
    }

    @PutMapping
    @ApiOperation("UPDATE")
    public Result update(@RequestBody SetmealDTO setmealDTO){
        log.info("Update setmeal:{}", setmealDTO);
        setmealService.update(setmealDTO);
        return  Result.success();

    }

    @PostMapping("/status/{status}")
    @ApiOperation("start")
    public Result start(@PathVariable Integer status, Long id){
        log.info("start or stop");
        setmealService.start(status,id);
        return Result.success();

    }

}
