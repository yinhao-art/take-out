package com.hyh.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyh.reggie.common.R;
import com.hyh.reggie.dto.SetmealDto;
import com.hyh.reggie.entity.Category;
import com.hyh.reggie.entity.Setmeal;
import com.hyh.reggie.service.CategoryService;
import com.hyh.reggie.service.SetMealDishService;
import com.hyh.reggie.service.SetMealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 套餐管理
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetMealDishService setMealDishService;
    @Autowired
    private SetMealService setMealService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增套餐
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info("套餐信息,{}",setmealDto);
        setMealService.saveWithDish(setmealDto);
        log.info("test");
        return R.success("新增套餐成功");
    }

    /**
     * 套餐分页查询
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        Page<Setmeal> pageInfo=new Page<>(page,pageSize);
        //对象拷贝
        Page<SetmealDto> setmealDtoPage=new Page<>();
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name),Setmeal::getName,name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setMealService.page(pageInfo,queryWrapper);
        BeanUtils.copyProperties(pageInfo,setmealDtoPage,"records");

        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list=records.stream().map((item)->{
            SetmealDto setmealDto=new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            Long categoryId = item.getCategoryId();
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null){
                //分类名称
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
                return setmealDto;
        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(list);
        return R.success(setmealDtoPage);
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("ids:{}",ids);
        setMealService.removeWithDish(ids);
        return R.success("套餐删除成功!");
    }

    /**
     * 根据条件查询套餐数据
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public  R<List<Setmeal>> list( Setmeal  setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list=setMealService.list(queryWrapper);
        return R.success(list);
    }
}

