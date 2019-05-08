package com.example.peach.service;

import com.example.peach.common.Pager;
import com.example.peach.common.ServiceResponse;
import com.example.peach.pojo.Interest;
import com.example.peach.pojo.merge.InterestU;
import com.example.peach.pojo.merge.UAlbum;

import java.util.List;

/**
 * Created by Administrator on 2018/12/4.
 */
public interface InterestService {

    List<String> selectById(Integer id);

    ServiceResponse updateInterest(Interest interest);

    ServiceResponse insertInterest(Interest interest);

    ServiceResponse selectUserByInterest(Integer id);
    //查询兴趣去详细
    ServiceResponse<List<String>> selectAllInterest();
    //兴趣展示,查询InteretU
    ServiceResponse<List<InterestU>> selectInteretU();
    //根据兴趣id查询所有用户
    ServiceResponse<Pager<UAlbum>> selectUserAlbumBy(Integer id, Integer pageIndex);
}
