package org.hollins.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepExtendedTesting {

    private static double pifrac(double fraction) {
        return fraction * 3.141592654;
    }


    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width

                .setConstraints(52.48180821614297, 52.48180821614297, Math.toRadians(184.02607784577722), Math.toRadians(184.02607784577722), 18.81)

                .followTrajectorySequence(drive ->

                            drive.trajectorySequenceBuilder(new Pose2d(11, -61, Math.toRadians(90)))
                                    .forward(28)
                                    .waitSeconds(1)
                                    //.addTemporalMarker(() -> RobotComponents.front_intake_motor.setPower(.30)) // Spit out
                                    //.waitSeconds(.45)
                                    //.addTemporalMarker(() -> RobotComponents.front_intake_motor.setPower(0)) // Stop outtake

                                    .back(3.5)
                                    .turn(Math.toRadians(90))
                                    .back(41)
                                    .waitSeconds(.55)
                                    .waitSeconds(.25)
                                    .forward(2)
                                    .strafeLeft(22.5)

                                    /* extended bit */

                                    .forward(46 + 40)
                                    .strafeRight(24)
                                    //.waitSeconds(0.2)

                                    .forward(18)
                                    //.splineTo(new Vector2d(-48, -36), Math.PI)
                                    .turn(Math.toRadians(-25))
                                    // do arm extend and smack smack
                                    //.addTemporalMarker()
                                    .waitSeconds(0.5)
                                    .turn(Math.toRadians(25))
                                    // do arm re-tuck and suck suck
                                    //.addTemporalMarker()
                                    // turn on intake too
                                    .strafeLeft(4)
                                    .forward(6)
                                    .waitSeconds(0.7)
                                    // no more intake
                                    //.addTemporalMarker()

                                    // could consolidate a 6 and 14 into a back 20 here if we don't think going back
                                    // will block alliance
                                    .back(6)
                                    .strafeLeft(20)
                                    .back(14 + 46 + 40)

                                    /* end extended bit */

                                    .back(8)
                                    .build()

                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(1f)
                .addEntity(myBot)
                .start();
    }
}
