package com.example.peach.service.impl;

import com.example.peach.common.Pager;
import com.example.peach.common.ServiceResponse;
import com.example.peach.dao.AlbumMapper;
import com.example.peach.dao.InterestMapper;
import com.example.peach.dao.UILinkedMapper;
import com.example.peach.dao.UserMapper;
import com.example.peach.pojo.Interest;
import com.example.peach.pojo.User;
import com.example.peach.pojo.merge.InterestU;
import com.example.peach.pojo.merge.UAlbum;
import com.example.peach.service.InterestService;
import com.example.peach.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2018/12/4.
 */
@Service
public class InterestServiceImpl implements InterestService {

    @Resource
    private InterestMapper interestMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private UILinkedMapper uiLinkedMapper;
    @Resource
    private AlbumMapper albumMapper;
    @Resource
    private UserService userService;
    @Override
    public List<String> selectById(Integer id) {

        String interest = interestMapper.selectById(id);
        List<String> list = Arrays.asList(interest.split(","));
        return list;
    }


    @Override
    public ServiceResponse selectUserByInterest(Integer id) {

        List<String> list = selectById(id);
        List<User> users = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            List<User> user1 = userMapper.selectUserByInterest(list.get(i));
            if (user1 != null && user1.size() > 0) {
                for (int j = 0; j < user1.size(); j++) {
                    users.add(user1.get(j));
                }
            }
        }

        if (users != null && users.size() > 0) {
            return ServiceResponse.createBySuccess(users);
        } else {
            return ServiceResponse.createByError("该兴趣没有用户");
        }
    }

    /**
     * 所有兴趣详细
     *
     * @return 兴趣详细list集合
     */
    @Override
    public ServiceResponse<List<String>> selectAllInterest() {
        List<Interest> interestList = interestMapper.selectAllInterest();
        if (interestList != null) {
            List<String> list = new ArrayList<String>();
            for (Interest interest : interestList) {
                String[] category = interest.getCategory().split(",");
                for (int i = 0; i < category.length; i++) {
                    list.add(category[i]);
                }
            }
            return ServiceResponse.createBySuccess(list);
        } else {
            return ServiceResponse.createByError();
        }
    }

    /**
     * 兴趣展示(兴趣分类,用户数量)
     *
     * @return
     */
    @Override
    public ServiceResponse<List<InterestU>> selectInteretU() {
        List<Interest> interestList = interestMapper.selectAllInterest();
        if (interestList != null) {
            List<InterestU> interestUList = new ArrayList<InterestU>();
            for (Interest interest : interestList) {
                InterestU interestU = new InterestU();
                int usercount = uiLinkedMapper.selectCountByinterest_id(interest.getId());
                interestU.setId(interest.getId());
                interestU.setName(interest.getName());
                interestU.setImageUrl(interest.getImageUrl());
                interestU.setCategory(interest.getCategory());
                interestU.setUsercunt(usercount);
                interestUList.add(interestU);
            }
            return ServiceResponse.createBySuccess(interestUList);
        }
        return ServiceResponse.createByError("兴趣表没有信息");
    }

    /**
     *  根据兴趣id查询所有用户
     * @param id 兴趣id
     * @return
     */
    @Override
    public ServiceResponse<Pager<UAlbum>> selectUserAlbumBy(Integer id, Integer pageIndex) {
        List<UAlbum> uAlbumList = interestMapper.selectUserAlbumBy(id);
        ServiceResponse userResponse = userService.foruAlbumList(uAlbumList,pageIndex);
        return userResponse;
    }


    @Override
    public ServiceResponse updateInterest(Interest interest) {

        int rs = interestMapper.updateInterest(interest);

        if (rs > 0) {
            return ServiceResponse.createBySuccess("修改成功！");
        } else {
            return ServiceResponse.createByError("修改失败！");
        }
    }

    @Override
    public ServiceResponse insertInterest(Interest interest) {

        int rs = interestMapper.insertInterest(interest);
        if (rs > 0) {
            return ServiceResponse.createBySuccess("添加成功！");
        } else {
            return ServiceResponse.createByError("添加失败！");
        }
    }


}
