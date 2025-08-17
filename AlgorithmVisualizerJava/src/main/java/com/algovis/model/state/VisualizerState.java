package com.algovis.model.state;

import java.util.Collections;
import java.util.Map;

import com.algovis.model.data.ArrayModel;
import com.algovis.model.data.GraphModel;
import com.algovis.model.data.TreeModel;
import com.algovis.model.step.AlgorithmStep;

public class VisualizerState {

    public enum Mode { NONE, ARRAY, GRAPH, TREE }

    private Mode mode = Mode.NONE;
    private ArrayModel array;
    private GraphModel graph;
    private TreeModel tree;

    private int stepIndex = 0;
    private String stepDescription = "";
    private Map<String, Object> currentHighlights = Collections.emptyMap();


    public void apply(AlgorithmStep step) {
        if (step == null) return;
        if (step.snapshot instanceof ArrayModel arr) { array = arr; mode = Mode.ARRAY; }
        if (step.snapshot instanceof GraphModel gr) { graph = gr; mode = Mode.GRAPH; }
        if (step.snapshot instanceof TreeModel tr)  { tree  = tr; mode = Mode.TREE;  }
        stepIndex++;
        stepDescription = step.description;
        currentHighlights = step.highlights;
    }

    public void resetToInput(Object input) {
        if (input instanceof ArrayModel arr) { array = arr.copy(); mode = Mode.ARRAY; }
        if (input instanceof GraphModel gr) { graph = gr.copy(); mode = Mode.GRAPH; }
        if (input instanceof TreeModel tr)  { tree  = tr.copy();  mode = Mode.TREE; }
        stepDescription = "Ready.";
        currentHighlights = Collections.emptyMap();
        stepIndex = 0;
    }

    public Mode getMode() { return mode; }
    public void setMode(Mode m) { this.mode = m; }

    public ArrayModel getArray() { return array; }
    public void setArray(ArrayModel a) { this.array = a; }

    public GraphModel getGraph() { return graph; }
    public void setGraph(GraphModel g) { this.graph = g; }

    public TreeModel getTree() { return tree; }
    public void setTree(TreeModel t) { this.tree = t; }

    public int getStepIndex() { return stepIndex; }
    public void setStepIndex(int i) { this.stepIndex = i; }

    public String getStepDescription() { return stepDescription; }
    public Map<String, Object> getCurrentHighlights() { return currentHighlights; }
}