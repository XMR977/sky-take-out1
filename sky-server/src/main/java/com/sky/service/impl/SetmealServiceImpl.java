package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    public SetmealMapper setmealMapper;
    @Autowired
    public SetmealDishMapper setmealDishMapper;
    @Autowired
    public DishMapper dishMapper;

    /**
     * page query
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult query(SetmealPageQueryDTO setmealPageQueryDTO) {

        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.query(setmealPageQueryDTO);
        long total = page.getTotal();
        List<SetmealVO> records = page.getResult();

        PageResult pageResult= new PageResult(total,records);
        return pageResult;
    }

    /**
     * insert new setmeal
     * @param setmealDTO
     */

    @Transactional
    @Override
    public void insert(SetmealDTO setmealDTO) {
        //insert into setmeal table
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.insert(setmeal);
        //get the primary key from setmeal
        Long id = setmeal.getId();

        //insert setmeal_dish
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if(setmealDishes != null && !setmealDishes.isEmpty()) {
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(id);
            });

            setmealDishMapper.insert(setmealDishes);
        }


    }



    @Override
    public SetmealVO getbyid(Long id) {
        SetmealVO setmealVO = new SetmealVO();
        Setmeal setmeal = setmealMapper.getbyid(id);
        BeanUtils.copyProperties(setmeal,setmealVO);

        List<SetmealDish> setmealDishes = setmealDishMapper.getBysetmealId(id);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    @Transactional
    @Override
    public void delete(List<Long> ids) {
        ids.forEach(id->{
            Setmeal setmeal = setmealMapper.getbyid(id);
            if(setmeal.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });


            setmealMapper.deletesetmeal(ids);
            setmealDishMapper.deletebysetmeal(ids);


    }

    @Transactional
    @Override
    public void update(SetmealDTO setmealDTO) {

        //copy properties from DTO to entity
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);

        Long id = setmeal.getId();
        setmealDishMapper.deletebysetmeal(Collections.singletonList(id));
        //get setmealdish from DTO
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        //update setmealdish based on setmeal id
        if(setmealDishes != null && !setmealDishes.isEmpty()){
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(id);
            });
            setmealDishMapper.insert(setmealDishes);
        }
    }

    @Override
    public void start(Integer status, Long id) {

        if(status == StatusConstant.ENABLE){
            List<Dish> dish = dishMapper.getBysmid(id);
            dish.forEach(dish1 -> {
                if(dish1.getStatus() == StatusConstant.DISABLE){
                    throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                }
            });
        }

        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(status);
        setmeal.setId(id);

        setmealMapper.update(setmeal);
    }
}
