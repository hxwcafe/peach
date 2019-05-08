package com.example.peach.service.impl;

import com.example.peach.common.ServiceResponse;
import com.example.peach.dao.AlbumMapper;
import com.example.peach.dao.MateMapper;
import com.example.peach.pojo.Album;
import com.example.peach.pojo.Mate;
import com.example.peach.pojo.merge.UAMate;
import com.example.peach.service.MateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2018/11/22.
 */
@Service
public class MateServiceImpl implements MateService {
    @Resource
   private  MateMapper mateMapper;

    @Resource
    private AlbumMapper albumMapper;
    /**
     * 根据userId查询
     * @param userId
     * @return
     */
    @Override
    public Mate selectByuserId(Integer userId) {
        return mateMapper.selectByuserId( userId);
    }

    /**添加择偶要求
     *
     * @param mate
     * @return
     */
    @Override
    public ServiceResponse<String> insertMate(Mate mate) {
        int insert=  mateMapper.insertMate(mate);
        if (insert>0){
           return ServiceResponse.createBySuccess("添加成功");
        }
        return ServiceResponse.createByError("添加失败");
    }

    /**
     * 根据userId修改
     * @param mate
     * @return
     */
    @Override
    public ServiceResponse<String> updateMate(Mate mate) {
        int update=mateMapper.updateMate(mate);
        if (update>0){
            return ServiceResponse.createBySuccess("修改成功");
        }
        return ServiceResponse.createByError("修改失败");
    }

    /**
     * 根据openId查询用户和择偶要求
     * @param openid
     * @return
     */
    @Override
    public Mate selectByOpenId(String openid) {
        return mateMapper.selectByOpenId(openid);
    }

    /**
     * 根据userid,获取Alum,Mate,Uservip,User
     * @param userId 用户id
     * @return User,UserVip,Alum,Mate
     */
    @Override
    public ServiceResponse<UAMate> selectUAMate(Integer userId) {
       UAMate uaMate = mateMapper.selectUAMate(userId);
       if(uaMate!=null) {
           uaMate.setMate(mateMapper.selectByuserId(userId));
           List<Album> albumList = albumMapper.seletByuserId(userId);
           uaMate.setAlbumList(albumList);
           return ServiceResponse.createBySuccess(uaMate);
       }
       return ServiceResponse.createByError();
    }

    /**
     * 删除
     * @param userId 用户id
     * @return true false
     */
    @Override
    public ServiceResponse<String> delectmate(Integer userId) {
        int getrows = mateMapper.deleteByPrimaryKey(userId);
        if(getrows>0){
            return ServiceResponse.createBySuccess();
        }
        return ServiceResponse.createByError();
    }
}
