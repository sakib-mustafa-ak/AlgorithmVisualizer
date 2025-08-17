package com.algovis.algorithms.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.algovis.core.Algorithm;
import com.algovis.model.data.TreeModel;
import com.algovis.model.step.AlgorithmStep;

public class Traversals implements Algorithm<TreeModel> {

    private TreeModel t;
    private List<Integer> preOrder, inOrder, postOrder;
    private int index;
    private TraversalType currentTraversal;
    private boolean done = false;

    private enum TraversalType { PRE, IN, POST, DONE }

    @Override
    public void init(TreeModel input) {
        // Use the user's loaded tree directly.
        t = input.copy();
        preOrder = new ArrayList<>();
        inOrder = new ArrayList<>();
        postOrder = new ArrayList<>();
        if (t.root != null) {
            generatePreOrder(t.root);
            generateInOrder(t.root);
            generatePostOrder(t.root);
        }
        index = 0;
        currentTraversal = (t.root == null) ? TraversalType.DONE : TraversalType.PRE;
        done = (t.root == null);
    }

    @Override
    public boolean isDone() { return done; }

    @Override
    public AlgorithmStep nextStep() {
        if (done) return null;

        Map<String, Object> hl = new HashMap<>();
        String desc;
        List<Integer> currentList;

        switch (currentTraversal) {
            case PRE -> {
                currentList = preOrder;
                desc = "Pre-order Traversal";
            }
            case IN -> {
                currentList = inOrder;
                desc = "In-order Traversal";
            }
            case POST -> {
                currentList = postOrder;
                desc = "Post-order Traversal";
            }
            default -> {
                done = true;
                return new AlgorithmStep("Tree Traversals", "All traversals complete.", new HashMap<>(), t.copy());
            }
        }

        hl.put("visitKey", currentList.get(index));
        hl.put("visitedPath", new ArrayList<>(currentList.subList(0, index + 1)));
        desc += ": visit " + currentList.get(index);

        index++;
        if (index >= currentList.size()) {
            index = 0;
            currentTraversal = TraversalType.values()[currentTraversal.ordinal() + 1];
            if (currentTraversal == TraversalType.DONE) done = true;
        }

        return new AlgorithmStep("Tree Traversals", desc, hl, t.copy());
    }

    private void generatePreOrder(TreeModel.Node node) {
        if (node == null) return;
        preOrder.add(node.key);
        generatePreOrder(node.left);
        generatePreOrder(node.right);
    }
    private void generateInOrder(TreeModel.Node node) {
        if (node == null) return;
        generateInOrder(node.left);
        inOrder.add(node.key);
        generateInOrder(node.right);
    }
    private void generatePostOrder(TreeModel.Node node) {
        if (node == null) return;
        generatePostOrder(node.left);
        generatePostOrder(node.right);
        postOrder.add(node.key);
    }

    @Override
    public void reset() { init(t); }
}
