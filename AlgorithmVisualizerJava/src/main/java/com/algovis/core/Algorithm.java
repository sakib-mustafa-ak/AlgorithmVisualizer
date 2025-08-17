package com.algovis.core;

import com.algovis.model.step.AlgorithmStep;

public interface Algorithm<I> {
    void init(I input);
    boolean isDone();
    AlgorithmStep nextStep();
    void reset();
}