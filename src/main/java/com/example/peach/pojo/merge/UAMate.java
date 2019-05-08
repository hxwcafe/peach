package com.example.peach.pojo.merge;

import com.example.peach.pojo.Album;
import com.example.peach.pojo.Mate;
import com.example.peach.pojo.User;
import com.example.peach.pojo.UserVip;

import java.util.List;

public class UAMate extends User {
    private List<Album> albumList;

    private UserVip userVipl;

    private Mate mate;

    public List<Album> getAlbumList() {
        return albumList;
    }

    public void setAlbumList(List<Album> albumList) {
        this.albumList = albumList;
    }

    public UserVip getUserVipl() {
        return userVipl;
    }

    public void setUserVipl(UserVip userVipl) {
        this.userVipl = userVipl;
    }

    public Mate getMate() {
        return mate;
    }

    public void setMate(Mate mate) {
        this.mate = mate;
    }
}



