package org.firstinspires.ftc.teamcode.macros;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

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
    }

    public void setHostPath(MacroSequence path) {
        hostPath = path;
    }

    public void finish() {
        running = false;
        if (hostPath != null) {
            hostPath.runNext();
        }
    }

    public abstract void tick(OpMode opMode);

}
