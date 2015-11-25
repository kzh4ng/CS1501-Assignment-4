//package com.company;

import javax.lang.model.type.PrimitiveType;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class Airline {

    static EdgeWeightedGraph edgeWeightedGraph;
    static EdgeWeightedDigraph edgeWeightedDigraph;
    public static void main(String[] args) throws Exception {
        // write your code here
        System.out.println("Please enter the filename of flights");
        Scanner scan = new Scanner(System.in);
        String fileName = scan.nextLine();

        URL path = Airline.class.getResource(fileName);
        if (path == null) {
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

            while (!(line == null)) {                                           //while the line isn't null
                String input[] = line.trim().split("\\s+");
                int start = Integer.parseInt(input[0]) - 1;
                int end = Integer.parseInt(input[1]) - 1;
                int distance = Integer.parseInt(input[2]);
                double price = Double.parseDouble(input[3]);
                Edge e1 = new Edge(start, end, price, distance);
                DirectedEdge e2 = new DirectedEdge(start, end, price, distance);
                DirectedEdge e3 = new DirectedEdge(end, start, price, distance);
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
        PrintWriter writer = new PrintWriter("flightdata.txt", "UTF-8");
        writer.println("Input file: "+fileName);
        writer.println();
        boolean quit = false;
        while(!quit){
            int choice = mainMenu();
            switch (choice) {
                case 0:
                    showFlights(edgeWeightedGraph,writer);
                    break;
                case 1:
                    PrimMST mst = new PrimMST(edgeWeightedGraph);
                    showTree(mst, edgeWeightedGraph ,writer);
                    break;
                case 2:
                    int a = 3;
                    while (a == 3) {
                        a = shortestPathChoice();
                    }
                    int v = -1, w = -1;
                    while (v == -1 || w == -1) {
                        v = -1;
                        w = -1;
                        System.out.println("Enter starting city: ");
                        Scanner scanner = new Scanner(new InputStreamReader(System.in));
                        String start = scanner.nextLine();
                        System.out.println("Enter destination city: ");
                        String destination = scanner.nextLine();
                        v = edgeWeightedDigraph.getCityNameIndex(start);                    //returns -1 if not found
                        w = edgeWeightedDigraph.getCityNameIndex(destination);
                        if (v == -1) System.out.println("Starting city not found");
                        if (w == -1) System.out.println("Destination city or flight not found");
                    }

                    switch (a) {
                        case 0:
                            DijkstraSP d1 = shortestPricePath(edgeWeightedDigraph, v);
                            printShortestPath(edgeWeightedDigraph, d1, w, "price",writer);
                            break;
                        case 1:
                            DijkstraSP d2 = shortestDistancePath(edgeWeightedDigraph, v);
                            printShortestPath(edgeWeightedDigraph, d2, w, "distance",writer);
                            break;
                        case 2:
                            EdgeWeightedDigraph ewd = digraphWithConstantDistanceWeights(edgeWeightedDigraph);
                            DijkstraSP d3 = shortestDistancePath(ewd, v);
                            printShortestPath(ewd, d3, w, "hops",writer);
                            break;
                        default:
                            break;
                    }
                    break;
                case 3:
                    System.out.println("Enter maximum price: ");
                    Scanner scanner = new Scanner(new InputStreamReader(System.in));
                    Double price = Double.parseDouble(scanner.nextLine());
                    System.out.println();
                    writer.println();
                    System.out.println("All flights that cost less than "+price);
                    writer.println("All flights that cost less than "+price);
                    for (int i = 0; i < edgeWeightedDigraph.V(); i++) {                                                 //for every vertex in the graph
                        DepthFirstPaths dfs = new DepthFirstPaths(edgeWeightedDigraph, i,price);
                        LinkedList<Arc>[] paths = dfs.getPaths();
                        for(LinkedList<Arc> l : paths){
                            if(l == null) break;
                            Arc n = l.getFirst();
                            System.out.print(edgeWeightedDigraph.getCityName(n.v()));
                            writer.print(edgeWeightedDigraph.getCityName(n.v()));
                            System.out.print(" -> "+edgeWeightedDigraph.getCityName(n.w()));
                            writer.print(" -> "+edgeWeightedDigraph.getCityName(n.w()));
                            for (int j = 1; j < l.size(); j++) {
                                n = l.get(j);
                                System.out.print(" -> "+edgeWeightedDigraph.getCityName(n.w()));
                                writer.print(" -> "+edgeWeightedDigraph.getCityName(n.w()));
                            }
                            System.out.println();
                            writer.println();
                        }
                    }
                    break;
                case 4:
                    boolean change = true;
                    while(change){
                        System.out.println("Enter starting city: ");
                        scanner = new Scanner(new InputStreamReader(System.in));
                        String start = scanner.nextLine();
                        int index = edgeWeightedDigraph.getCityNameIndex(start);
                        if(index == -1){
                            System.out.println("Starting city not found: ");
                            continue;
                        }
                        System.out.println("Enter destination city: ");
                        String destination = scanner.nextLine();
                        int index2 = edgeWeightedDigraph.getCityNameIndex(destination);
                        if(index2 == -1){
                            System.out.println("Destination city or flight not found: ");
                            continue;
                        }
                        Edge ed1 = new Edge();
                        Edge ed2 = new Edge();
                        DirectedEdge d = new DirectedEdge();
                        boolean both1 = false;
                        boolean both2 = false;
                        for(Edge e : edgeWeightedGraph.adj(index)){
                            if(both1 && both2)break;
                            if(e.either() == index && e.other(e.either())==index2){
                                ed1 = e;
                            }
                            if(e.either() == index2 && e.other(e.either())==index){
                                ed2 = e;
                            }
                        }
                        for(DirectedEdge de : edgeWeightedDigraph.adj(index)){
                            if(de.from() == index && de.to()==index2){
                                d = de;
                                break;
                            }
                        }
                        System.out.println("Enter cost: ");
                        scanner = new Scanner(new InputStreamReader(System.in));
                        double c = Double.parseDouble(scanner.nextLine());
                        ed1.setCost(c);
                        ed2.setCost(c);
                        d.setCost(c);
                        System.out.println("Enter distance: ");
                        int dis = Integer.parseInt(scanner.nextLine());
                        ed1.setDistance(dis);
                        ed2.setDistance(dis);
                        d.setDistance(dis);

                        System.out.println("Flight details changed: ");
                        writer.println("Flight details changed: ");
                        System.out.println("Start City: " + start + " Destination: " + destination + " Price: " + c + " Distance: "+dis);
                        writer.println("Start City: " + start + " Destination: " + destination + " Price: " + c + " Distance: "+dis);
                        change = false;
                    }
                    break;
                case 5:
                    change = true;
                    while(change){
                        System.out.println("Enter starting city: ");
                        scanner = new Scanner(new InputStreamReader(System.in));
                        String start = scanner.nextLine();
                        int index = edgeWeightedDigraph.getCityNameIndex(start);
                        if(index == -1){
                            System.out.println("Starting city not found: ");
                            continue;
                        }
                        System.out.println("Enter destination city: ");
                        String destination = scanner.nextLine();
                        int index2 = edgeWeightedDigraph.getCityNameIndex(destination);
                        if(index2 == -1){
                            System.out.println("Destination city not found: ");
                            continue;
                        }
                        Edge ed1 = new Edge();
                        Edge ed2 = new Edge();
                        DirectedEdge d = new DirectedEdge();
                        DirectedEdge d2 = new DirectedEdge();
                        boolean found = false;
                        for(Edge e : edgeWeightedGraph.adj(index)){
                            if(e.either() == index && e.other(e.either())==index2){             //find the edges undirected graph
                                found = true;
                                removeFlight(e,edgeWeightedGraph.adja(index));
                                break;
                            }
                            if(e.either() == index2 && e.other(e.either())==index){
                                removeFlight(e,edgeWeightedGraph.adja(index));
                                found = true;
                                break;
                            }
                        }
                        for(Edge e : edgeWeightedGraph.adj(index2)){
                            if(e.either() == index2 && e.other(e.either())==index){
                                found = true;
                                removeFlight(e,edgeWeightedGraph.adja(index2));
                                break;
                            }
                            if(e.either() == index && e.other(e.either())==index2){
                                found = true;
                                removeFlight(e,edgeWeightedGraph.adja(index2));
                                break;
                            }
                        }
                        for(DirectedEdge de : edgeWeightedDigraph.adj(index)) {     //find the edges in the directed graphs
                            if (de.from() == index && de.to() == index2) {
                                removeDirectFlight(de, edgeWeightedDigraph.adjacent(index));
                                break;
                            }
                        }
                        for(DirectedEdge de : edgeWeightedDigraph.adj(index2)) {
                            if (de.from() == index2 && de.to() == index) {
                                removeDirectFlight(de, edgeWeightedDigraph.adjacent(index2));
                                break;
                            }
                        }
                        if(!found){
                            System.out.println("Route not found, please try again");
                            continue;
                        }

                        System.out.println("Flight deleted");
                        change = false;
                    }
                    break;
                case 6:
                    writer.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Input not recognized");
                    System.out.println();
            }
        }

    }
    public static int mainMenu(){
        System.out.println();
        System.out.println("Would you like to view all available 'flights', the 'MST', a shortest 'path', flights under a certain 'price', 'modify' flight details, 'remove' a flight, or 'quit'?");
        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        String input = scanner.nextLine();
        if(input.equals("flights")) return 0;
        else if(input.equals("MST")) return 1;
        else if(input.equals("path")) return 2;
        else if(input.equals("price")) return 3;
        else if(input.equals("modify")) return 4;
        else if(input.equals("remove")) return 5;
        else if(input.equals("quit"))return 6;
        return 7;
    }
    public static void removeFlight(Edge e, DoublyLinkedList<Edge> d){
        ListIterator<Edge> iterator = d.iterator();
        while(iterator.hasNext()){
            if (e == iterator.next()) iterator.remove();
        }
    }
    public static void removeDirectFlight(DirectedEdge e, DoublyLinkedList<DirectedEdge> d){
        ListIterator<DirectedEdge> iterator = d.iterator();
        while(iterator.hasNext()){
            if ( e == iterator.next()) iterator.remove();
        }
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

    public static void printShortestPath(EdgeWeightedDigraph e, DijkstraSP d, int w, String criteria, PrintWriter writer){
        if(d.hasPathTo(w)){
            System.out.println("Shortest path to " + edgeWeightedDigraph.getCityName(w) +" by " + criteria);
            writer.println("");
            writer.println("Shortest path to " + edgeWeightedDigraph.getCityName(w) +" by " + criteria);
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            writer.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
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
            if(criteria.equals("price")){
                System.out.println("Lowest cost: $" + price);
                writer.println("Lowest cost: $" + price);
            }
            else if(criteria.equals("distance")){
                System.out.println("Shortest distance: " + distance);
                writer.println("Shortest distance: " + distance);
            }
            else{
                System.out.println("Fewest hops: " + distance);
                writer.println("Fewest hops: " + distance);
            }
            System.out.println("Path: " + path.reverse().toString());                   //reversed string is path in correct order
            writer.println("Path: " + path.reverse().toString());                   //reversed string is path in correct order
        }
        else {
            System.out.println("There is no path to " + e.getCityName(w));
        }
    }

    public static EdgeWeightedDigraph digraphWithConstantDistanceWeights(EdgeWeightedDigraph e){
        EdgeWeightedDigraph ewd = new EdgeWeightedDigraph(e);
        for(DirectedEdge directedEdge : ewd.edges()){
            directedEdge.setDistance(1);
        }
        return ewd;
    }

    public static void showFlights(EdgeWeightedGraph e, PrintWriter writer){
        writer.println("");
        writer.println("List of flights");
        for(Edge edge : e.edges()){
                String start = e.getCityName(edge.either());
                String end = e.getCityName(edge.other(edge.either()));
                int dist = edge.distance();
                double p = edge.cost();
                System.out.println( start + " <--> " + end + "\t Price: " + p + "\t Distance: "+dist);
                writer.println( start + " <--> " + end + "\t Price: " + p + "\t Distance: "+dist);
        }
    }

    public static void showTree(PrimMST mst, EdgeWeightedGraph e, PrintWriter writer){
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
        writer.println("");
        for(int x : roots){
            System.out.println("MST "+treeNumber);
            writer.println("MST "+treeNumber);
            for(Edge edge : mst.edges()){
                if(x == parents[edge.either()]){
                    String start = e.getCityName(edge.either());
                    String end = e.getCityName(edge.other(edge.either()));
                    int dist = edge.distance();
                    double p = edge.cost();
                    System.out.println(start + " -> " + end + "\t Price: " + p + "\t Distance: " + dist);
                    writer.println(start + " -> " + end + "\t Price: " + p + "\t Distance: " + dist);
                }
            }
            System.out.println();
            treeNumber++;
        }

    }

}
