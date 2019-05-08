package com.example.peach.pojo;

/**
 * 择偶要求表
 * Created by Administrator on 2018/11/22.
 */
public class Mate {
    private Integer id;
    private String mateAge;//年龄
    private String mateHeight;//身高
    private String mateSalary;//薪资
    private String mateEducation;//学历
    private String mateMarrytime;//期望结婚时间
    private String mateNative;//籍贯
    private String mateMarriage;//婚姻状况
    private String mateBelief;//信仰
    private String mateDrink;//饮酒
    private String mateSmoking;//抽烟
    private String isChildren;//有无子女
    private Integer userId;//用户id
    private User user;//用户

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMateAge() {
        return mateAge;
    }

    public void setMateAge(String mateAge) {
        this.mateAge = mateAge;
    }

    public String getMateHeight() {
        return mateHeight;
    }

    public void setMateHeight(String mateHeight) {
        this.mateHeight = mateHeight;
    }

    public String getMateSalary() {
        return mateSalary;
    }

    public void setMateSalary(String mateSalary) {
        this.mateSalary = mateSalary;
    }

    public String getMateEducation() {
        return mateEducation;
    }

    public void setMateEducation(String mateEducation) {
        this.mateEducation = mateEducation;
    }

    public String getMateMarrytime() {
        return mateMarrytime;
    }

    public void setMateMarrytime(String mateMarrytime) {
        this.mateMarrytime = mateMarrytime;
    }


    public String getMateNative() {
        return mateNative;
    }

    public void setMateNative(String mateNative) {
        this.mateNative = mateNative;
    }

    public String getMateMarriage() {
        return mateMarriage;
    }

    public void setMateMarriage(String mateMarriage) {
        this.mateMarriage = mateMarriage;
    }

    public String getMateBelief() {
        return mateBelief;
    }

    public void setMateBelief(String mateBelief) {
        this.mateBelief = mateBelief;
    }

    public String getMateDrink() {
        return mateDrink;
    }

    public void setMateDrink(String mateDrink) {
        this.mateDrink = mateDrink;
    }

    public String getMateSmoking() {
        return mateSmoking;
    }

    public void setMateSmoking(String mateSmoking) {
        this.mateSmoking = mateSmoking;
    }

    public String getIsChildren() {
        return isChildren;
    }

    public void setIsChildren(String isChildren) {
        this.isChildren = isChildren;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
