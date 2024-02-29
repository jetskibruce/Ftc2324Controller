package org.firstinspires.ftc.teamcode.sensing;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevColorSensorV3;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Config
public class BucketDistanceSensingArray {

    private static double LeftThreshold = 5;
    private static double RightThreshold = 5;
    private RevColorSensorV3 leftSensor, rightSensor;

    public BucketDistanceSensingArray(RevColorSensorV3 left, RevColorSensorV3 right) {
        this.leftSensor = left;
        this.rightSensor = right;
    }

    public boolean isLeftPixelIn() {
        return leftCentimeters() < LeftThreshold;
    }

    public boolean isRightPixelIn() {
        return rightCentimeters() < RightThreshold;
    }

    public double leftCentimeters() {
        return leftSensor.getDistance(DistanceUnit.CM);
    }
    public double rightCentimeters() {
        return rightSensor.getDistance(DistanceUnit.CM);
    }

    public RevColorSensorV3 left() {
        return leftSensor;
    }

    public RevColorSensorV3 right() {
        return rightSensor;
    }



}
