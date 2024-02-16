package org.firstinspires.ftc.teamcode.opmodes.auto;

import static org.firstinspires.ftc.teamcode.opmodes.teleop.functional.CompDrive.PIXEL_HOLD_POSITION;
import static org.firstinspires.ftc.teamcode.opmodes.teleop.functional.CompDrive.PIXEL_RELEASE_POSITION;
import static org.firstinspires.ftc.teamcode.opmodes.teleop.functional.CompDrive.TOWER_UP_SEQUENCE;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.Servo;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequenceBuilder;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.components.RobotComponents;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.macros.MacroSequence;
import org.firstinspires.ftc.teamcode.macros.arm.up.DumpBucketMacro;
import org.firstinspires.ftc.teamcode.macros.auto.AutoCorrectHeadingDumb;
import org.firstinspires.ftc.teamcode.macros.auto.AutoMoveMacro;
import org.firstinspires.ftc.teamcode.macros.auto.FindTeamElementPosition;
import org.firstinspires.ftc.teamcode.macros.MacroSequence;
import org.firstinspires.ftc.teamcode.macros.arm.IntakePoseMacro;
import org.firstinspires.ftc.teamcode.macros.arm.down.LowerArmMacro;
import org.firstinspires.ftc.teamcode.macros.arm.down.TuckWristDownMacro;
import org.firstinspires.ftc.teamcode.macros.arm.up.ArmToDumpPointMacro;
import org.firstinspires.ftc.teamcode.macros.arm.up.TuckWristForRiseMacro;
import org.firstinspires.ftc.teamcode.macros.generic.RunActionMacro;
import org.firstinspires.ftc.teamcode.macros.tuckdown.ArbitraryDelayMacro;
import org.firstinspires.ftc.teamcode.opmodes.auto.Webcam.PrimaryDetectionPipeline;
import org.firstinspires.ftc.teamcode.opmodes.auto.Webcam.Webcam;
import org.firstinspires.ftc.teamcode.opmodes.teleop.functional.CompDrive;

@Autonomous(name="RedEndzone Shortside SENSING Auto", group="aCompete")
public class CloseRedSENSING extends LinearOpMode {


    public Webcam webcam = new Webcam();
    int timeYouGotHere = 0;
    public static final double PIXEL_RELEASE_POSITION = 0.5;
    public static final double PIXEL_HOLD_POSITION = 1.0;




    public void runOpMode() throws InterruptedException {
        RobotComponents.init(hardwareMap);


        RobotComponents.left_pixel_hold_servo.setPosition(PIXEL_HOLD_POSITION);
        RobotComponents.right_pixel_hold_servo.setPosition( PIXEL_HOLD_POSITION);


        int direction = -999;
        double speedMult = 1f;
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        direction = -1;

        webcam.initCamera(hardwareMap, PrimaryDetectionPipeline.Color.RED);

        int ElementLocation = 0;

        while (opModeInInit()) {
            telemetry.addData("Location: ", webcam.getLocation());
            telemetry.update();
            if (webcam.getLocation() == PrimaryDetectionPipeline.ItemLocation.CENTER) {
                telemetry.addLine();
                ElementLocation = 1;
            } else if (webcam.getLocation() == PrimaryDetectionPipeline.ItemLocation.RIGHT) {
                telemetry.addLine();
                ElementLocation = 2;
            } else if (webcam.getLocation() == PrimaryDetectionPipeline.ItemLocation.LEFT) {
                telemetry.addLine();

            }
        }

        waitForStart();
        if(isStopRequested()) {
            return;
        }



        Pose2d startPose = new Pose2d(11, -61, Math.toRadians(90));


                if (ElementLocation == 0) {

                    drive.setPoseEstimate(startPose);

                    Trajectory toSpikemark = drive.trajectoryBuilder(startPose)
                            .back(28)
                            .build();

                    drive.followTrajectory(toSpikemark);

                    drive.turn(Math.toRadians(-90));


                    //spikemark spitout

                    while (timeYouGotHere < 2) {
                        RobotComponents.front_intake_motor.setPower(direction * speedMult);
                        timeYouGotHere = 2;
                    }

                    if(timeYouGotHere == 2) {
                        RobotComponents.front_intake_motor.setPower(0);
                    }

                    Trajectory toBackboard = drive.trajectoryBuilder(toSpikemark.end())
                            .back(38.5).build();

                    drive.followTrajectory(toBackboard);


                    //drop board
                    TOWER_UP_SEQUENCE.get().append(
                            new RunActionMacro((o) -> {
                                telemetry.speak("standing by");
                                return false;
                            })
                    ).start();


                    RobotComponents.left_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION);
                    RobotComponents.right_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION);



                    Trajectory scoot = drive.trajectoryBuilder(toBackboard.end())
                            .forward(2)
                            .splineTo(new Vector2d(36.5, -37),-90)
                            .build();

                    drive.followTrajectory(scoot);


                } else if (ElementLocation == 1) {

                    drive.setPoseEstimate(startPose);

                    Trajectory toSpikemark = drive.trajectoryBuilder(startPose)
                            .back(26.5)
                            .build();

                    drive.followTrajectory(toSpikemark);

                    drive.turn(Math.toRadians(180));


                    //spikemark spitout IT IS SKIPPING THIS

                    while (timeYouGotHere < 2) {
                        RobotComponents.front_intake_motor.setPower(direction * speedMult);
                        timeYouGotHere = 2;
                    }

                    if(timeYouGotHere == 2) {
                        RobotComponents.front_intake_motor.setPower(0);
                    }


                    drive.turn(Math.toRadians(90));

                    // IT IS SKIPPING THIS

                    Trajectory toBackboard = drive.trajectoryBuilder(toSpikemark.end())
                            .back(38.5).build();

                    //^^

                    drive.followTrajectory(toBackboard);


                    //drop board
                    TOWER_UP_SEQUENCE.get().append(
                            new RunActionMacro((o) -> {
                                telemetry.speak("standing by");
                                return false;
                            })
                    ).start();


                    RobotComponents.left_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION);
                    RobotComponents.right_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION);



                    Trajectory scoot = drive.trajectoryBuilder(toBackboard.end())
                            .forward(2)
                            .splineTo(new Vector2d(36.5, -37),-90)
                            .build();

                    drive.followTrajectory(scoot);

                } else {
                    drive.setPoseEstimate(startPose);

                    Trajectory toSpikemark = drive.trajectoryBuilder(startPose)
                            .back(28)
                            .build();

                    drive.followTrajectory(toSpikemark);

                    drive.turn(Math.toRadians(90));


                    //spikemark spitout

                    while (timeYouGotHere < 2) {
                        RobotComponents.front_intake_motor.setPower(direction * speedMult);
                        timeYouGotHere = 2;
                    }

                    if(timeYouGotHere == 2) {
                        RobotComponents.front_intake_motor.setPower(0);
                    }



                    drive.turn(Math.toRadians(180));
                    Trajectory toBackboard = drive.trajectoryBuilder(toSpikemark.end())
                            .back(38.5).build();

                    drive.followTrajectory(toBackboard);


                    //drop board
                    TOWER_UP_SEQUENCE.get().append(
                            new RunActionMacro((o) -> {
                                telemetry.speak("standing by");
                                return false;
                            })
                    ).start();


                    RobotComponents.left_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION);
                    RobotComponents.right_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION);



                    Trajectory scoot = drive.trajectoryBuilder(toBackboard.end())
                            .forward(2)
                            .splineTo(new Vector2d(36.5, -37),-90)
                            .build();

                    drive.followTrajectory(scoot);
                }




}

}
