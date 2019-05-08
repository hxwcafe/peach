package com.example.peach.dao;

import com.example.peach.pojo.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface AdminMapper {
    Admin selectAdminByAccount(@Param("account") String account);//根据登录账户查询用户
    Admin selectAdminByAccountAndPwd(@Param("account") String account, @Param("pwd") String pwd);//根据用户密码查询用户,登录用
    Integer addAdmin(Admin admin);//添加用户
//    Integer delAdminById(Admin admin);//删除用户
    Integer updatepwdByid(Admin admin);//修改密码
    List<Admin> selectAdmin();//查询所有账号信息
    Integer updateAdminById(Admin admin);//修改管理员信息
}
