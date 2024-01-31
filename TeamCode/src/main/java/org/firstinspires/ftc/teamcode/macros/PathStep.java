package org.firstinspires.ftc.teamcode.macros;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.function.Predicate;

public abstract class PathStep {

    private PathStep nextStep = null;
    protected boolean running = false;
    protected MacroSequence hostPath = null;

    public PathStep() {
        this(null);
    }

    public PathStep(PathStep then) {
        nextStep = then;
    }

    public void start() {
        running = true;
        onStart();
    }

    public abstract void onStart();

    public void setHostPath(MacroSequence path) {
        hostPath = path;
    }

    public void finish() {
        running = false;
        if (hostPath != null && !hostPath.Finished) {
            hostPath.runNext();
        }
    }

    public abstract void onTick(OpMode opMode);

    public boolean canTimeout() {
        return true;
    }

}
