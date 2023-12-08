package org.firstinspires.ftc.teamcode.macros.generic;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.macros.PathStep;

public class ServoPositionMacro extends PathStep {

    private ElapsedTime runtime = new ElapsedTime();
    private Servo servo = null;
    private double msDuration = 0, position = 0;

    public ServoPositionMacro(Servo servo, double position, double delayAfter) {

        this.servo = servo;
        this.msDuration = delayAfter;
        this.position = position;
    }

    @Override
    public void onStart() {
        runtime.reset();
        servo.setPosition(position);
    }

    @Override
    public void onTick(OpMode opMode) {
        opMode.telemetry.addData("elapsed on servpos: ", runtime.milliseconds());
        if (runtime.milliseconds() > msDuration) {
            finish();
            return;
        }
    }
}
