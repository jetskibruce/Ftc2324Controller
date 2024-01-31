package org.firstinspires.ftc.teamcode.macros.generic;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.macros.PathStep;

import java.util.function.Predicate;

public class AwaitConditionMacro extends PathStep {

    private Predicate<OpMode> condition;

    public AwaitConditionMacro(Predicate<OpMode> condition) {
        this.condition = condition;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onTick(OpMode opMode) {
        if (condition.test(opMode)) {
            finish();
        }
    }

    @Override
    public boolean canTimeout() {
        return false;
    }
}
