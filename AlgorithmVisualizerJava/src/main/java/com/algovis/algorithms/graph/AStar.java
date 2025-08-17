package com.algovis.algorithms.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import com.algovis.core.Algorithm;
import com.algovis.model.data.GraphModel;
import com.algovis.model.step.AlgorithmStep;
import com.algovis.util.Pair;

public class AStar implements Algorithm<GraphModel> {

    private GraphModel g;
    private int start = 0;
    private int goal;
    private PriorityQueue<Pair<Double, Integer>> openSet;
    private Map<Integer, Integer> cameFrom;
    private Map<Integer, Double> gScore;
    private boolean done = false;
    private final List<Integer> visitedOrder = new ArrayList<>();
    private List<GraphModel.Edge> finalPath = null;


    @Override
    public void init(GraphModel input) {
        g = input.copy();
        start = 0;
        goal = g.n - 1;

        openSet = new PriorityQueue<>(Comparator.comparingDouble(Pair::first));
        cameFrom = new HashMap<>();
        gScore = new HashMap<>();
        for (int i = 0; i < g.n; i++) {
            gScore.put(i, Double.POSITIVE_INFINITY);
        }

        gScore.put(start, 0.0);
        openSet.add(new Pair<>(heuristic(start), start));

        done = false;
        visitedOrder.clear();
        finalPath = null;
    }

    private double heuristic(int node) {
        double dx = g.pos[node][0] - g.pos[goal][0];
        double dy = g.pos[node][1] - g.pos[goal][1];
        return Math.sqrt(dx * dx + dy * dy); // Euclidean distance
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public AlgorithmStep nextStep() {
        if (finalPath != null) {
            done = true;
            return new AlgorithmStep("A*", "Path found!", Map.of("mstEdges", finalPath), g.copy());
        }

        if (openSet.isEmpty()) {
            done = true;
            return new AlgorithmStep("A*", "Path not found.", new HashMap<>(), g.copy());
        }

        int current = openSet.poll().second();
        visitedOrder.add(current);

        if (current == goal) {
            reconstructPath(current);
            return new AlgorithmStep("A*", "Goal reached! Reconstructing path.", Map.of("mstEdges", finalPath, "visitedNodes", visitedOrder.stream().mapToInt(i->i).toArray()), g.copy());
        }

        for (GraphModel.Edge neighborEdge : g.adj.get(current)) {
            int neighbor = neighborEdge.v;
            double tentative_gScore = gScore.get(current) + neighborEdge.w;
            if (tentative_gScore < gScore.get(neighbor)) {
                cameFrom.put(neighbor, current);
                gScore.put(neighbor, tentative_gScore);
                double fScore = tentative_gScore + heuristic(neighbor);
                openSet.add(new Pair<>(fScore, neighbor));
            }
        }

        Map<String, Object> hl = new HashMap<>();
        hl.put("visitNode", current);
        hl.put("visitedNodes", visitedOrder.stream().mapToInt(i -> i).toArray());
        return new AlgorithmStep("A*", "Visiting node " + current, hl, g.copy());
    }

    private void reconstructPath(int current) {
        finalPath = new ArrayList<>();
        while (cameFrom.containsKey(current)) {
            int previous = cameFrom.get(current);
            double weight = gScore.get(current) - gScore.get(previous);
            finalPath.add(new GraphModel.Edge(previous, current, weight));
            current = previous;
        }
        Collections.reverse(finalPath);
    }

    @Override
    public void reset() { init(g); }
}