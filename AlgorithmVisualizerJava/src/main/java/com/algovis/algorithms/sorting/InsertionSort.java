package com.algovis.algorithms.sorting;

import java.util.HashMap;
import java.util.Map;

import com.algovis.core.Algorithm;
import com.algovis.model.data.ArrayModel;
import com.algovis.model.step.AlgorithmStep;

public class InsertionSort implements Algorithm<ArrayModel> {

    private ArrayModel a;
    private int i, j;
    private int key;
    private boolean findingPosition;
    private boolean done;

    @Override
    public void init(ArrayModel input) {
        this.a = input.copy();
        this.i = 1;
        this.j = 1;
        this.findingPosition = false;
        this.done = a.size() <= 1;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public AlgorithmStep nextStep() {
        if (done) return null;

        Map<String, Object> hl = new HashMap<>();

        if (i < a.size()) {
            if (!findingPosition) {
                key = a.get(i);
                j = i;
                findingPosition = true;
                hl.put("keyIndex", i);
                return new AlgorithmStep("InsertionSort", "Select a[" + i + "] as key", hl, a.copy());
            }

            hl.put("keyIndex", j);
            if (j > 0 && a.get(j - 1) > key) {
                hl.put("compareIndices", new int[]{j - 1});
                hl.put("swapIndices", new int[]{j, j - 1});
                a.set(j, a.get(j - 1));
                j--;
                return new AlgorithmStep("InsertionSort", "Shift a[" + (j) + "] right", hl, a.copy());
            } else {
                a.set(j, key);
                findingPosition = false;
                i++;
                hl.put("sortedPartition", i);
                if (i >= a.size()) done = true;
                return new AlgorithmStep("InsertionSort", "Insert key at correct position", hl, a.copy());
            }
        } else {
            done = true;
            hl.put("sortedPartition", a.size());
            return new AlgorithmStep("InsertionSort", "Array is sorted", hl, a.copy());
        }
    }

    @Override
    public void reset() { init(a); }
}