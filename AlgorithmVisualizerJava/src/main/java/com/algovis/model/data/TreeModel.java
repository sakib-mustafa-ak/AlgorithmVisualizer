package com.algovis.model.data;

import java.util.ArrayList;
import java.util.List;

public class TreeModel {
    public static class Node {
        public int key;
        public int height;
        public Node left, right;
        public Node(int k){
            key = k;
            height = 1;
        }
    }
    public Node root;

    public static TreeModel fromCsv(String csv){
        if (csv == null || csv.isBlank()) return new TreeModel();
        String[] parts = csv.split(",");
        List<Integer> vals = new ArrayList<>();
        for (String p : parts){
            String s = p.trim();
            if (s.isEmpty()) continue;
            if (s.equalsIgnoreCase("null") || s.equalsIgnoreCase("x")) continue;
            try {
                vals.add(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                // Optionally handle parse errors, e.g. skip or log
            }
        }
        int[] arr = new int[vals.size()];
        for (int i = 0; i < vals.size(); i++) arr[i] = vals.get(i);
        return fromArrayLevelOrder(arr);
    }

    // Build a tree from an int[] interpreted in LEVEL-ORDER (array) form.
    // Index i -> left child 2*i+1, right child 2*i+2.
    public static TreeModel fromArrayLevelOrder(int[] a){
        TreeModel t = new TreeModel();
        if (a == null || a.length == 0) return t;
        Node[] nodes = new Node[a.length];
        for (int i = 0; i < a.length; i++) nodes[i] = new Node(a[i]);
        for (int i = 0; i < a.length; i++){
            int li = 2*i + 1, ri = 2*i + 2;
            if (li < a.length) nodes[i].left  = nodes[li];
            if (ri < a.length) nodes[i].right = nodes[ri];
            
            nodes[i].height = 1 + Math.max(height(nodes[i].left), height(nodes[i].right));
        }
        t.root = nodes[0];
        return t;
    }

    
    public void insert(int key){ root = insert(root, key); }
    private Node insert(Node x, int k){
        if (x==null) return new Node(k);
        if (k < x.key) x.left = insert(x.left, k);
        else if (k > x.key) x.right = insert(x.right, k);
        
        x.height = 1 + Math.max(height(x.left), height(x.right));
        return x;
    }

    public TreeModel copy(){
        TreeModel t = new TreeModel();
        t.root = copy(root);
        return t;
    }
    private Node copy(Node x){
        if (x==null) return null;
        Node y = new Node(x.key);
        y.height = x.height;
        y.left = copy(x.left);
        y.right = copy(x.right);
        return y;
    }

    private static int height(Node n){
        return (n == null) ? 0 : n.height;
    }
}
