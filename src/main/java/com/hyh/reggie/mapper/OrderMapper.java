package com.hyh.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hyh.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

import javax.annotation.ManagedBean;

/**
 * @author xiaoH01
 * @date 2022/11/13 19:25
 */
@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
