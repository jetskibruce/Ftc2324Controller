package org.firstinspires.ftc.teamcode.macros.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.components.RobotComponents;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.macros.PathStep;
import org.firstinspires.ftc.teamcode.util.Encoder;


public class AutoAdvancedMoveMacro extends AutoMoveMacro {

    double goalHeading, headingCorrectionPower;

    public AutoAdvancedMoveMacro(
            SampleMecanumDrive drive, LateralDirection direction,
            double distanceInches, double power,
            double goalHeading, double headingCorrectionPower) {
        super(drive, direction, distanceInches, power);

        this.goalHeading = goalHeading;
        this.headingCorrectionPower = headingCorrectionPower;
    }

    @Override
    public void onTick(OpMode opMode) {
        calculateLerpMultiplier();

        YawPitchRollAngles angles = RobotComponents.imu.getRobotYawPitchRollAngles();

        Pose2d lerpedDrive = drivePowers.times(lerpMultiplier);

        Pose2d newDrive;
        if (goalHeading > angles.getYaw(AngleUnit.DEGREES)) {
            newDrive = new Pose2d(lerpedDrive.getX(), lerpedDrive.getY(), -1 * headingCorrectionPower);
        } else if (goalHeading < angles.getYaw(AngleUnit.DEGREES)) {
            newDrive = new Pose2d(lerpedDrive.getX(), lerpedDrive.getY(), 1 * headingCorrectionPower);
        } else {
            newDrive = lerpedDrive;
        }

        drive.setWeightedDrivePower(newDrive);

    }

}
