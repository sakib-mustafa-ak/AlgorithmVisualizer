package com.algovis.algorithms.sorting;

import java.util.HashMap;
import java.util.Map;

import com.algovis.core.Algorithm;
import com.algovis.model.data.ArrayModel;
import com.algovis.model.step.AlgorithmStep;

public class BubbleSort implements Algorithm<ArrayModel> {

    private ArrayModel a;
    private int n, i;
    private boolean swapped;
    private boolean done;

    @Override public void init(ArrayModel input) {
        this.a = input.copy();
        this.n = a.size();
        this.i = 1;
        this.swapped = false;
        this.done = (n<=1);
    }

    @Override public boolean isDone() { return done; }

    @Override public AlgorithmStep nextStep() {
        if (done) return null;
        if (i < n) {
            int x = a.get(i-1), y = a.get(i);
            Map<String,Object> hl = new HashMap<>();
            hl.put("compareIndices", new int[]{i-1, i});
            String desc = "Compare a["+(i-1)+"] and a["+i+"]";
            if (x > y) {
                a.set(i-1, y); a.set(i, x);
                hl.put("swapIndices", new int[]{i-1, i});
                desc += " → swap";
                swapped = true;
            }
            i++;
            return new AlgorithmStep("BubbleSort", desc, hl, a.copy());
        } else {
            n--;
            i = 1;
            if (!swapped || n<=1) {
                done = true;
                Map<String,Object> hl = new HashMap<>();
                hl.put("sortedPartition", a.size());
                return new AlgorithmStep("BubbleSort", "Array is sorted", hl, a.copy());
            }
            swapped = false;
            Map<String,Object> hl = new HashMap<>();
            hl.put("sortedPartition", a.size() - n);
            return new AlgorithmStep("BubbleSort", "Pass complete", hl, a.copy());
        }
    }

    @Override public void reset() { init(a); }
}