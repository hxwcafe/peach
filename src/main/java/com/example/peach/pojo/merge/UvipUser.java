package com.example.peach.pojo.merge;

import com.example.peach.pojo.User;
import com.example.peach.pojo.UserVip;

public class UvipUser extends UserVip {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
