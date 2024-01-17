package org.firstinspires.ftc.teamcode.opmodes.teleop.debug;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.components.RobotComponents;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.excutil.Input;
import org.firstinspires.ftc.teamcode.macros.MacroSequence;
import org.firstinspires.ftc.teamcode.macros.generic.DriveToTickOffset;

@TeleOp(group = "zDebug", name = "Fixed Move Testing")
public class FixedMoveDebug extends OpMode {

    static final double     COUNTS_PER_ENCODER_REV    = 8192 ;    // eg: TETRIX Motor Encoder
    static final double     ENCODER_GEAR_REDUCTION    = 1.0 ;     // No External Gearing.
    static final double     WHEEL_DIAMETER_INCHES   = 2.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_ENCODER_REV * ENCODER_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);

    SampleMecanumDrive drive;

    @Override
    public void init() {
        RobotComponents.init(hardwareMap);

        drive = new SampleMecanumDrive(hardwareMap);

        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    private Input input = new Input();

    private Pose2d pose = new Pose2d(0,0,0);

    private Pose2d mkPose(double x, double y, double heading) {
        return new Pose2d(x, y, heading);
    }

    int desiredInches = 18;

    @Override
    public void loop() {
        input.pollGamepad(gamepad1);
        RobotComponents.tickSystems(this);

        if (input.left_bumper.down()) desiredInches--;
        if (input.right_bumper.down()) desiredInches++;

        if (input.b.down() && !MacroSequence.RunningMacro) {
            MacroSequence.compose("drive +x",
                    new DriveToTickOffset(drive, new Pose2d(
                            1, 0, 0
                    ), (int)(COUNTS_PER_INCH * desiredInches), 0
                    )
            ).start();
        }
        if (input.x.down() && !MacroSequence.RunningMacro) {
            MacroSequence.compose("drive -x",
                    new DriveToTickOffset(drive, new Pose2d(
                            -1, 0, 0
                    ), (int)(COUNTS_PER_INCH * desiredInches), 0
                    )
            ).start();
        }

        if (input.y.down() && !MacroSequence.RunningMacro) {
            MacroSequence.compose("drive +y",
                    new DriveToTickOffset(drive, new Pose2d(
                            0, -1, 0
                    ), 0, (int)(COUNTS_PER_INCH * desiredInches)
                    )
            ).start();
        }
        if (input.a.down() && !MacroSequence.RunningMacro) {
            MacroSequence.compose("drive +y",
                    new DriveToTickOffset(drive, new Pose2d(
                            0, 1, 0
                    ), 0, (int)(COUNTS_PER_INCH * desiredInches)
                    )
            ).start();
        }


        drive.setWeightedDrivePower(pose);

    }
}
