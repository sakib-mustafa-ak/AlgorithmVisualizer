package com.algovis.core;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class AnimationScheduler {
    private Timeline timeline = new Timeline();
    private double speed = 2.0; // Steps per second
    private Runnable currentTick = null;

    public void setSpeed(double speed) {
        this.speed = speed;
        if (isRunning()) {
            play(currentTick); // Restart with new speed
        }
    }

    public void play(Runnable tick) {
        currentTick = tick;
        if (timeline != null) timeline.stop();
        if (tick == null) return;
        timeline = new Timeline(new KeyFrame(Duration.seconds(1.0 / speed), e -> tick.run()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void pause() {
        if (timeline != null) timeline.pause();
    }

    public boolean isRunning() {
        return timeline != null && timeline.getStatus() == Timeline.Status.RUNNING;
    }
}