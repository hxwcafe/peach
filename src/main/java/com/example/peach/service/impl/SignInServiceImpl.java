package com.example.peach.service.impl;

import com.example.peach.common.ServiceResponse;
import com.example.peach.dao.SignInMapper;
import com.example.peach.dao.UserMapper;
import com.example.peach.pojo.SignIn;
import com.example.peach.pojo.User;
import com.example.peach.service.SignInService;
import com.example.peach.util.DateSub;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/12/6.
 */
@Service
public class SignInServiceImpl implements SignInService {

    @Resource
    private SignInMapper sigInMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    /**
     * 查询是否打卡
     */
    public ServiceResponse<Map> selectByuserId(Integer userId) {
        SignIn sigIn = sigInMapper.selectByuserId(userId);
        Map<String, Object> map = new HashMap<>();
        if (sigIn == null) {
            return ServiceResponse.createByError("打卡");
        } else if (DateSub.getDaySub(sigIn.getSignInTime()) <= -1) {
            return ServiceResponse.createByError("打卡");
        }
        map.put("sigIn", sigIn);
        return ServiceResponse.createBySuccess("已打卡", map);
    }

    /**
     * 签到
     *
     * @param signIn
     * @return
     */
    @Override
    public ServiceResponse<Map> insertSignIn(SignIn signIn) {
        SignIn selectSignIn = sigInMapper.selectByuserId(signIn.getUserId());
        User user = userMapper.selectByPrimaryKey(signIn.getUserId());
        Long longs = DateSub.getDaySub(selectSignIn.getSignInTime());
        User users = new User();
        if (selectSignIn == null) {
            signIn.setSignInNumber(1);
            int insert = sigInMapper.insertSignIn(signIn);
            if (insert > 0) {
                users.setOpenid(user.getOpenid());
                users.setUserIntegral(user.getUserIntegral() + 3);
                int update = userMapper.updateByPrimaryKeySelective(users);
                return ServiceResponse.createBySuccess("打卡成功");
            }
        } else if (longs <= -1) {
            if (longs<=-2){
                signIn.setSignInNumber(1);
                users.setUserIntegral(user.getUserIntegral() + 3);
            }else if (longs==-1){
            Integer number = selectSignIn.getSignInNumber() + 1;
            if (number < 7) {
                users.setUserIntegral(user.getUserIntegral() + 3);
                signIn.setSignInNumber(number);
            } else if (number == 7) {
                users.setUserIntegral(user.getUserIntegral() + 23);
                signIn.setSignInNumber(0);
            }
            }
            users.setOpenid(user.getOpenid());
            int sUpdate =  sigInMapper.updateSignIn(signIn);
            int uUpdate= userMapper.updateByPrimaryKeySelective(users);
            if (uUpdate>0&&sUpdate>0){
                return ServiceResponse.createBySuccess("打卡成功");
        }

        }
        return ServiceResponse.createByError("打卡失败,已经打过卡了");
    }
    @Override
    public ServiceResponse<Map> updateSignIn(SignIn signIn) {
        return null;
    }
}
