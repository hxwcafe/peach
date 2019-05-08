package com.example.peach.service;

import com.example.peach.common.Pager;
import com.example.peach.common.ServiceResponse;
import com.example.peach.pojo.Favorite;
import com.example.peach.pojo.merge.UAlbum;

/**
 * 心仪表 接口
 * Created by Administrator on 2018/11/17.
 */
public interface FavoriteService {

    //查询自己的心仪对象
    ServiceResponse<Pager<UAlbum>> selectByMyId(Integer myid, Integer pageIndex);

    //查询心仪自己的对象
    ServiceResponse<Object> selectByYouId(Integer youid);

    //查询相互心仪的对象
    ServiceResponse<Object> selectAll();

    //查询是否重复
    ServiceResponse<String> selectFavorite(Favorite favorite);

    //添加心仪对象
    ServiceResponse<String> insertFavorite(Favorite favorite);

    //删除心仪对象
    ServiceResponse<String> deleteFavorite(Favorite favorite);
}
