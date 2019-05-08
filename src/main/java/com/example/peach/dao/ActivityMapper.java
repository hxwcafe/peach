package com.example.peach.dao;

import com.example.peach.pojo.Activity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Administrator on 2018/11/14.
 */
@Component
@Mapper
public interface ActivityMapper {
    Activity selectById(Integer id);

    int insertActivity(Activity activity);

    int updateActivity(Activity activity);

    int deleteActivity(Integer id);

    List<Activity> selectActivity();

    Activity selectByActnature(String actnature);

    List<Activity> selectActivityBydesc();//所有活动时间倒序

}
