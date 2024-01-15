package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    PageResult query(SetmealPageQueryDTO setmealPageQueryDTO);

    void insert(SetmealDTO setmealDTO);


    SetmealVO getbyid(Long id);

    void delete(List<Long> ids);

    void update(SetmealDTO setmealDTO);

    void start(Integer status, Long id);
}
