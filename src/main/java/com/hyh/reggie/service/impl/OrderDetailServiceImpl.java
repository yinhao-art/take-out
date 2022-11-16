package com.hyh.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyh.reggie.entity.OrderDetail;
import com.hyh.reggie.mapper.OrderDetailMapper;
import com.hyh.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @author xiaoH01
 * @date 2022/11/13 19:29
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
