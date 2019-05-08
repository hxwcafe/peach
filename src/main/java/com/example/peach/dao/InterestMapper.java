package com.example.peach.dao;

import com.example.peach.pojo.Interest;
import com.example.peach.pojo.merge.UAlbum;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Administrator on 2018/12/4.
 */
@Component
@Mapper
public interface InterestMapper {
    //兴趣分类详细集合
    String selectById(Integer id);

    int updateInterest(Interest interest);

    int insertInterest(Interest interest);
    //模糊查询兴趣详细
    Interest selectLikeCategory(String category);
    //获取所有兴趣信息
    List<Interest> selectAllInterest();
    //根据兴趣id查询所有用户
   List<UAlbum> selectUserAlbumBy(Integer id);
}
