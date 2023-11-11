package org.firstinspires.ftc.teamcode.macros.tuckdown;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.robot.Robot;

import org.firstinspires.ftc.teamcode.excutil.coroutines.CoroutineResult;
import org.firstinspires.ftc.teamcode.macros.PathStep;
import org.firstinspires.ftc.teamcode.macros.RobotComponents;

public class IntakePoseMacro extends PathStep {

    private static final double WRIST_GOAL_POS = 0.64;
    private static final double BUCKET_GOAL_POS = 0.12;

    @Override
    public void start() {
        super.start();


        RobotComponents.coroutines.startRoutineLater((mode, d) -> {
            finish();
            return CoroutineResult.Stop;
        }, 400);
    }

    @Override
    public void tick(OpMode opMode) {

    }


}
