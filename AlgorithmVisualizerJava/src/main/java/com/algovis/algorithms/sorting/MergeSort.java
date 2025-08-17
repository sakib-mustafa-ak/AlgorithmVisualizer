package com.algovis.algorithms.sorting;

import java.util.HashMap;
import java.util.Map;

import com.algovis.core.Algorithm;
import com.algovis.model.data.ArrayModel;
import com.algovis.model.step.AlgorithmStep;

public class MergeSort implements Algorithm<ArrayModel> {
    private ArrayModel a;
    private int[] aux;
    private int currentSize;
    private int leftStart;
    private boolean done = false;

    @Override
    public void init(ArrayModel input) {
        this.a = input.copy();
        this.aux = new int[a.size()];
        this.currentSize = 1;
        this.leftStart = 0;
        this.done = a.size() <= 1;
    }

    @Override
    public boolean isDone() { return done; }

    @Override
    public AlgorithmStep nextStep() {
        if (done) return null;

        int n = a.size();
        if (currentSize >= n) {
            done = true;
            Map<String, Object> hl = new HashMap<>();
            hl.put("sortedPartition", n);
            return new AlgorithmStep("MergeSort", "Array is sorted", hl, a.copy());
        }

        int mid = Math.min(leftStart + currentSize - 1, n - 1);
        int rightEnd = Math.min(leftStart + 2 * currentSize - 1, n - 1);

        merge(leftStart, mid, rightEnd);

        String desc = "Merge subarrays of size " + currentSize;
        Map<String, Object> hl = new HashMap<>();
        hl.put("compareIndices", new int[]{leftStart, rightEnd});

        leftStart += 2 * currentSize;
        if (leftStart >= n) {
            leftStart = 0;
            currentSize *= 2;
        }
        
        if(currentSize >=n) hl.put("sortedPartition", n);

        return new AlgorithmStep("MergeSort", desc, hl, a.copy());
    }

    private void merge(int l, int m, int r) {
        System.arraycopy(a.a, l, aux, l, r - l + 1);

        int i = l, j = m + 1, k = l;
        while (i <= m && j <= r) {
            if (aux[i] <= aux[j]) {
                a.set(k++, aux[i++]);
            } else {
                a.set(k++, aux[j++]);
            }
        }
        while (i <= m) {
            a.set(k++, aux[i++]);
        }
        while (j <= r) {
            a.set(k++, aux[j++]);
        }
    }

    @Override
    public void reset() { init(a); }
}