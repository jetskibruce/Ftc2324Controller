package org.firstinspires.ftc.teamcode.macros.auto;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.excutil.MotorPath;
import org.firstinspires.ftc.teamcode.excutil.coroutines.CoroutineResult;
import org.firstinspires.ftc.teamcode.macros.PathStep;
import org.firstinspires.ftc.teamcode.components.RobotComponents;

public class AutoOuttakePoseMacro extends PathStep {

    private static final double WRIST_GOAL_POS = 0.62;
    private static final double BUCKET_GOAL_POS = 0.0;

    MotorPath upPath = null;

    @Override
    public void onStart(){
        ;

        upPath = MotorPath.runToPosition(RobotComponents.tower_motor, 240, 0.7);


    }

    boolean startOnce = true;

    @Override
    public void onTick(OpMode opMode) {
        if (startOnce && upPath.isComplete(8)) {
            startOnce = false;

            RobotComponents.coroutines.startRoutineLater((mode, d) -> {
                RobotComponents.wrist_servo.setPosition(0.46);

                return CoroutineResult.Stop;
            }, 50);

            RobotComponents.coroutines.startRoutineLater((mode, d) -> {
                finish();
                return CoroutineResult.Stop;
            }, 200);
        }
    }


}
