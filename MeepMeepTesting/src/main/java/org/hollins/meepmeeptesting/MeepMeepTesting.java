package org.hollins.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequenceBuilder;
import com.acmerobotics.roadrunner.trajectory.Trajectory;

import java.util.Vector;

public class MeepMeepTesting {

    private static double pifrac(double fraction) {
        return fraction * 3.141592654;
    }

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);
        Vector2d spikemarkDrop = new Vector2d(42, -33);
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width

                .setConstraints(52.48180821614297, 52.48180821614297, Math.toRadians(184.02607784577722), Math.toRadians(184.02607784577722), 18.81)
                .followTrajectorySequence(drive ->
                        {
                            TrajectorySequenceBuilder builder = drive.trajectorySequenceBuilder(new Pose2d(11, -61, Math.toRadians(270)))
                                    .strafeLeft(25)
                                    .forward(25);
                            return builder.build();

                        }
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(1f)
                .addEntity(myBot)
                .start();
    }
}
