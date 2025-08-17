package com.algovis.model.step;

import java.util.Map;

public class AlgorithmStep {
    public final String title;
    public final String description;
    public final Map<String, Object> highlights;
    public final Object snapshot;

    public AlgorithmStep(String title, String description, Map<String, Object> highlights, Object snapshot) {
        this.title = title;
        this.description = description;
        this.highlights = highlights;
        this.snapshot = snapshot;
    }
}