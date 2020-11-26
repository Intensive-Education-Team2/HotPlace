package com.posturn.hotplace;

class ObjectPlace {
    String name;
    double lat;
    double lon;
    String img;
    String tag;
    int index;


    public ObjectPlace(String name, double lat, double lon, String img, String tag, int index) {
        this.name=name;
        this.lat=lat;
        this.lon=lon;
        this.img=img;
        this.tag=tag;
        this.index=index;
    }

    public String getName(){
        return name;
    }
    public double getLat(){
        return lat;
    }
    public double getLon(){
        return lon;
    }
    public String getImg(){
        return img;
    }
    public String getTag(){
        return tag;
    }
    public int getindex(){
        return index;
    }

    public void setName(String name){
        this.name=name;
    }
    public void setLat(double lat){
        this.lat=lat;
    }
    public void setLon(double lon){
        this.lon=lon;
    }
    public void setImg(String img){
        this.img=img;
    }
    public void setTag(String tag){
        this.tag=tag;
    }
    public void setindex(int index){
        this.index=index;
    }
}
