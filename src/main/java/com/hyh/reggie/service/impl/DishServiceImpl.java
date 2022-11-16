package com.hyh.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyh.reggie.dto.DishDto;
import com.hyh.reggie.entity.Dish;
import com.hyh.reggie.entity.DishFlavor;
import com.hyh.reggie.mapper.DishMapper;
import com.hyh.reggie.service.DishFlavorService;
import com.hyh.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper,Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    /**
     * 新增菜品同时保存口味数据
     * @param dishDto
     */
    @Transactional
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品到菜品表dish
        this.save(dishDto);
       //dishid已经生成,通过遍历数组来获得dishid,以便传入菜品口味表
        //Long dishId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors= flavors.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        //保存菜品口味到菜品口味表中(saveBatch是保存数组的方法)
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 根据id查询菜品和口味信息
     * @param id
     * @return
     */
    public DishDto getByIdWithFlavor(Long id){
        //查询菜品基本信息
        Dish dish = this.getById(id);
        DishDto dishDto=new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        //查询菜品对应口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavor = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavor);
        return dishDto;
    }
    @Transactional

    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表
        this.updateById(dishDto);
        //清理菜品口味dish_flavor的delete
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        //添加提交过来的口味dish_flavor的insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors= flavors.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }
}
