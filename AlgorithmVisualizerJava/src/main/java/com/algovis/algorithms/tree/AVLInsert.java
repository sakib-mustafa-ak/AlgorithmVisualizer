package com.algovis.algorithms.tree;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import com.algovis.core.Algorithm;
import com.algovis.model.data.TreeModel;
import com.algovis.model.step.AlgorithmStep;

public class AVLInsert implements Algorithm<TreeModel> {

    private TreeModel t;                // working AVL tree we build into
    private Queue<Integer> toInsert;    // keys to insert (from the user's loaded tree)
    private boolean done = false;

    @Override
    public void init(TreeModel input) {
        // Same idea as BSTInsert: reconstruct the user's tree progressively,
        // but using AVL insertions so rotations are visualized.
        toInsert = new ArrayDeque<>();
        levelOrderFill(input, toInsert);
        t = new TreeModel(); // start empty
        done = toInsert.isEmpty();
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public AlgorithmStep nextStep() {
        if (toInsert.isEmpty()) {
            done = true;
            return new AlgorithmStep("AVL Insert", "Insertion complete.", new HashMap<>(), t.copy());
        }
        int key = toInsert.poll();
        t.root = insertAVL(t.root, key);
        Map<String, Object> hl = new HashMap<>();
        hl.put("insertKey", key);
        return new AlgorithmStep("AVL Insert", "Insert key " + key, hl, t.copy());
    }

    // --- AVL implementation (works on TreeModel.Node) ---
    private int height(TreeModel.Node N) {
        return (N == null) ? 0 : N.height;
    }

    private TreeModel.Node rightRotate(TreeModel.Node y) {
        TreeModel.Node x = y.left;
        TreeModel.Node T2 = x.right;
        x.right = y;
        y.left = T2;
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        return x;
    }

    private TreeModel.Node leftRotate(TreeModel.Node x) {
        TreeModel.Node y = x.right;
        TreeModel.Node T2 = y.left;
        y.left = x;
        x.right = T2;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        return y;
    }

    private int getBalance(TreeModel.Node N) {
        return (N == null) ? 0 : height(N.left) - height(N.right);
    }

    private TreeModel.Node insertAVL(TreeModel.Node node, int key) {
        if (node == null) return (new TreeModel.Node(key));

        if (key < node.key)
            node.left = insertAVL(node.left, key);
        else if (key > node.key)
            node.right = insertAVL(node.right, key);
        else
            return node; // Duplicate keys not allowed

        node.height = 1 + Math.max(height(node.left), height(node.right));

        int balance = getBalance(node);

        // LL
        if (balance > 1 && key < node.left.key)
            return rightRotate(node);

        // RR
        if (balance < -1 && key > node.right.key)
            return leftRotate(node);

        // LR
        if (balance > 1 && key > node.left.key) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // RL
        if (balance < -1 && key < node.right.key) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
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
}
