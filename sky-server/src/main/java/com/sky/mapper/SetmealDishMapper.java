package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

//    @Select("select * from setmeal_dish where dish_id = #{id} ")
    List<Long> getById(List<Long> id);

    @Delete("delete from setmeal_dish where dish_id = #{id}")
    void deletebydishid(List<Long> id);
}
