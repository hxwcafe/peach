package com.example.peach.controllermanager;


import com.example.peach.common.ServiceResponse;
import com.example.peach.pojo.Admin;
import com.example.peach.service.AdminService;
import com.example.peach.util.ImageUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private AdminService adminService;

    //验证码
    @RequestMapping("/getVerificationCode")
    @ResponseBody
    public void getVerificationCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //第一个参数是生成的验证码，第二个参数是生成的图片
        Object[] objs = ImageUtil.createImage();
        //将验证码存入Session
        request.getSession(true).setAttribute("verificationCode",objs[0]);
        System.out.println("验证码是:"+objs[0]);
        //将图片输出给浏览器
        BufferedImage image = (BufferedImage) objs[1];
        // 禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        OutputStream os = response.getOutputStream();
        ImageIO.write(image, "jpeg", os);
        os.flush();
    }

    //登录
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ServiceResponse<Void> recordExtension(HttpServletRequest request){
        return  adminService.login(request);
    }

    //登录态验证(用于限制页面的访问)
    @RequestMapping(value = "/sessionVerification",method = RequestMethod.GET)
    public ServiceResponse<Admin> sessionVerification(HttpServletRequest request){
        Object obj = request.getSession().getAttribute("loginInfo");
        Admin admin =  (Admin) obj;
        if(admin != null){
            admin.setPassword(null);
            admin.setCreateTime(null);
            admin.setUpdateTime(null);
            return ServiceResponse.createBySuccess(admin);
        }
        return ServiceResponse.createByError(null);
    }


    //退出登录
    @RequestMapping(value = "/loginOut",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<Void> loginOut(HttpServletRequest request) {
        request.getSession().removeAttribute("loginInfo");
        return ServiceResponse.createBySuccess("成功退出登录!");
    }

    /**
     *  修改密码
     * @param request uanme 用户账号 password 原密码  newpass新密码
     * @return true false
     */
    @RequestMapping(value = "/updatelogin",method =RequestMethod.POST)
    public ServiceResponse<String> upateManager(HttpServletRequest request){
            return adminService.upateManager(request);
    }

    /**
     * 查询所有账号信息
     * @return List<Admin>
     */
    @RequestMapping(value = "/sadmin",method = RequestMethod.GET)
    public ServiceResponse<List<Admin>> selectAdmin(){
        return adminService.addAdmin();
    }

    /**
     * 添加账号
     * @param request Admin
     * @return true false 0 请进行登录
     */
    @RequestMapping(value = "/appendmanager",method = RequestMethod.POST)
    public ServiceResponse appendManager(HttpServletRequest request){
        ServiceResponse response = adminService.judgelognAdmin(request);
        if(!response.isSuccess()) {
            return response;
        }
        return adminService.appendManager(request);
    }

    /**
     *  修改管理员
     * @param request Admin
     * @return true false
     */
    @RequestMapping(value = "/updatemanager",method = RequestMethod.POST)
    public ServiceResponse updateManager(HttpServletRequest request){
        ServiceResponse response = adminService.judgelognAdmin(request);
        if(!response.isSuccess()) {
            return response;
        }
        return adminService.upateAdmin(request);
    }
}
