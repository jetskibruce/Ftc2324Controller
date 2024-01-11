package org.firstinspires.ftc.teamcode.macros.arm.down;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.excutil.MotorPath;
import org.firstinspires.ftc.teamcode.excutil.coroutines.CoroutineResult;
import org.firstinspires.ftc.teamcode.macros.PathStep;
import org.firstinspires.ftc.teamcode.components.RobotComponents;

public class TuckWristDownMacro extends PathStep {

    private static final double WRIST_GOAL_POS = 0.54;
    private static final double BUCKET_GOAL_POS = 0.227;
    int intakeOut = 1;

    MotorPath downPath;

    @Override
    public void onStart(){
        ;

        //RobotComponents.back_intake_servo.setPower(intakeOut);
        //RobotComponents.front_intake_motor.setPower(intakeOut);

        downPath = MotorPath.runToPosition(RobotComponents.tower_motor, -135, 0.35);




    }

    private boolean ranServosYet = false;

    @Override
    public void onTick(OpMode opMode) {
        if (!ranServosYet && downPath.isComplete(5)) {
            ranServosYet = true;

            RobotComponents.coroutines.runLater(() -> {
                RobotComponents.wrist_servo.setPosition(WRIST_GOAL_POS);
                RobotComponents.coroutines.runLater(() -> {
                    RobotComponents.bucket_servo.setPosition(BUCKET_GOAL_POS);
                }, 40);
            }, 40);

            RobotComponents.coroutines.runLater(() -> {
                finish();
            }, 200);
        }
    }


}
