package com.example.peach.dao;

import com.example.peach.pojo.User;
import com.example.peach.pojo.merge.UAlbum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User user);

    User selectByPrimaryKey(Integer id);

    User selectByOpenid(String openid);

    User selectByphone(String phone);

    int updateByPrimaryKeySelective(User user);

    int updateByPrimaryKey(User record);

    List<User> userList();

    Boolean selectBynewolduser(String openid);

    //充值钱包
    int updateUnewoldAndUIntegralByOpenid(User user);
    //查询所有用户,按会员排序
    List<UAlbum> selectAllUserBydesc();

    List<User> selectUserByInterest(String interest);

    int updateUserPhone(User user);
    //根据年龄区间查询用户及相册
    List<UAlbum> selectByUserage(@Param(value = "ageone") Integer ageone, @Param(value = "agetwe") Integer agetwe);
    //查询所有用户及相册
    List<UAlbum> selectAllUserAlbum();
    //根据学历查询用户及相册
    List<UAlbum> selectByUserEducation(User user);

    List<UAlbum> selectByUserSalary(User user);
    //根据电话号码查询用户,且为B用户(openid为null)
    User selectByphoneAndopenid(String userphone);
    //点赞修改
    int updateUserpraise(User user);


}