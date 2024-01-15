package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

//    @Select("select * from setmeal_dish where dish_id = #{id} ")
    List<Long> getById(List<Long> id);

    @Delete("delete from setmeal_dish where dish_id = #{id}")
    void deletebydishid(List<Long> id);


    void insert(List<SetmealDish> setmealDishes);

    List<SetmealDish> getBysetmealId(Long id);

    void deletebysetmeal(List<Long> smid);


    List<SetmealDish> getbydishid(Long id);
}
