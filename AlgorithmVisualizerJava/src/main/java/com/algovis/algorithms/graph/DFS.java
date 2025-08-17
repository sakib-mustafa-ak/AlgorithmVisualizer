package com.algovis.algorithms.graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.algovis.core.Algorithm;
import com.algovis.model.data.GraphModel;
import com.algovis.model.step.AlgorithmStep;

public class DFS implements Algorithm<GraphModel> {
    private GraphModel g;
    private boolean[] visited;
    private Deque<Integer> stack;
    private boolean done = false;
    private int s = 0;
    private final List<Integer> visitedOrder = new ArrayList<>();


    @Override
    public void init(GraphModel input) {
        g = input.copy();
        visited = new boolean[g.n];
        stack = new ArrayDeque<>();
        s = 0;
        stack.push(s);
        done = false;
        visitedOrder.clear();
    }

    @Override
    public boolean isDone() { return done; }

    @Override
    public AlgorithmStep nextStep() {
        if (stack.isEmpty()) {
            done = true;
            return null;
        }
        int u = stack.pop();

        if (visited[u]) {
            return nextStep(); // Skip already visited nodes
        }

        visited[u] = true;
        visitedOrder.add(u);

        for (int i = g.adj.get(u).size() - 1; i >= 0; i--) {
            GraphModel.Edge e = g.adj.get(u).get(i);
            int v = e.v;
            if (!visited[v]) {
                stack.push(v);
            }
        }
        Map<String, Object> hl = new HashMap<>();
        hl.put("visitNode", u);
        hl.put("visitedNodes", visitedOrder.stream().mapToInt(i -> i).toArray());
        return new AlgorithmStep("DFS", "Visit node " + u, hl, g.copy());
    }

    @Override
    public void reset() { init(g); }
}