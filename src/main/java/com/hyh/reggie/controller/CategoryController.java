package com.hyh.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyh.reggie.common.R;
import com.hyh.reggie.entity.Category;
import com.hyh.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
分类管理
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    /*
    新增商品
     */
    @Autowired
    private CategoryService categoryService;
    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("category:{}",category);
        categoryService.save(category);
        return R.success("新增商品成功");
    }

    /*
    菜品分页展示
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize ){
        //构造分页器
        Page<Category> pageInfo=new Page<>(page,pageSize);
        //分页条件
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        //添加排序条件,根据sort进行排序
        queryWrapper.orderByDesc(Category::getSort);
        //执行查询
        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }
    /*
    删除菜品信息,这里注意请求参数是ids,小坑!
     */
    @DeleteMapping
    public R<String> deleteById(@RequestParam Long ids){
        log.info("删除分类,id:{}",ids);
        //categoryService.removeById(id);
        categoryService.remove(ids);
        return R.success("分类删除成功");
    }
    /**
    根据id修改菜品信息,前端请求是json格式,加上@RequestBody注解
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类信息,{}",category);
        categoryService.updateById(category);
        return R.success("修改分类成功");
    }

    /**
     * 根据条件查询菜品分类数据,显示在菜品管理的下拉框中
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        //条件构造器
        LambdaQueryWrapper<Category> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        //添加条件
        lambdaQueryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        //添加排序条件
        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(lambdaQueryWrapper);
        return R.success(list);
    }


}


