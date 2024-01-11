package org.firstinspires.ftc.teamcode.opmodes.teleop.debug;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.components.RobotComponents;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.excutil.Input;
import org.firstinspires.ftc.teamcode.excutil.MotorPath;
import org.firstinspires.ftc.teamcode.excutil.RMath;
import org.firstinspires.ftc.teamcode.macros.Flag;
import org.firstinspires.ftc.teamcode.macros.MacroSequence;
import org.firstinspires.ftc.teamcode.macros.arm.IntakePoseMacro;
import org.firstinspires.ftc.teamcode.macros.arm.down.LowerArmMacro;
import org.firstinspires.ftc.teamcode.macros.arm.down.TuckWristDownMacro;
import org.firstinspires.ftc.teamcode.macros.arm.up.ArmToDumpPointMacro;
import org.firstinspires.ftc.teamcode.macros.arm.up.TuckWristForRiseMacro;
import org.firstinspires.ftc.teamcode.macros.generic.RunActionMacro;
import org.firstinspires.ftc.teamcode.opmodes.teleop.functional.DriveTest_2;

@TeleOp(group = "drive", name = "Spool Control")
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
