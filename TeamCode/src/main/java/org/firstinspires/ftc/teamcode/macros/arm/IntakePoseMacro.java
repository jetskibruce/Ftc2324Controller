package org.firstinspires.ftc.teamcode.macros.arm;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.excutil.MotorPath;
import org.firstinspires.ftc.teamcode.excutil.coroutines.CoroutineResult;
import org.firstinspires.ftc.teamcode.macros.PathStep;
import org.firstinspires.ftc.teamcode.components.RobotComponents;

public class IntakePoseMacro extends PathStep {

    private static final double WRIST_GOAL_POS = 0.54;
    private static final double BUCKET_GOAL_POS = 0.227;

    MotorPath finishUp = null;

    @Override
    public void onStart(){
        ;


        finishUp = MotorPath.runToPosition(RobotComponents.tower_motor, -40, 0.4);

        RobotComponents.coroutines.runLater(() -> {
            RobotComponents.wrist_servo.setPosition(WRIST_GOAL_POS);
            RobotComponents.bucket_servo.setPosition(BUCKET_GOAL_POS);
        }, 20);

        RobotComponents.coroutines.startRoutineLater((mode, d) -> {
            finish();
            return CoroutineResult.Stop;
        }, 100);
    }

    @Override
    public void onTick(OpMode opMode) {

    }

    @Override
    public void finish() {
        RobotComponents.back_intake_servo.setPower(0);
        RobotComponents.front_intake_motor.setPower(0);
        super.finish();
    }
}
