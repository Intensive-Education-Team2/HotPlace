package com.posturn.hotplace;

import android.widget.ImageView;

public class MarketObject {
    String market_name;
    String place;
    String category;
    double lon; //경도
    double lat; //위도
    String comment;
    String imgcover;
    String detail_uri;

    public MarketObject(String market_name, String place, String category, double lon, double lat, String comment, String imgcover, String detail_uri) {
        this.market_name = market_name;
        this.place = place;
        this.category = category;
        this.lat = lat;
        this.lon = lon;
        this.comment = comment;
        this.imgcover = imgcover;
        this.detail_uri = detail_uri;
    }

    public MarketObject(){
        this.market_name = "없음";
        this.place = "없음";
        this.category = "없음";
        this.lat = 0.0;
        this.lon = 0.0;
        this.comment = "없음";
        this.imgcover = "없음";
        this.detail_uri = "없음";
    }

    public String getMarketName(){return market_name;}
    public String getImgCover(){return imgcover;}
    public String getPlace(){return place;}
    public String getCategory(){return category;}
    public double getLat(){return lat;}
    public double getLon(){return lon;}
    public String getComment(){return comment;}
    public String getDetailUri(){return detail_uri;}

    public void setMarketName(String market_name){this.market_name=market_name;}
    public void setPlace(String place){this.place = place;}
    public void setCategory(String category){this.category = category;}
    public void setLat(double lat){this.lat = lat;}
    public void setLon(double lon){this.lon = lon;}
    public void setComment(String comment){this.comment = comment;}
    public void setImgCover(String imgcover){this.imgcover = imgcover;}
    public void setDetailUri(String detail_uri){this.detail_uri = detail_uri;}
}
