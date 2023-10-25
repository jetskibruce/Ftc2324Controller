package org.firstinspires.ftc.teamcode.excutil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class EncodedPath {

    private List<Predicate[]> steps = new ArrayList<>();

    private Predicate[] currentlyExecutingSteps = new Predicate[0];

    public int numExecutionTicks = 0;

    public boolean onFirstExecutionTick() {
        return numExecutionTicks == 0;
    }

    private int executionStepIndex = 0;

    public void finish(Predicate<EncodedPath> action) {
        finish(new Predicate[] {action});
    }

    public void finish(Predicate<EncodedPath>... actions) {
        steps.add(actions);
    }

    public void tick() {
        boolean nextStep = true;
        for (Predicate<EncodedPath> predicate : currentlyExecutingSteps) {
            nextStep = nextStep && predicate.test(this);
        }

        if (nextStep) {
            currentlyExecutingSteps = steps.get(executionStepIndex);
            executionStepIndex++;
            numExecutionTicks = 0;
        } else {
            numExecutionTicks++;
        }
    }


}
