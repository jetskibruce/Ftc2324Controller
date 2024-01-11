package org.firstinspires.ftc.teamcode.macros.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.components.RobotComponents;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.macros.PathStep;
import org.firstinspires.ftc.teamcode.opmodes.teleop.functional.DriveTest_2;
import org.firstinspires.ftc.teamcode.util.Encoder;


public class AutoMoveMacro extends PathStep {



    // "Forward" direction is the direction the arm points when stowed (from tower, towards intake)
    public enum LateralDirection {
        Forward,
        Left,
        Backward,
        Right
    }

    private SampleMecanumDrive drive;
    private int goalTicksTraveled;
    private int startTicks;
    private Encoder relevantEncoder;
    private Pose2d drivePowers;
    private double lerpMultiplier = 1;

    public AutoMoveMacro(SampleMecanumDrive drive, LateralDirection direction, double distanceInches, double power) {
        this.drive = drive;

        switch (direction) {
            case Forward:
                    drivePowers = new Pose2d(
                            -1, // swapped 1 & 2
                            0,
                            0
                    );
                relevantEncoder = RobotComponents.parallelEncoder;
                break;
            case Backward:
                    drivePowers = new Pose2d(
                            1, // swapped 1 & 2
                            0,
                            0
                    );
                relevantEncoder = RobotComponents.parallelEncoder;
                break;

            case Left:
                drivePowers = new Pose2d(
                        0, // swapped 1 & 2
                        -1,
                        0
                );
                relevantEncoder = RobotComponents.perpendicularEncoder;
                break;
            case Right:
                drivePowers = new Pose2d(
                        0, // swapped 1 & 2
                        1,
                        0
                );
                relevantEncoder = RobotComponents.perpendicularEncoder;
                break;
        }

        drivePowers = drivePowers.times(power);

        goalTicksTraveled = Math.abs(
                RobotComponents.encoderDistance(distanceInches)
        );
        startTicks = relevantEncoder.getCurrentPosition();



    }

    @Override
    public void onStart() {

    }

    private static final int FORGIVENESS_FACTOR = 40;

    private double smoothSpeed(double proportionTraveled) {
        return Math.pow(proportionTraveled, 3) + 1;
    }

    @Override
    public void onTick(OpMode opMode) {

        int ticksTraveled = Math.abs(relevantEncoder.getCurrentPosition() - startTicks);

        if (ticksTraveled >= goalTicksTraveled - FORGIVENESS_FACTOR) {
            drive.setWeightedDrivePower(new Pose2d(0, 0, 0));
            finish();
            return;
        }

        double proportionTraveled = (double)ticksTraveled / goalTicksTraveled;

        lerpMultiplier = smoothSpeed(proportionTraveled);

        drive.setWeightedDrivePower(drivePowers.times(lerpMultiplier));

    }

}
