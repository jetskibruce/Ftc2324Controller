package org.firstinspires.ftc.teamcode.macros;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class MacroSequence {

    private static PathStep executingMacro = null;

    public static boolean RunningMacro = false;

    private static void run(PathStep step) {
        executingMacro = step;
        executingMacro.start();
        RunningMacro = true;
    }

    public static void stopActiveMacro() {
        executingMacro = null;
        RunningMacro = false;
    }

    public static void tick(OpMode opMode) {
        if (executingMacro != null) executingMacro.tick(opMode);
    }

    public static MacroSequence compose(PathStep... steps) {
        return new MacroSequence(steps);
    }

    public static PathStep getExecutingMacro() {
        return executingMacro;
    }

    private PathStep[] steps;
    private int stepIndex = -1;
    public boolean Finished = false;

    public MacroSequence(PathStep[] steps) {
        this.steps = steps;
    }

    public void runNext() {
        stepIndex++;
        if (stepIndex >= steps.length) {
            Finished = true;
            MacroSequence.stopActiveMacro();
            return;
        }
        steps[stepIndex].setHostPath(this);
        MacroSequence.run(steps[stepIndex]);
    }

    public void start() {
        stepIndex = -1;
        runNext();
    }

}
