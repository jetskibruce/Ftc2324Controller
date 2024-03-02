package org.firstinspires.ftc.teamcode.opmodes.auto;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.components.RobotComponents;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;


import org.firstinspires.ftc.teamcode.excutil.MotorPath;
import org.firstinspires.ftc.teamcode.opmodes.auto.Webcam.PrimaryDetectionPipeline;
import org.firstinspires.ftc.teamcode.opmodes.auto.Webcam.Webcam;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

import static org.firstinspires.ftc.teamcode.components.RobotComponents.*;
import static org.firstinspires.ftc.teamcode.components.RobotConstants.*;
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

import static org.firstinspires.ftc.teamcode.components.RobotComponents.*;
import static org.firstinspires.ftc.teamcode.components.RobotConstants.*;










@Disabled

@Autonomous(name="RedEndzone Shortside SENSING 2+2Auto", group="aCompete")
public class CloseREDSensing2_2 extends LinearOpMode {

    public DcMotor tower_motor = RobotComponents.tower_motor;
    public Servo   wrist_servo = RobotComponents.wrist_servo;
    public Servo   bucket_servo = RobotComponents.wrist_servo;

    public Webcam webcam = new Webcam();
    public static final double PIXEL_RELEASE_POSITION = 0.5;
    public static final double PIXEL_HOLD_POSITION = 1.0;




    public void runOpMode() throws InterruptedException {



        RobotComponents.init(hardwareMap);
        final double TowerGoalPosition = (RobotComponents.tower_motor.getCurrentPosition() - 1288);

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
                ElementLocation = 0;
            }
        }

        waitForStart();
        if(isStopRequested()) {
            return;
        }


        Pose2d startPose = new Pose2d(11, 61, Math.toRadians(90));

        drive.setPoseEstimate(startPose);

        //RIGHT
        TrajectorySequence rightCloseAuto = drive.trajectorySequenceBuilder(startPose)
                .forward(2)
                .strafeRight(24)
                .forward(30)
                .turn(Math.toRadians(90))
                .forward(8)
                .back(5)
                .waitSeconds(.05)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.front_intake_motor.setPower(.25)) // Spit out
                .waitSeconds(.65)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.front_intake_motor.setPower(0)) // Stop outtake
                .waitSeconds(.05)
                .strafeLeft(10)
                .back(15)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(WRIST_IDLE_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(BUCKET_IDLE_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.tower_motor.setTargetPosition((int) TowerGoalPosition))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> MotorPath.runToPosition(RobotComponents.tower_motor, (int)(TowerGoalPosition ), 0.5))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(WRIST_SCORE_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(BUCKET_SCORE_POSITION))
                .waitSeconds(.5)
                .UNSTABLE_addTemporalMarkerOffset(.25, () -> RobotComponents.right_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION)) //Drops Pixel
                .UNSTABLE_addTemporalMarkerOffset(.25, () -> RobotComponents.left_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION)) //Drops Pixel
                .waitSeconds(.35)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(BUCKET_SCORE_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(WRIST_SCORE_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.tower_motor.setDirection(REVERSE))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> MotorPath.runToPosition(RobotComponents.tower_motor, (int)(TowerGoalPosition ), 0.2))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(BUCKET_TELEOP_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(WRIST_TELEOP_POSITION))
                .waitSeconds(.05)
                .forward(4)
                .strafeRight(28)
                .forward(102)
                .turn(Math.toRadians(25))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.front_intake_motor.setPower(-1)) // intake
                .back(1)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.front_intake_motor.setPower(0)) // Stop intake
                .UNSTABLE_addTemporalMarkerOffset(.25, () -> RobotComponents.right_pixel_hold_servo.setPosition(PIXEL_HOLD_POSITION)) //holds Pixel
                .UNSTABLE_addTemporalMarkerOffset(.25, () -> RobotComponents.left_pixel_hold_servo.setPosition(PIXEL_HOLD_POSITION)) //holds Pixel
                .turn(Math.toRadians(-25))
                .back(101)
                .strafeLeft(23)
                .back(5)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(WRIST_TELEOP_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(BUCKET_TELEOP_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.tower_motor.setTargetPosition((int) TowerGoalPosition))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> MotorPath.runToPosition(RobotComponents.tower_motor, (int)(TowerGoalPosition ), 0.5))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(WRIST_SCORE_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(BUCKET_SCORE_POSITION))
                .waitSeconds(.5)
                .UNSTABLE_addTemporalMarkerOffset(.25, () -> RobotComponents.right_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION)) //Drops Pixel
                .UNSTABLE_addTemporalMarkerOffset(.25, () -> RobotComponents.left_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION)) //Drops Pixel
                .waitSeconds(.35)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(BUCKET_SCORE_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(WRIST_SCORE_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.tower_motor.setDirection(REVERSE))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> MotorPath.runToPosition(RobotComponents.tower_motor, (int)(TowerGoalPosition ), 0.2))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(BUCKET_TELEOP_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(WRIST_TELEOP_POSITION))
                .build();



// LEFT
        TrajectorySequence closeLeftAuto = drive.trajectorySequenceBuilder(startPose)
                .forward(28)
                .waitSeconds(.15)
                .turn(Math.toRadians(90))
                .waitSeconds(.55)
                .forward(1)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.front_intake_motor.setPower(.3)) // Spit out
                .waitSeconds(.65)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.front_intake_motor.setPower(0)) // Stop outtake
                .waitSeconds(.15)
                .back((43-8))
                .strafeRight(7)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(WRIST_IDLE_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(BUCKET_IDLE_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.tower_motor.setTargetPosition((int) TowerGoalPosition))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> MotorPath.runToPosition(RobotComponents.tower_motor, (int)(TowerGoalPosition ), 0.5))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(WRIST_SCORE_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(BUCKET_SCORE_POSITION))
                .waitSeconds(.70)
                .UNSTABLE_addTemporalMarkerOffset(.25, () -> RobotComponents.right_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION)) //Drops Pixel
                .UNSTABLE_addTemporalMarkerOffset(.25, () -> RobotComponents.left_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION)) //Drops Pixel
                .waitSeconds(.55)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(BUCKET_SCORE_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(WRIST_SCORE_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.tower_motor.setDirection(REVERSE))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> MotorPath.runToPosition(RobotComponents.tower_motor, (int)(TowerGoalPosition ), 0.2))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(BUCKET_TELEOP_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(WRIST_TELEOP_POSITION))
                .waitSeconds(.25)
                .forward(2)
                .strafeLeft(32)
                .back(8)
                .build();

        // CENTER

        TrajectorySequence closeCenterAuto = drive.trajectorySequenceBuilder(startPose)
                .forward(28)
                .waitSeconds(1)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.front_intake_motor.setPower(.25)) // Spit out
                .waitSeconds(.65)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.front_intake_motor.setPower(0)) // Stop outtake
                .back(3.5)
                .turn(Math.toRadians(90))
                .strafeRight(1.5)
                .back((42-6))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(WRIST_IDLE_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(BUCKET_IDLE_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.tower_motor.setTargetPosition((int) TowerGoalPosition))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> MotorPath.runToPosition(RobotComponents.tower_motor, (int)(TowerGoalPosition ), 0.5))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(WRIST_SCORE_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(BUCKET_SCORE_POSITION))
                .waitSeconds(.70)
                .UNSTABLE_addTemporalMarkerOffset(.25, () -> RobotComponents.right_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION)) //Drops Pixel
                .UNSTABLE_addTemporalMarkerOffset(.25, () -> RobotComponents.left_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION)) //Drops Pixel
                .waitSeconds(.55)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(BUCKET_SCORE_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(WRIST_SCORE_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.tower_motor.setDirection(REVERSE))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> MotorPath.runToPosition(RobotComponents.tower_motor, (int)(TowerGoalPosition ), 0.2))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(BUCKET_TELEOP_POSITION))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(WRIST_TELEOP_POSITION))
                .waitSeconds(.25)
                .forward(2)
                .strafeLeft(24.5)
                .back(8)
                .build();




        if (webcam.getLocation() == PrimaryDetectionPipeline.ItemLocation.LEFT) {
            drive.followTrajectorySequence(closeLeftAuto);
        } else if (webcam.getLocation() == PrimaryDetectionPipeline.ItemLocation.CENTER) {
            drive.followTrajectorySequence(closeCenterAuto);
        } else {
            drive.followTrajectorySequence(rightCloseAuto);
        }
        RobotComponents.extendo_servo.setPosition(0.7);

    }




}

