package org.firstinspires.ftc.teamcode.macros.newarm;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.components.RobotComponents;
import org.firstinspires.ftc.teamcode.excutil.MotorPath;
import org.firstinspires.ftc.teamcode.excutil.RMath;
import org.firstinspires.ftc.teamcode.macros.PathStep;

public class NewPrepareForLift extends PathStep {
    static int ARM_LIFT_POSITION = 0;
    static double WRIST_SERVO_POSITION;
    static double BUCKET_SERVO_POSITION;

    MotorPath upPath;

    @Override
    public void onStart() {
        upPath = MotorPath.runToPosition(RobotComponents.tower_motor, 400, 0.5);


        RobotComponents.coroutines.runLater(() -> {
            RobotComponents.wrist_servo.setPosition(WRIST_SERVO_POSITION);
            RobotComponents.bucket_servo.setPosition(BUCKET_SERVO_POSITION);
        }, 50);
    }

    @Override
    public void onTick(OpMode opMode) {
        if (upPath.isComplete(6)) {

        }
    }
}
