package com.example.peach.service;

import com.example.peach.common.ServiceResponse;
import com.example.peach.pojo.Album;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/12/8.
 */
public interface AlbumService {
    //插入相册
    ServiceResponse<Map> insertAlbum(Album album);
    //查询用户相册
    ServiceResponse<List<Album>> selectAlbum(Integer userId);
}
