package com.algovis.algorithms.tree;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import com.algovis.core.Algorithm;
import com.algovis.model.data.TreeModel;
import com.algovis.model.step.AlgorithmStep;

public class BSTInsert implements Algorithm<TreeModel> {
    private TreeModel t;                 // working tree we build into
    private Queue<Integer> toInsert;     // keys we will insert (derived from the user's loaded tree)
    private boolean done=false;

    @Override
    public void init(TreeModel input){
        // Build exactly the user's loaded tree step-by-step:
        // 1) Take the keys from the provided tree in level-order
        // 2) Start from an empty tree
        // 3) Insert keys one by one on each nextStep()
        toInsert = new ArrayDeque<>();
        levelOrderFill(input, toInsert);
        t = new TreeModel(); // start empty so visualization shows progressive insertion
        done = toInsert.isEmpty(); // if user loaded empty tree, we are done
    }

    @Override public boolean isDone(){ return done; }

    @Override
    public AlgorithmStep nextStep(){
        if (toInsert.isEmpty()){
            done=true;
            return new AlgorithmStep("BST Insert", "Insertion complete.", new HashMap<>(), t.copy());
        }
        int k = toInsert.poll();
        t.insert(k);
        Map<String,Object> hl=new HashMap<>();
        hl.put("insertKey", k);
        return new AlgorithmStep("BST Insert", "Insert key "+k, hl, t.copy());
    }

    @Override public void reset(){ init(t); }

    // --- helpers ---
    private void levelOrderFill(TreeModel input, Queue<Integer> out){
        if (input == null || input.root == null) return;
        ArrayDeque<TreeModel.Node> q = new ArrayDeque<>();
        q.add(input.root);
        while(!q.isEmpty()){
            var n = q.poll();
            out.add(n.key);
            if (n.left != null)  q.add(n.left);
            if (n.right != null) q.add(n.right);
        }
    }
}
