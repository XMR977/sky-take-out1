package com.sky.service;


import com.github.pagehelper.Page;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


public interface DishService {


    void addwithflavor(DishDTO dishDTO);

    PageResult pagequery(DishPageQueryDTO dishPageQueryDTO);

    void deletedish(List<Long> ids);

    DishVO getbyid(Long id);

    void update(DishDTO dishDTO);
}
