package com.algovis.controller;

import java.util.Random;

import com.algovis.core.AlgorithmId;
import com.algovis.core.AlgorithmRegistry;
import com.algovis.core.AlgorithmRunner;
import com.algovis.model.data.ArrayModel;
import com.algovis.model.data.GraphModel;
import com.algovis.model.data.TreeModel;
import com.algovis.model.state.VisualizerState;
import com.algovis.pseudo.PseudocodeRepository;
import com.algovis.viewrender.ArrayRenderer;
import com.algovis.viewrender.GraphRenderer;
import com.algovis.viewrender.TreeRenderer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class MainController {

    @FXML private Canvas canvas;
    @FXML private Slider arraySizeSlider;
    @FXML private TextField treeCsvField;
    @FXML private TextArea cppArea, pyArea;
    @FXML private Label bigOAvg, bigOWorst, spaceComplexity;
    @FXML private Label statusLabel, metricsLabel;
    @FXML private Slider speedSlider;

    private final VisualizerState state = new VisualizerState();
    private final AlgorithmRunner runner = new AlgorithmRunner(state);
    private final ArrayRenderer arrayRenderer = new ArrayRenderer();
    private final GraphRenderer graphRenderer = new GraphRenderer();
    private final TreeRenderer treeRenderer = new TreeRenderer();

    private boolean gridVisible = false;

    @FXML
    public void initialize() {
        canvas.widthProperty().addListener((obs, o, n) -> draw());
        canvas.heightProperty().addListener((obs, o, n) -> draw());
        speedSlider.valueProperty().addListener((obs, o, n) -> runner.setSpeed(n.doubleValue()));

        canvas.sceneProperty().addListener((obs, old, sc) -> {
            if (sc != null) {
                sc.setOnKeyPressed(e -> {
                    if (e.getCode() == KeyCode.SPACE) onPlayPause(null);
                    if (e.getCode() == KeyCode.RIGHT) onStep(null);
                    if (e.getCode() == KeyCode.LEFT) onPrev(null);
                    if (e.getCode() == KeyCode.R) onReset(null);
                });
            }
        });

        onRandomizeArray(null);
        status("Ready. Select an algorithm to begin.");
    }

    private void status(String s) { statusLabel.setText(s); }

    private void drawGrid(GraphicsContext gc, double w, double h) {
        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(0.5);
        for (double x = 0; x < w; x += 20) gc.strokeLine(x, 0, x, h);
        for (double y = 0; y < h; y += 20) gc.strokeLine(0, y, w, y);
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        if (gridVisible) drawGrid(gc, canvas.getWidth(), canvas.getHeight());

        switch (state.getMode()) {
            case ARRAY -> arrayRenderer.render(gc, canvas.getWidth(), canvas.getHeight(), state);
            case GRAPH -> graphRenderer.render(gc, canvas.getWidth(), canvas.getHeight(), state);
            case TREE  -> treeRenderer.render(gc, canvas.getWidth(), canvas.getHeight(), state);
            default    -> {}
        }
    }

    private void loadPseudo(AlgorithmId id) {
        cppArea.setText(PseudocodeRepository.cpp(id));
        pyArea.setText(PseudocodeRepository.python(id));
        var meta = AlgorithmRegistry.metadata(id);
        if (meta != null) {
            bigOAvg.setText("Average: " + meta.bigOAvg());
            bigOWorst.setText("Worst: " + meta.bigOWorst());
            spaceComplexity.setText("Space: " + meta.space());
        } else {
            bigOAvg.setText("");
            bigOWorst.setText("");
            spaceComplexity.setText("");
        }
    }

    private void loadAndRun(AlgorithmId id, Object input) {
        var algo = AlgorithmRegistry.create(id);
        runner.load(algo, input, this::onStepApplied);
        loadPseudo(id);
        onStepApplied(); // To draw initial state
        draw();
    }

    private void onStepApplied() {
        metricsLabel.setText("Step: " + state.getStepIndex());
        status(state.getStepDescription());
        draw();
    }

    // Toolbar controls
    @FXML public void onPlayPause(ActionEvent e) { runner.togglePlay(); }
    @FXML public void onStep(ActionEvent e)      { runner.stepOnce(); }
    @FXML public void onPrev(ActionEvent e)      { runner.stepBack(); }
    @FXML public void onReset(ActionEvent e)     { runner.reset(); onStepApplied(); }

    // ---------- Ensure inputs for modes ----------
    private ArrayModel ensureArray() {
        if (state.getArray() == null) {
            int n = Math.max(5, (int) arraySizeSlider.getValue());
            int[] arr = new int[n];
            Random rnd = new Random();
            for (int i = 0; i < n; i++) arr[i] = rnd.nextInt(100) + 1;
            state.setArray(new ArrayModel(arr));
        }
        state.setMode(VisualizerState.Mode.ARRAY);
        return state.getArray();
    }

    private GraphModel ensureGraph() {
        if (state.getGraph() == null) {
            state.setGraph(GraphModel.random(16, 0.25, true));
        }
        state.setMode(VisualizerState.Mode.GRAPH);
        return state.getGraph();
    }

    /**
     * Ensure a TreeModel exists. If the user provided CSV, use it as LEVEL-ORDER.
     * Otherwise, build the tree from the CURRENT ARRAY (level-order).
     */
    private TreeModel ensureTree() {
        if (state.getTree() == null) {
            String csv = (treeCsvField != null) ? treeCsvField.getText() : null;
            if (csv != null && !csv.isBlank()) {
                state.setTree(TreeModel.fromCsv(csv)); // level-order from CSV
            } else if (state.getArray() != null) {
                // Build from the array currently shown in the left panel (level-order)
                int n = state.getArray().size();
                int[] arr = new int[n];
                for (int i = 0; i < n; i++) arr[i] = state.getArray().get(i);
                state.setTree(TreeModel.fromArrayLevelOrder(arr));
            } else {
                // Fallback: a small sample so the UI has something
                state.setTree(TreeModel.fromCsv("8,3,10,1,6,14,4,7,13"));
            }
        }
        state.setMode(VisualizerState.Mode.TREE);
        return state.getTree();
    }

    // ---------- Sorting ----------
    @FXML public void onAlgorithmBubbleSort(ActionEvent e)    { loadAndRun(AlgorithmId.BUBBLE_SORT,    ensureArray()); }
    @FXML public void onAlgorithmInsertionSort(ActionEvent e) { loadAndRun(AlgorithmId.INSERTION_SORT, ensureArray()); }
    @FXML public void onAlgorithmSelectionSort(ActionEvent e) { loadAndRun(AlgorithmId.SELECTION_SORT, ensureArray()); }
    @FXML public void onAlgorithmMergeSort(ActionEvent e)     { loadAndRun(AlgorithmId.MERGE_SORT,     ensureArray()); }
    @FXML public void onAlgorithmQuickSort(ActionEvent e)     { loadAndRun(AlgorithmId.QUICK_SORT,     ensureArray()); }
    @FXML public void onAlgorithmHeapSort(ActionEvent e)      { loadAndRun(AlgorithmId.HEAP_SORT,      ensureArray()); }

    // ---------- Graph ----------
    @FXML public void onAlgorithmBFS(ActionEvent e)      { loadAndRun(AlgorithmId.BFS,      ensureGraph()); }
    @FXML public void onAlgorithmDFS(ActionEvent e)      { loadAndRun(AlgorithmId.DFS,      ensureGraph()); }
    @FXML public void onAlgorithmDijkstra(ActionEvent e) { loadAndRun(AlgorithmId.DIJKSTRA, ensureGraph()); }
    @FXML public void onAlgorithmAStar(ActionEvent e)    { loadAndRun(AlgorithmId.ASTAR,    ensureGraph()); }
    @FXML public void onAlgorithmKruskal(ActionEvent e)  { loadAndRun(AlgorithmId.KRUSKAL,  ensureGraph()); }
    @FXML public void onAlgorithmPrim(ActionEvent e)     { loadAndRun(AlgorithmId.PRIM,     ensureGraph()); }

    // ---------- Tree ----------
    @FXML public void onAlgorithmBSTInsert(ActionEvent e)  { loadAndRun(AlgorithmId.BST_INSERT,  ensureTree()); }
    @FXML public void onAlgorithmBSTDelete(ActionEvent e)  { loadAndRun(AlgorithmId.BST_DELETE,  ensureTree()); }
    @FXML public void onAlgorithmTraversals(ActionEvent e) { loadAndRun(AlgorithmId.TRAVERSALS,  ensureTree()); }
    @FXML public void onAlgorithmAVLInsert(ActionEvent e)  { loadAndRun(AlgorithmId.AVL_INSERT,  ensureTree()); }

    // ---------- Data & Presets ----------
    @FXML public void onRandomizeArray(ActionEvent e) {
        int n = (int) arraySizeSlider.getValue();
        int[] arr = new int[n];
        Random rnd = new Random();
        for (int i = 0; i < n; i++) arr[i] = rnd.nextInt(100) + 1;

        ArrayModel newArray = new ArrayModel(arr);
        state.setArray(newArray);
        state.setMode(VisualizerState.Mode.ARRAY);

        if (runner.hasAlgorithm()) {
            runner.reloadWithNewInput(newArray);
        } else {
            draw();
        }
        status("Array randomized.");
    }

    // @FXML public void onShowPresets(ActionEvent e) {
    //     Alert alert = new Alert(Alert.AlertType.INFORMATION);
    //     alert.setTitle("Presets");
    //     alert.setHeaderText("Data Presets");
    //     alert.setContentText("Preset functionality is not implemented yet. Please use the random data generators.");
    //     alert.showAndWait();
    // }

    @FXML public void onGraphRandom(ActionEvent e) {
        GraphModel g = GraphModel.random(16, 0.2, true);
        state.setGraph(g);
        state.setMode(VisualizerState.Mode.GRAPH);

        if (runner.hasAlgorithm()) {
            runner.reloadWithNewInput(g);
        } else {
            draw();
        }
        status("Random graph generated.");
    }

    @FXML public void onGraphGrid(ActionEvent e) {
        GraphModel g = GraphModel.grid(4, 4);
        state.setGraph(g);
        state.setMode(VisualizerState.Mode.GRAPH);

        if (runner.hasAlgorithm()) {
            runner.reloadWithNewInput(g);
        } else {
            draw();
        }
        status("Grid graph loaded.");
    }

    @FXML public void onLoadTreeCsv(ActionEvent e) {
        var txt = (treeCsvField != null) ? treeCsvField.getText() : null;

        TreeModel t;
        if (txt != null && !txt.isBlank()) {
            // Use the exact numbers AS-IS in level order
            t = TreeModel.fromCsv(txt);
        } else if (state.getArray() != null) {
            // If user didn't type CSV, build the tree directly from the current array (level-order)
            int n = state.getArray().size();
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) arr[i] = state.getArray().get(i);
            t = TreeModel.fromArrayLevelOrder(arr);
        } else {
            t = TreeModel.fromCsv("8,3,10,1,6,14,4,7,13");
            if (treeCsvField != null) treeCsvField.setText("8,3,10,1,6,14,4,7,13");
        }

        state.setTree(t);
        state.setMode(VisualizerState.Mode.TREE);

        if (runner.hasAlgorithm()) {
            runner.reloadWithNewInput(t);
        } else {
            draw();
        }
        status("Tree loaded.");
    }

    @FXML public void onToggleGrid(ActionEvent e) {
        gridVisible = !gridVisible;
        draw();
    }
    // @FXML public void onAbout(ActionEvent e) { status("Algorithm Visualizer — JavaFX. Keyboard: Space/Left/Right/R."); }
}
