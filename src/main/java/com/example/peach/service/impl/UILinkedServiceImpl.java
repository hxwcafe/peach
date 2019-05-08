package com.example.peach.service.impl;

import com.example.peach.common.ServiceResponse;
import com.example.peach.dao.InterestMapper;
import com.example.peach.dao.UILinkedMapper;
import com.example.peach.pojo.Interest;
import com.example.peach.pojo.UILinked;
import com.example.peach.service.UILinkedService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class UILinkedServiceImpl implements UILinkedService {
    @Resource
    private InterestMapper interestMapper;
    @Resource
    private UILinkedMapper uiLinkedMapper;

    /**
     * 循环添加
     * @param intlist 添加的集合
     * @param userId 用户id
     * @return
     */
    @Transactional
    @Override
    public ServiceResponse<String> addUILinked(List<Integer> intlist, Integer userId) {
        UILinked uiLinked = new UILinked();
        uiLinked.setUserId(userId);
        for (Integer integer : intlist) {
            uiLinked.setInterestId(integer);
            int getrows = uiLinkedMapper.insert(uiLinked);
           if(getrows<=0){
                return ServiceResponse.createBySuccess("添加失败");
            }
        }
        return ServiceResponse.createBySuccess("添加成功");
    }

    /**
     * 修改,添加兴趣
     * @param category 兴趣详细
     * @param userId 用户id
     * @return
     */
    @Transactional
    @Override
    public ServiceResponse<String> UIKinkedHandle(String category, Integer userId){
        //进行逗号分割
        String[] as = category.split(",");
        List<Integer> intlist = new ArrayList<Integer>();
        for (int i = 0; i < as.length; i++) {
            //模糊查询获取兴趣信息
            Interest interest = interestMapper.selectLikeCategory(as[i]);
            if (interest != null) {
                //去除重复的id
                if (!intlist.contains(interest.getId())) {
                    intlist.add(interest.getId());
                }
            }
        }
        if (intlist != null) {
            //查询数据库中用户兴趣id
            List<UILinked> uiLinkedListist = uiLinkedMapper.selectByUserId(userId);
            //判断数据库中是否有兴趣,有的话,根据id删除再进行添加,没有直接进行添加
            if (uiLinkedListist == null) {
                //循环添加
                ServiceResponse<String> addResponse =this.addUILinked(intlist,userId);
                if(!addResponse.isSuccess()){
                    return addResponse;
                }
            }else{
                //循环删除
                for (UILinked uimysql: uiLinkedListist){
                    int scrows = uiLinkedMapper.deleteByPrimaryKey(uimysql.getId());
                    if (scrows <= 0) {
                       return ServiceResponse.createByError("删除失败");
                    }
                }
                //循环添加
                ServiceResponse<String> addResponse =this.addUILinked(intlist,userId);
                if(!addResponse.isSuccess()){
                    return addResponse;
                }
            }
        }else{
            return ServiceResponse.createByError("兴趣为空");
        }
        return ServiceResponse.createBySuccess();
    }
}
