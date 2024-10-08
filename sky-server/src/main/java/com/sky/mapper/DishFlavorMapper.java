package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface DishFlavorMapper {


    void insertFlavor(List<DishFlavor> flavors);

    void deleteflavorbyid(Long id);

     List<DishFlavor> getflavorbyid(Long id);
}
