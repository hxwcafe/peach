package com.example.peach.dao;

import com.example.peach.pojo.Album;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Administrator on 2018/12/8.
 */
@Component
@Mapper
public interface AlbumMapper {
    int insertAlbum(Album album);
    //根据用户id查询相册
    List<Album> seletByuserId(Integer userId);
    //删除图片
    int deleteAlbum(Integer userId);
}
