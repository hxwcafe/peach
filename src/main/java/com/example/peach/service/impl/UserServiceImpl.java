package com.example.peach.service.impl;

import com.example.peach.common.Conts;
import com.example.peach.common.Pager;
import com.example.peach.common.ServiceResponse;
import com.example.peach.dao.AlbumMapper;
import com.example.peach.dao.UserMapper;
import com.example.peach.pojo.Album;
import com.example.peach.pojo.User;
import com.example.peach.pojo.merge.UAlbum;
import com.example.peach.pojo.merge.UvipUser;
import com.example.peach.service.UserService;
import com.example.peach.service.UserVipService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private AlbumMapper albumMapper;
    @Resource
    private UserVipService userVipService;

    /**
     * 根据openid查询用户
     * @param openid 用户唯一标识
     * @param type   判断有无类型 openid type
     * @return
     */
    @Override
    public ServiceResponse<Map> selectByOpenid(String openid, String type) {
        if (openid != null) {
            User user = userMapper.selectByOpenid(openid);
            Map<String, Object> map = new HashMap<>();
            if (type.equals(Conts.OPENID)) {
                if (user != null) {
                    map.put("user", user);
                    return ServiceResponse.createBySuccess("用户登录成功", map);
                }
                return ServiceResponse.createByError("用户需要注册");
            }
            if (type.equals(Conts.NEWOLD)) {
                if (user.getUserNewold()) {
                    return ServiceResponse.createBySuccess("用户是新用户");
                } else {
                    return ServiceResponse.createByError("用户是老用户");
                }
            }
        }
        return ServiceResponse.createByError("错误");
    }

    /**
     * code 获取openid,如果没有用户,插入返回用户信息,有的直接放回数据
     *
     * @param user
     * @return
     */
    public ServiceResponse<UvipUser> addOpenid(User user) {
        ServiceResponse<Map> response = this.selectByOpenid(user.getOpenid(), Conts.OPENID);
        if (response.isSuccess()) {
            ServiceResponse<UvipUser> uvipUserResponse = userVipService.selectUvipUserByopenid(user.getOpenid());
            return uvipUserResponse;
        } else {
            System.out.println("user.getUserphone()"+user.getUserphone());
            int getrows = userMapper.insertSelective(user);
            if (getrows > 0) {
                User user1 = this.selectByOpenid(user.getOpenid());
                //添加vip信息
                ServiceResponse userVipresponse = userVipService.addUserVip(user1.getId());
                if (userVipresponse.isSuccess()) {
                    ServiceResponse<UvipUser> uvipUserResponse = userVipService.selectUvipUserByopenid(user.getOpenid());
                    return ServiceResponse.createByError(uvipUserResponse.getData());
                }
                return ServiceResponse.createByError("uservip插入失败");
            }
            return ServiceResponse.createByError("user插入失败");

        }
    }

    /**
     * 更新手机号
     * @param user userphone openid
     * @return UvipUser
     */
    public ServiceResponse<UvipUser> lognUpdateUserPhonse(User user) {
        //判断手机号是否被绑定
        ServiceResponse userphonse = this.selectPhone(user.getUserphone());
        if(!userphonse.isSuccess()){
            return userphonse;
        }
        int getrows = userMapper.updateByPrimaryKeySelective(user);
        if (getrows > 0) {
            ServiceResponse<UvipUser> uvipUserResponse = userVipService.selectUvipUserByopenid(user.getOpenid());
            return uvipUserResponse;
        }
        return ServiceResponse.createByError("更新失败");
    }

    /**
     * 授权成功保存用户数据
     *
     * @param user
     * @return
     */
    @Transactional
    @Override
    public ServiceResponse<String> lognUser(User user) {
        ServiceResponse<Map> response = this.selectByOpenid(user.getOpenid(), Conts.OPENID);
        if (response.isSuccess()) {
            int getroews = userMapper.updateByPrimaryKeySelective(user);
            if (getroews > 0) {
                return ServiceResponse.createBySuccess("保存成功");
            }
            return ServiceResponse.createByError("保存失败");
        }
        return ServiceResponse.createByError("openid不存在");
    }


    /**
     * 根据phone查询user
     * @param phone 手机号
     * @return true false
     */
    @Override
    public ServiceResponse<String> selectPhone(String phone) {
        User user = userMapper.selectByphone(phone);
        if (user != null) {
            return ServiceResponse.createByError("该手机号已经绑定了");
        }
        return ServiceResponse.createBySuccess();
    }

    @Override
    public ServiceResponse<String> updateUser(User user) {
        int users = userMapper.updateByPrimaryKeySelective(user);
        if (users > 0) {
            return ServiceResponse.createBySuccess("修改成功");
        }
        return ServiceResponse.createByError("修改失败");
    }

    @Override
    public ServiceResponse<String> updateUserByid(User user) {
        int users = userMapper.updateByPrimaryKey(user);
        if (users > 0) {
            return ServiceResponse.createBySuccess("修改成功");
        }
        return ServiceResponse.createByError("修改失败");
    }



    /**
     * 该买会员修改积分,如果是新用户修改成老用户
     *
     * @param user
     * @return
     */
    @Override
    public ServiceResponse<Integer> updateUnewoldAndUIntegralByOpenid(User user) {
        int getrows = userMapper.updateByPrimaryKeySelective(user);
        if (getrows > 0) {
            return ServiceResponse.createBySuccess("用户信息修改成功");
        } else {
            return ServiceResponse.createByError("用户信息修改失败");
        }
    }
//

    /**
     * 根据openid 查询用户
     *
     * @param openid
     * @return
     */
    @Override
    public User selectByOpenid(String openid) {
        User user = userMapper.selectByOpenid(openid);
        if (user != null) {
            return user;
        } else {
            return user;
        }
    }

    @Override
    public List<User> userList() {
        return userMapper.userList();
    }

    @Override
    public int updateUserPhone(User user) {

        return userMapper.updateUserPhone(user);
    }

    @Override
    public User selectByPrimaryKey(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    /**
     * 展示用户兴趣
     *
     * @param user
     * @return
     */
    @Override
    public ServiceResponse<List<String>> selectUserInterestByOpenId(User user) {
        List<String> list = new ArrayList<String>();
        if (user != null) {
            if (user.getUserInterest() == null || user.getUserInterest().equals("")) {
                list = null;
            } else {
                String[] str = user.getUserInterest().split(",");
                for (int i = 0; i < str.length; i++) {
                    list.add(str[i]);
                }
            }
            return ServiceResponse.createBySuccess(list);
        } else {
            return ServiceResponse.createByError("用户不存在");
        }
    }

    //用户,相册
    public ServiceResponse<Pager<UAlbum>> foruAlbumList(List<UAlbum> uAlbumList, int pageIndex) {
        if (uAlbumList != null) {
            for (UAlbum uAlbum : uAlbumList) {
                List<Album> albumList = albumMapper.seletByuserId(uAlbum.getId());
                uAlbum.setAlbumList(albumList);
            }
            Pager<UAlbum> pager = new Pager<>(pageIndex, 10, uAlbumList.size(), uAlbumList);
            return ServiceResponse.createBySuccess(pager);
        }
        return ServiceResponse.createByError("没有用户");
    }

    /**
     * 根据年龄区间查询用户
     *
     * @param ageone 最小年龄
     * @param agetwe 最大年龄
     * @return
     */
    @Override
    public ServiceResponse<Pager<UAlbum>> selectByUserage(Integer ageone, Integer agetwe, Integer pageIndex) {
        List<UAlbum> uAlbumList = new ArrayList<UAlbum>();
        if (ageone == 0 || agetwe == 0) {
            uAlbumList = userMapper.selectAllUserAlbum();
        } else {
            uAlbumList = userMapper.selectByUserage(ageone, agetwe);
        }
        return this.foruAlbumList(uAlbumList, pageIndex);
    }

    /**
     * 根据学历查询用户及相册
     *
     * @param user 用户
     * @return
     */
    @Override
    public ServiceResponse<Pager<UAlbum>> selectByUserEandS(User user, Integer pageIndex) {
        List<UAlbum> uAlbumList = new ArrayList<UAlbum>();
        if (user.getUserEducation() != null) {
            if (user.getUserEducation().equals("none")) {
                uAlbumList = userMapper.selectAllUserAlbum();
            } else {
                if (user.getUserEducation().equals("大专及以上")) {
                    user.setUserEducation("大专");
                    uAlbumList = userMapper.selectByUserEducation(user);
                    user.setUserEducation("硕士");
                    uAlbumList.addAll(userMapper.selectByUserEducation(user));
                    user.setUserEducation("本科");
                    uAlbumList.addAll(userMapper.selectByUserEducation(user));
                    user.setUserEducation("博士");
                    uAlbumList.addAll(userMapper.selectByUserEducation(user));
                } else if (user.getUserEducation().equals("本科及以上")) {
                    user.setUserEducation("本科");
                    uAlbumList.addAll(userMapper.selectByUserEducation(user));
                    user.setUserEducation("硕士");
                    uAlbumList.addAll(userMapper.selectByUserEducation(user));
                    user.setUserEducation("博士");
                    uAlbumList.addAll(userMapper.selectByUserEducation(user));
                } else if (user.getUserEducation().equals("硕士及以上")) {
                    user.setUserEducation("硕士");
                    uAlbumList.addAll(userMapper.selectByUserEducation(user));
                    user.setUserEducation("博士");
                    uAlbumList.addAll(userMapper.selectByUserEducation(user));
                }

            }
            return this.foruAlbumList(uAlbumList, pageIndex);
        }
        if (user.getUserSalary() != null) {
            if (user.getUserSalary().equals("none")) {
                uAlbumList = userMapper.selectAllUserAlbum();
            } else {
                uAlbumList = userMapper.selectByUserSalary(user);
            }
            return this.foruAlbumList(uAlbumList, pageIndex);
        }
        return ServiceResponse.createByError();
    }

    //////////////////////////////////////后台//////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 所有A类用户
     *
     * @return List<User>
     */
    @Override
    public ServiceResponse<List<User>> selectAllA() {
        List<User> userList = userMapper.userList();
        if (userList == null) {
            return ServiceResponse.createByError("没有用户");
        }
        List<User> newuserList = new ArrayList<User>();
        for (int i = userList.size() - 1; i >= 0; i--) {
            if (userList.get(i).getOpenid() != null && userList.get(i).getOpenid().length() != 0) {
                newuserList.add(userList.get(i));
            }
        }
        return ServiceResponse.createBySuccess(newuserList);
    }

    /**
     * 所有B类用户
     *
     * @return List<User>
     */
    @Override
    public ServiceResponse<List<User>> selectAllB() {
        List<User> userList = userMapper.userList();
        if (userList == null) {
            return ServiceResponse.createByError("没有用户");
        }
        List<User> newuserList = new ArrayList<User>();
        for (User user : userList) {
            if (user.getOpenid() == null || user.getOpenid().length() == 0) {
                newuserList.add(user);
            }
        }
        return ServiceResponse.createBySuccess(newuserList);
    }

    /**
     * 所有B类用户
     *
     * @return List<User>
     */
    @Override
    public ServiceResponse<List<User>> selectBlack() {
        List<User> userList = userMapper.userList();
        if (userList == null) {
            return ServiceResponse.createByError("没有用户");
        }
        List<User> newuserList = new ArrayList<User>();
        for (User user : userList) {
            if (user.getUserBlacklist()==1) {
                newuserList.add(user);
            }
        }
        return ServiceResponse.createBySuccess(newuserList);
    }
    /**
     * 查询所有用户,分页
     *
     * @param pageIndex 当前显示的页数
     * @return Pager <UAlbum>
     */
    @Override
    public ServiceResponse<Pager<UAlbum>> allUAlbum(Integer pageIndex) {
        List<UAlbum> uAlbumList = userMapper.selectAllUserBydesc();
        if (uAlbumList == null) {
            return ServiceResponse.createByError();
        }
        return this.foruAlbumList(uAlbumList, pageIndex);
    }

    /**
     * 插入user信息
     * @param user 用户
     * @return true false
     */
    @Override
    @Transactional
    public ServiceResponse<String> insertUser(User user) {
        int getrows = userMapper.insertSelective(user);
        if (getrows > 0) {
            User user1 = selectByphoneAndopenid(user.getUserphone());
            //添加vip信息
            ServiceResponse userVipresponse = userVipService.addUserVip(user1.getId());
            if (userVipresponse.isSuccess()) {
                return ServiceResponse.createBySuccess();
            }
            return ServiceResponse.createByError("uservip插入失败");
        }
        return ServiceResponse.createByError("user插入失败");
    }
    //根据电话号码查询用户,且为B用户(openid为null)
    @Override
    public User selectByphoneAndopenid(String userphonse){
        return userMapper.selectByphoneAndopenid(userphonse);
    }
    //删除用户基本信息
    @Override
    public ServiceResponse<String> delectByid(Integer id) {
        int getrows = userMapper.deleteByPrimaryKey(id);
        if(getrows>0){
            return ServiceResponse.createBySuccess();
        }
        return ServiceResponse.createByError();
    }
    //点赞
    @Override
    public ServiceResponse<String> updateUserpraise(User user) {
        int getrows =  userMapper.updateUserpraise(user);
        if(getrows>0){
            return ServiceResponse.createBySuccess();
        }else{
            return ServiceResponse.createByError();
        }
    }
}
