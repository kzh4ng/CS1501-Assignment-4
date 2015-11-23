package com.company;

import java.util.Stack;

/**
 * Created by kevin on 11/23/15.
 */
public class DepthFirstPaths {
    private boolean[] marked;    // marked[v] = is there an s-v path?
    private int[] edgeTo;        // edgeTo[v] = last edge on s-v path
    private final int s;         // source vertex
    private double price = 0;
    private double maxPrice;

    /**
     * Computes a path between <tt>s</tt> and every other vertex in graph <tt>G</tt>.
     * @param G the graph
     * @param s the source vertex
     */
    public DepthFirstPaths(EdgeWeightedDigraph G, int s, double price) {
        this.s = s;
        edgeTo = new int[G.V()];
        marked = new boolean[G.V()];
        maxPrice = price;
        dfs(G, s);
    }

    // depth first search from v
    private void dfs(EdgeWeightedDigraph G, int v) {
        marked[v] = true;                                            //mark vertex v as visited
        for (DirectedEdge edge : G.adj(v)) {                         //for all edges out of v
            int w = edge.to();                                              //add the edge to edgeTo if not vertex w isn't visited
            if (!marked[w]) {
                price += price + edge.cost();
                if(price < maxPrice) {
                    edgeTo[w] = v;
                    dfs(G, w);
                }
            }
        }
    }

    /**
     * Is there a path between the source vertex <tt>s</tt> and vertex <tt>v</tt>?
     * @param v the vertex
     * @return <tt>true</tt> if there is a path, <tt>false</tt> otherwise
     */
    public boolean hasPathTo(int v) {
        return marked[v];
    }

    /**
     * Returns a path between the source vertex <tt>s</tt> and vertex <tt>v</tt>, or
     * <tt>null</tt> if no such path.
     * @param v the vertex
     * @return the sequence of vertices on a path between the source vertex
     *   <tt>s</tt> and vertex <tt>v</tt>, as an Iterable
     */
    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<Integer> path = new Stack<Integer>();
        for (int x = v; x != s; x = edgeTo[x])
            path.push(x);
        path.push(s);
        return path;
    }



    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }
}