package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.excutil.liveconfig.LiveSetting;
import org.firstinspires.ftc.teamcode.excutil.liveconfig.LiveSettings;

@TeleOp(name="aGOODGOOD")
public class LastYearBotValueTest extends OpMode {

    Servo servo;

    @Override
    public void init() {
        servo = hardwareMap.get(Servo.class, "pos");

    }

    LiveSetting<Double> servoPosition;

    @Override
    public void loop() {
        if (gamepad1.dpad_left) {
            servo.setPosition(0);
        } else if (gamepad1.dpad_right) {
            servo.setPosition(servoPosition.value());
        }
        LiveSettings.tick(this);
    }
}
