package org.firstinspires.ftc.teamcode.opmodes.teleop.misc;



import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name= "Extendo")
    public class extendo extends OpMode {

    Servo outake_Servo;
    boolean isExtened = false;
    double extened_pos = 0;
    double closed_pos = .7;

    @Override
    public void init() {
        outake_Servo = hardwareMap.get(Servo.class, "outake_servo");
        outake_Servo.setPosition(closed_pos);
    }

    @Override
    public void loop() {
        if (gamepad1.dpad_right) {
            if (isExtened) {
                outake_Servo.setPosition(closed_pos);
                isExtened = false;
            } else {
                outake_Servo.setPosition(extened_pos);
                isExtened = true;
            }
            //arm_Servo.setPosition(extened_pos);
        }
    }
}
