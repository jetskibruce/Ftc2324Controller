package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.robot.Robot;

import org.firstinspires.ftc.teamcode.components.RobotComponents;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.excutil.Input;

/**
 * Not a real opmode. Derive this for some useful helpers built in!
 *
 * Remember to call base init() and loop(), unless you're doing their
 * job on your own.
 */
@Disabled
public class NerdOpMode extends OpMode {

    protected Input input;
    protected SampleMecanumDrive drive;

    @Override
    public void init() {
        input = new Input();
        RobotComponents.init(hardwareMap);
        drive = new SampleMecanumDrive(hardwareMap);
    }

    @Override
    public void loop() {
        RobotComponents.tickSystems(this);
        input.pollGamepad(gamepad1);
    }
}
