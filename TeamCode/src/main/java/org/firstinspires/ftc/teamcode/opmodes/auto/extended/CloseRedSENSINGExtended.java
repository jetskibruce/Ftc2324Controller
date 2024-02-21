package org.firstinspires.ftc.teamcode.opmodes.auto.extended;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
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

@Autonomous(name="RedEndzone Shortside SENSING Auto", group="aCompete")
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
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(0.54))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(0.1))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.tower_motor.setTargetPosition((int) TowerGoalPosition))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> MotorPath.runToPosition(RobotComponents.tower_motor, (int) (TowerGoalPosition), 0.5))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(0.9))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(0.49))
                .waitSeconds(.70)
                .UNSTABLE_addTemporalMarkerOffset(.25, () -> RobotComponents.right_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION)) //Drops Pixel
                .UNSTABLE_addTemporalMarkerOffset(.25, () -> RobotComponents.left_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION)) //Drops Pixel
                .waitSeconds(.55)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(0.49))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(0.9))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.tower_motor.setDirection(REVERSE))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> MotorPath.runToPosition(RobotComponents.tower_motor, (int) (TowerGoalPosition), 0.2))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(00.227))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(0.54))
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
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(0.54))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(0.1))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.tower_motor.setTargetPosition((int) TowerGoalPosition))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> MotorPath.runToPosition(RobotComponents.tower_motor, (int) (TowerGoalPosition), 0.5))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(0.9))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(0.49))
                .waitSeconds(.70)
                .UNSTABLE_addTemporalMarkerOffset(.25, () -> RobotComponents.right_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION)) //Drops Pixel
                .UNSTABLE_addTemporalMarkerOffset(.25, () -> RobotComponents.left_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION)) //Drops Pixel
                .waitSeconds(.55)
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(0.49))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(0.9))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.tower_motor.setDirection(REVERSE))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> MotorPath.runToPosition(RobotComponents.tower_motor, (int) (TowerGoalPosition), 0.2))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(00.227))
                .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(0.54))
                .waitSeconds(.25)
                .forward(2)
                .strafeLeft(32)
                .back(8)
                .build();

        // CENTER

        TrajectorySequence closeCenterAuto = drive.trajectorySequenceBuilder(startPose)
                .forward(28)
                .waitSeconds(1)
                .addTemporalMarker(() -> RobotComponents.front_intake_motor.setPower(.30)) // Spit out
                .waitSeconds(.45)
                .addTemporalMarker(() -> RobotComponents.front_intake_motor.setPower(0)) // Stop outtake
                .back(3.5)
                .turn(Math.toRadians(90))
                .back(41)
                .addTemporalMarker(() -> {
                    RobotComponents.wrist_servo.setPosition(0.54);
                    RobotComponents.bucket_servo.setPosition(0.1);
                    RobotComponents.tower_motor.setTargetPosition((int) TowerGoalPosition);
                    towerUpPath = MotorPath.runToPosition(RobotComponents.tower_motor, (int) (TowerGoalPosition), 0.5);
                    RobotComponents.wrist_servo.setPosition(0.9);
                    RobotComponents.bucket_servo.setPosition(0.49);
                })
                .waitUntil(() -> towerUpPath.isComplete(10), 2.0)
                //.waitSeconds(.70)

                .addTemporalMarker(() -> {
                    RobotComponents.right_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION); //Drops Pixel
                    RobotComponents.left_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION);
                }) //Drops Pixel
                .waitSeconds(.55)
                .addTemporalMarker(() -> {
                        RobotComponents.bucket_servo.setPosition(0.49);
                        RobotComponents.wrist_servo.setPosition(0.9);
                        RobotComponents.tower_motor.setDirection(REVERSE);
                        MotorPath.runToPosition(RobotComponents.tower_motor, (int) (TowerGoalPosition), 0.2);
                        RobotComponents.bucket_servo.setPosition(00.227);
                        RobotComponents.wrist_servo.setPosition(0.54);
                })
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

        while (opModeIsActive()) {
            RobotComponents.tickSystems(this);
        }

    }




}

