package org.firstinspires.ftc.teamcode.opmodes.auto.extended;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.MarkerCallback;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robot.Robot;

import org.firstinspires.ftc.teamcode.components.RobotComponents;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.excutil.MotorPath;
import org.firstinspires.ftc.teamcode.opmodes.auto.Webcam.PrimaryDetectionPipeline;
import org.firstinspires.ftc.teamcode.opmodes.auto.Webcam.Webcam;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

import static org.firstinspires.ftc.teamcode.components.RobotComponents.*;
import static org.firstinspires.ftc.teamcode.components.RobotConstants.*;

import java.util.function.Supplier;
@Disabled
@Autonomous(name="RedEndzone Shortside SENSING Extended Auto", group="aCompete")
public class CloseRedSENSINGExtended extends LinearOpMode {

    public Webcam webcam = new Webcam();


    // filled in by auto code during execution
    MotorPath towerUpPath = null;

    public void runOpMode() throws InterruptedException {

        RobotComponents.init(hardwareMap);
        final double TowerGoalPosition = (tower_motor.getCurrentPosition() - 1042);

        left_pixel_hold_servo.setPosition(PIXEL_HOLD_POSITION);
        right_pixel_hold_servo.setPosition(PIXEL_HOLD_POSITION);

        extendo_servo.setPosition(0.7);

        Supplier<MarkerCallback> armRaiseMarker = () -> () -> {
            RobotComponents.wrist_servo.setPosition(0.54);
            RobotComponents.bucket_servo.setPosition(0.1);
            RobotComponents.tower_motor.setTargetPosition((int) TowerGoalPosition);
            towerUpPath = MotorPath.runToPosition(RobotComponents.tower_motor, (int) (TowerGoalPosition), 0.5);
            RobotComponents.wrist_servo.setPosition(0.9);
            RobotComponents.bucket_servo.setPosition(0.49);
        };

        Supplier<MarkerCallback> armLowerMarker = () -> () -> {
            RobotComponents.bucket_servo.setPosition(0.49);
            RobotComponents.wrist_servo.setPosition(0.9);
            RobotComponents.tower_motor.setDirection(REVERSE);
            MotorPath.runToPosition(RobotComponents.tower_motor, (int) (TowerGoalPosition), 0.2);
            RobotComponents.bucket_servo.setPosition(00.227);
            RobotComponents.wrist_servo.setPosition(0.54);
        };

        Supplier<MarkerCallback> dropPixelMarker = () -> () -> {
            RobotComponents.right_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION); //Drops Pixel
            RobotComponents.left_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION);
        };

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        webcam.initCamera(hardwareMap, PrimaryDetectionPipeline.Color.RED);

        int ElementLocation = 0;

        while (opModeInInit()) {
            telemetry.addData("Location: ", webcam.getLocation());
            telemetry.update();
            if (webcam.getLocation() == PrimaryDetectionPipeline.ItemLocation.CENTER) {
                telemetry.addLine("IS CENTER");
                ElementLocation = 1;
            } else if (webcam.getLocation() == PrimaryDetectionPipeline.ItemLocation.RIGHT) {
                telemetry.addLine("IS RIGHT");
                ElementLocation = 2;
            } else if (webcam.getLocation() == PrimaryDetectionPipeline.ItemLocation.LEFT) {
                telemetry.addLine("IS LEFT");
                ElementLocation = 0;
            }
        }

        waitForStart();
        if (isStopRequested()) {
            return;
        }


        Pose2d startPose = new Pose2d(11, 61, Math.toRadians(90));

        drive.setPoseEstimate(startPose);


        TrajectorySequence rightCloseAuto = drive.trajectorySequenceBuilder(startPose)
                .splineTo(new Vector2d(32.5, -36), Math.toRadians(180))
                .waitSeconds(.15)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.front_intake_motor.setPower(.25)) // Spit out
                .waitSeconds(.45)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.front_intake_motor.setPower(0)) // Stop outtake
                .waitSeconds(.15)
                .back(18)
                .strafeLeft(5)
                .addTemporalMarker(armRaiseMarker.get())
                .waitSeconds(.70)
                .addTemporalMarker(dropPixelMarker.get())
                .waitSeconds(.55)
                .addTemporalMarker(armLowerMarker.get())
                .waitSeconds(.25)
                .forward(2)
                .strafeLeft(18)
                .back(8)
                .build();


        TrajectorySequence closeLeftAuto = drive.trajectorySequenceBuilder(startPose)
                .forward(28)
                .waitSeconds(.15)
                .turn(Math.toRadians(90))
                .waitSeconds(.55)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.front_intake_motor.setPower(.3)) // Spit out
                .waitSeconds(.45)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.front_intake_motor.setPower(0)) // Stop outtake
                .waitSeconds(.15)
                .back(40)
                .strafeRight(5)
                .addTemporalMarker(armRaiseMarker.get())
                .waitSeconds(.70)
                .addTemporalMarker(dropPixelMarker.get())
                .waitSeconds(.55)
                .addTemporalMarker(armLowerMarker.get())
                .waitSeconds(.25)
                .forward(2)
                .strafeLeft(32)
                .back(8)
                .build();

        // CENTER

        TrajectorySequence closeCenterAuto = drive.trajectorySequenceBuilder(startPose)
                /*.forward(28)
                .waitSeconds(1)
                .addTemporalMarker(() -> RobotComponents.front_intake_motor.setPower(.30)) // Spit out
                .waitSeconds(.45)
                .addTemporalMarker(() -> RobotComponents.front_intake_motor.setPower(0)) // Stop outtake
                .back(3.5)
                .turn(Math.toRadians(90))
                .back(41)
                .addTemporalMarker(armRaiseMarker.get())
                // example of how you can now use conditions as well
                //.waitUntil(() -> towerUpPath.isComplete(10), 2.0)
                .waitSeconds(.70)
                .addTemporalMarker(dropPixelMarker.get()) //Drops Pixel
                .waitSeconds(.55)
                .addTemporalMarker(armLowerMarker.get())
                .waitSeconds(.25)
                .forward(2)
                .strafeLeft(24.5)
                .back(8)
                .build();*/
                .forward(28)
                .waitSeconds(1)
                .addTemporalMarker(() -> front_intake_motor.setPower(.30)) // Spit out
                .waitSeconds(.45)
                .addTemporalMarker(() -> front_intake_motor.setPower(0)) // Stop outtake
                .back(3.5)
                .turn(Math.toRadians(90))
                .back(41)
                .addTemporalMarker(armRaiseMarker.get())
                // example of how you can now use conditions as well
                //.waitUntil(() -> towerUpPath.isComplete(10), 2.0)
                .waitSeconds(.70)
                .addTemporalMarker(dropPixelMarker.get()) //Drops Pixel
                .waitSeconds(.55)
                .addTemporalMarker(armLowerMarker.get())
                .waitSeconds(.25)
                .forward(2)
                //.strafeLeft(24.5)
                .strafeLeft(21)

                .forward(86+18)
                .strafeRight(24)
                //.waitSeconds(0.2)

                .forward(18)
                //.splineTo(new Vector2d(-48, -36), Math.PI)
                .turn(Math.toRadians(-25))
                // do arm extend and smack smack
                .waitSeconds(0.5)
                .turn(Math.toRadians(25))
                // do arm re-tuck and suck suck
                .strafeLeft(4)
                .forward(6)
                .waitSeconds(0.7)
                // no more intake
                .addTemporalMarker(() -> {
                    front_intake_motor.setPower(0);
                })
                .back(6)
                .strafeLeft(20)
                .back(14 + 46 + 40 + 8)

                .build();




        if (webcam.getLocation() == PrimaryDetectionPipeline.ItemLocation.LEFT) {
            drive.followTrajectorySequence(closeLeftAuto);
        } else if (webcam.getLocation() == PrimaryDetectionPipeline.ItemLocation.CENTER) {
            drive.followTrajectorySequence(closeCenterAuto);
        } else {
           drive.followTrajectorySequence(rightCloseAuto);
        }
        RobotComponents.extendo_servo.setPosition(0.7);

        while (opModeIsActive()) {
            RobotComponents.tickSystems(this);
        }

    }




}

