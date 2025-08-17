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

public class Prim implements Algorithm<GraphModel> {
    private GraphModel g;
    private double[] key;
    private int[] parent;
    private boolean[] inMST;
    private PriorityQueue<Pair<Double, Integer>> pq;
    private boolean done = false;
    private final List<GraphModel.Edge> mstEdges = new ArrayList<>();

    @Override
    public void init(GraphModel input) {
        g = input.copy();
        key = new double[g.n];
        parent = new int[g.n];
        inMST = new boolean[g.n];
        pq = new PriorityQueue<>(Comparator.comparingDouble(Pair::first));
        mstEdges.clear();

        Arrays.fill(key, Double.POSITIVE_INFINITY);
        Arrays.fill(parent, -1);

        key[0] = 0;
        pq.add(new Pair<>(0.0, 0));
        done = false;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public AlgorithmStep nextStep() {
        if (pq.isEmpty()) {
            done = true;
            return new AlgorithmStep("Prim's", "MST Complete", Map.of("mstEdges", new ArrayList<>(mstEdges)), g.copy());
        }

        int u = pq.poll().second();

        if (inMST[u]) {
            return nextStep(); // Skip if already in MST
        }
        inMST[u] = true;

        if (parent[u] != -1) {
            mstEdges.add(new GraphModel.Edge(parent[u], u, key[u]));
        }

        for (GraphModel.Edge e : g.adj.get(u)) {
            int v = e.v;
            double weight = e.w;
            if (!inMST[v] && key[v] > weight) {
                key[v] = weight;
                parent[v] = u;
                pq.add(new Pair<>(key[v], v));
            }
        }

        Map<String, Object> hl = new HashMap<>();
        hl.put("visitNode", u);
        hl.put("mstEdges", new ArrayList<>(mstEdges));

        return new AlgorithmStep("Prim's", "Add node " + u + " to MST", hl, g.copy());
    }

    @Override
    public void reset() { init(g); }
}