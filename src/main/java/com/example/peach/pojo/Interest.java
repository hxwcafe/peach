package com.example.peach.pojo;

/**
 * 兴趣表
 * Created by Administrator on 2018/12/4.
 */
public class Interest {

    private int id;
    //分类名称
    private String name;
    //图片路径
    private String imageUrl;
    //详细兴趣
    private String category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
