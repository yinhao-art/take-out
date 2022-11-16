package com.hyh.reggie.dto;

import com.hyh.reggie.entity.Setmeal;
import com.hyh.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
