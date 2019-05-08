package com.example.peach.pojo;

import java.sql.Timestamp;

/**相册
 * Created by Administrator on 2018/12/8.
 */
public class Album {
    private  Integer id;//主键id
    private  String albumPath;//相册路径
    private Timestamp albumDate;//上传时间
    private  Integer userId;//用户id
    private  Integer status;//状态

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAlbumPath() {
        return albumPath;
    }

    public void setAlbumPath(String albumPath) {
        this.albumPath = albumPath;
    }

    public Timestamp getAlbumDate() {
        return albumDate;
    }

    public void setAlbumDate(Timestamp albumDate) {
        this.albumDate = albumDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
