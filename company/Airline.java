package com.company;

import javax.lang.model.type.PrimitiveType;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

public class Airline {

    static EdgeWeightedGraph edgeWeightedGraph;
    static EdgeWeightedDigraph edgeWeightedDigraph;
    public static void main(String[] args) throws Exception {
	// write your code here
        System.out.println("Please enter the filename of flights");
        Scanner scan = new Scanner(System.in);
        String fileName = scan.nextLine();

        URL path = Airline.class.getResource(fileName);
        if(path==null) {
            throw new Exception("file name not accepted");
        }

        BufferedReader reader;


        try {
            File f = new File(path.toURI());
            reader = new BufferedReader(new FileReader(f));

            int V = Integer.parseInt(reader.readLine());           //first line contains # of cities
            edgeWeightedGraph = new EdgeWeightedGraph(V);                             //initialize adjacency list
            edgeWeightedDigraph = new EdgeWeightedDigraph(V);

            for (int i = 0; i < V; i++) {                          //create lists for each city
                String name = reader.readLine();
                edgeWeightedGraph.setCityName(i, name);
                edgeWeightedDigraph.setCityName(i, name);
            }
            String line = reader.readLine();

            while(!(line==null)) {                                           //while the line isn't null
                String input[] = line.trim().split("\\s+");
                int start = Integer.parseInt(input[0]) - 1;
                int end = Integer.parseInt(input[1]) - 1;
                int distance = Integer.parseInt(input[2]);
                double price = Double.parseDouble(input[3]);
                Edge e1 = new Edge(start, end, price,distance);
                DirectedEdge e2 = new DirectedEdge(start, end, price,distance);
                DirectedEdge e3 = new DirectedEdge(end, start, price,distance);
                edgeWeightedGraph.addEdge(e1);
                edgeWeightedDigraph.addEdge(e2);
                edgeWeightedDigraph.addEdge(e3);
                line = reader.readLine();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        int choice = mainMenu();
        if(choice == 0){
            showFlights(edgeWeightedGraph);
        }
        else if(choice == 1){
            PrimMST mst = new PrimMST(edgeWeightedGraph);
            showTree(mst,edgeWeightedGraph);
        }
        else if(choice == 2){

            int a = 3;
            while(a==3){
                a = shortestPathChoice();
            }
            int v = -1, w = -1;
            while(v == -1 || w == -1){
                v = -1;
                w = -1;
                System.out.println("Enter starting city: ");
                Scanner scanner = new Scanner(new InputStreamReader(System.in));
                String start = scanner.nextLine();
                System.out.println("Enter destination city: ");
                String destination = scanner.nextLine();
                v = edgeWeightedDigraph.getCityNameIndex(start);                    //returns -1 if not found
                w = edgeWeightedDigraph.getCityNameIndex(destination);
                if(v == -1) System.out.println("Starting city not found");
                if(w == -1) System.out.println("Destination city not found");
            }

            switch (a){
                case 0:
                    DijkstraSP d1 = shortestPricePath(edgeWeightedDigraph, v);
                    printShortestPath(edgeWeightedDigraph, d1, w, "price");
                    break;
                case 1:
                    DijkstraSP d2 =shortestDistancePath(edgeWeightedDigraph, v);
                    printShortestPath(edgeWeightedDigraph, d2, w, "distance");

                    break;
                case 2:
                    EdgeWeightedDigraph ewd = digraphWithConstantDistanceWeights(edgeWeightedDigraph);
                    DijkstraSP d3 = shortestDistancePath(ewd, v);
                    printShortestPath(ewd, d3, w, "hops");
                    break;
                default:
                    break;
            }
        }
    }
    public static int mainMenu(){
        System.out.println("Would you like to view all available 'flights', the 'MST', or a shortest 'path'?");
        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        String input = scanner.nextLine();
        if(input.equals("flights")) return 0;
        else if(input.equals("MST")) return 1;
        else if(input.equals("path")) return 2;
        else return 3;
    }
    public static int shortestPathChoice(){
        System.out.println("Would you like to view the shortest path by 'price', 'distance', or 'hops'?");
        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        String input = scanner.nextLine();
        if(input.equals("price")) return 0;
        else if(input.equals("distance")) return 1;
        else if(input.equals("hops")) return 2;
        else return 3;
    }

    public static DijkstraSP shortestPricePath(EdgeWeightedDigraph e, int v){
        DijkstraSP d = new DijkstraSP(e, v, true);
        return d;
    }

    public static DijkstraSP shortestDistancePath(EdgeWeightedDigraph e, int v){
        DijkstraSP d = new DijkstraSP(e, v, false);
        return d;
    }

    public static void printShortestPath(EdgeWeightedDigraph e, DijkstraSP d, int w, String criteria){
        if(d.hasPathTo(w)){
            System.out.println("Shortest path to " + edgeWeightedDigraph.getCityName(w) +" by " + criteria);
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            StringBuilder path = new StringBuilder("");
            double price = 0;
            int distance = 0;
            for (DirectedEdge edge : d.pathTo(w)) {
                String start = edgeWeightedDigraph.getCityName(edge.from());
                String end = edgeWeightedDigraph.getCityName(edge.to());
                if(distance == 0) path.append(end).reverse();                         //reverse cities
                StringBuilder s = new StringBuilder(start + " -> ").reverse();
                path.append(s);                                                         //append cities in reverse
                distance += edge.distance();
                price += edge.cost();
                //System.out.println("Start City: " + start + " Destination: " + end + " Price: " + p + " Distance: " + dist);
            }
            if(criteria.equals("price")) System.out.println("Lowest cost: $" + price);
            else if(criteria.equals("distance")) System.out.println("Shortest distance: " + distance);
            else System.out.println("Fewest hops: " + distance);
            System.out.println("Path: " + path.reverse().toString());                   //reversed string is path in correct order
        }
        else System.out.println("There is no path to " + e.getCityName(w));
    }

    public static EdgeWeightedDigraph digraphWithConstantDistanceWeights(EdgeWeightedDigraph e){
        EdgeWeightedDigraph ewd = new EdgeWeightedDigraph(e);
        for(DirectedEdge directedEdge : ewd.edges()){
            directedEdge.setDistance(1);
        }
        return ewd;
    }

    public static void showFlights(EdgeWeightedGraph e){
        for(Edge edge : e.edges()){
                String start = e.getCityName(edge.either());
                String end = e.getCityName(edge.other(edge.either()));
                int dist = edge.distance();
                double p = edge.cost();
                System.out.println("Start City: " + start + " Destination: " + end + " Price: " + p + " Distance: "+dist);
        }
    }

    public static void showTree(PrimMST mst, EdgeWeightedGraph e){
        WeightedQuickUnionUF uf= new WeightedQuickUnionUF(e.V());
        for(Edge edge : mst.edges()){
            int v = edge.either(), w = edge.other(v);
            uf.union(v,w);
        }
        int parents[] = uf.parentArray();
        //int sizes[] = uf.sizeArray();

        for(Edge edge : mst.edges()){                           //compress paths so that all vertices of a tree share the same root
            int v = edge.either(), w = edge.other(v);
            parents[v] = uf.find(v);
            parents[w] = uf.find(w);
        }

        Set<Integer> roots = new LinkedHashSet<Integer>();
        for(int x : parents){
            roots.add(x);                                       //compile list of roots
        }
        int treeNumber = 1;
        for(int x : roots){
            System.out.println("MST "+treeNumber);
            for(Edge edge : mst.edges()){
                if(x == parents[edge.either()]){
                    String start = e.getCityName(edge.either());
                    String end = e.getCityName(edge.other(edge.either()));
                    int dist = edge.distance();
                    double p = edge.cost();
                    System.out.println("Start City: " + start + " Destination: " + end + " Price: " + p + " Distance: "+dist);
                }
            }
            System.out.println();
            treeNumber++;
        }

    }

}
