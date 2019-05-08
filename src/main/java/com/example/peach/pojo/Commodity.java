package com.example.peach.pojo;

import java.util.Date;

/**
 * 会卡商品表
 */
public class Commodity {
    private Integer id;
    //会员卡名字
    private String commodityName;
    //会员卡描述
    private String commodityBody;
    //约见次数
    private Integer commodityFrequency;
    //报价
    private Double commodityPrice;
    //首次进店优惠价
    private Double firstdiscount;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName == null ? null : commodityName.trim();
    }

    public String getCommodityBody() {
        return commodityBody;
    }

    public void setCommodityBody(String commodityBody) {
        this.commodityBody = commodityBody == null ? null : commodityBody.trim();
    }

    public Integer getCommodityFrequency() {
        return commodityFrequency;
    }

    public void setCommodityFrequency(Integer commodityFrequency) {
        this.commodityFrequency = commodityFrequency;
    }

    public Double getCommodityPrice() {
        return commodityPrice;
    }

    public void setCommodityPrice(Double commodityPrice) {
        this.commodityPrice = commodityPrice;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Double getFirstdiscount() {
        return firstdiscount;
    }

    public void setFirstdiscount(Double firstdiscount) {
        this.firstdiscount = firstdiscount;
    }
}