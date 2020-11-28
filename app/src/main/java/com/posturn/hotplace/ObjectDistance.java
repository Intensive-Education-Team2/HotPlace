package com.posturn.hotplace;

public class ObjectDistance implements Comparable<ObjectDistance>{
    String name;
    double distance;

    public ObjectDistance(String name, double distance){
        this.name=name;
        this.distance=distance;
    }

    public String getName(){
        return name;
    }
    public double getDistance(){
        return distance;
    }

    @Override
    public int compareTo(ObjectDistance objectDistance) {
        if (this.distance == objectDistance.distance) {
            return 0;
        } else if (this.distance < objectDistance.distance) {
            return -1;
        } else {
            return 1;
        }
    }
}
