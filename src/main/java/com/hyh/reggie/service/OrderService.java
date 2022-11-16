package com.hyh.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hyh.reggie.entity.Orders;

/**
 * @author xiaoH01
 * @date 2022/11/13 19:27
 */

public interface OrderService extends IService<Orders> {
    /**
     * 用户下单
     * @param orders
     */
    public void submit(Orders orders);
}
