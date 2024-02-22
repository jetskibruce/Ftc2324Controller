package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.opmodes.auto.Webcam.PrimaryDetectionPipeline;
import org.firstinspires.ftc.teamcode.opmodes.auto.Webcam.Webcam;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;





@Config
@Disabled

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="BlueSensing")
public class BlueSensing extends LinearOpMode {
    Autonomous autonomous;
    SampleMecanumDrive drive;


    Webcam webcam = new Webcam();

    public TrajectorySequence scoreSpikeMark, scoreBackDrop, park;
    public static double power = 0.25;

    @Override
    public void runOpMode() throws InterruptedException {


        webcam.initCamera(hardwareMap, PrimaryDetectionPipeline.Color.BLUE);


        while (opModeInInit()) {
            telemetry.addData("Location: ", webcam.getLocation());
            telemetry.update();
            if (webcam.getLocation() == PrimaryDetectionPipeline.ItemLocation.CENTER) {
                telemetry.addLine();
            } else if (webcam.getLocation() == PrimaryDetectionPipeline.ItemLocation.RIGHT) {
                telemetry.addLine();
            } else if (webcam.getLocation() == PrimaryDetectionPipeline.ItemLocation.LEFT) {
                telemetry.addLine();
            }
        }


    }
}



