package com.example.peach.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;
import java.util.Objects;

public class Admin {
    private Integer id;
    private String account;
    private String nikeName;
    private String password;
    private Integer isSuper;
    private Integer isProhibit;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp createTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp updateTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNikeName() {
        return nikeName;
    }

    public void setNikeName(String nikeName) {
        this.nikeName = nikeName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getIsSuper() {
        return isSuper;
    }

    public void setIsSuper(Integer isSuper) {
        this.isSuper = isSuper;
    }

    public Integer getIsProhibit() {
        return isProhibit;
    }

    public void setIsProhibit(Integer isProhibit) {
        this.isProhibit = isProhibit;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Admin admin = (Admin) o;
        return Objects.equals(id, admin.id) &&
                Objects.equals(account, admin.account) &&
                Objects.equals(nikeName, admin.nikeName) &&
                Objects.equals(password, admin.password) &&
                Objects.equals(isSuper, admin.isSuper) &&
                Objects.equals(isProhibit, admin.isProhibit) &&
                Objects.equals(createTime, admin.createTime) &&
                Objects.equals(updateTime, admin.updateTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, account, nikeName, password, isSuper, isProhibit, createTime, updateTime);
    }


    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", nikeName='" + nikeName + '\'' +
                ", password='" + password + '\'' +
                ", isSuper=" + isSuper +
                ", isProhibit=" + isProhibit +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
