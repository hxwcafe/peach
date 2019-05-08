package com.example.peach.service;

import com.example.peach.common.Pager;
import com.example.peach.common.ServiceResponse;
import com.example.peach.pojo.User;
import com.example.peach.pojo.merge.UAlbum;
import com.example.peach.pojo.merge.UvipUser;

import java.util.List;
import java.util.Map;

public interface UserService {
    User selectByOpenid(String openid);
    //code得到openid逻辑
    ServiceResponse<UvipUser> addOpenid(User user);
    //更新手机号
    ServiceResponse<UvipUser> lognUpdateUserPhonse(User user);
    //修改用户是否新老用户,用户vip等级
    ServiceResponse<Integer> updateUnewoldAndUIntegralByOpenid(User user);

    ServiceResponse<Map> selectByOpenid(String str, String type);

    ServiceResponse<String> lognUser(User user);

    ServiceResponse<String> selectPhone(String phone);

    ServiceResponse<String> updateUser(User user);

    List<User> userList();

    int updateUserPhone(User user);

    User selectByPrimaryKey(Integer id);

    //查询用户兴趣数组
    ServiceResponse<List<String>> selectUserInterestByOpenId(User user);

    //根据年龄区间查询用户信息
    ServiceResponse<Pager<UAlbum>> selectByUserage(Integer ageone, Integer agetwe, Integer pageIndex);

    //根据学历查询用户及相册
    ServiceResponse<Pager<UAlbum>> selectByUserEandS(User user, Integer pageIndex);
    //查询用户及相册
    ServiceResponse<Pager<UAlbum>> foruAlbumList(List<UAlbum> uAlbumList, int pageIndex);

    ServiceResponse<List<User>> selectAllA();//查询A用户,含有openid

    ServiceResponse<List<User>> selectAllB();//查询B用户,不含有openid

    ServiceResponse<List<User>> selectBlack();//查询所有黑名单人

    ServiceResponse<Pager<UAlbum>> allUAlbum(Integer pageIndex);

    ServiceResponse<String> insertUser(User user);

    User selectByphoneAndopenid(String userophonse);//根据电话号码查询用户,且为B用户(openid为null)

    ServiceResponse<String> updateUserByid(User user);//根据修改用户信息

    ServiceResponse<String> delectByid(Integer id);//根据id删除

    ServiceResponse<String> updateUserpraise(User user);//点赞


}

