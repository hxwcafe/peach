package com.example.peach.controllermanager;

import com.example.peach.common.ServiceResponse;
import com.example.peach.pojo.Mate;
import com.example.peach.pojo.User;
import com.example.peach.pojo.merge.UAMate;
import com.example.peach.service.*;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserMController {
    @Resource
    private UserService userService;
    @Resource
    private UserVipService userVipService;
    @Resource
    private AdminService adminService;
    @Resource
    private MateService mateService;
    @Resource
    private UILinkedService uiLinkedService;
    @Resource
    private PublicService publicService;
    /**
     * 查询所有Al类用户
     * @return List<User> true false
     */
    @RequestMapping(value = "/usera",method = RequestMethod.GET)
    public ServiceResponse<List<User>> seletuserA(){
        return userService.selectAllA();
    }
    /**
     * 查询所有B类用户
     * @return List<User> true false
     */
    @RequestMapping(value = "/userb",method = RequestMethod.GET)
    public ServiceResponse<List<User>> seletuserB(){
        return userService.selectAllB();
    }

    /**
     * 查询所有B类用户
     * @return List<User> true false
     */
    @RequestMapping(value = "/userblack",method = RequestMethod.GET)
    public ServiceResponse<List<User>> seletuserBlack(){
        return userService.selectBlack();
    }

    //根据id动态修改vip信息
    @RequestMapping(value = "/upuservip",method = RequestMethod.POST)
    public ServiceResponse<String> updateUservip(HttpServletRequest request) throws ParseException {
        ServiceResponse<String> lognResponse = adminService.judgelognAdmin(request);
        if(!lognResponse.isSuccess()) {
            return lognResponse;
        }
        return userVipService.updateUservipByid(request);
    }

    /**
     * 后台插入用户
     * @param uaMate 用户所有信息
     * @param request 用户的登录状态
     * @return true  false
     */
    @RequestMapping(value = "/adduamate",method = RequestMethod.POST)
    @Transactional
    public  ServiceResponse insertallUser(UAMate uaMate,HttpServletRequest request){
        ServiceResponse<String> lognResponse = adminService.judgelognAdmin(request);
        if(!lognResponse.isSuccess()) {
            return lognResponse;
        }
        User user = uaMate;
        //插入user信息
        ServiceResponse userResponse = userService.insertUser(user);
        if(!userResponse.isSuccess()){
            return userResponse;
        }
        User user1 = userService.selectByphoneAndopenid(user.getUserphone());
        //插入兴趣关联表
        if(user1.getUserInterest()!=null) {
            ServiceResponse<String> stringResponse = uiLinkedService.UIKinkedHandle(user.getUserInterest(), user1.getId());
            if (!stringResponse.isSuccess()) {
                System.out.println(10 / 0);
                return stringResponse;
            }
        }
        Mate mate = uaMate.getMate();
        mate.setUserId(user1.getId());
        //插入择偶表
        ServiceResponse mateReponse = mateService.insertMate(mate);
        if(!mateReponse.isSuccess()){
            System.out.println(10/0);
            return mateReponse;
        }
        return ServiceResponse.createBySuccess("添加成功");
    }

    /**
     * 修改用户信息
     */
    @RequestMapping(value = "/upuser",method = RequestMethod.POST)
    public ServiceResponse<String> updateUser(User user,HttpServletRequest request){
//        ServiceResponse<String> lognResponse = adminService.judgelognAdmin(request);
//        if(!lognResponse.isSuccess()) {
//            return lognResponse;
//        }
        //查询数据库中原来兴趣
        User yzuser = userService.selectByPrimaryKey(user.getId());
        ServiceResponse<String> response = userService.updateUserByid(user);
        if(response.isSuccess()) {
            if(user.getUserInterest()!=null&&!user.getUserInterest().equals("")
                    &&!yzuser.getUserInterest().equals(user.getUserInterest())) {
                //修改兴趣关联表
                ServiceResponse<String> stringResponse = uiLinkedService.UIKinkedHandle(user.getUserInterest(), user.getId());
                if (stringResponse.isSuccess()) {
                    return ServiceResponse.createBySuccess("修改成功");
                }
                return stringResponse;
            }
            return ServiceResponse.createBySuccess("修改成功");
        }
        return response;
    }

    /**
     * 修改用户择偶要求
     * @param mate 择偶信息
     * @return true false
     */
    @RequestMapping(value = "/mangerupmate",method = RequestMethod.POST)
    public ServiceResponse<String> updateMate(Mate mate,HttpServletRequest request){
        ServiceResponse<String> lognResponse = adminService.judgelognAdmin(request);
        if(!lognResponse.isSuccess()) {
            return lognResponse;
        }
        return mateService.updateMate(mate);
    }
    //删除用户所有信息
    @RequestMapping(value = "/deluser",method = RequestMethod.POST)
    @Transactional
    public ServiceResponse<String> delectUser(Integer id,HttpServletRequest request){
        ServiceResponse<String> lognResponse = adminService.judgelognAdmin(request);
        if(!lognResponse.isSuccess()) {
            return lognResponse;
        }
        ServiceResponse delectUser = userService.delectByid(id);
        if(!delectUser.isSuccess()){
            return ServiceResponse.createByError("删除失败");
        }
        userVipService.delectUvip(id);
        mateService.delectmate(id);
        return ServiceResponse.createBySuccess("删除成功");
    }

    /**
     *导出用户信息
     * @param ides 用户id
     * @param request 验证管理员
     * @param response excel文件
     */
    @RequestMapping(value = "/exceluserss",method = RequestMethod.GET, produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void excelUser(String ides, HttpServletRequest request, HttpServletResponse response){
       publicService.ExcelUser(ides,response);
    }


}
