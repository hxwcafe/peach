package com.example.peach.dao;

import com.example.peach.pojo.Favorite;
import com.example.peach.pojo.merge.UAlbum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

/**
 * Created by Administrator on 2018/11/17.
 */

@Component
@Mapper
public interface FavoriteMapper {

    //查询自己的心仪对象
    List<UAlbum> selectByMyId(@Param("myid") Integer myid);

    //查询心仪自己的对象
    List<Favorite> selectByYouId(@Param("youid") Integer youid);

    //查询相互心仪的对象
    List<Favorite> selectAll();

    //查询是否重复
    Favorite selectFavorite(@ModelAttribute Favorite favorite);

    //添加
    int insertFavorite(@ModelAttribute Favorite favorite);

    //删除
    int deleteFavorite(@ModelAttribute Favorite favorite);

}
