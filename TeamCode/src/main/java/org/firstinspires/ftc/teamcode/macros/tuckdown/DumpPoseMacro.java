package org.firstinspires.ftc.teamcode.macros.tuckdown;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.excutil.coroutines.CoroutineResult;
import org.firstinspires.ftc.teamcode.macros.PathStep;
import org.firstinspires.ftc.teamcode.macros.RobotComponents;

public class DumpPoseMacro extends PathStep {

    private static final double WRIST_GOAL_POS = 0.85;
    private static final double BUCKET_GOAL_POS = 0.38;

    @Override
    public void start() {
        super.start();
        RobotComponents.wrist_servo.setPosition(WRIST_GOAL_POS);
        RobotComponents.bucket_servo.setPosition(BUCKET_GOAL_POS);

        RobotComponents.coroutines.startRoutineLater((mode, d) -> {
            finish();
            return CoroutineResult.Stop;
        }, 400);
    }

    @Override
    public void tick(OpMode opMode) {

    }


}
