package com.example.peach.controller;

import com.example.peach.common.Pager;
import com.example.peach.common.ServiceResponse;
import com.example.peach.pojo.Favorite;
import com.example.peach.pojo.merge.UAlbum;
import com.example.peach.service.FavoriteService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 心仪
 * Created by Administrator on 2018/11/18.
 */
@RestController
@RequestMapping(value = "/favorite")
public class FavoriteController {

    @Resource
    private FavoriteService favoriteService;

    //查询自己的心仪对象
    @RequestMapping(value = "/selectbymyid" , method = RequestMethod.GET)
    public ServiceResponse<Pager<UAlbum>> selectByMyId(@RequestParam Integer myid, Integer pageIndex) {

        if (myid!=null){
            ServiceResponse response= favoriteService.selectByMyId(myid,pageIndex);
            return response;
        }else {
            return ServiceResponse.createByError("传入空值！");
        }
    }


    //查询心仪自己的对象
    @RequestMapping(value = "/selectbyyouid", method = RequestMethod.GET)
    public ServiceResponse selectByYouId(@RequestParam Integer myid) {

        if (myid!=null){
            ServiceResponse response= favoriteService.selectByYouId(myid);
            return response;
        }else{
            return ServiceResponse.createByError("传入空值！");
        }
    }


    //查询相互心仪的对象
    @RequestMapping(value = "/selectall" , method = RequestMethod.GET)
    public ServiceResponse selectAll() {

        ServiceResponse response = favoriteService.selectAll();
        return response;
    }

    /**
     * 是否心仪
     * @param favorite myid ,youid
     * @return true false
     */
    @RequestMapping(value = "/iffavorite",method = RequestMethod.GET)
    public ServiceResponse<String> ifFavorite(@ModelAttribute Favorite favorite) {
        return favoriteService.selectFavorite(favorite);
    }
    //添加心仪对象
    @RequestMapping(value = "/insertfavorite", method = RequestMethod.POST)
    public ServiceResponse<String> insertFavorite(@ModelAttribute Favorite favorite) {
        //查询是否已存在
        ServiceResponse<String> response= favoriteService.selectFavorite(favorite);
        if (!response.isSuccess()) {
            return response;
        } else {
            //添加
            response = favoriteService.insertFavorite(favorite);
            return response;
        }
    }


    //删除心仪对象
    @RequestMapping(value = "/deletefavorite", method = RequestMethod.POST)
    public ServiceResponse<String> deleteFavorite(@ModelAttribute Favorite favorite) {

        if (favorite!=null){
            ServiceResponse<String> response = favoriteService.deleteFavorite(favorite);
            return response;
        }else{
            return ServiceResponse.createByError("传入空值！");
        }
    }

}
