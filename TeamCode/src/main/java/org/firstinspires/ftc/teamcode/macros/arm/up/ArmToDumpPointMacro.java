package org.firstinspires.ftc.teamcode.macros.arm.up;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.excutil.MotorPath;
import org.firstinspires.ftc.teamcode.excutil.coroutines.CoroutineResult;
import org.firstinspires.ftc.teamcode.macros.PathStep;
import org.firstinspires.ftc.teamcode.components.RobotComponents;

public class ArmToDumpPointMacro extends PathStep {

    private static final double WRIST_GOAL_POS = 0.9;
    private static final double BUCKET_GOAL_POS = 0.43;

    public static final int DEFAULT_TUNED_ARM_POS = -1145;
    public static int TUNED_ARM_POS = DEFAULT_TUNED_ARM_POS;

    MotorPath upPath;

    @Override
    public void onStart() {

        //RobotComponents.bucket_servo.setPosition(BUCKET_GOAL_POS);

        upPath = runToTunedArmPos(0.6);
    }

    public static MotorPath runToTunedArmPos(double power) {
        return MotorPath.runToPosition(RobotComponents.tower_motor, -1105, power);
    }

    private boolean ranServosYet = false;

    @Override
    public void onTick(OpMode opMode) {
        if (!ranServosYet && upPath.isComplete(10)) {
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
