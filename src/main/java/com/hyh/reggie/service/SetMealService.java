package com.hyh.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hyh.reggie.dto.SetmealDto;
import com.hyh.reggie.entity.Setmeal;

import java.util.List;

public interface SetMealService extends IService<Setmeal> {
    /**
     * 新增套餐,同时保存套餐与菜品的关联关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐,同时删除套餐与菜品的关联关系
     * @param ids
     */
    public void removeWithDish(List<Long> ids);
}
