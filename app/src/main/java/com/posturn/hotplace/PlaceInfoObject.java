package com.posturn.hotplace;

public class PlaceInfoObject {
    private String title;
    private String category;
    private String img_url;
    private String detail_link;


    public PlaceInfoObject(String title, String category, String url, String link){
        this.title = title;
        this.category = category;
        this.img_url = url;
        this.detail_link = link;

    }


    public String getTitle() {
        return title;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getCategory() { return category; }

    public String getDetail_link() {
        return detail_link;
    }


}