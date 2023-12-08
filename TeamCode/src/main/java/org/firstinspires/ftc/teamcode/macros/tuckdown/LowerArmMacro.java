package org.firstinspires.ftc.teamcode.macros.tuckdown;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.excutil.MotorPath;
import org.firstinspires.ftc.teamcode.macros.PathStep;
import org.firstinspires.ftc.teamcode.components.RobotComponents;

public class LowerArmMacro extends PathStep {

    private static final int LOWER_GOAL_TICKS = 0;//900;
    private static final double BUCKET_GOAL_POS = 0.11;

    public MotorPath raisePath = null;

    @Override
    public void onStart(){
        RobotComponents.tower_motor.setPower(0.61);
        raisePath = MotorPath.runToPosition(RobotComponents.tower_motor, LOWER_GOAL_TICKS, 0.61);
       // RobotComponents.bucket_servo.setPosition(BUCKET_GOAL_POS);

        RobotComponents.coroutines.runLater(() -> {
         //   RobotComponents.bucket_servo.setPosition(BUCKET_GOAL_POS * 3);
        }, 1200);
    }

    @Override
    public void onTick(OpMode opMode) {
        if (raisePath.isComplete(20)) {
            finish();

        }
        opMode.telemetry.addData("Tower Motor Status: ",  raisePath.toString() + " / " + raisePath.isComplete(20));
    }
}
