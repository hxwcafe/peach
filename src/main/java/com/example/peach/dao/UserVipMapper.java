package com.example.peach.dao;

import com.example.peach.pojo.UserVip;
import com.example.peach.pojo.merge.UvipUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface UserVipMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(UserVip record);
    //插入会员信息
    int insertSelective(UserVip record);

    UserVip selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserVip record);

    int updateByPrimaryKey(UserVip record);
    //根据userid查询用户会员信息
    UserVip selectByUserId(int userId);
    //修改会员信息
    int updateByUserId(UserVip userVip);
    //相减约见次数
    int updateAppiontmentByUserId(@Param(value = "vipAppointment") Integer vipAppointment, @Param(value = "userId") Integer userid);

    int updateUMoneyByUserId(UserVip userVip);
    //查询会员信息,和user   id,openiid
    UvipUser selectUvipUser(Integer userId);
    //查询所有vip(非vip和vip)信息
    List<UvipUser> selectAllUvipUser();
    //根据openid查询UvipUser
    UvipUser selectUvipUserByopenid(String openid);
    //根据id动态修改vip信息
    int updateByid(UserVip userVip);
}