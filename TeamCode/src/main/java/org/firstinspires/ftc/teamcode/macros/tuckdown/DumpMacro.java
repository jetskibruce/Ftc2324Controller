package org.firstinspires.ftc.teamcode.macros.tuckdown;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.excutil.MotorPath;
import org.firstinspires.ftc.teamcode.excutil.coroutines.CoroutineResult;
import org.firstinspires.ftc.teamcode.macros.PathStep;
import org.firstinspires.ftc.teamcode.macros.RobotComponents;

public class DumpMacro extends PathStep {

    private static final double BUCKET_GOAL_POS = 0.31;

    private MotorPath upPath = null;

    @Override
    public void start() {
        super.start();



        upPath = MotorPath.runToPosition(RobotComponents.tower_motor, 980, 0.45);


    }

    private boolean goOnce = true;

    @Override
    public void tick(OpMode opMode) {
        if (upPath.isComplete(10)) {
            if (goOnce) {
                goOnce = false;

                RobotComponents.bucket_servo.setPosition(BUCKET_GOAL_POS);

                RobotComponents.coroutines.startRoutineLater((mode, d) -> {
                    finish();
                    return CoroutineResult.Stop;
                }, 150);
            }
        }
    }


}
