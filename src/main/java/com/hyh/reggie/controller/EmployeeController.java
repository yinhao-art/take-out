package com.hyh.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyh.reggie.common.R;
import com.hyh.reggie.entity.Employee;
import com.hyh.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    /*
    * 员工登录
    */

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //1.将密码进行MD5加密处理
        String password =employee.getPassword();
        password= DigestUtils.md5DigestAsHex(password.getBytes());
        //2.查询数据库
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        //3.判断是否查到
        if(emp==null){
            return R.error("用户不存在,登录失败");
        }
        //4.密码比对
        if(!emp.getPassword().equals(password)){
            return R.error("密码错误,登录失败");
        }
        //5.查看员工状态
        if (emp.getStatus()==0){
            return R.error("账号已禁用");
        }
        //6.成功后,将员工id放入session并返回登录成功结果
       request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }
    /*
    员工退出
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清理session中保存的员工id
        request.getSession().removeAttribute("employee");
        return R.success("账号退出成功");
    }
    /*
    新增员工
     */

    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee){
        log.info("新增员工,员工信息 {}",employee.toString());
        //设置初始密码123456,md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //employee.setCreateTime(LocalDateTime.now());
        //employee.setUpdateTime(LocalDateTime.now());

        //获得当前登录用户的id
        //Long empId = (Long) request.getSession().getAttribute("employee");
        //employee.setCreateUser(empId);
        //employee.setUpdateUser(empId);
        employeeService.save(employee);
        return R.success("新增成功");
    }

    /*
    员工信息分页查询(使用mybatisPlus分页插件)
     */
    @GetMapping("/page")
    public  R<Page> page(int page,int pageSize,String name){
        //log.info("page={},pageSize={},name={}",page,pageSize,name);
        //构造分页构造器
        Page pageInfo=new Page(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper();
        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }
    /*
    根据id来修改员工信息
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());
        log.info("开始更新员工信息了");
        Long empId =(Long) request.getSession().getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(empId);
        employeeService.updateById(employee);
        return R.success("员工信息修改成功!");
    }

    /*
    根据id查员工信息
     */
    @GetMapping("/{id}")
    public R<Employee> getById( @PathVariable Long id){
        log.info("查询员工id");
        Employee employee = employeeService.getById(id);
        if (employee!=null){
            return R.success(employee);
        }
        return R.error("没有查询到员工信息");
    }
}
