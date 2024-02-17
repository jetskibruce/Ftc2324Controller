package org.firstinspires.ftc.teamcode.opmodes.auto;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.components.RobotComponents;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;


import org.firstinspires.ftc.teamcode.excutil.MotorPath;
import org.firstinspires.ftc.teamcode.opmodes.auto.Webcam.PrimaryDetectionPipeline;
import org.firstinspires.ftc.teamcode.opmodes.auto.Webcam.Webcam;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

import java.util.function.Supplier;

@Autonomous(name="RedEndzone Shortside SENSING Auto", group="aCompete")
public class CloseRedSENSING extends LinearOpMode {

    public DcMotor tower_motor = RobotComponents.tower_motor;
    public Servo   wrist_servo = RobotComponents.wrist_servo;
    public Servo   bucket_servo = RobotComponents.wrist_servo;

    public Webcam webcam = new Webcam();
    public static final double PIXEL_RELEASE_POSITION = 0.5;
    public static final double PIXEL_HOLD_POSITION = 1.0;




    public void runOpMode() throws InterruptedException {

        RobotComponents.init(hardwareMap);
        final double TowerGoalPosition = (RobotComponents.tower_motor.getCurrentPosition() - 1042);

        RobotComponents.left_pixel_hold_servo.setPosition(PIXEL_HOLD_POSITION);
        RobotComponents.right_pixel_hold_servo.setPosition( PIXEL_HOLD_POSITION);

        RobotComponents.extendo_servo.setPosition(0.7);



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

            }
        }

        waitForStart();
        if(isStopRequested()) {
            return;
        }

        Vector2d spikemarkDrop = new Vector2d(11, -33);

        Pose2d startPose = new Pose2d(11, -61, Math.toRadians(270));

        drive.setPoseEstimate(startPose);

                //LEFT


        TrajectorySequence closeLeftAuto = drive.trajectorySequenceBuilder(startPose)
                .back(26)
                .waitSeconds(1)
                .turn(Math.toRadians(90))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.front_intake_motor.setPower(.25)) // Spit out
                .waitSeconds(.45)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.front_intake_motor.setPower(0)) // Stop outtake
                .back(42)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(0.54))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(0.1))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.tower_motor.setTargetPosition((int) TowerGoalPosition))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> MotorPath.runToPosition(RobotComponents.tower_motor, (int)(TowerGoalPosition ), 0.5))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(0.9))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(0.49))
                .waitSeconds(.70)
                .UNSTABLE_addTemporalMarkerOffset(.25, () -> RobotComponents.right_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION)) //Drops Pixel
                .UNSTABLE_addTemporalMarkerOffset(.25, () -> RobotComponents.left_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION)) //Drops Pixel
                .waitSeconds(.55)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(0.49))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(0.9))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.tower_motor.setDirection(REVERSE))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> MotorPath.runToPosition(RobotComponents.tower_motor, (int)(TowerGoalPosition ), 0.2))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(0.227))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(0.54))
                .waitSeconds(.25)
                .forward(2)
                .strafeLeft(24)
                .back(8)
                .build();
        //CENTER


        TrajectorySequence closeCenterAuto = drive.trajectorySequenceBuilder(startPose)
                .back(26)
                .waitSeconds(1)
                .turn(Math.toRadians(180 + .00000000000000001))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.front_intake_motor.setPower(.25)) // Spit out
                .waitSeconds(.45)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.front_intake_motor.setPower(0)) // Stop outtake
                .back(2)
                .turn(Math.toRadians(90))
                .back(42)
                .strafeRight(4)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(0.54))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(0.1))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.tower_motor.setTargetPosition((int) TowerGoalPosition))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> MotorPath.runToPosition(RobotComponents.tower_motor, (int)(TowerGoalPosition ), 0.5))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(0.9))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(0.49))
                .waitSeconds(.70)
                .UNSTABLE_addTemporalMarkerOffset(.25, () -> RobotComponents.right_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION)) //Drops Pixel
                .UNSTABLE_addTemporalMarkerOffset(.25, () -> RobotComponents.left_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION)) //Drops Pixel
                .waitSeconds(.55)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(0.49))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(0.9))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.tower_motor.setDirection(REVERSE))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> MotorPath.runToPosition(RobotComponents.tower_motor, (int)(TowerGoalPosition ), 0.2))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(00.227))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(0.54))
                .waitSeconds(.25)
                .forward(2)
                .strafeLeft(27)
                .back(8)
                .build();


        // RIGHT
        // TODO MAKE NOT HIT THE PIXEL ON SPIKEMARK

        TrajectorySequence closeRightAuto = drive.trajectorySequenceBuilder(startPose)
                // TODO GO TO BETWEEN BOARD AND SPIKEMARK AND OUTTAKE

                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(0.54))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(0.1))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.tower_motor.setTargetPosition((int) TowerGoalPosition))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> MotorPath.runToPosition(RobotComponents.tower_motor, (int)(TowerGoalPosition ), 0.5))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(0.9))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(0.49))
                .waitSeconds(.70)
                .UNSTABLE_addTemporalMarkerOffset(.25, () -> RobotComponents.right_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION)) //Drops Pixel
                .UNSTABLE_addTemporalMarkerOffset(.25, () -> RobotComponents.left_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION)) //Drops Pixel
                .waitSeconds(.55)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(0.49))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(0.9))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.tower_motor.setDirection(REVERSE))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> MotorPath.runToPosition(RobotComponents.tower_motor, (int)(TowerGoalPosition ), 0.2))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(0.227))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(0.54))
                .waitSeconds(.25)
                .forward(2)
                .strafeLeft(24)
                .back(8)
                .build();


        ElementLocation=1;


        if (ElementLocation == 0) {

            drive.followTrajectorySequence(closeLeftAuto);
        } else if (ElementLocation == 1) {
            drive.followTrajectorySequence(closeCenterAuto);
        } else {
            drive.followTrajectorySequence(closeRightAuto);
        }
        RobotComponents.extendo_servo.setPosition(0.7);

    }




}

