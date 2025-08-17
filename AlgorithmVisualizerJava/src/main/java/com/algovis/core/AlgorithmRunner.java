package com.algovis.core;

import com.algovis.model.state.VisualizerState;
import com.algovis.model.step.AlgorithmStep;

public class AlgorithmRunner {
    private final VisualizerState state;
    private final AnimationScheduler scheduler = new AnimationScheduler();
    private Runnable onStepApplied = () -> {};

    private Algorithm<Object> algorithm;
    private Object input;

    public AlgorithmRunner(VisualizerState state) {
        this.state = state;
    }

    public void setSpeed(double speed) { scheduler.setSpeed(speed); }

    @SuppressWarnings("unchecked")
    public void load(Algorithm<?> algo, Object input, Runnable onStepApplied) {
        this.algorithm = (Algorithm<Object>) algo;
        this.input = input;
        this.onStepApplied = onStepApplied != null ? onStepApplied : () -> {};
        this.algorithm.init(this.input);
        state.resetToInput(this.input);
        state.setStepIndex(0);
        scheduler.pause();
    }

    public void togglePlay() {
        if (algorithm == null) return;
        if (scheduler.isRunning()) {
            scheduler.pause();
        } else {
            scheduler.play(this::stepOnce);
        }
    }

    public void stepOnce() {
        if (algorithm == null) return;
        if (algorithm.isDone()) { scheduler.pause(); return; }
        AlgorithmStep step = algorithm.nextStep();
        if (step != null) {
            state.apply(step);
            onStepApplied.run();
        } else if (algorithm.isDone()) {
            scheduler.pause();
        }
    }

    public void stepBack() {
        if (algorithm == null) return;
        scheduler.pause();

        int current = state.getStepIndex();
        int target = Math.max(0, current - 1);

        // Re-init algorithm and state to the original input
        this.algorithm.init(this.input);
        state.resetToInput(input);
        state.setStepIndex(0);

        // Replay forward to the target step
        for (int i = 0; i < target; i++) {
            AlgorithmStep s = algorithm.nextStep();
            if (s == null) break;
            state.apply(s);
        }

        if (onStepApplied != null) onStepApplied.run();
    }

    public void reset() {
        if (algorithm == null) return;
        scheduler.pause();
        this.algorithm.init(this.input);
        state.resetToInput(input);
        state.setStepIndex(0);
    }

    // ---- Added methods to make data buttons responsive ----

    public boolean hasAlgorithm() {
        return algorithm != null;
    }

    public void reloadWithNewInput(Object newInput) {
        if (algorithm == null) return;
        scheduler.pause();
        this.input = newInput;
        this.algorithm.init(this.input);
        state.resetToInput(this.input);
        state.setStepIndex(0);
        if (onStepApplied != null) onStepApplied.run();
    }
}
