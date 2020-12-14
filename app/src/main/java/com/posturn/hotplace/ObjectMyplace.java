package com.posturn.hotplace;

import java.util.ArrayList;

public class ObjectMyplace {
    ArrayList<String> myplacelist;

    public ObjectMyplace(){
        this.myplacelist = new ArrayList<String>();
    }

    public ArrayList<String> getMyplacelist() {
        return myplacelist;
    }

    public void setMyplacelist(ArrayList<String> myplacelist) {
        this.myplacelist = myplacelist;
    }
}
