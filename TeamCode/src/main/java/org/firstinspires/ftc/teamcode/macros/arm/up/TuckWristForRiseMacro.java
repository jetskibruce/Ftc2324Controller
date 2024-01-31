package org.firstinspires.ftc.teamcode.macros.arm.up;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.robot.Robot;

import org.firstinspires.ftc.teamcode.excutil.MotorPath;
import org.firstinspires.ftc.teamcode.excutil.coroutines.CoroutineResult;
import org.firstinspires.ftc.teamcode.macros.PathStep;
import org.firstinspires.ftc.teamcode.components.RobotComponents;
import org.firstinspires.ftc.teamcode.opmodes.teleop.functional.CompDrive;

public class TuckWristForRiseMacro extends PathStep {


    int intakeIn = -1;

    private MotorPath upPath = null;

    @Override
    public void onStart(){
        //RobotComponents.back_intake_servo.setPower(intakeIn);
        //RobotComponents.front_intake_motor.setPower(intakeIn);

        RobotComponents.left_pixel_hold_servo.setPosition(0.95);
        RobotComponents.right_pixel_hold_servo.setPosition(0.95);

        upPath = MotorPath.runToPosition(RobotComponents.tower_motor, -100, 0.7);


    }

    private boolean ranServosYet = false;

    private static final double WRIST_GOAL_POS = 0.54;
    private static final double BUCKET_GOAL_POS = 0.1;

    @Override
    public void onTick(OpMode opMode) {
        if (!ranServosYet && upPath.isComplete(25, 1000)) {
            ranServosYet = true;

            RobotComponents.coroutines.runLater(() -> {
                RobotComponents.wrist_servo.setPosition(WRIST_GOAL_POS);

                RobotComponents.coroutines.runLater(() -> {
                    RobotComponents.bucket_servo.setPosition(BUCKET_GOAL_POS);

                    RobotComponents.extendo_servo.setPosition(CompDrive.ARM_RETRACT_POSITION);
                }, 80);
            }, 80);

            RobotComponents.coroutines.startRoutineLater((mode, d) -> {
                finish();
                return CoroutineResult.Stop;
            }, 190);
        }

    }


}
