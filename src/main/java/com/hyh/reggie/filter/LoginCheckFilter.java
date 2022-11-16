package com.hyh.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.hyh.reggie.common.BaseContext;
import com.hyh.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
检查用户是否登录
 */
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器,支持通配符
    public  static  final AntPathMatcher PATH_MATCHER=new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;
        //1.获取本次请求的uri
        String requestURI = request.getRequestURI();
        //log.info("拦截到请求:{}",requestURI);
        //定义不需要请求的路径
        String [] urls=new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",//移动段发送短信
                "/user/login"//移动端登录
        };
        //2.判断本次请求是否需要放行
        boolean check = check(urls, requestURI);
        //3.如果不需要处理,直接放行
        if (check){
            //log.info("本次请求不需要处理{}",requestURI);
            filterChain.doFilter(request,response);
            return;
        }

        //4-1.判断登录状态,如果登录,直接放行
        if (request.getSession().getAttribute("employee")!=null){
            //log.info("用户已登录,用户id是{}",request.getSession().getAttribute("employee"));
            Long empID =(Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empID);

            filterChain.doFilter(request,response);
            return;
        }
        //4-2.判断用户登录状态,如果登录,直接放行
        if (request.getSession().getAttribute("user")!=null){
            //log.info("用户已登录,用户id是{}",request.getSession().getAttribute("employee"));
            Long userID =(Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userID);
            filterChain.doFilter(request,response);
            return;
        }
        //log.info("用户没登录");
        //5.如果没有登录,返回结果,输出流的方式像客户端相应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;

    }
    /*
    路径匹配
     */
    public  boolean check(String[]urls ,String requestURI){
        for (String url: urls){
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }
}

