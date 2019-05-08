package com.example.peach.pojo.merge;

import com.example.peach.pojo.Album;
import com.example.peach.pojo.User;

import java.util.List;

public class UAlbum extends User {
    private List<Album> albumList;

    public List<Album> getAlbumList() {
        return albumList;
    }

    public void setAlbumList(List<Album> albumList) {
        this.albumList = albumList;
    }
}
