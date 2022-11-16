package com.hyh.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyh.reggie.common.CustomException;
import com.hyh.reggie.entity.Category;
import com.hyh.reggie.entity.Dish;
import com.hyh.reggie.entity.Setmeal;
import com.hyh.reggie.mapper.CategoryMapper;
import com.hyh.reggie.service.CategoryService;
import com.hyh.reggie.service.DishService;
import com.hyh.reggie.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper,Category> implements CategoryService{

    @Autowired
    private DishService dishService;
    @Autowired
    private SetMealService setMealService;

    /*
    根据id删除分类,删除之前要判断
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper=new LambdaQueryWrapper<>();
        //添加查询条件,根据分类id
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        //查询当前分类是否关联菜品,如果关联,抛出异常
        if (count1>0){
            throw new CustomException("当前分类关联了菜品,不能删除哦");
        }
        //查询当前分类是否关联套餐,如果关联,抛出异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper=new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setMealService.count(setmealLambdaQueryWrapper);
        if (count2>0){
            throw new CustomException("当前分类关联了套餐,不能删除哦");
        }
        //正常删除
        super.removeById(id);
    }
}
