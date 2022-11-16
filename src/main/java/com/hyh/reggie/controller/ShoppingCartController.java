package com.hyh.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hyh.reggie.common.BaseContext;
import com.hyh.reggie.common.R;
import com.hyh.reggie.entity.ShoppingCart;
import com.hyh.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xiaoH01
 * @date 2022/11/13 10:52
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info("shoppingCart: {}",shoppingCart);
        //设置用户id,指定是那个用户的购物车数据
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        //查询菜品或者套餐是否在购物车中
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        if (dishId != null){
            //添加到购物车是菜品
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else {
            //添加到购物车是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        //sql:select * from shoppingcart where user_id=? and dish_id/setmeal_id=?
        ShoppingCart shoppingCartServiceOne = shoppingCartService.getOne(queryWrapper);
        if (shoppingCartServiceOne!=null){
            //如果已经存在,在原来基础上加一
            Integer number = shoppingCartServiceOne.getNumber();
            shoppingCartServiceOne.setNumber(number+1);
            shoppingCartService.updateById(shoppingCartServiceOne);
        }else {
            //如果不存在,加入购物车,默认数量为1
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            shoppingCartServiceOne=shoppingCart;
        }
        return R.success(shoppingCartServiceOne);
    }

    /**
     * 查看购物车
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        log.info("查看购物车...");
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(queryWrapper);

        return R.success(shoppingCartList);
    }

    /**
     *清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean(){
        //sql:delet from shopping_cart where use_id=?
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper);
        return R.success("清空购物车成功");
    }

    /**
     * 删除菜品(与添加一样)
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public R<String> sub(@RequestBody ShoppingCart shoppingCart) {
        shoppingCart.setUserId(BaseContext.getCurrentId());
        // 查询当前菜品或套餐是否 在购物车中
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        // 根据登录用户的 userId去ShoppingCart表中查询该用户的购物车数据
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        // 添加进购物车的是菜品，且 购物车中已经添加过 该菜品
        if (dishId != null) {
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart oneCart = shoppingCartService.getOne(queryWrapper);
        //  如果购物车中 已经存在该菜品或套餐
        if (oneCart != null) {
            Integer number = oneCart.getNumber();
            // 如果数量大于 0，使其数量减1， 否则清除
            if (number != 0) {
                oneCart.setNumber(number - 1);
                shoppingCartService.updateById(oneCart);
            } else {
                shoppingCartService.remove(queryWrapper);
            }
        }
        return R.success("删除菜品成功");
    }

}
