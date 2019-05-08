package com.example.peach.service.impl;

import com.example.peach.common.ServiceResponse;
import com.example.peach.dao.AdminMapper;
import com.example.peach.pojo.Admin;
import com.example.peach.service.AdminService;
import com.example.peach.util.Check;
import com.example.peach.util.MD5;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Resource
    private AdminMapper adminMapper;


    /**
     *
     * 登录
     *
     * uname     登录账户
     * pwd          密码
     * validateCode 验证码
     * @return true false
     */
    @Override
    public ServiceResponse<Void> login(HttpServletRequest request) {
        String account = request.getParameter("uname");
        String pwd = request.getParameter("password");
        String inCode = request.getParameter("verificationCode");
        Check c = new Check();
        try{
            c.checkRequestParam(new String[]{account,pwd,inCode});
        }catch(Exception e){
            return ServiceResponse.createByError(e.getMessage());
        }
        //未生成验证码的极端情况
        if(request.getSession(true).getAttribute("verificationCode") == null){
            return ServiceResponse.createByError("验证码错误.");
        }
        String sysCode = request.getSession(true).getAttribute("verificationCode").toString();

        if(!sysCode.toUpperCase().equals(inCode.toUpperCase())){

            return ServiceResponse.createByError("验证码错误,请重新输入.");
        }
        Admin admin = adminMapper.selectAdminByAccountAndPwd(account, MD5.MD5Encode(pwd.trim()));
        if( admin != null){
            if(admin.getIsProhibit() == 1){
                //用户被禁用
                return ServiceResponse.createByError("账号被禁用,登陆失败!");
            }
            request.getSession(true).setAttribute("loginInfo",admin);
        }else{
            return ServiceResponse.createByError("帐号或密码错误!");
        }

        return ServiceResponse.createBySuccess("登陆成功");
    }

    /**
     *  修改密码
     * @param request uanme用户账号 oldpass 原密码  newpass新密码
     * @return true false
     */
    @Override
    public ServiceResponse<String> upateManager(HttpServletRequest request) {
        String account = request.getParameter("uname");
        String npwd = request.getParameter("newpass");
        String pwd = request.getParameter("oldpass");
        Check c = new Check();
        try{
            c.checkRequestParam(new String[]{account,pwd,npwd});
        }catch(Exception e){
            return ServiceResponse.createByError(e.getMessage());
        }
        Admin admin = adminMapper.selectAdminByAccountAndPwd(account, MD5.MD5Encode(pwd.trim()));
        if( admin != null){
            admin.setPassword(MD5.MD5Encode(npwd.trim()));
            int getrows = adminMapper.updatepwdByid(admin);
            if(getrows>0){
                return ServiceResponse.createBySuccess("修改成功");
            }
            return ServiceResponse.createByError("修改失败!");
        }else{
            return ServiceResponse.createByError("原密码错误!");
        }
    }
    //查询所有账号信息
    @Override
    public ServiceResponse<List<Admin>> addAdmin() {
        List<Admin> adminList= adminMapper.selectAdmin();
        for(Admin admin:adminList){
            admin.setPassword(null);
        }
      return ServiceResponse.createBySuccess(adminList);
    }

    /**
     * 添加管理员
     * @param request Admin
     * @return true false
     */
    @Override
    public ServiceResponse<String> appendManager(HttpServletRequest request) {
        Admin admin = new Admin();
        String account = request.getParameter("account");
        String password =request.getParameter("password");
        String nikeName = request.getParameter("nikeName");
        try{
            new Check().checkRequestParam(new String[]{account,password,nikeName});
        }catch(Exception e){
            return ServiceResponse.createByError(e.getMessage());
        }
        admin.setAccount(account);
        admin.setPassword(MD5.MD5Encode(password));
        admin.setNikeName(nikeName);
        if(request.getParameter("isSuper").equals("是")){
            admin.setIsSuper(1);
        }else{
            admin.setIsSuper(0);
        }
        //账号是否存在
        if(adminMapper.selectAdminByAccount(account)!=null){
            return ServiceResponse.createBySuccess("账号已存在!");
        }

        int getrows = adminMapper.addAdmin(admin);
        if(getrows>0){
            return ServiceResponse.createBySuccess("添加成功");
        }
       return  ServiceResponse.createByError("添加失败");
    }

    /**
     *  修改管理员
     * @param request Admin
     * @return true false
     */
    @Override
    public ServiceResponse<String> upateAdmin(HttpServletRequest request) {
        Admin admin = new Admin();
        admin.setId(Integer.parseInt(request.getParameter("id")));
        admin.setAccount(request.getParameter("account"));
        admin.setNikeName(request.getParameter("nikeName"));
        if(request.getParameter("isSuper").equals("是")){
            admin.setIsSuper(1);
        }else{
            admin.setIsSuper(0);
        }
        if(request.getParameter("isProhibit").equals("是")){
            admin.setIsProhibit(1);
        }else{
            admin.setIsProhibit(0);
        }
        int getrows = adminMapper.updateAdminById(admin);
        if(getrows>0){
            return ServiceResponse.createBySuccess("修改成功");
        }
        return ServiceResponse.createByError("修改失败");
    }

    /**
     * 判断是否登录,是否超级管理员
     * @return true false
     */
    @Override
    public ServiceResponse<String> judgelognAdmin(HttpServletRequest request) {
        Admin admin = (Admin) request.getSession().getAttribute("loginInfo");
        if(admin==null){
            return ServiceResponse.createByError("您没有进行登录");
        }else if(admin.getIsSuper()==0){
            return ServiceResponse.createByError("您不是超级管理员");
        }
        return ServiceResponse.createBySuccess();
    }


}
