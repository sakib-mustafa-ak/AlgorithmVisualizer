package com.algovis.algorithms.sorting;

import java.util.HashMap;
import java.util.Map;

import com.algovis.core.Algorithm;
import com.algovis.model.data.ArrayModel;
import com.algovis.model.step.AlgorithmStep;

public class SelectionSort implements Algorithm<ArrayModel> {
    private ArrayModel a;
    private int i=0, j=1, minIdx=0;
    private boolean placing=false, done=false;

    @Override public void init(ArrayModel input) {
        this.a = input.copy();
        i=0; j=i+1; minIdx=i; placing=false; done = a.size()<=1;
    }
    @Override public boolean isDone(){ return done; }

    @Override public AlgorithmStep nextStep(){
        if (done) return null;
        int n=a.size();
        Map<String,Object> hl=new HashMap<>();
        hl.put("sortedPartition", i);

        if (!placing){
            if (j<n){
                hl.put("compareIndices", new int[]{j, minIdx});
                hl.put("keyIndex", minIdx);
                String desc="Check a["+j+"] vs current min a["+minIdx+"]";
                if (a.get(j) < a.get(minIdx)){
                    minIdx=j;
                    desc+=" (new min)";
                }
                j++;
                return new AlgorithmStep("SelectionSort", desc, hl, a.copy());
            } else {
                placing=true;
            }
        }
        int tmp=a.get(i); a.set(i, a.get(minIdx)); a.set(minIdx, tmp);
        hl.put("swapIndices", new int[]{i, minIdx});
        String desc="Place min at index "+i;
        i++;
        if (i>=n-1) {
            done=true;
            hl.put("sortedPartition", n);
        }
        j=i+1; minIdx=i; placing=false;
        return new AlgorithmStep("SelectionSort", desc, hl, a.copy());
    }

    @Override public void reset(){ init(a); }
}