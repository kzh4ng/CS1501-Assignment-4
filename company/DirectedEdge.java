package com.company;

public class DirectedEdge {
    private int v;
    private int w;
    private double cost;
    private int distance;


    public DirectedEdge(){}

    public DirectedEdge(int v, int w, double cost, int distance) {
        if (v < 0) throw new IndexOutOfBoundsException("Vertex names must be nonnegative integers");
        if (w < 0) throw new IndexOutOfBoundsException("Vertex names must be nonnegative integers");
        if (Double.isNaN(cost)) throw new IllegalArgumentException("Weight is NaN");
        this.v = v;
        this.w = w;
        this.setCost(cost);
        this.setDistance(distance);
    }

    public int from() {
        return v;
    }

    public int to() {
        return w;
    }

    public double cost() {
        return cost;
    }
    public int distance(){return distance;}

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
    public static void assignValues(DirectedEdge e, DirectedEdge f){
        e.v = f.v;
        e.w = f.w;
        e.distance = f.distance;
        e.cost = f.cost;
    }
}