package com.hyh.reggie.controller;

import com.hyh.reggie.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaoH01
 * @date 2022/11/13 19:35
 */
@Slf4j
@RestController
@RequestMapping
public class OrderDetailController {
    @Autowired
    private OrderDetailService orderDetailService;
}
