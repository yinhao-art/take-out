package com.hyh.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hyh.reggie.common.R;
import com.hyh.reggie.entity.User;
import com.hyh.reggie.service.UserService;
import com.hyh.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 发送手机验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession httpSession){
        //获取手机号
        String phone= user.getPhone();
        //随机生成4位号码
        if (StringUtils.isNotEmpty(phone)) {
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code:{}",code);
            httpSession.setAttribute(phone,code);
            return R.success("短信发送成功");
        }
            return R.error("短信发送失败");
        //调用阿里云短信服务
        //保存到session
    }
    @PostMapping("/login")
    public R<User> sendMsg(@RequestBody Map map, HttpSession httpSession){
        log.info(map.toString());
        //从map中获取手机号和验证码
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
        //从session中获取验证码,进行比对(取出key就行)
        Object codeInSession = httpSession.getAttribute(phone);
        if (codeInSession !=null && codeInSession.equals(code)){
            LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            if (user==null ){
                //判断当前手机号是否新用户,是就直接注册
                user=new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            httpSession.setAttribute("user",user.getId());
            return R.success(user);
        }
        return R.error("登录失败了哦");

    }


}
