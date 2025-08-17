package com.algovis.model.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GraphModel {
    public static class Edge {
        public final int u, v; public final double w;
        public Edge(int u, int v, double w){ this.u=u; this.v=v; this.w=w; }
    }
    public int n;
    public List<List<Edge>> adj = new ArrayList<>();
    public double[][] pos; // [n][2] normalized positions [0..1]

    public GraphModel(int n){
        this.n = n;
        for(int i=0;i<n;i++) adj.add(new ArrayList<>());
        this.pos = new double[n][2];
        Random r = new Random();
        for (int i=0;i<n;i++){ pos[i][0]=r.nextDouble(); pos[i][1]=r.nextDouble(); }
    }

    public void addUndirected(int u, int v, double w){
        adj.get(u).add(new Edge(u,v,w));
        adj.get(v).add(new Edge(v,u,w));
    }

    public GraphModel copy() {
        GraphModel g = new GraphModel(n);
        g.adj.clear();
        for (int i=0;i<n;i++){
            List<Edge> list = new ArrayList<>();
            for (Edge e: adj.get(i)) list.add(new Edge(e.u, e.v, e.w));
            g.adj.add(list);
        }
        g.pos = new double[n][2];
        for (int i=0;i<n;i++){ g.pos[i][0]=pos[i][0]; g.pos[i][1]=pos[i][1]; }
        return g;
    }

    public static GraphModel random(int n, double p, boolean weighted){
        GraphModel g = new GraphModel(n);
        Random r = new Random();
        for(int i=0;i<n;i++) for(int j=i+1;j<n;j++){
            if (r.nextDouble() < p){
                double w = weighted ? (1+r.nextInt(9)) : 1.0;
                g.addUndirected(i,j,w);
            }
        }
        return g;
    }

    public static GraphModel grid(int rows, int cols){
        int n = rows*cols;
        GraphModel g = new GraphModel(n);
        g.adj.clear();
        for(int i=0;i<n;i++) g.adj.add(new ArrayList<>());

        for (int r=0;r<rows;r++){
            for (int c=0;c<cols;c++){
                int i = r*cols+c;
                g.pos[i][0] = (c+1) / (double)(cols+1);
                g.pos[i][1] = (r+1) / (double)(rows+1);
                if (c+1<cols) g.addUndirected(i, i+1, 1.0);
                if (r+1<rows) g.addUndirected(i, i+cols, 1.0);
            }
        }
        return g;
    }
}