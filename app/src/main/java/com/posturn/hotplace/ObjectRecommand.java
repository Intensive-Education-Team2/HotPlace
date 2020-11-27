package com.posturn.hotplace;

class ObjectRecommand implements Comparable<ObjectRecommand>{
    String name;
    double percent;

    public ObjectRecommand(String name, double percent){
        this.name=name;
        this.percent=percent;
    }

    public String getName(){
        return name;
    }
    public double getPercent(){
        return percent;
    }

    @Override
    public int compareTo(ObjectRecommand objectRecommand) {
        if (this.percent == objectRecommand.percent) {
            return 0;
        } else if(this.percent < objectRecommand.percent) {
            return 1;
        } else {
            return -1;
        }
    }
}
