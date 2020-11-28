package com.posturn.hotplace;

public class ObjectUser {
    String name;
    String id;
    String password;
    String age;
    String img;

    ObjectUser(String name, String id, String password, String age, String img){
        this.name = name;
        this.id = id;
        this.password = password;
        this.age = age;
        this.img = img;
    }
    ObjectUser(){}

    public String getName(){
        return name;
    }
    public String getId(){
        return id;
    }
    public String getPassword(){
        return password;
    }
    public String getAge(){ return age; }
    public String getImg(){return img;}

    public void setName(String name){
        this.name=name;
    }
    public void setId(String id){
        this.id=id;
    }
    public void setPassword(String password){
        this.password=password;
    }
    public void setAge(String age){
        this.age=age;
    }
    public void setImg(String img) {this.img = img;}

}
