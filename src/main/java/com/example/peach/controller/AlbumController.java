package com.example.peach.controller;

import com.example.peach.common.Conts;
import com.example.peach.common.ServiceResponse;
import com.example.peach.pojo.Album;
import com.example.peach.pojo.User;
import com.example.peach.service.AlbumService;
import com.example.peach.service.UserService;
import com.example.peach.util.FileUpload;
import com.example.peach.util.ImageUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.awt.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2018/11/23.
 */
@RestController
@RequestMapping("/album")
public class AlbumController {


    @Resource
    private AlbumService albumService;
    @Resource
    private UserService userService;

    /**
     * 上传用户相册
     *
     * @param file  图片路径
     * @param album userId用户id
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ServiceResponse<Map> upload(@RequestParam(value = "file", required = false) MultipartFile file, Album album) {
        User user = userService.selectByPrimaryKey(album.getUserId());
        if(user.getUserBlacklist()==1){//查看用户是否是黑名单
            return ServiceResponse.createByError("用户处于黑名单内");
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sysDate = format.format(new Date());
        String scfille = Conts.UserAlbumURL;//用户相册路径
        String photoFolder = Conts.UserAlbum;
        String fileName = FileUpload.fileUpload(file, scfille,photoFolder);
        album.setAlbumPath(fileName);
        album.setAlbumDate(Timestamp.valueOf(sysDate));
        return albumService.insertAlbum(album);
    }

    /**
     * 查询用户相册
     * @param userId
     * @return
     */
    @RequestMapping(value = "/addalbum", method = RequestMethod.GET)
    public ServiceResponse<List<Album>> selectAlbum(Integer userId){
        return albumService.selectAlbum(userId);
    }

}
