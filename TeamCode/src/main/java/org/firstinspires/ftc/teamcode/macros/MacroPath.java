package org.firstinspires.ftc.teamcode.macros;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.List;

public class MacroPath {

    private static PathStep executingMacro = null;

    public static void run(PathStep step) {
        executingMacro = step;
        executingMacro.start();
    }

    public static void stop() {
        executingMacro = null;
    }

    public static void tick(OpMode opMode) {
        if (executingMacro != null) executingMacro.tick(opMode);
    }

    public static MacroPath compose(PathStep... steps) {
        return new MacroPath(steps);
    }

    public static PathStep getExecutingMacro() {
        return executingMacro;
    }

    private PathStep[] steps;
    private int stepIndex = -1;
    public boolean Finished = false;

    public MacroPath(PathStep[] steps) {
        this.steps = steps;
    }

    public void runNext() {
        stepIndex++;
        if (stepIndex >= steps.length) {
            Finished = true;
            return;
        }
        steps[stepIndex].setHostPath(this);
        MacroPath.run(steps[stepIndex]);
    }

    public void start() {
        stepIndex = -1;
        runNext();
    }

}
