package com.algovis.algorithms.sorting;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import com.algovis.core.Algorithm;
import com.algovis.model.data.ArrayModel;
import com.algovis.model.step.AlgorithmStep;

public class QuickSort implements Algorithm<ArrayModel> {
    private ArrayModel a;
    private Deque<Integer> stack;
    private int low, high, p, i;
    private boolean partitioning;
    private boolean done;

    @Override
    public void init(ArrayModel input) {
        this.a = input.copy();
        this.stack = new ArrayDeque<>();
        stack.push(0);
        stack.push(a.size() - 1);
        this.partitioning = false;
        this.done = a.size() <= 1;
    }

    @Override
    public boolean isDone() { return done; }

    @Override
    public AlgorithmStep nextStep() {
        if (done) return null;

        if (!partitioning) {
            if (stack.isEmpty()) {
                done = true;
                Map<String, Object> hl = new HashMap<>();
                hl.put("sortedPartition", a.size());
                return new AlgorithmStep("QuickSort", "Array is sorted", hl, a.copy());
            }
            high = stack.pop();
            low = stack.pop();

            if (low < high) {
                p = high; // Use last element as pivot
                i = low;
                partitioning = true;
                // This step just sets up the partition
            } else {
                return nextStep(); // Skip empty ranges
            }
        }

        // Perform one step of partitioning
        Map<String, Object> hl = new HashMap<>();
        hl.put("pivot", p);
        hl.put("compareIndices", new int[]{low, high});
        hl.put("keyIndex", i - 1);

        for (int j = i; j < high; j++) {
            hl.put("compareIndices", new int[]{j, p});
            String desc = "Comparing a[" + j + "] with pivot a[" + p + "]";
            if (a.get(j) < a.get(p)) {
                int temp = a.get(i);
                a.set(i, a.get(j));
                a.set(j, temp);
                hl.put("swapIndices", new int[]{i, j});
                desc += " -> swap(a[" + i + "], a[" + j + "])";
                i++;
                return new AlgorithmStep("QuickSort", desc, hl, a.copy());
            }
        }

        // End of partition loop, swap pivot into place
        int temp = a.get(i);
        a.set(i, a.get(p));
        a.set(p, temp);

        int partitionIndex = i;
        hl.put("swapIndices", new int[]{i, high});

        stack.push(low);
        stack.push(partitionIndex - 1);
        stack.push(partitionIndex + 1);
        stack.push(high);
        partitioning = false;

        return new AlgorithmStep("QuickSort", "Pivot placed at " + partitionIndex, hl, a.copy());
    }

    @Override
    public void reset() { init(a); }
}