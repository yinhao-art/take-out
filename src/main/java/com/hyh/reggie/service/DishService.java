package com.hyh.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hyh.reggie.dto.DishDto;
import com.hyh.reggie.entity.Dish;

public interface DishService extends IService<Dish> {
    //新增菜品同时插入菜品对应的口味数据,两张表:dish,dish_flavor
    public void saveWithFlavor(DishDto dishDto);
    //根据id来查询菜品和口味信息
    public  DishDto getByIdWithFlavor(Long id);
    //更新菜品信息和口味信息
    public void updateWithFlavor(DishDto dishDto);
}
