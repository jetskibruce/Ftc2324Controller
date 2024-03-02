package org.hollins.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequenceBuilder;
import com.acmerobotics.roadrunner.trajectory.Trajectory;

import java.util.Vector;

public class MeepMeepTesting {

    private static double pifrac(double fraction) {
        return fraction * 3.141592654;
    }


    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width

                .setConstraints(52.48180821614297, 52.48180821614297, Math.toRadians(184.02607784577722), Math.toRadians(184.02607784577722), 18.81)

                .followTrajectorySequence(drive ->

                            drive.trajectorySequenceBuilder(new Pose2d(11, 61, Math.toRadians(-90)))
                                    .forward(2)
                                    .strafeLeft(24)
                                    .forward(30)
                                    .turn(Math.toRadians(-90))
                                    .forward(8)
                                    .back(5)
                                //    .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.front_intake_motor.setPower(.25)) // Spit out
                                    .waitSeconds(.45)
                               //     .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.front_intake_motor.setPower(0)) // Stop outtake
                                    .waitSeconds(.15)
                                    .back(10)
                                    .strafeRight(9)
                                //   .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(WRIST_IDLE_POSITION))
                                  //  .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(BUCKET_IDLE_POSITION))
                                 //   .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.tower_motor.setTargetPosition((int) TowerGoalPosition))
                                   // .UNSTABLE_addTemporalMarkerOffset(0, () -> MotorPath.runToPosition(RobotComponents.tower_motor, (int)(TowerGoalPosition ), 0.5))
                               //     .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(WRIST_SCORE_POSITION))
                                 //   .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(BUCKET_SCORE_POSITION))
                             //       .waitSeconds(.70)
                             //       .UNSTABLE_addTemporalMarkerOffset(.25, () -> RobotComponents.right_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION)) //Drops Pixel
                           //         .UNSTABLE_addTemporalMarkerOffset(.25, () -> RobotComponents.left_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION)) //Drops Pixel
                                    .waitSeconds(.55)
                        //.UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(BUCKET_SCORE_POSITION))
                                   // .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(WRIST_SCORE_POSITION))
                                 //   .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.tower_motor.setDirection(REVERSE))
                                 //   .UNSTABLE_addTemporalMarkerOffset(0, () -> MotorPath.runToPosition(RobotComponents.tower_motor, (int)(TowerGoalPosition ), 0.2))
                                   // .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.bucket_servo.setPosition(BUCKET_TELEOP_POSITION))
                                  //  .UNSTABLE_addTemporalMarkerOffset(0, () -> RobotComponents.wrist_servo.setPosition(WRIST_TELEOP_POSITION))
                                    .waitSeconds(.25)
                                    .forward(4)
                                    .strafeRight(22)
                                    .back(8)
                                    .build()



















                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(1f)
                .addEntity(myBot)
                .start();
    }
}
