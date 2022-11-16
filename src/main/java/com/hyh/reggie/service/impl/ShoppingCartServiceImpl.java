package com.hyh.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyh.reggie.entity.ShoppingCart;
import com.hyh.reggie.mapper.ShoppingCartMapper;
import com.hyh.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @author xiaoH01
 * @date 2022/11/13 10:51
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
