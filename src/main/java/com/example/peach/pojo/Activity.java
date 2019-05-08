package com.example.peach.pojo;


import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;

/**
 * 活动表
 * Created by Administrator on 2018/11/14.
 */
public class Activity {
    private  Integer id;//主键id
    private  String actname;//活动标题
    private  String actcontentname;//活动内容标题
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    private Timestamp acttime;//活动时间
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private  Timestamp actendtime;//活动截止时间
    private  String actplace;//活动地点
    private Double actcost;//活动费用
    private Double actdeposit;//活动押金
    private  String actrule;//活动规则
    private  String actcontent;//活动内容
    private  String actimgurl;//活动图片路径
    private String actnature;//活动性质
    private String actinvitation;//邀请奖励描述
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getActname() {
        return actname;
    }

    public void setActname(String actname) {
        this.actname = actname;
    }

    public String getActcontentname() {
        return actcontentname;
    }

    public void setActcontentname(String actcontentname) {
        this.actcontentname = actcontentname;
    }

    public Timestamp getActtime() {
        return acttime;
    }

    public void setActtime(Timestamp acttime) {
        this.acttime = acttime;
    }

    public Timestamp getActendtime() {
        return actendtime;
    }

    public void setActendtime(Timestamp actendtime) {
        this.actendtime = actendtime;
    }

    public String getActplace() {
        return actplace;
    }

    public void setActplace(String actplace) {
        this.actplace = actplace;
    }

    public double getActcost() {
        return actcost;
    }

    public void setActcost(double actcost) {
        this.actcost = actcost;
    }

    public Double getActdeposit() {
        return actdeposit;
    }

    public void setActdeposit(Double actdeposit) {
        this.actdeposit = actdeposit;
    }

    public String getActrule() {
        return actrule;
    }

    public void setActrule(String actrule) {
        this.actrule = actrule;
    }

    public String getActcontent() {
        return actcontent;
    }

    public void setActcontent(String actcontent) {
        this.actcontent = actcontent;
    }

    public String getActimgurl() {
        return actimgurl;
    }

    public void setActimgurl(String actimgurl) {
        this.actimgurl = actimgurl;
    }

    public String getActnature() {
        return actnature;
    }

    public void setActnature(String actnature) {
        this.actnature = actnature;
    }

    public String getActinvitation() {
        return actinvitation;
    }

    public void setActinvitation(String actinvitation) {
        this.actinvitation = actinvitation;
    }
}
