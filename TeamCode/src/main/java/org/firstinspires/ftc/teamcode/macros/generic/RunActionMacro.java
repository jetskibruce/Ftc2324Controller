package org.firstinspires.ftc.teamcode.macros.generic;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.macros.PathStep;

import java.util.function.Predicate;

public class RunActionMacro extends PathStep {
    private Predicate<Object> action = null;
    private Object state = null;
    private boolean keepRunning = true;

    public RunActionMacro(Predicate<Object> action) {
        this(action, null);
    }

    public RunActionMacro(Predicate<Object> action, Object state) {
        this.action = action;
        this.state = state;
    }

    @Override
    public void onStart() {
        keepRunning = action.test(state);
    }

    @Override
    public void onTick(OpMode opMode) {
        if (!keepRunning) {
            finish();
            return;
        }
        keepRunning = action.test(state);
    }
}
