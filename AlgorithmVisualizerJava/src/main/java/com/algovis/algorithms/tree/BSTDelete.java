package com.algovis.algorithms.tree;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import com.algovis.core.Algorithm;
import com.algovis.model.data.TreeModel;
import com.algovis.model.step.AlgorithmStep;

public class BSTDelete implements Algorithm<TreeModel> {
    private TreeModel t;               // user's loaded tree (copy)
    private Queue<Integer> toDelete;   // delete sequence derived from the user's tree
    private boolean done = false;

    @Override
    public void init(TreeModel input) {
        // Work on the user's loaded tree directly.
        // Build a deletion queue from the current tree (level-order),
        // so the algorithm deletes keys that actually exist.
        t = input.copy();
        toDelete = new ArrayDeque<>();
        levelOrderFill(t, toDelete);
        done = (t.root == null) || toDelete.isEmpty();
    }

    @Override
    public boolean isDone() { return done; }

    @Override
    public AlgorithmStep nextStep() {
        if (toDelete.isEmpty() || t.root == null) {
            done = true;
            return new AlgorithmStep("BST Delete", "Deletion complete", new HashMap<>(), t.copy());
        }
        int k = toDelete.poll();
        t.root = deleteRec(t.root, k);

        Map<String, Object> hl = new HashMap<>();
        hl.put("deleteKey", k);
        return new AlgorithmStep("BST Delete", "Delete key " + k, hl, t.copy());
    }

    private TreeModel.Node deleteRec(TreeModel.Node root, int key) {
        if (root == null) return root;

        if (key < root.key)
            root.left = deleteRec(root.left, key);
        else if (key > root.key)
            root.right = deleteRec(root.right, key);
        else {
            if (root.left == null) return root.right;
            else if (root.right == null) return root.left;

            root.key = minValue(root.right);
            root.right = deleteRec(root.right, root.key);
        }
        // (Optional) update height for nicer rendering consistency
        root.height = 1 + Math.max(height(root.left), height(root.right));
        return root;
    }

    private int minValue(TreeModel.Node root) {
        int minv = root.key;
        while (root.left != null) {
            minv = root.left.key;
            root = root.left;
        }
        return minv;
    }

    @Override
    public void reset() { init(t); }

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

    private int height(TreeModel.Node n){
        return (n==null)?0:n.height;
    }
}
