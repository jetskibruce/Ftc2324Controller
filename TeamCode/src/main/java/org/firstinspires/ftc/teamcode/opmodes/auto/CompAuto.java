package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.components.RobotComponents;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.excutil.MotorPath;
import org.firstinspires.ftc.teamcode.macros.MacroSequence;
import org.firstinspires.ftc.teamcode.macros.arm.IntakePoseMacro;
import org.firstinspires.ftc.teamcode.macros.auto.AutoCorrectHeadingDumb;
import org.firstinspires.ftc.teamcode.macros.auto.AutoMoveMacro;
import org.firstinspires.ftc.teamcode.macros.auto.AutoOuttakePoseMacro;
import org.firstinspires.ftc.teamcode.macros.generic.AwaitConditionMacro;
import org.firstinspires.ftc.teamcode.macros.generic.RunActionMacro;
import org.firstinspires.ftc.teamcode.macros.generic.RunExtraMacro;
import org.firstinspires.ftc.teamcode.macros.tuckdown.ArbitraryDelayMacro;
import org.firstinspires.ftc.teamcode.opmodes.teleop.functional.CompDrive;

@Autonomous(name="RedEndzone CLOSE Placement Auto", group="aCompete")
public class CompAuto extends OpMode {

    protected AutoMoveMacro.LateralDirection strafeDirection = AutoMoveMacro.LateralDirection.Right;
    protected AutoMoveMacro.LateralDirection oppDirection = AutoMoveMacro.LateralDirection.Left;
    protected boolean doOpp = false;

    SampleMecanumDrive drive;

    @Override
    public void init() {
        RobotComponents.init(this.hardwareMap);

        drive = new SampleMecanumDrive(this.hardwareMap);

    }

    static final double DISTANCE_IN = 36;

    ElapsedTime runtime = new ElapsedTime();


    @Override
    public void start() {

        //MacroSequence.setTimeoutMs(90_000);

        //telemetry.speak("Auto robot oh. Auto! Auto!");

        //
        runtime.reset();

        RobotComponents.imu.resetYaw();

        float scootDistanceMultiplier = 0f;
        if (doOpp) scootDistanceMultiplier = 1f;

        float finalScootDistanceMultiplier = scootDistanceMultiplier;
        MacroSequence auto = MacroSequence.compose("Route to Backboard",
                new RunActionMacro((o) -> {
                    RobotComponents.left_pixel_hold_servo.setPosition(
                            CompDrive.PIXEL_HOLD_POSITION
                    );
                    RobotComponents.right_pixel_hold_servo.setPosition(
                            CompDrive.PIXEL_HOLD_POSITION
                    );
                    return false;
                }),
                new AutoMoveMacro(
                        drive, strafeDirection,
                        22.5, 0.5
                        ),
                new AutoCorrectHeadingDumb(
                        drive, 0, 0.04, 4.0
                ).giveTimeout(750),
                new ArbitraryDelayMacro(400),
                new AutoMoveMacro(
                        drive, AutoMoveMacro.LateralDirection.Backward,
                        35, 0.35
                        ),
                new RunActionMacro((o) -> {
                        CompDrive.TOWER_UP_SEQUENCE.get().append(
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
                                                                new AutoMoveMacro(
                                                                        drive, AutoMoveMacro.LateralDirection.Forward,
                                                                        4, 0.5
                                                                ),
                                                                new AutoCorrectHeadingDumb(
                                                                        drive, 0, 0.04, 2.5
                                                                ).giveTimeout(750),
                                                                new AutoMoveMacro(
                                                                        drive, oppDirection,
                                                                        18 * finalScootDistanceMultiplier, 0.4
                                                                ),
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

        auto.start();



    }

    @Override
    public void loop() {
        RobotComponents.tickSystems(this);
        MacroSequence.appendDebugTo(telemetry);
        YawPitchRollAngles orientation = RobotComponents.imu.getRobotYawPitchRollAngles();
        telemetry.addData("Orientation: ",
                "H (Zrot): " + orientation.getYaw(AngleUnit.DEGREES) +
                        ", P (Xrot): " + orientation.getPitch(AngleUnit.DEGREES) +
                        ", R (Yrot): " + orientation.getRoll(AngleUnit.DEGREES));
    }
}
