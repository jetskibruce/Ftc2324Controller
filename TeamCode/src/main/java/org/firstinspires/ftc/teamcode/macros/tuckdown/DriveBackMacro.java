package org.firstinspires.ftc.teamcode.macros.tuckdown;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.excutil.coroutines.CoroutineResult;
import org.firstinspires.ftc.teamcode.macros.PathStep;
import org.firstinspires.ftc.teamcode.macros.RobotComponents;
import org.firstinspires.ftc.teamcode.opmodes.auto.DriveForwardAuto;

public class DriveBackMacro extends PathStep {

    private double delay;

    public DriveBackMacro(double msDelay) {
        delay = msDelay;


    }

    @Override
    public void start() {
        RobotComponents.back_intake_servo.setPower(-1);
        RobotComponents.front_intake_servo.setPower(-1);
        DriveForwardAuto.equalPowers(0.2);
        RobotComponents.coroutines.startRoutineLater((mode, d) -> {
            DriveForwardAuto.equalPowers(0);

            RobotComponents.back_intake_servo.setPower(0);
            RobotComponents.front_intake_servo.setPower(0);
            finish();
            return CoroutineResult.Stop;
        }, delay);
    }

    @Override
    public void tick(OpMode opMode) {

    }

}
