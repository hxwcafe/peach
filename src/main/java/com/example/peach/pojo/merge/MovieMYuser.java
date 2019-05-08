package com.example.peach.pojo.merge;

import com.example.peach.pojo.MovieInvitation;
import com.example.peach.pojo.User;

public class MovieMYuser extends MovieInvitation {
    private User myuser;

    private User youuser;

    public User getMyuser() {
        return myuser;
    }

    public void setMyuser(User myuser) {
        this.myuser = myuser;
    }

    public User getYouuser() {
        return youuser;
    }

    public void setYouuser(User youuser) {
        this.youuser = youuser;
    }
}
