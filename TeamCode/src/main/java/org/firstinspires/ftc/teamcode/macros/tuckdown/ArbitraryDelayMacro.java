package org.firstinspires.ftc.teamcode.macros.tuckdown;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.excutil.coroutines.CoroutineResult;
import org.firstinspires.ftc.teamcode.macros.PathStep;
import org.firstinspires.ftc.teamcode.macros.RobotComponents;

public class ArbitraryDelayMacro extends PathStep {

    private double delay;

    public ArbitraryDelayMacro(double msDelay) {
        delay = msDelay;
    }

    @Override
    public void start() {
        RobotComponents.coroutines.startRoutineLater((mode, d) -> {
            finish();
            return CoroutineResult.Stop;
        }, delay);
    }

    @Override
    public void tick(OpMode opMode) {

    }

}
