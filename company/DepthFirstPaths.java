package com.company;

import java.util.LinkedList;
import java.util.Stack;

/**
 * Created by kevin on 11/23/15.
 */
public class DepthFirstPaths {

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
        maxPrice = price;
        paths =  (LinkedList<Arc>[]) new LinkedList[(int)Math.pow(G.V(),2)];                     //at most there will be v(v-1) edges
        temp = new Arc();
        tempLL = new LinkedList<Arc>();
        dfs(G, s);
    }

    private void dfs(EdgeWeightedDigraph G, int v) {
        uf = new WeightedQuickUnionUF(G.V());                       //cycle detection for each new path
        for(Arc a : tempLL){
            uf.union(a.v(),a.w());
        }
        for (DirectedEdge edge : G.adj(v)) {                         //for all edges out of v
            int w = edge.to();                                              //add the edge to edgeTo if not vertex w isn't visited
            if(!(uf.find(v)==uf.find(w))) {
                price = sumPricesStack(prices) + edge.cost();
                if (price <= maxPrice) {
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

    public LinkedList<Arc> copy (LinkedList<Arc> ll){
        LinkedList<Arc> copy = new LinkedList<Arc>();
        for(Arc a : ll){
            Arc arc = new Arc(a.v(),a.w());
            copy.addLast(arc);
        }
        return copy;
    }

    public LinkedList<Arc>[] getPaths() {
        return paths;
    }
}