package org.firstinspires.ftc.teamcode.macros.tuckdown;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robot.Robot;

import org.firstinspires.ftc.teamcode.excutil.RMath;
import org.firstinspires.ftc.teamcode.excutil.coroutines.CoroutineResult;
import org.firstinspires.ftc.teamcode.macros.Flag;
import org.firstinspires.ftc.teamcode.macros.PathStep;
import org.firstinspires.ftc.teamcode.macros.RobotComponents;

public class WristTuckMacro extends PathStep {

    private static final double BUCKET_SERVO_POS = 0.27;
    private static final double WRIST_SERVO_POS = 0.68;


    @Override
    public void start() {
        super.start();

        RobotComponents.bucket_servo.setPosition(BUCKET_SERVO_POS);
        RobotComponents.coroutines.startRoutineLater((mode, d) -> {
            RobotComponents.wrist_servo.setPosition(WRIST_SERVO_POS);
            return CoroutineResult.Stop;
        }, 50);
        RobotComponents.coroutines.startRoutineLater((mode, d) -> {
            finish();
            return CoroutineResult.Stop;
        }, 200);
    }

    @Override
    public void tick(OpMode opMode) {
    }

}
