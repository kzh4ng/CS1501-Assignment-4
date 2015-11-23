package com.company;

public class Edge implements Comparable<Edge> {

    private int v;
    private int w;
    private double cost;
    private int distance;
    private boolean criteria;

    /**
     * Initializes an edge between vertices <tt>v</tt> and <tt>w</tt> of
     * the given <tt>cost</tt>.
     *
     * @param  v one vertex
     * @param  w the other vertex
     * @param  cost the cost of this edge
     * @throws IndexOutOfBoundsException if either <tt>v</tt> or <tt>w</tt>
     *         is a negative integer
     * @throws IllegalArgumentException if <tt>cost</tt> is <tt>NaN</tt>
     */
    public Edge(int v, int w, double cost, int distance) {
        if (v < 0) throw new IndexOutOfBoundsException("Vertex name must be a nonnegative integer");
        if (w < 0) throw new IndexOutOfBoundsException("Vertex name must be a nonnegative integer");
        if (Double.isNaN(cost)) throw new IllegalArgumentException("Weight is NaN");
        this.v = v;
        this.w = w;
        this.cost = cost;
        this.distance = distance;
    }

    /**
     * Returns the cost of this edge.
     *
     * @return the cost of this edge
     */
    public double cost() {
        return cost;
    }
    public int distance(){return distance;}

    /**
     * Returns either endpoint of this edge.
     *
     * @return either endpoint of this edge
     */
    public int either() {
        return v;
    }
    public void setCriteria(boolean value){
        this.criteria = value;
    }
    public boolean getCriteria(){return criteria;}

    /**
     * Returns the endpoint of this edge that is different from the given vertex.
     *
     * @param  vertex one endpoint of this edge
     * @return the other endpoint of this edge
     * @throws IllegalArgumentException if the vertex is not one of the
     *         endpoints of this edge
     */
    public int other(int vertex) {
        if      (vertex == v) return w;
        else if (vertex == w) return v;
        else throw new IllegalArgumentException("Illegal endpoint");
    }

    /**
     * Compares two edges by cost.
     * Note that <tt>compareTo()</tt> is not consistent with <tt>equals()</tt>,
     * which uses the reference equality implementation inherited from <tt>Object</tt>.
     *
     * @param  that the other edge
     * @return a negative integer, zero, or positive integer depending on whether
     *         the cost of this is less than, equal to, or greater than the
     *         argument edge
     */
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


    /**
     * Returns a string representation of this edge.
     *
     * @return a string representation of this edge
     */
    public String toString() {
        return String.format("%d-%d %.5f", v, w, cost);
    }

}
