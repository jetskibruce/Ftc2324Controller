package org.firstinspires.ftc.teamcode.macros;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class MacroSequence {

    private static ElapsedTime executionTime = new ElapsedTime();

    private static PathStep executingMacro = null;

    private static double TIMEOUT_MS = 5000;

    public static void setTimeoutMs(double maxExecutionTime) {
        TIMEOUT_MS = maxExecutionTime;
    }

    public static boolean RunningMacro = false;

    private static void run(PathStep step) {
        executingMacro = step;
        executingMacro.start();
        RunningMacro = true;
        executionTime.reset();
    }

    public static void stopActiveMacro() {
        RunningMacro = false;
    }

    public static void tick(OpMode opMode) {
        if (executingMacro != null)  {
            if (RunningMacro) {
                executingMacro.onTick(opMode);
            }
            // force executing path step to stop if it exceeds TIMEOUT_MS
            if (executionTime.milliseconds() > TIMEOUT_MS) {
                executingMacro.finish();
            }
        }

    }

    public static MacroSequence compose(String label, PathStep... steps) {
        return new MacroSequence(label, steps);
    }

    public static void begin(String label, PathStep... steps) {
        compose(label, steps).start();
    }

    public static PathStep getActiveMacro() {
        return executingMacro;
    }
    public static boolean isRunning() {
        return RunningMacro;
    }

    public static void appendDebugTo(Telemetry telemetry) {
        if (executingMacro == null) return;
        if (executingMacro.hostPath != null) {
            telemetry.addData("Executing Sequence: ", executingMacro.hostPath.label);
            telemetry.addData("\tIs finished?", executingMacro.hostPath.Finished);
            telemetry.addData("\tIdx Progress: ", executingMacro.hostPath.getStepIndex() + " / " + executingMacro.hostPath.getNumSteps());
        }
        telemetry.addData("Executing Step: ", executingMacro.getClass().getSimpleName());
        telemetry.addData("Timeout: ", executionTime.milliseconds() + " / " + TIMEOUT_MS);

    }

    private PathStep[] steps;
    private int stepIndex = -1;
    public boolean Finished = false;

    private String label = "unlabeled";

    public MacroSequence(String label, PathStep[] steps) {
        this.label = label;
        this.steps = steps;
    }

    public void runNext() {
        if (Finished) throw new RuntimeException("Tried to run the next sequence step on a stopped sequence!");
        stepIndex++;
        if (stepIndex >= steps.length) {
            stop();
            return;
        }
        steps[stepIndex].setHostPath(this);
        MacroSequence.run(steps[stepIndex]);
    }

    public void stop() {
        Finished = true;
        MacroSequence.stopActiveMacro();
        return;
    }

    public void start() {
        stepIndex = -1;
        runNext();
    }

    public String getLabel() {
        return label;
    }

    public int getStepIndex() {
        return stepIndex;
    }

    public int getNumSteps() {
        return steps.length;
    }

}
