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
        MeepMeep meepMeep = new MeepMeep(800);
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width

                .setConstraints(52.48180821614297, 52.48180821614297, Math.toRadians(184.02607784577722), Math.toRadians(184.02607784577722), 18.81)
                .followTrajectorySequence(drive ->

                            drive.trajectorySequenceBuilder(new Pose2d(0, 0, 0))
                                    .forward(28)
                                    .waitSeconds(1)
                                    //.addTemporalMarker(() -> RobotComponents.front_intake_motor.setPower(.30)) // Spit out
                                    .waitSeconds(.45)
                                    //.addTemporalMarker(() -> RobotComponents.front_intake_motor.setPower(0)) // Stop outtake
                                    .back(3.5)
                                    .turn(Math.toRadians(90))
                                    .back(41)
                                    .waitSeconds(.55)
                                    .waitSeconds(.25)
                                    .forward(2)
                                    .strafeLeft(24.5)
                                    .back(8)
                                    .build()



                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(1f)
                .addEntity(myBot)
                .start();
    }
}
