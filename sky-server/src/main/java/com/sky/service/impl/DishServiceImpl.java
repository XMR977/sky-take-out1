package com.sky.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.AutoFill;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.DishDisableFailedException;
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

    @Autowired
    public SetmealMapper setmealMapper;

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
    public void deletedish(List<Long> ids) {
        for (Long id: ids){
            Dish dish = dishMapper.getById(id);
            if(dish.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        List<Long> setmealDishid = setmealDishMapper.getById(ids);
        if(setmealDishid != null && setmealDishid.size() > 0){
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }

        dishMapper.deletebyid(ids);

        for (Long id : ids) {
            dishFlavorMapper.deleteflavorbyid(id);
        }

    }

    /**
     * find by id
     * @param id
     * @return
     */
    @Override
    public DishVO getbyid(Long id) {


        Dish dish = dishMapper.getById(id);

        List<DishFlavor> dishFlavors = dishFlavorMapper.getflavorbyid(id);

            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(dish, dishVO);
            dishVO.setFlavors(dishFlavors);

            return dishVO;
    }

    @Transactional
    @Override
    public void update(DishDTO dishDTO) {

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);


        dishFlavorMapper.deleteflavorbyid(dish.getId());
        List<DishFlavor> dishFlavors = dishDTO.getFlavors();
        if(dishFlavors != null && dishFlavors.size()>0) {
            dishFlavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dish.getId());
            });
            dishFlavorMapper.insertFlavor(dishFlavors);
        }
    }

    /**
     * query by category id
     * @param categoryId
     * @return
     */
    @Override
    public List<Dish> list(Long categoryId) {
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return dishMapper.list(dish);
    }

    /**
     * start or stop dish
     * @param status
     * @param id
     */

    @Override
    public void start(Integer status, Long id) {

        List<SetmealDish> setmealDish = setmealDishMapper.getbydishid(id);
        setmealDish.forEach(setmealDish1 -> {
            Long ids = setmealDish1.getSetmealId();
            Setmeal setmeal = setmealMapper.getbysmid(ids);
            if (setmeal.getStatus() == StatusConstant.ENABLE) {
                throw new DishDisableFailedException(MessageConstant.SETMEAL_WITH_DISH);
            }
        });


        Dish dish = Dish.builder()
                .status(status)
                .id(id)
                .build();
        dishMapper.update(dish);

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
