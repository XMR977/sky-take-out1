package com.sky.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DishServiceImpl implements DishService {

    @Autowired
    public DishMapper dishMapper;

    @Autowired
    public DishFlavorMapper dishFlavorMapper;

    @Autowired
    public SetmealDishMapper setmealDishMapper;

    @Override
    public PageResult pagequery(DishPageQueryDTO dishPageQueryDTO) {

        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        Page<DishVO> page = dishMapper.query(dishPageQueryDTO);
        Long total = page.getTotal();
        List<DishVO> records = page.getResult();
        PageResult pageResult = new PageResult(total, records);

        return pageResult;
    }

    /**
     * delete dish
     * @param ids
     */

    @Transactional
    @Override
    public void deletedish(List<Integer> ids) {
        for (Integer id: ids){
            Dish dish = dishMapper.getById(id);
            if(dish.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        List<Integer> setmealDishid = setmealDishMapper.getById(ids);
        if(setmealDishid != null && setmealDishid.size() > 0){
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }

        dishMapper.deletebyid(ids);

        for (Integer id : ids) {
            dishFlavorMapper.deleteflavorbyid(id);
        }

    }

    /**
     * add dish and dish flavor
     * @param dishDTO
     */
    @Transactional
    public void addwithflavor(DishDTO dishDTO) {

        Dish dish = Dish.builder()
//                .createTime(LocalDateTime.now())
//                .createUser(BaseContext.getCurrentId())
//                .updateTime(LocalDateTime.now())
//                .updateUser(BaseContext.getCurrentId())
                .build();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.add(dish);

        //get id primary key
        Long id = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors!=null && flavors.size()>0){
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(id);
            });
            dishFlavorMapper.insertFlavor(flavors);
        }
    }
}
