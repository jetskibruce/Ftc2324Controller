package org.firstinspires.ftc.teamcode;

        import com.qualcomm.robotcore.eventloop.opmode.OpMode;
        import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
        import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Servo_calibrate", group="Iterative Opmode")
public class Servo_calibrate extends OpMode {

    private Servo intake_tilt_servo;
    @Override
    public void init() {
        intake_tilt_servo = hardwareMap.get(Servo.class,"intake_tilt_servo");

    }

    @Override
    public void loop() {
        telemetry.addData("tilt position:",intake_tilt_servo.getPosition());

        if (gamepad1.x){
            intake_tilt_servo.setPosition(.80);
        }
        else if (gamepad1.b){
            intake_tilt_servo.setPosition(0.83);
        }
        else if (gamepad1.a){
            intake_tilt_servo.setPosition(0.91);
        }
        else if (gamepad1.y){
            intake_tilt_servo.setPosition(0.91);
        }

        if (gamepad2.x){
            intake_tilt_servo.setPosition(0.86);
        }
        else if (gamepad2.b){
            intake_tilt_servo.setPosition(0.88);
        }
        else if (gamepad2.a){
            intake_tilt_servo.setPosition(.92);
        }
        else if (gamepad2.y){
            intake_tilt_servo.setPosition(.96);
        }

    }
}
