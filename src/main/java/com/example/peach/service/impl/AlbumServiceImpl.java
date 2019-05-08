package com.example.peach.service.impl;

import com.example.peach.common.Conts;
import com.example.peach.common.ServiceResponse;
import com.example.peach.dao.AlbumMapper;
import com.example.peach.pojo.Album;
import com.example.peach.service.AlbumService;
import com.example.peach.service.UserService;
import com.example.peach.util.FileUpload;
import com.example.peach.util.ImageUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/12/8.
 */
@Service
public class AlbumServiceImpl implements AlbumService {
    @Resource
    private AlbumMapper albumMapper;

    /**
     * 上传用户照片
     * @param album 相册表
     * @return true false
     */
    @Override
    public ServiceResponse<Map> insertAlbum(Album album) {
        List<Album> albumList= albumMapper.seletByuserId(album.getUserId());
        if(albumList.size()<=3) {
            //删除数据库数据
            albumMapper.deleteAlbum(album.getUserId());
            //删除文件图片
            if(albumList!=null) {
                for (Album album1 : albumList) {
                    FileUpload.filedele(Conts.UserAlbum,album1.getAlbumPath());
                }
            }
            int insert = albumMapper.insertAlbum(album);
            if (insert > 0) {
                String[] ar = album.getAlbumPath().split("/");
                String file = Conts.UserAlbum+ar[ar.length-1];
                ImageUtil.markImgeByText("桃花源",file,file,0,Color.black,"png");
                return ServiceResponse.createBySuccess("添加成功");
            }
            return ServiceResponse.createByError("添加失败");
        }else{
            return ServiceResponse.createByError("图片超过三张");
        }
    }

    /**
     * 查询用户相册
     * @param userId 用户id
     * @return List<Album>
     */
    @Override
    public ServiceResponse<List<Album>> selectAlbum(Integer userId) {
        List<Album> albumList = albumMapper.seletByuserId(userId);
        if(albumList!=null){
            return ServiceResponse.createBySuccess(albumList);
        }
        return ServiceResponse.createByError("没有相册照片");
    }
}
