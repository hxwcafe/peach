package com.example.peach.service;

import com.example.peach.common.ServiceResponse;
import com.example.peach.pojo.Admin;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AdminService {
    ServiceResponse<Void> login(HttpServletRequest request);//登录

    ServiceResponse<String> upateManager(HttpServletRequest request);//修改密码

    ServiceResponse<List<Admin>> addAdmin();//显示所有账号信息

    ServiceResponse<String> appendManager(HttpServletRequest request);//添加账号

    ServiceResponse<String> upateAdmin(HttpServletRequest request);//修改管理员

    ServiceResponse<String> judgelognAdmin(HttpServletRequest request);//判断是否登录,是否超级管理员
}
