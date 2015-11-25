//package com.company;

/**
 * Created by kevin on 11/23/15.
 */
public class Arc {
    private int v;
    private int w;

    public Arc(){
        v = -1;
        w = -1;
    }
    public Arc(int i, int j){
        v = i;
        w = j;
    }

    public int v(){
        return v;
    }
    public int w(){
        return w;
    }
}
