package org.firstinspires.ftc.teamcode.macros.tuckdown;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.excutil.coroutines.CoroutineResult;
import org.firstinspires.ftc.teamcode.macros.PathStep;
import org.firstinspires.ftc.teamcode.components.RobotComponents;

public class ArbitraryDelayMacro extends PathStep {

    private double delay;

    public ArbitraryDelayMacro(double msDelay) {
        delay = msDelay;
    }

    @Override
    public void onStart(){
        RobotComponents.coroutines.startRoutineLater((mode, d) -> {
            finish();
            return CoroutineResult.Stop;
        }, delay);
    }

    @Override
    public void onTick(OpMode opMode) {

    }

}
