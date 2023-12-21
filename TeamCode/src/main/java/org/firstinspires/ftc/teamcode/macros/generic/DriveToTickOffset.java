package org.firstinspires.ftc.teamcode.macros.generic;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.components.RobotComponents;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.macros.PathStep;

public class DriveToTickOffset extends PathStep {

    private SampleMecanumDrive drive;
    private Pose2d pose;
    int xTickOffset = 0;
    int yTickOffset = 0;

    public DriveToTickOffset(SampleMecanumDrive drive, Pose2d pose, int xTicks, int yTicks) {
        this.drive = drive;
        this.pose = pose;
        this.xTickOffset = xTicks;
        this.yTickOffset = yTicks;
    }

    private int startXTicks, startYTicks;

    @Override
    public void onStart() {
        drive.setWeightedDrivePower(pose);
        startXTicks = RobotComponents.perpendicularEncoder.getCurrentPosition();
        startYTicks = RobotComponents.parallelEncoder.getCurrentPosition();
    }

    @Override
    public void onTick(OpMode opMode) {
        boolean reachedYThreshold = Math.abs(RobotComponents.parallelEncoder.getCurrentPosition() - startYTicks) >= yTickOffset;
        boolean reachedXThreshold = Math.abs(RobotComponents.perpendicularEncoder.getCurrentPosition() - startXTicks) >= xTickOffset;

        drive.setWeightedDrivePower(pose);

        if (reachedXThreshold && reachedYThreshold) {
            finish();

        }
    }
}
