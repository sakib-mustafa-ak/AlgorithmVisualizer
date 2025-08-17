package com.algovis.viewrender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.algovis.model.data.TreeModel;
import com.algovis.model.state.VisualizerState;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class TreeRenderer {

    public void render(GraphicsContext gc, double w, double h, VisualizerState state){
        TreeModel t = state.getTree();
        if (t == null || t.root == null) return;

        Map<String, Object> hl = state.getCurrentHighlights();
        int insertKey = (int) hl.getOrDefault("insertKey", Integer.MIN_VALUE);
        int deleteKey = (int) hl.getOrDefault("deleteKey", Integer.MIN_VALUE);
        int visitKey = (int) hl.getOrDefault("visitKey", Integer.MIN_VALUE);
        @SuppressWarnings("unchecked")
        List<Integer> visitedPath = (List<Integer>) hl.getOrDefault("visitedPath", Collections.emptyList());

        List<List<NodePos>> levels = new ArrayList<>();
        calculatePositions(t.root, 0, 0, w, levels);

        gc.setLineWidth(1.5);
        gc.setFont(Font.font("System", 14));
        
        int depth = getDepth(t.root);
        double levelHeight = (h - 40) / depth;

        // Draw lines first
        for (List<NodePos> level : levels) {
            for (NodePos np : level) {
                np.y = np.depth * levelHeight + 40;
                if (np.node.left != null) {
                    NodePos childPos = findPos(np.node.left, levels);
                    if (childPos != null) gc.strokeLine(np.x, np.y, childPos.x, np.y + levelHeight);
                }
                if (np.node.right != null) {
                    NodePos childPos = findPos(np.node.right, levels);
                    if (childPos != null) gc.strokeLine(np.x, np.y, childPos.x, np.y + levelHeight);
                }
            }
        }

        // Draw nodes on top of lines
        for (List<NodePos> level : levels) {
            for (NodePos np : level) {
                if (np.node.key == insertKey) gc.setFill(Color.LIMEGREEN);
                else if (np.node.key == deleteKey) gc.setFill(Color.TOMATO);
                else if (np.node.key == visitKey) gc.setFill(Color.ORANGE);
                else if (visitedPath.contains(np.node.key)) gc.setFill(Color.LIGHTBLUE);
                else gc.setFill(Color.WHITE);

                gc.setStroke(Color.BLACK);
                gc.fillOval(np.x-15, np.y-15, 30, 30);
                gc.strokeOval(np.x-15, np.y-15, 30, 30);
                gc.setFill(Color.BLACK);
                gc.setTextAlign(TextAlignment.CENTER);
                gc.fillText(Integer.toString(np.node.key), np.x, np.y+5);
            }
        }
    }

    private void calculatePositions(TreeModel.Node node, int depth, double minX, double maxX, List<List<NodePos>> levels) {
        if (node == null) return;
        if (levels.size() <= depth) levels.add(new ArrayList<>());
        
        double x = (minX + maxX) / 2.0;
        levels.get(depth).add(new NodePos(node, x, 0, depth));

        calculatePositions(node.left, depth + 1, minX, x, levels);
        calculatePositions(node.right, depth + 1, x, maxX, levels);
    }
    
    private int getDepth(TreeModel.Node node) {
        if (node == null) return 0;
        return 1 + Math.max(getDepth(node.left), getDepth(node.right));
    }

    private NodePos findPos(TreeModel.Node node, List<List<NodePos>> levels) {
        for (List<NodePos> level : levels) {
            for (NodePos np : level) {
                if (np.node == node) return np;
            }
        }
        return null;
    }

    private static class NodePos {
        TreeModel.Node node; double x, y; int depth;
        NodePos(TreeModel.Node node, double x, double y, int depth) {
            this.node = node;
            this.x = x;
            this.y = y;
            this.depth = depth;
        }
    }
}