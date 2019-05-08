package com.example.peach.controller;


import com.example.peach.common.Pager;
import com.example.peach.common.ServiceResponse;
import com.example.peach.pojo.User;
import com.example.peach.pojo.merge.UAlbum;
import com.example.peach.service.UILinkedService;
import com.example.peach.service.UserService;
import com.example.peach.service.UserVipService;
import com.example.peach.util.DateSub;
import org.apache.commons.lang.StringUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private UILinkedService uiLinkedService;
    @Resource
    private UserVipService userVipService;
    /**
     * 根据openid查询用户信息
     * @param openid
     * @return
     */
    @RequestMapping(value = "/selectopenid",method = RequestMethod.GET)
    public ServiceResponse selectOpenid(@RequestParam String openid){
       ServiceResponse openidResponse = userService.selectByOpenid(openid,"openid");
       if(openidResponse.isSuccess()){
          ServiceResponse userVipResponse =  userVipService.selectUvipUserByopenid(openid);
          return userVipResponse;
       }
       return openidResponse;
    }
    /**
     *  查询手机号是否已经绑定
     */
    @RequestMapping(value = "/selectPhone",method = RequestMethod.POST)
    public ServiceResponse<String> selectPhone(@RequestParam("userphone") String phone){
        ServiceResponse<String> response = userService.selectPhone(phone);
        return response;
    }

    /**
     * 根据Id查询用户
     */
    @RequestMapping(value = "/selectById",method = RequestMethod.GET)
    public  Map selectById(@RequestParam Integer id){
       User user= userService.selectByPrimaryKey(id);
        Map<String,Object> map=new HashMap<>();
        map.put("user",user);
        return map;
    }
    /**
     * 用户信息完善
     */
    @RequestMapping(value = "/updateuser",method = RequestMethod.POST)
    public ServiceResponse<String> updateUser(User user){
        if(user.getUserBirthday()!=null){
            user.setUserage(DateSub.getYear(user.getUserBirthday()));
        }
        ServiceResponse<String> response = userService.updateUser(user);
        return response;
    }

    /**
     * 查询所有用户
     */
    @RequestMapping(value = "/userList",method = RequestMethod.GET)
    public Map<String,Object> userList(){
        List<User> users= userService.userList();
        Map<String,Object> map =new HashMap<>();
        map.put("userlist",users);
        return  map;
    }
    /**
     * 更改兴趣爱好
     * @param
     * @return
     */
    @RequestMapping(value = "/updatecategory",method = RequestMethod.POST)
    public ServiceResponse<String> updatecategory(String[] string , User user){
        String str= StringUtils.join(string,",");
        user.setUserInterest(str);
        ServiceResponse updateResponse =  userService.updateUser(user);
        if(updateResponse.isSuccess()){
            User useradd =  userService.selectByOpenid(user.getOpenid());
           ServiceResponse<String> stringResponse = uiLinkedService.UIKinkedHandle(str,useradd.getId());
           return stringResponse;
        }
        return updateResponse;
    }

    /**
     * 修改个性签名
     * @param user
     * @return
     */
    @RequestMapping(value = "/updatautograph",method = RequestMethod.POST)
    public ServiceResponse<String> updatAutograph(User user){
        if(userService.selectByOpenid(user.getOpenid()).getUserBlacklist()==1){//查看用户是否是黑名单
            return ServiceResponse.createByError("用户处于黑名单内");
        }
        return userService.updateUser(user);
    }

    /**
     *修改个人
     * @param user
     * @return
     */
    @RequestMapping(value = "/updatelabel",method = RequestMethod.POST)
    public ServiceResponse<String> updatLabel(User user){
        return userService.updateUser(user);
    }
    /**
     * 用户认证 是：true
     * @param
     * @return
     */
    @RequestMapping(value = "/updateRealName",method = RequestMethod.POST)
    public ServiceResponse<String> updateRealName(User user){
        user.setIsrealname(true);
        return userService.updateUser(user);
    }
    @RequestMapping(value = "/test")
    public String test(Model model){
        model.addAttribute("name","saa");
        return "demo";
    }

    /**
     * 查询用户兴趣
     * @param openId 用户唯一标识
     * @return List
     */
    @RequestMapping(value = "/interest",method = RequestMethod.GET)
    public ServiceResponse<List<String>> selectUserInterestByOpenId(String openId){
       User user = userService.selectByOpenid(openId);
       ServiceResponse<List<String>> stringResponse = userService.selectUserInterestByOpenId(user);
       return stringResponse;

    }

    /**
     *  根据年龄区间查询用户
     * @param ageone 最小年龄
     * @param agetwe 最大年龄
     * @return List<UAlbum></>
     */
    @RequestMapping(value = "/sectionage",method = RequestMethod.GET)
    public ServiceResponse<Pager<UAlbum>> selectByUserage(Integer ageone, Integer agetwe, Integer pageIndex){
        return userService.selectByUserage(ageone,agetwe,pageIndex);
    }

    /**
     * 根据学历查询用户及相册 或根据薪资
     * @param user userEducation学历 或 userSalary薪资
     * @return Pager<UAlbum>
     */
    @RequestMapping(value = "/sectioneands",method = RequestMethod.GET)
    public ServiceResponse<Pager<UAlbum>> selectByUserEandS(User user, Integer pageIndex){
        return userService.selectByUserEandS(user,pageIndex);
    }

    /**
     * 修改用户提示
     * @param user user实体类
     * @return true false
     */
    @RequestMapping(value = "/updatetips",method = RequestMethod.POST)
    public ServiceResponse<String> updateTips(User user){
        return userService.updateUser(user);
    }

    /**
     * 显示所用用户(用户相册)
     * @param pageIndex (当前显示的页数)
     * @return Pager<UAlbum>
     */
    @RequestMapping(value = "/alluser",method = RequestMethod.GET)
    public ServiceResponse<Pager<UAlbum>> allUAlbum(Integer pageIndex){
        return userService.allUAlbum(pageIndex);
    }


}
