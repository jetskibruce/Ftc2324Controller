package org.firstinspires.ftc.teamcode.macros.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.components.RobotComponents;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.excutil.RMath;
import org.firstinspires.ftc.teamcode.macros.PathStep;

public class AutoCorrectHeadingDumb extends PathStep {

    private SampleMecanumDrive drive;
    private double goalHeading;
    private double power;
    private double threshold;
    private Pose2d drivePowers;

    public AutoCorrectHeadingDumb(SampleMecanumDrive drive, double goal, double power, double threshold) {
        this.threshold = threshold;
        this.goalHeading = goal;
        this.power = power;
        this.drive = drive;
    }

    private ElapsedTime time = new ElapsedTime();
    private Double timeout = null;

    public AutoCorrectHeadingDumb giveTimeout(double ms) {
        timeout = ms;
        return this;
    }

    @Override
    public void onStart() {
        time.reset();
    }

    int numTimesCorrect = 0;

    double speedMultiplier = 1;

    @Override
    public void onTick(OpMode opMode) {
        YawPitchRollAngles angles = RobotComponents.imu.getRobotYawPitchRollAngles();

        if (goalHeading > angles.getYaw(AngleUnit.DEGREES)) {
            drivePowers = new Pose2d(0, 0, -1);
        } else if (goalHeading < angles.getYaw(AngleUnit.DEGREES)) {
            drivePowers = new Pose2d(0, 0, 1);
        } else {
            finish();
        }

        drivePowers = drivePowers.times(power);
        drive.setWeightedDrivePower(drivePowers.times(speedMultiplier));

        if (Math.abs(angles.getYaw(AngleUnit.DEGREES) - goalHeading) < threshold) {
            numTimesCorrect++;
            speedMultiplier *= 0.95f;
            speedMultiplier = RMath.clamp(speedMultiplier, 0.01f, 1f);
            // prolly make time based
            if ((timeout != null && (time.milliseconds() > timeout)) || numTimesCorrect > 50) {
                finish();
                return;
            } else {
                numTimesCorrect = 0;
            }
        }
    }
}
