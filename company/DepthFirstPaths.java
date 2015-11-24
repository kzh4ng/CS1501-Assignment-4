package com.company;

import java.util.LinkedList;
import java.util.Stack;

/**
 * Created by kevin on 11/23/15.
 */
public class DepthFirstPaths {
    private boolean[] marked;    // marked[v] = is there an s-v path?
    private int[] edgeTo;        // edgeTo[v] = last edge on s-v path
    private LinkedList<Arc>[] paths;
    private final int s;         // source vertex
    private double price = 0;
    private double maxPrice;
    private int count = 0;
    private Arc temp;
    private LinkedList<Arc> tempLL;
    private Stack<Double> prices = new Stack<Double>();
    private WeightedQuickUnionUF uf;

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
        paths =  (LinkedList<Arc>[]) new LinkedList[(int)Math.pow(G.V(),2)];                     //at most there will be v(v-1) edges
        temp = new Arc();
        tempLL = new LinkedList<Arc>();
        dfs(G, s);
    }

    // depth first search from v
    private void dfs(EdgeWeightedDigraph G, int v) {
        uf = new WeightedQuickUnionUF(9);                       //cycle detection for each new path
        for(Arc a : tempLL){
            uf.union(a.v(),a.w());
        }
        for (DirectedEdge edge : G.adj(v)) {                         //for all edges out of v
            int w = edge.to();                                              //add the edge to edgeTo if not vertex w isn't visited
            if(!(uf.find(v)==uf.find(w))) {
                price = sumPricesStack(prices) + edge.cost();
                if (price <= maxPrice) {
                    edgeTo[w] = v;
                    prices.push(edge.cost());
                    temp = new Arc(v, w);
                    tempLL.addLast(temp);
                    dfs(G, w);
                }
            }
        }
        if(!tempLL.isEmpty()){
            LinkedList<Arc> list = copy(tempLL);
            paths[count] = list;
            tempLL.removeLast();
        }
        count++;
        if(!prices.empty())prices.pop();
    }
    private static double sumPricesStack(Stack<Double> s){
        double sum = 0;
        for(double d : s){
            sum+=d;
        }
        return sum;
    }

    /**
     * Is there a path between the source vertex <tt>s</tt> and vertex <tt>v</tt>?
     * @param v the vertex
     * @return <tt>true</tt> if there is a path, <tt>false</tt> otherwise
     */
    public boolean hasPathTo(int v) {
        return marked[v];
    }
    public LinkedList<Arc> copy (LinkedList<Arc> ll){
        LinkedList<Arc> copy = new LinkedList<Arc>();
        for(Arc a : ll){
            Arc arc = new Arc(a.v(),a.w());
            copy.addLast(arc);
        }
        return copy;
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

    public LinkedList<Arc>[] getPaths() {
        return paths;
    }
}