package com.posturn.hotplace;

class ObjectPlace {
    String name;
    double lat;
    double lon;
    String img;
    String tag;
    int rank;

    public ObjectPlace(String name, double lat, double lon, String img, String tag, int rank) {
        this.name=name;
        this.lat=lat;
        this.lon=lon;
        this.img=img;
        this.tag=tag;
        this.rank=rank;
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
    public int getRank(){
        return rank;
    }
}
