package org.hollins.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequenceBuilder;

public class MeepMeepTesting {

    private static double pifrac(double fraction) {
        return fraction * 3.141592654;
    }

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(600000, 600000, Math.toRadians(1800) * 100, Math.toRadians(1800) * 100, 15)
                .followTrajectorySequence(drive ->
                        {
                            TrajectorySequenceBuilder builder = drive.trajectorySequenceBuilder(new Pose2d(0, 0, 0))
                                    .forward(30)
                                    .turn(Math.toRadians(90))
                                    .forward(30)
                                    .turn(Math.toRadians(90))
                                    .forward(30)
                                    .turn(Math.toRadians(90))
                                    .forward(30)
                                    .turn(Math.pow(Math.toRadians(90), 60));



                                return builder.build();
                        }
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}