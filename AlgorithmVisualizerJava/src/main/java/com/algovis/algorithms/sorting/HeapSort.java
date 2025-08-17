package com.algovis.algorithms.sorting;

import java.util.HashMap;
import java.util.Map;

import com.algovis.core.Algorithm;
import com.algovis.model.data.ArrayModel;
import com.algovis.model.step.AlgorithmStep;

public class HeapSort implements Algorithm<ArrayModel> {
    private ArrayModel a;
    private int n;
    private int i;
    private boolean buildingHeap;
    private boolean done;

    @Override
    public void init(ArrayModel input) {
        this.a = input.copy();
        this.n = a.size();
        this.i = n / 2 - 1;
        this.buildingHeap = true;
        this.done = n <= 1;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public AlgorithmStep nextStep() {
        if (done) return null;

        if (buildingHeap) {
            if (i >= 0) {
                heapify(n, i);
                i--;
                return new AlgorithmStep("HeapSort", "Building heap...", new HashMap<>(), a.copy());
            } else {
                buildingHeap = false;
                i = n - 1;
            }
        }

        if (i > 0) {
            Map<String, Object> hl = new HashMap<>();
            // Move current root to end
            int temp = a.get(0);
            a.set(0, a.get(i));
            a.set(i, temp);
            hl.put("swapIndices", new int[]{0, i});
            hl.put("sortedPartition", n - i);

            // call max heapify on the reduced heap
            heapify(i, 0);

            i--;
            return new AlgorithmStep("HeapSort", "Extract max, place at end", hl, a.copy());
        }

        done = true;
        Map<String, Object> hl = new HashMap<>();
        hl.put("sortedPartition", n);
        return new AlgorithmStep("HeapSort", "Array is sorted", hl, a.copy());
    }

    private void heapify(int size, int root) {
        int largest = root;
        int l = 2 * root + 1;
        int r = 2 * root + 2;

        if (l < size && a.get(l) > a.get(largest))
            largest = l;
        if (r < size && a.get(r) > a.get(largest))
            largest = r;

        if (largest != root) {
            int swap = a.get(root);
            a.set(root, a.get(largest));
            a.set(largest, swap);
            heapify(size, largest);
        }
    }

    @Override
    public void reset() { init(a); }
}