package org.firstinspires.ftc.teamcode.macros.arm.down;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.excutil.MotorPath;
import org.firstinspires.ftc.teamcode.excutil.coroutines.CoroutineResult;
import org.firstinspires.ftc.teamcode.macros.PathStep;
import org.firstinspires.ftc.teamcode.components.RobotComponents;

public class TuckWristDownMacro extends PathStep {

    private static final double WRIST_GOAL_POS = 0.60;
    private static final double BUCKET_GOAL_POS = 0.38;

    MotorPath downPath;

    @Override
    public void onStart(){
        ;

        downPath = MotorPath.runToPosition(RobotComponents.tower_motor, -20, 0.3);

        RobotComponents.coroutines.runLater(() -> {
            RobotComponents.wrist_servo.setPosition(0.62);
            RobotComponents.bucket_servo.setPosition(0);
        }, 50);

        RobotComponents.coroutines.startRoutineLater((mode, d) -> {
            finish();
            return CoroutineResult.Stop;
        }, 120);
    }

    @Override
    public void onTick(OpMode opMode) {

    }


}
