package org.firstinspires.ftc.teamcode.macros.tuckdown;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.excutil.MotorPath;
import org.firstinspires.ftc.teamcode.excutil.coroutines.CoroutineResult;
import org.firstinspires.ftc.teamcode.macros.PathStep;
import org.firstinspires.ftc.teamcode.macros.RobotComponents;

public class PrepDownMacro extends PathStep {

    private static final double WRIST_GOAL_POS = 0.85;
    private static final double BUCKET_GOAL_POS = 0.38;

    MotorPath downPath;

    @Override
    public void start() {
        super.start();

        downPath = MotorPath.runToPosition(RobotComponents.tower_motor, 400, 0.5);

        RobotComponents.coroutines.runLater(() -> {
            RobotComponents.wrist_servo.setPosition(0.52);
            RobotComponents.bucket_servo.setPosition(0.21);
        }, 50);

        RobotComponents.coroutines.startRoutineLater((mode, d) -> {
            finish();
            return CoroutineResult.Stop;
        }, 120);
    }

    @Override
    public void tick(OpMode opMode) {

    }


}
