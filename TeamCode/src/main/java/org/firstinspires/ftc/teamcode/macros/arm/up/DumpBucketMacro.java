package org.firstinspires.ftc.teamcode.macros.arm.up;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.excutil.MotorPath;
import org.firstinspires.ftc.teamcode.excutil.coroutines.CoroutineResult;
import org.firstinspires.ftc.teamcode.macros.PathStep;
import org.firstinspires.ftc.teamcode.components.RobotComponents;

public class DumpBucketMacro extends PathStep {

    private static final double BUCKET_GOAL_POS = 0.3;

    private MotorPath upPath = null;

    @Override
    public void onStart(){
        ;



        upPath = MotorPath.runToPosition(RobotComponents.tower_motor, -1110, 0.6);


    }

    private boolean goOnce = true;

    @Override
    public void onTick(OpMode opMode) {
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
