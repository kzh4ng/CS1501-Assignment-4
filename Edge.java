//package com.company;

public class Edge implements Comparable<Edge> {

    private int v;
    private int w;
    private double cost;
    private int distance;
    private boolean criteria;

    public Edge(){
    }
    public Edge(int v, int w, double cost, int distance) {
        if (v < 0) throw new IndexOutOfBoundsException("Vertex name must be a nonnegative integer");
        if (w < 0) throw new IndexOutOfBoundsException("Vertex name must be a nonnegative integer");
        if (Double.isNaN(cost)) throw new IllegalArgumentException("Weight is NaN");
        this.v = v;
        this.w = w;
        this.setCost(cost);
        this.distance = distance;
    }

    public double cost() {
        return getCost();
    }
    public int distance(){return distance;}

    public int either() {
        return v;
    }
    public void setCriteria(boolean value){
        this.criteria = value;
    }
    public boolean getCriteria(){return criteria;}


    public int other(int vertex) {
        if      (vertex == v) return w;
        else if (vertex == w) return v;
        else throw new IllegalArgumentException("Illegal endpoint");
    }

    @Override
    public int compareTo(Edge that) {
        if(criteria){                                               //criteria is true for price comparison
            if      (this.cost() < that.cost()) return -1;
            else if (this.cost() > that.cost()) return +1;
            else                                    return  0;
        }
        else{                                                       //false for distance
            if      (this.distance() < that.distance()) return -1;
            else if (this.distance() > that.distance()) return +1;
            else return 0;
        }

    }
    public double getCost() {
        return cost;
    }
    public void setDistance(int d){
        this.distance = d;
    }


    public void setCost(double cost) {
        this.cost = cost;
    }
    public static void assignValues(Edge e, Edge f){
        e.v = f.v;
        e.w = f.w;
        e.distance = f.distance;
        e.cost = f.cost;
    }
}
