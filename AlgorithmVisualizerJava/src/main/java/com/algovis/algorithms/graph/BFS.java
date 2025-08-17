package com.algovis.algorithms.graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.algovis.core.Algorithm;
import com.algovis.model.data.GraphModel;
import com.algovis.model.step.AlgorithmStep;

public class BFS implements Algorithm<GraphModel> {
    private GraphModel g;
    private int s=0;
    private int[] dist;
    private Queue<Integer> q;
    private boolean done=false;
    private final List<Integer> visitedOrder = new ArrayList<>();


    @Override public void init(GraphModel input) {
        g = input.copy();
        dist = new int[g.n];
        Arrays.fill(dist, -1);
        s = 0;
        q = new ArrayDeque<>();
        q.add(s); dist[s]=0;
        done = false;
        visitedOrder.clear();
    }
    @Override public boolean isDone(){ return done; }

    @Override public AlgorithmStep nextStep(){
        if (q.isEmpty()){ done=true; return null; }
        int u = q.poll();
        visitedOrder.add(u);
        for (GraphModel.Edge e : g.adj.get(u)){
            int v = e.v;
            if (dist[v] == -1){
                dist[v] = dist[u] + 1;
                q.add(v);
            }
        }
        Map<String,Object> hl = new HashMap<>();
        hl.put("visitNode", u);
        hl.put("visitedNodes", visitedOrder.stream().mapToInt(i -> i).toArray());
        return new AlgorithmStep("BFS", "Visit node "+u, hl, g.copy());
    }

    @Override public void reset(){ init(g); }
}