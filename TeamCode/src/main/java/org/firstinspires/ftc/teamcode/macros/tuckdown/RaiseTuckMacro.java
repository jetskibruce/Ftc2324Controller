package org.firstinspires.ftc.teamcode.macros.tuckdown;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.robot.Robot;

import org.firstinspires.ftc.teamcode.excutil.MotorPath;
import org.firstinspires.ftc.teamcode.excutil.RMath;
import org.firstinspires.ftc.teamcode.excutil.coroutines.CoroutineResult;
import org.firstinspires.ftc.teamcode.macros.PathStep;
import org.firstinspires.ftc.teamcode.macros.RobotComponents;

public class RaiseTuckMacro extends PathStep {

    private static final int RAISE_GOAL_TICKS = 1000;//900;
    private static final double BUCKET_GOAL_POS = 0.11;

    public MotorPath raisePath = null;

    @Override
    public void start() {
        RobotComponents.tower_motor.setPower(0.61);
        raisePath = MotorPath.runToPosition(RobotComponents.tower_motor, RAISE_GOAL_TICKS);
       // RobotComponents.bucket_servo.setPosition(BUCKET_GOAL_POS);

        RobotComponents.coroutines.runLater(() -> {
         //   RobotComponents.bucket_servo.setPosition(BUCKET_GOAL_POS * 3);
        }, 1200);
    }

    @Override
    public void tick(OpMode opMode) {
        if (raisePath.isComplete(20)) {
            finish();

        }
        opMode.telemetry.addData("Tower Motor Status: ",  raisePath.toString() + " / " + raisePath.isComplete(20));
    }
}
