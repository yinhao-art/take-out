package com.hyh.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyh.reggie.common.CustomException;
import com.hyh.reggie.dto.SetmealDto;
import com.hyh.reggie.entity.Setmeal;
import com.hyh.reggie.entity.SetmealDish;
import com.hyh.reggie.mapper.SetMealMapper;
import com.hyh.reggie.service.SetMealDishService;
import com.hyh.reggie.service.SetMealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetMealServiceImpl extends ServiceImpl<SetMealMapper, Setmeal> implements SetMealService {
    @Autowired
    private SetMealDishService setMealDishService;
    @Transactional
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐基本信息,操作setmeal,执行insert操作
        this.save(setmealDto);
        //保存菜品与套餐的信息,操作setmeal_dish,执行insert操作
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes= setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setMealDishService.saveBatch(setmealDishes);
    }
    @Transactional
    @Override
    public void removeWithDish(List<Long> ids) {
        //select count(*) from setmale where id in(1,2,3) and status=1
        //查询套餐状态,确定是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        int count = this.count(queryWrapper);
        if (count>0){
            //如果不能删除,抛出一个异常
            throw new CustomException("套餐正在售卖,不能删除");
        }
        //如果可以删除,先删除表中的套餐--setmeal
        this.removeByIds(ids);
        //delete from setmeal-dish where setmeal_id in(1,2,3)
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        //删除关系表中的数据--setmeal-dish
        setMealDishService.remove(lambdaQueryWrapper);
    }

}
