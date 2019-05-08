package com.example.peach.pojo;




import java.sql.Date;
import java.sql.Timestamp;

public class User {
    private Integer id;//主键id
    private String nickName;//昵称
    private String userRealname;//真实姓名
    private Date userBirthday;//生日
    private String userphone;//手机号
    private Integer userage;//年龄
    private Integer gender; //性别
    private String country;//国家
    private String province;//省份
    private String city;//市
    private String userAddress;//地址
    private String userAutograph;//个性签名
    private String userOccupation;//职业
    private Boolean isMarriage;//婚否
    private Integer userHeight;//身高
    private String userEducation;//学历
    private String userSalary;//薪资
    private String userInterest;//兴趣爱好
    private String userLabel;//个性标签
    private Integer userJurisdiction;//会员等级
    private String userIdcard;//身份证
    private String avatarUrl;//头像
    private Integer userIntegral;//积分
    private String openid;//用户唯一标识
    private  Boolean isrealname;//是否实名验证
    private String usernative;//籍贯
    private String unionid;//
    private  String userMarrytime;//期望结婚时间
    private  Boolean userNewold;//是否为新老用户
    private Boolean userTips;//用户是否提示
    private Integer userHouse;//是否有车
    private Integer userCar;//是否有车
    private Integer userPraise;//点赞
    private Integer userBlacklist;//黑名单标识
    private Timestamp userCreatetime;//创建时间
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserRealname() {
        return userRealname;
    }

    public void setUserRealname(String userRealname) {
        this.userRealname = userRealname;
    }

    public Date getUserBirthday() {
        return userBirthday;
    }

    public void setUserBirthday(Date userBirthday) {
        this.userBirthday = userBirthday;
    }

    public String getUserLabel() {
        return userLabel;
    }

    public void setUserLabel(String userLabel) {
        this.userLabel = userLabel;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public Integer getUserage() {
        return userage;
    }

    public void setUserage(Integer userage) {
        this.userage = userage;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserAutograph() {
        return userAutograph;
    }

    public void setUserAutograph(String userAutograph) {
        this.userAutograph = userAutograph;
    }

    public String getUserOccupation() {
        return userOccupation;
    }

    public void setUserOccupation(String userOccupation) {
        this.userOccupation = userOccupation;
    }

    public Boolean getIsMarriage() {
        return isMarriage;
    }

    public void setIsMarriage(Boolean marriage) {
        isMarriage = marriage;
    }

    public Integer getUserHeight() {
        return userHeight;
    }

    public void setUserHeight(Integer userHeight) {
        this.userHeight = userHeight;
    }

    public String getUserEducation() {
        return userEducation;
    }

    public void setUserEducation(String userEducation) {
        this.userEducation = userEducation;
    }

    public String getUserSalary() {
        return userSalary;
    }

    public void setUserSalary(String userSalary) {
        this.userSalary = userSalary;
    }

    public String getUserInterest() {
        return userInterest;
    }

    public void setUserInterest(String userInterest) {
        this.userInterest = userInterest;
    }

    public Integer getUserJurisdiction() {
        return userJurisdiction;
    }

    public void setUserJurisdiction(Integer userJurisdiction) {
        this.userJurisdiction = userJurisdiction;
    }

    public String getUserIdcard() {
        return userIdcard;
    }

    public void setUserIdcard(String userIdcard) {
        this.userIdcard = userIdcard;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Integer getUserIntegral() {
        return userIntegral;
    }

    public void setUserIntegral(Integer userIntegral) {
        this.userIntegral = userIntegral;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Boolean getIsrealname() {
        return isrealname;
    }

    public void setIsrealname(Boolean isrealname) {
        this.isrealname = isrealname;
    }

    public String getUsernative() {
        return usernative;
    }

    public void setUsernative(String usernative) {
        this.usernative = usernative;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getUserMarrytime() {
        return userMarrytime;
    }

    public void setUserMarrytime(String userMarrytime) {
        this.userMarrytime = userMarrytime;
    }

    public Boolean getUserNewold() {
        return userNewold;
    }

    public void setUserNewold(Boolean userNewold) {
        this.userNewold = userNewold;
    }

    public Boolean getUserTips() {
        return userTips;
    }

    public void setUserTips(Boolean userTips) {
        this.userTips = userTips;
    }

    public Integer getUserHouse() {
        return userHouse;
    }

    public void setUserHouse(Integer userHouse) {
        this.userHouse = userHouse;
    }

    public Integer getUserCar() {
        return userCar;
    }

    public void setUserCar(Integer userCar) {
        this.userCar = userCar;
    }

    public Integer getUserPraise() {
        return userPraise;
    }

    public void setUserPraise(Integer userPraise) {
        this.userPraise = userPraise;
    }

    public Timestamp getUserCreatetime() {
        return userCreatetime;
    }

    public void setUserCreatetime(Timestamp userCreatetime) {
        this.userCreatetime = userCreatetime;
    }

    public Integer getUserBlacklist() {
        return userBlacklist;
    }

    public void setUserBlacklist(Integer userBlacklist) {
        this.userBlacklist = userBlacklist;
    }
}