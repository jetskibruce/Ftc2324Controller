package org.firstinspires.ftc.teamcode.opmodes.teleop.debug;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.components.RobotComponents;
import org.firstinspires.ftc.teamcode.excutil.Input;

@TeleOp(group = "bControl", name = "Spool Control")
public class Spool extends OpMode {

    private Input input;


    @Override
    public void start() {

    }

    @Override
    public void init() {
        RobotComponents.init(hardwareMap);

        RobotComponents.climb_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RobotComponents.climb_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

    }

    @Override
    public void loop() {

        RobotComponents.tickSystems(this);

        pollClimbInputs();

        telemetry.addData("Extend using", "B");
        telemetry.addData("Retract using ", "A");
        telemetry.update();

    }

    public void pollClimbInputs(){
        if (gamepad1.a) {
            RobotComponents.climb_motor.setPower(1);
        }
        else if (gamepad1.b) {
            RobotComponents.climb_motor.setPower(-1);
        } else {
            RobotComponents.climb_motor.setPower(0);
        }
    }





}
