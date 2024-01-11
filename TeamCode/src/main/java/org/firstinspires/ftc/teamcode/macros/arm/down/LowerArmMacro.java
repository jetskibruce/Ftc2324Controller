package org.firstinspires.ftc.teamcode.macros.arm.down;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.excutil.MotorPath;
import org.firstinspires.ftc.teamcode.excutil.coroutines.CoroutineResult;
import org.firstinspires.ftc.teamcode.macros.PathStep;
import org.firstinspires.ftc.teamcode.components.RobotComponents;

public class LowerArmMacro extends PathStep {


    private static final double WRIST_GOAL_POS = 0.54;
    private static final double BUCKET_GOAL_POS = 0.16;

    MotorPath downPath;

    @Override
    public void onStart() {

        downPath = MotorPath.runToPosition(RobotComponents.tower_motor, -240, 0.55);


    }

        ;

    private boolean ranServosYet = false;

    @Override
    public void onTick(OpMode opMode) {
        if (!ranServosYet && downPath.isComplete(10)) {
            ranServosYet = true;

            RobotComponents.wrist_servo.setPosition(WRIST_GOAL_POS);
            RobotComponents.bucket_servo.setPosition(BUCKET_GOAL_POS);

            RobotComponents.coroutines.startRoutineLater((mode, d) -> {
                finish();
                return CoroutineResult.Stop;
            }, 130);
        }
    }


}
