package org.firstinspires.ftc.teamcode.excutil;

import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Deprecated
/**
 * "Do not use, rely on new macro system instead"
 */
public class EncodedPath {

    private boolean started = false;

    public boolean isStarted() {
        return started;
    }

    private List<Predicate[]> steps = new ArrayList<>();

    private Predicate[] currentlyExecutingSteps = new Predicate[0];

    public int numExecutionTicks = 0;

    public long executionStartTimestamp = 0;

    public boolean isFirstExecutionTick() {
        return numExecutionTicks == 0;
    }

    public boolean timedOut(long after) {
        return false;//(System.currentTimeMillis() - executionStartTimestamp) >= after;
    }

    private int executionStepIndex = 0;

    public int getStepIndex() {
        return executionStepIndex;
    }

    public EncodedPath finish(Predicate<EncodedPath> action) {
        finish(new Predicate[] {action});
        return this;
    }

    public EncodedPath finish(Predicate<EncodedPath>... actions) {
        steps.add(actions);
        return this;
    }

    public void tick() {
        if (!started) return;
        boolean nextStep = true;
        for (Predicate<EncodedPath> predicate : currentlyExecutingSteps) {
            nextStep = nextStep && predicate.test(this);
        }

        if (nextStep) {
            if (executionStepIndex == steps.size()) {
                stop();
                return;
            }
            executionStartTimestamp = System.currentTimeMillis();
            currentlyExecutingSteps = steps.get(executionStepIndex);
            executionStepIndex++;
            numExecutionTicks = 0;


        } else {
            numExecutionTicks++;
        }
    }

    public void start() {
        executionStartTimestamp = System.currentTimeMillis();
        executionStepIndex = 0;
        currentlyExecutingSteps = new Predicate[0];
        numExecutionTicks = 0;
        started = true;
    }

    public void stop() {
        started = false;

    }

}
