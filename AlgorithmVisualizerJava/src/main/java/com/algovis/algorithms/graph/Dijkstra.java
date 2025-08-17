package com.algovis.algorithms.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import com.algovis.core.Algorithm;
import com.algovis.model.data.GraphModel;
import com.algovis.model.step.AlgorithmStep;
import com.algovis.util.Pair;

public class Dijkstra implements Algorithm<GraphModel> {
    private GraphModel g;
    private double[] dist;
    private boolean[] visited;
    private PriorityQueue<Pair<Double, Integer>> pq;
    private boolean done=false;
    private final List<Integer> visitedOrder = new ArrayList<>();

    @Override public void init(GraphModel input){
        g = input.copy();
        dist = new double[g.n];
        visited = new boolean[g.n];
        pq = new PriorityQueue<>(Comparator.comparingDouble(Pair::first));
        visitedOrder.clear();

        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        dist[0]=0;
        pq.add(new Pair<>(0.0, 0));
        done = false;
    }
    @Override public boolean isDone(){ return done; }

    @Override public AlgorithmStep nextStep(){
        if (pq.isEmpty()){
            done=true;
            return null;
        }

        int u = -1;
        // Extract node with smallest distance
        while(!pq.isEmpty()) {
            int candidate = pq.poll().second();
            if (!visited[candidate]) {
                u = candidate;
                break;
            }
        }

        if (u==-1){ done=true; return null; }

        visited[u]=true;
        visitedOrder.add(u);

        for (GraphModel.Edge e: g.adj.get(u)){
            if (!visited[e.v] && dist[e.v] > dist[u] + e.w){
                dist[e.v] = dist[u] + e.w;
                pq.add(new Pair<>(dist[e.v], e.v));
            }
        }

        Map<String,Object> hl=new HashMap<>();
        hl.put("visitNode", u);
        hl.put("visitedNodes", visitedOrder.stream().mapToInt(i -> i).toArray());
        return new AlgorithmStep("Dijkstra", "Pick next closest node "+u, hl, g.copy());
    }
    @Override public void reset(){ init(g); }
}