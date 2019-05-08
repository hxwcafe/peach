package com.example.peach.controllermanager;

import com.example.peach.common.Conts;
import com.example.peach.common.ServiceResponse;
import com.example.peach.pojo.ActOrder;
import com.example.peach.pojo.Activity;
import com.example.peach.pojo.merge.MovieMYuser;
import com.example.peach.pojo.merge.UAlbum;
import com.example.peach.service.ActOrderService;
import com.example.peach.service.ActivityService;
import com.example.peach.service.AdminService;
import com.example.peach.service.PublicService;
import com.example.peach.util.FileUpload;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping(value = "/mactivity")
public class ActivityMController {
    @Resource
    private ActivityService activityService;
    @Resource
    private AdminService adminService;
    @Resource
    private ActOrderService actOrderService;
    @Resource
    private PublicService publicService;

    @RequestMapping(value = "/insertActivity",method = RequestMethod.POST)
    public ServiceResponse<String> insertActivitys(@RequestParam(value = "file",required=false)MultipartFile file, HttpServletRequest request) throws ParseException {
        ServiceResponse<String> lognResponse = adminService.judgelognAdmin(request);
        if(!lognResponse.isSuccess()) {
            return lognResponse;
        }
        Activity activity = activityService.activityMzw(request);
        String scfille = Conts.ActivityImgURL;//用户相册路径
        String photoFolder = Conts.ActivityImg;
        String fileName = FileUpload.fileUpload(file, scfille,photoFolder);
        activity.setActimgurl(fileName);
        return activityService.insertActivity(activity);
    }

    /**
     * 时间倒序查询所有活动
     * @return activit实体类
     */
    @RequestMapping(value = "/allactivity",method = RequestMethod.GET)
    public ServiceResponse allActivitybesc(){
        return activityService.allActivitydesc();
    }

    /**
     * 查询该活动的所有用户
     * @param activityId 活动id
     * @return UAlbum(User)
     */
    @RequestMapping(value = "/allactivityuser",method = RequestMethod.GET)
    public List<UAlbum> allActivityUser(Integer activityId){
        return actOrderService.selectUAlbum(activityId);
    }

    /**
     * 根据id修改活动
     */
    @RequestMapping(value = "/updateactivitys",method = RequestMethod.POST)
    @Transactional
    public ServiceResponse<String> updateActivitys(@RequestParam(value = "file",required=false)MultipartFile file, HttpServletRequest request) throws ParseException {
//        ServiceResponse<String> lognResponse = adminService.judgelognAdmin(request);
//        if(!lognResponse.isSuccess()) {
//            return lognResponse;
//        }
        Activity activity = activityService.activityMzw(request);
        activity.setId(Integer.valueOf(request.getParameter("id")));
        String scfille = Conts.ActivityImgURL;//用户相册路径
        String photoFolder = Conts.ActivityImg;
        String fileName = FileUpload.fileUpload(file, scfille,photoFolder);
        activity.setActimgurl(fileName);
        return activityService.updateActivity(activity);
    }

    /**
     * 根据id删除
     */
    @RequestMapping(value = "/deleteactivitys",method = RequestMethod.POST)
    public ServiceResponse<String> deleteActivitys(Integer id,HttpServletRequest request){
        ServiceResponse<String> lognResponse = adminService.judgelognAdmin(request);
        if(!lognResponse.isSuccess()) {
            return lognResponse;
        }
        return activityService.deleteActivity(id);
    }

    /**
     * 活动报名导出
     */
    @RequestMapping(value = "/actexcel",method = RequestMethod.POST)
    public void activityExcel(Integer activityId,HttpServletResponse response) throws IOException {
        List<UAlbum> uAlbumList =actOrderService.selectUAlbum(activityId);
        publicService.ExcelDown(uAlbumList,response);
    }

    /**
     * 查询约伴电影邀请
     * @param activityId 活动id
     * @return List MovirMYuser
     */
    @RequestMapping(value = "/actmovie",method = RequestMethod.GET)
    public List<MovieMYuser> actorderMovie(Integer activityId){
       return actOrderService.selectMovieMYuser(activityId);
    }

    /**
     * 修改活动订单
     */
    @RequestMapping(value = "/upactorder",method = RequestMethod.POST)
    public ServiceResponse<String> updateManagerOrder(ActOrder actOrder,HttpServletRequest request){
        ServiceResponse<String> lognResponse = adminService.judgelognAdmin(request);
        if(!lognResponse.isSuccess()) {
            return lognResponse;
        }
        return actOrderService.updateManagerOrder(actOrder);
    }

    /**
     * 删除活动订单
     */
    @RequestMapping(value = "/delactorder",method = RequestMethod.POST)
    public ServiceResponse<String> deleteActOrder(Integer id,HttpServletRequest request){
        ServiceResponse<String> lognResponse = adminService.judgelognAdmin(request);
        if(!lognResponse.isSuccess()) {
            return lognResponse;
        }
        return actOrderService.delectActOrder(id);
    }
}
