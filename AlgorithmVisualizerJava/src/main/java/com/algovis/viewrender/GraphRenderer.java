package com.algovis.viewrender;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.algovis.model.data.GraphModel;
import com.algovis.model.state.VisualizerState;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class GraphRenderer {
    public void render(GraphicsContext gc, double w, double h, VisualizerState state){
        GraphModel g = state.getGraph();
        if (g == null) return;

        Map<String, Object> hl = state.getCurrentHighlights();
        int visitNode = (int) hl.getOrDefault("visitNode", -1);
        int[] visitedNodes = (int[]) hl.getOrDefault("visitedNodes", new int[0]);
        @SuppressWarnings("unchecked")
        List<GraphModel.Edge> mstEdges = (List<GraphModel.Edge>) hl.getOrDefault("mstEdges", Collections.emptyList());
        GraphModel.Edge highlightEdge = (GraphModel.Edge) hl.get("highlightEdge");


        gc.setFont(Font.font("System", 12));
        gc.setLineWidth(1.0);

        // edges
        for (int u=0; u<g.n; u++){
            for (GraphModel.Edge e : g.adj.get(u)){
                if (e.u < e.v){
                    double x1 = g.pos[u][0]*w*0.9 + 30, y1 = g.pos[u][1]*h*0.9 + 30;
                    double x2 = g.pos[e.v][0]*w*0.9 + 30, y2 = g.pos[e.v][1]*h*0.9 + 30;

                    gc.setStroke(Color.GRAY);
                    gc.setLineWidth(1.0);

                    if (isEdgeInList(e, mstEdges)) {
                        gc.setStroke(Color.FORESTGREEN);
                        gc.setLineWidth(2.5);
                    }
                    if (highlightEdge != null && ((e.u == highlightEdge.u && e.v == highlightEdge.v) || (e.u == highlightEdge.v && e.v == highlightEdge.u))) {
                        gc.setStroke(Color.RED);
                        gc.setLineWidth(2.5);
                    }

                    gc.strokeLine(x1,y1,x2,y2);
                }
            }
        }
        gc.setLineWidth(1.0);

        // nodes
        for (int i=0;i<g.n;i++){
            double x = g.pos[i][0]*w*0.9 + 30, y = g.pos[i][1]*h*0.9 + 30;

            if (i == visitNode) {
                gc.setFill(Color.ORANGE);
            } else if (contains(visitedNodes, i)) {
                gc.setFill(Color.LIGHTBLUE);
            } else {
                gc.setFill(Color.WHITE);
            }

            gc.setStroke(Color.BLACK);
            gc.fillOval(x-8,y-8,16,16);
            gc.strokeOval(x-8,y-8,16,16);

            gc.setFill(Color.BLACK);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText(Integer.toString(i), x, y + 4);
        }
    }

    private boolean contains(int[] arr, int key) {
        for (int i : arr) if (i == key) return true;
        return false;
    }

    private boolean isEdgeInList(GraphModel.Edge edge, List<GraphModel.Edge> list) {
        for (GraphModel.Edge e : list) {
            if ((e.u == edge.u && e.v == edge.v) || (e.u == edge.v && e.v == edge.u)) {
                return true;
            }
        }
        return false;
    }
}