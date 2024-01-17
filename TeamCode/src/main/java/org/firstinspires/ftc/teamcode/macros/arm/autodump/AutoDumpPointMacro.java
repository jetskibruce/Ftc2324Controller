package org.firstinspires.ftc.teamcode.macros.arm.autodump;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.components.RobotComponents;
import org.firstinspires.ftc.teamcode.excutil.MotorPath;
import org.firstinspires.ftc.teamcode.excutil.coroutines.CoroutineResult;
import org.firstinspires.ftc.teamcode.macros.PathStep;

public class AutoDumpPointMacro extends PathStep {

    private static final double WRIST_GOAL_POS = 0.6;
    private static final double BUCKET_GOAL_POS = 0.43;

    public static final int ARM_GOAL_POS = -1605;

    MotorPath upPath;

    @Override
    public void onStart() {

        //RobotComponents.bucket_servo.setPosition(BUCKET_GOAL_POS);

        upPath = MotorPath.runToPosition(RobotComponents.tower_motor, ARM_GOAL_POS, 0.4);
    }


    private boolean ranServosYet = false;

    @Override
    public void onTick(OpMode opMode) {

        if (!ranServosYet && upPath.isComplete(50, 1000)) {
            ranServosYet = true;

                RobotComponents.bucket_servo.setPosition(BUCKET_GOAL_POS);
                RobotComponents.wrist_servo.setPosition(WRIST_GOAL_POS);

                RobotComponents.coroutines.startRoutineLater((mode, d) -> {
                    finish();
                    return CoroutineResult.Stop;
                }, 200);

        }
    }

    @Override
    public void finish() {
        RobotComponents.back_intake_servo.setPower(0);
        RobotComponents.front_intake_motor.setPower(0);
        super.finish();
    }

}
