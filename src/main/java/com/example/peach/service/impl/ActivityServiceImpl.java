package com.example.peach.service.impl;

import com.example.peach.common.Conts;
import com.example.peach.common.ServiceResponse;
import com.example.peach.dao.ActivityMapper;
import com.example.peach.pojo.Activity;
import com.example.peach.service.ActivityService;
import com.example.peach.util.FileUpload;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/11/15.
 */
@Service
public class ActivityServiceImpl implements ActivityService {
    @Resource
    private ActivityMapper activityMapper;

    /**
     * 根据活动id查询
     * @param id
     * @return
     */
    @Override
    public ServiceResponse<Map> selectById(Integer id) {
        Activity activity=activityMapper.selectById(id);
        if (activity!=null){
            Map<String,Object> map=new HashMap<>();
            map.put("activity",activity);
            return ServiceResponse.createBySuccess("有活动",map);
        }
        return ServiceResponse.createByError("没活动");
    }

    /**
     * 添加活动对象
     * @param activity
     * @return
     */
    @Override
    public ServiceResponse<String> insertActivity(Activity activity) {
        int insert=  activityMapper.insertActivity(activity);
        if (insert>0){
            return  ServiceResponse.createBySuccess("添加成功");
        }
        return ServiceResponse.createByError("添加失败");
    }

    /**
     * 根据id修改
     * @param activity
     * @return
     */
    @Override
    public ServiceResponse<String> updateActivity(Activity activity) {
        Activity activity1 = activityMapper.selectById(activity.getId());
        //删除原有照片
        FileUpload.filedele(Conts.ActivityImg,activity1.getActimgurl());
        int update=  activityMapper.updateActivity(activity);
        if (update>0){
            return  ServiceResponse.createBySuccess("修改成功");
        }
        return ServiceResponse.createByError("修改失败");
    }

    /**
     *删除活动
     * @param id
     * @return
     */
    @Override
    public ServiceResponse<String> deleteActivity(Integer id) {
        int delete=activityMapper.deleteActivity(id);
        if (delete>0){
            return  ServiceResponse.createBySuccess("删除成功");
        }
        return ServiceResponse.createByError("删除失败");
    }

    /**
     * 查询所有活动
     * @return
     */
    @Override
    public ServiceResponse<Map<String,List<Activity>>> selectActivity() {
        List<Activity> activityList = activityMapper.selectActivity();
        Map<String,List<Activity>> listMap = new HashMap<>();
        if(activityList!=null){
            List<Activity> bzlist = new ArrayList<Activity>();
            List<Activity> hwlist = new ArrayList<Activity>();
            List<Activity> rmlist = new ArrayList<Activity>();
            for(Activity activity:activityList){
                if(activity.getActname().equals("本周活动")){
                    bzlist.add(activity);
                }else if (activity.getActname().equals("户外活动")){
                    hwlist.add(activity);
                }else if(activity.getActname().equals("热门活动")){
                    rmlist.add(activity);
                }
            }
            listMap.put("bzlist",bzlist);
            listMap.put("hwlist",hwlist);
            listMap.put("rmlist",rmlist);
            return ServiceResponse.createBySuccess(listMap);
        }
      return ServiceResponse.createByError("没有查询到");
    }

    /**
     * 活动发布的参数处理
     * @param request activity
     * @return activity
     */
    @Override
    public Activity activityMzw(HttpServletRequest request) throws ParseException {
        Activity activity = new Activity();
        activity.setActname(request.getParameter("actname"));
        activity.setActcontentname(request.getParameter("actcontentname"));
        activity.setActplace(request.getParameter("actplace"));
        activity.setActcost(Double.parseDouble(request.getParameter("cost")));
        activity.setActdeposit(Double.valueOf(request.getParameter("deposit")));
        activity.setActrule(request.getParameter("actrule"));
        activity.setActcontent(request.getParameter("actcontent"));
        activity.setActnature(request.getParameter("actnature"));
        activity.setActinvitation(request.getParameter("actinvitation"));
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        activity.setActtime(new Timestamp(sdf.parse(request.getParameter("starttime")).getTime()));
        activity.setActendtime(new Timestamp(sdf.parse(request.getParameter("endtime")).getTime()));
        return  activity;
    }

    /**
     * 所有活动时间倒序
     * @return List Activity活动表
     */
    @Override
    public ServiceResponse<List<Activity>> allActivitydesc() {
        List<Activity> activityList = activityMapper.selectActivityBydesc();
        if (activityList==null){
            return ServiceResponse.createByError("没有活动");
        }
        return ServiceResponse.createBySuccess(activityList);
    }
}