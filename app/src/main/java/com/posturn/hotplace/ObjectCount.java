package com.posturn.hotplace;

class ObjectCount implements Comparable<ObjectCount>{
    String name;
    int count;

    public ObjectCount(String name, int count){
        this.name=name;
        this.count=count;
    }

    public String getName(){
        return name;
    }
    public int getCount(){
        return count;
    }


    @Override
    public int compareTo(ObjectCount objectCount) {
        if (this.count == objectCount.count) {
            return 0;
        } else if(this.count < objectCount.count) {
            return 1;
        } else {
            return -1;
        }
    }
}
