package com.hyh.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hyh.reggie.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
