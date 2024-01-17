package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.components.RobotComponents;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.macros.MacroSequence;
import org.firstinspires.ftc.teamcode.macros.arm.autodump.AutoDumpPointMacro;
import org.firstinspires.ftc.teamcode.macros.arm.up.TuckWristForRiseMacro;
import org.firstinspires.ftc.teamcode.macros.auto.AutoCorrectHeadingDumb;
import org.firstinspires.ftc.teamcode.macros.auto.AutoMoveMacro;
import org.firstinspires.ftc.teamcode.macros.generic.RunActionMacro;
import org.firstinspires.ftc.teamcode.macros.tuckdown.ArbitraryDelayMacro;
import org.firstinspires.ftc.teamcode.opmodes.NerdOpMode;
import org.firstinspires.ftc.teamcode.opmodes.teleop.functional.CompDrive;

@Autonomous(name = "RedEndzone FAR Placement Auto", group = "aCompete")
public class CompFarAuto extends NerdOpMode  {

    AutoMoveMacro.LateralDirection strafeDirection = AutoMoveMacro.LateralDirection.Right;

    SampleMecanumDrive drive;

    @Override
    public void init() {
        RobotComponents.init(this.hardwareMap);

        drive = new SampleMecanumDrive(this.hardwareMap);
    }

    @Override
    public void start() {
        RobotComponents.imu.resetYaw();

        MacroSequence.begin("Go to endzone",
                new ArbitraryDelayMacro(5_000),
                new ArbitraryDelayMacro(5_000),
                new ArbitraryDelayMacro(5_000),
                new AutoMoveMacro(drive, strafeDirection,
                        2.25, 0.2),
                new AutoCorrectHeadingDumb(
                        drive, 0, 0.08, 2.5
                ).giveTimeout(750),
                new ArbitraryDelayMacro(500),
                new AutoMoveMacro(drive, AutoMoveMacro.LateralDirection.Backward,
                        74.5 / 2, 0.3),
                new AutoCorrectHeadingDumb(
                        drive, -1, 0.08, 2.5
                ).giveTimeout(750),
                new RunActionMacro((o) -> {
                    CompDrive.TOWER_UP_SEQUENCE.get().prepend(
                            new AutoMoveMacro(drive, AutoMoveMacro.LateralDirection.Backward,
                                    74.5 / 2, 0.3)
                    ).append(
                            new AutoDumpPointMacro(),
                            new ArbitraryDelayMacro(700),
                            new RunActionMacro((o2) -> {
                                RobotComponents.left_pixel_hold_servo.setPosition(
                                        CompDrive.PIXEL_RELEASE_POSITION
                                );
                                RobotComponents.right_pixel_hold_servo.setPosition(
                                        CompDrive.PIXEL_RELEASE_POSITION
                                );
                                MacroSequence.begin("Scoot Back",
                                        new ArbitraryDelayMacro(700),
                                        new RunActionMacro((o3) -> {
                                            //telemetry.speak("beep beep beep beep");
                                            RobotComponents.front_intake_motor.setPower(1);
                                            CompDrive.TOWER_DOWN_SEQUENCE.get().append(
                                                            new ArbitraryDelayMacro(200),
                                                            /*new AutoMoveMacro(
                                                                    drive, AutoMoveMacro.LateralDirection.Forward,
                                                                    4, 0.5
                                                            ),*/
                                                            new RunActionMacro((o4) -> {
                                                                MacroSequence.begin("stop intake",
                                                                        new ArbitraryDelayMacro(3000),
                                                                        new RunActionMacro((o5) -> {
                                                                            RobotComponents.front_intake_motor.setPower(0);
                                                                            return false;
                                                                        })
                                                                );
                                                                return false;
                                                            })
                                                    )
                                                    .start();


                                            return false;
                                        })


                                );
                                return false;
                            })

                    ).start();

                    return false;
                })


                );
    }

    @Override
    public void loop() {
        RobotComponents.tickSystems(this);

    }

}
