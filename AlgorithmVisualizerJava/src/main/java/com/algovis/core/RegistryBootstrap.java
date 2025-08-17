package com.algovis.core;

import com.algovis.algorithms.graph.AStar;
import com.algovis.algorithms.graph.BFS;
import com.algovis.algorithms.graph.DFS;
import com.algovis.algorithms.graph.Dijkstra;
import com.algovis.algorithms.graph.Kruskal;
import com.algovis.algorithms.graph.Prim;
import com.algovis.algorithms.sorting.BubbleSort;
import com.algovis.algorithms.sorting.HeapSort;
import com.algovis.algorithms.sorting.InsertionSort;
import com.algovis.algorithms.sorting.MergeSort;
import com.algovis.algorithms.sorting.QuickSort;
import com.algovis.algorithms.sorting.SelectionSort;
import com.algovis.algorithms.tree.AVLInsert;
import com.algovis.algorithms.tree.BSTDelete;
import com.algovis.algorithms.tree.BSTInsert;
import com.algovis.algorithms.tree.Traversals;

public final class RegistryBootstrap {
    static {
        // Sorting
        AlgorithmRegistry.register(AlgorithmId.BUBBLE_SORT, BubbleSort::new,
                new AlgorithmRegistry.Metadata("Bubble Sort", "Sorting", false, "O(n^2)", "O(n^2)", "O(1)"));
        AlgorithmRegistry.register(AlgorithmId.SELECTION_SORT, SelectionSort::new,
                new AlgorithmRegistry.Metadata("Selection Sort", "Sorting", false, "O(n^2)", "O(n^2)", "O(1)"));
        AlgorithmRegistry.register(AlgorithmId.INSERTION_SORT, InsertionSort::new,
                new AlgorithmRegistry.Metadata("Insertion Sort", "Sorting", false, "O(n^2)", "O(n)", "O(1)"));
        AlgorithmRegistry.register(AlgorithmId.MERGE_SORT, MergeSort::new,
                new AlgorithmRegistry.Metadata("Merge Sort", "Sorting", false, "O(n log n)", "O(n log n)", "O(n)"));
        AlgorithmRegistry.register(AlgorithmId.QUICK_SORT, QuickSort::new,
                new AlgorithmRegistry.Metadata("Quick Sort", "Sorting", false, "O(n log n)", "O(n^2)", "O(log n)"));
        AlgorithmRegistry.register(AlgorithmId.HEAP_SORT, HeapSort::new,
                new AlgorithmRegistry.Metadata("Heap Sort", "Sorting", false, "O(n log n)", "O(n log n)", "O(1)"));

        // Graph
        AlgorithmRegistry.register(AlgorithmId.BFS, BFS::new,
                new AlgorithmRegistry.Metadata("BFS", "Graph", false, "O(V+E)", "O(V+E)", "O(V)"));
        AlgorithmRegistry.register(AlgorithmId.DFS, DFS::new,
                new AlgorithmRegistry.Metadata("DFS", "Graph", false, "O(V+E)", "O(V+E)", "O(V)"));
        AlgorithmRegistry.register(AlgorithmId.DIJKSTRA, Dijkstra::new,
                new AlgorithmRegistry.Metadata("Dijkstra", "Graph", false, "O(E log V)", "O(E log V)", "O(V)"));
        AlgorithmRegistry.register(AlgorithmId.PRIM, Prim::new,
                new AlgorithmRegistry.Metadata("Prim's MST", "Graph", false, "O(E log V)", "O(E log V)", "O(V)"));
        AlgorithmRegistry.register(AlgorithmId.KRUSKAL, Kruskal::new,
                new AlgorithmRegistry.Metadata("Kruskal's MST", "Graph", false, "O(E log E)", "O(E log E)", "O(V)"));
        AlgorithmRegistry.register(AlgorithmId.ASTAR, AStar::new,
                new AlgorithmRegistry.Metadata("A* Search", "Graph", false, "O(E)", "O(E)", "O(V)"));


        // Tree
        AlgorithmRegistry.register(AlgorithmId.BST_INSERT, BSTInsert::new,
                new AlgorithmRegistry.Metadata("BST Insert", "Tree", false, "O(h)", "O(n)", "O(h)"));
        AlgorithmRegistry.register(AlgorithmId.BST_DELETE, BSTDelete::new,
                new AlgorithmRegistry.Metadata("BST Delete", "Tree", false, "O(h)", "O(n)", "O(h)"));
        AlgorithmRegistry.register(AlgorithmId.TRAVERSALS, Traversals::new,
                new AlgorithmRegistry.Metadata("Tree Traversals", "Tree", false, "O(n)", "O(n)", "O(h)"));
        AlgorithmRegistry.register(AlgorithmId.AVL_INSERT, AVLInsert::new,
                new AlgorithmRegistry.Metadata("AVL Insert", "Tree", false, "O(log n)", "O(log n)", "O(h)"));
    }
    private RegistryBootstrap(){}
}