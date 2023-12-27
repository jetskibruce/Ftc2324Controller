package org.firstinspires.ftc.teamcode.macros.arm.up;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.excutil.MotorPath;
import org.firstinspires.ftc.teamcode.excutil.coroutines.CoroutineResult;
import org.firstinspires.ftc.teamcode.macros.PathStep;
import org.firstinspires.ftc.teamcode.components.RobotComponents;

public class ArmToDumpPointMacro extends PathStep {

    private static final double WRIST_GOAL_POS = 0.83;
    private static final double BUCKET_GOAL_POS = 0.50;

    MotorPath upPath;

    @Override
    public void onStart() {
        ;
        RobotComponents.bucket_servo.setPosition(BUCKET_GOAL_POS);

        upPath = MotorPath.runToPosition(RobotComponents.tower_motor, -1065, 0.6);
    }

    private boolean goOnce = true;

    @Override
    public void onTick(OpMode opMode) {
        if (upPath.isComplete(10)) {
            if (goOnce) {
                goOnce = false;

                RobotComponents.wrist_servo.setPosition(WRIST_GOAL_POS);

                RobotComponents.coroutines.startRoutineLater((mode, d) -> {
                    finish();
                    return CoroutineResult.Stop;
                }, 250);
            }
        }
    }

    @Override
    public void finish() {
        RobotComponents.back_intake_servo.setPower(0);
        RobotComponents.front_intake_motor.setPower(0);
        super.finish();
    }

}
