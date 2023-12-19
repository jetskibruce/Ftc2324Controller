package org.firstinspires.ftc.teamcode.macros.arm.up;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.excutil.MotorPath;
import org.firstinspires.ftc.teamcode.excutil.coroutines.CoroutineResult;
import org.firstinspires.ftc.teamcode.macros.PathStep;
import org.firstinspires.ftc.teamcode.components.RobotComponents;

public class TuckWristForRiseMacro extends PathStep {

    private static final double WRIST_GOAL_POS = 0.73;
    private static final double BUCKET_GOAL_POS = 0.378;

    private MotorPath upPath = null;

    @Override
    public void onStart(){

        upPath = MotorPath.runToPosition(RobotComponents.tower_motor, -10, 0.6);

        RobotComponents.coroutines.runLater(() -> {
            RobotComponents.bucket_servo.setPosition(BUCKET_GOAL_POS);
        }, 80);

        RobotComponents.coroutines.startRoutineLater((mode, d) -> {
            RobotComponents.wrist_servo.setPosition(WRIST_GOAL_POS);
            return CoroutineResult.Stop;
        }, 120);

        RobotComponents.coroutines.startRoutineLater((mode, d) -> {
            finish();
            return CoroutineResult.Stop;
        }, 200);
    }

    @Override
    public void onTick(OpMode opMode) {

    }


}
