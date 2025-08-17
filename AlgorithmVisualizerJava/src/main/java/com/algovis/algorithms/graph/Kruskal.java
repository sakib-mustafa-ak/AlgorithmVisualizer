package com.algovis.algorithms.graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.algovis.core.Algorithm;
import com.algovis.model.data.GraphModel;
import com.algovis.model.step.AlgorithmStep;

public class Kruskal implements Algorithm<GraphModel> {

    private static class DSU {
        int[] parent;
        DSU(int n) {
            parent = new int[n];
            for (int i = 0; i < n; i++) parent[i] = i;
        }
        int find(int i) {
            if (parent[i] == i) return i;
            return parent[i] = find(parent[i]);
        }
        void union(int i, int j) {
            int root_i = find(i);
            int root_j = find(j);
            if (root_i != root_j) parent[root_i] = root_j;
        }
    }

    private GraphModel g;
    private List<GraphModel.Edge> edges;
    private List<GraphModel.Edge> mstEdges;
    private DSU dsu;
    private int edgeIndex;
    private boolean done = false;

    @Override
    public void init(GraphModel input) {
        g = input.copy();
        edges = new ArrayList<>();
        for (int i = 0; i < g.n; i++) {
            for (GraphModel.Edge e : g.adj.get(i)) {
                if (e.u < e.v) edges.add(e);
            }
        }
        edges.sort(Comparator.comparingDouble(e -> e.w));
        mstEdges = new ArrayList<>();
        dsu = new DSU(g.n);
        edgeIndex = 0;
        done = false;
    }

    @Override
    public boolean isDone() { return done; }

    @Override
    public AlgorithmStep nextStep() {
        if (edgeIndex >= edges.size() || mstEdges.size() == g.n - 1) {
            done = true;
            return new AlgorithmStep("Kruskal's", "MST Complete", Map.of("mstEdges", new ArrayList<>(mstEdges)), g.copy());
        }

        GraphModel.Edge currentEdge = edges.get(edgeIndex++);
        int u = currentEdge.u;
        int v = currentEdge.v;
        String desc = "Considering edge (" + u + ", " + v + ")";

        Map<String, Object> hl = new HashMap<>();
        hl.put("highlightEdge", currentEdge);
        hl.put("mstEdges", new ArrayList<>(mstEdges));

        if (dsu.find(u) != dsu.find(v)) {
            dsu.union(u, v);
            mstEdges.add(currentEdge);
            hl.put("mstEdges", new ArrayList<>(mstEdges)); // update with new edge
            desc += " -> Added to MST";
        } else {
            desc += " -> Creates a cycle (skipped)";
        }

        return new AlgorithmStep("Kruskal's", desc, hl, g.copy());
    }

    @Override
    public void reset() { init(g); }
}