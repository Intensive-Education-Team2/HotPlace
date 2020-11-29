package com.posturn.hotplace;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ObjectPost {
    String userimg;
    String username;
    String place;
    Timestamp date;
    String img;
    String content;
    int hot;
    List hotuser;

    public ObjectPost(String userimg, String username, String place, Timestamp date, String img, String content, int hot, List hotuser){
        this.userimg=userimg;
        this.username=username;
        this.place=place;
        this.date=date;
        this.img=img;
        this.content=content;
        this.hot=hot;
        this.hotuser=hotuser;
    }

    public String getUserimg() {
        return userimg;
    }
    public String getUsername(){
        return username;
    }
    public String getPlace(){
        return place;
    }
    public Timestamp getDate(){
        return date;
    }
    public String getImg(){
        return img;
    }
    public String getContent(){
        return content;
    }
    public int getHot(){
        return hot;
    }
    public List getHotuser(){
        return hotuser;
    }
}
