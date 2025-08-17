package com.algovis.viewrender;

import java.util.Map;

import com.algovis.model.data.ArrayModel;
import com.algovis.model.state.VisualizerState;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ArrayRenderer {

    private boolean contains(int[] arr, int key) {
        if (arr == null) return false;
        for (int i : arr) {
            if (i == key) return true;
        }
        return false;
    }

    public void render(GraphicsContext gc, double w, double h, VisualizerState state){
        ArrayModel a = state.getArray();
        if (a == null) return;
        int n = a.size();
        double barW = Math.max(2, (w-20) / n);
        int max = 1;
        for (int i=0;i<n;i++) max = Math.max(max, a.get(i));
        double scale = (h-40) / max;

        Map<String, Object> hl = state.getCurrentHighlights();
        int[] compare = (int[]) hl.getOrDefault("compareIndices", new int[]{});
        int[] swap = (int[]) hl.getOrDefault("swapIndices", new int[]{});
        int sortedPartition = (int) hl.getOrDefault("sortedPartition", 0);
        int keyIndex = (int) hl.getOrDefault("keyIndex", -1);
        int pivot = (int) hl.getOrDefault("pivot", -1);


        gc.setFont(Font.font("System", 10));
        for (int i=0;i<n;i++){
            double x = i*barW + 10;
            double bh = a.get(i)*scale;
            double y = h - bh - 20;

            if (i == pivot) {
                gc.setFill(Color.MAGENTA);
            } else if (i == keyIndex) {
                gc.setFill(Color.CYAN);
            } else if (contains(swap, i)) {
                gc.setFill(Color.RED);
            } else if (contains(compare, i)) {
                gc.setFill(Color.ORANGE);
            } else if (i < sortedPartition || (a.size() - i) <= sortedPartition) {
                // sortedPartition can be from left (selection) or right (bubble)
                gc.setFill(Color.LIMEGREEN);
            }
             else {
                gc.setFill(Color.CORNFLOWERBLUE);
            }

            gc.fillRect(x, y, barW-1, bh);
            gc.setStroke(Color.BLACK);
            gc.strokeRect(x, y, barW-1, bh);
        }
    }
}