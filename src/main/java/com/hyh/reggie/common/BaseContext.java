package com.hyh.reggie.common;

/*
基于threadlocal封装的工具类,用户保存和获取当前用户的id
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal=new ThreadLocal<>();
    /*
    设置值
     */
    public  static void  setCurrentId(Long id){
        threadLocal.set(id);
    }
    /*
    获取值
     */
    public  static  Long getCurrentId(){
        return threadLocal.get();
    }
}
