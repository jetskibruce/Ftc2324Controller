package org.firstinspires.ftc.teamcode.opmodes.teleop.functional;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.android.AndroidSoundPool;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.components.RobotComponents;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.excutil.Input;
import org.firstinspires.ftc.teamcode.excutil.MotorPath;
import org.firstinspires.ftc.teamcode.excutil.RMath;
import org.firstinspires.ftc.teamcode.excutil.coroutines.CoroutineAction;
import org.firstinspires.ftc.teamcode.excutil.coroutines.CoroutineResult;
import org.firstinspires.ftc.teamcode.macros.Flag;
import org.firstinspires.ftc.teamcode.macros.MacroSequence;
import org.firstinspires.ftc.teamcode.macros.arm.IntakePoseMacro;
import org.firstinspires.ftc.teamcode.macros.arm.down.LowerArmMacro;
import org.firstinspires.ftc.teamcode.macros.arm.down.TuckWristDownMacro;
import org.firstinspires.ftc.teamcode.macros.arm.up.ArmToDumpPointMacro;
import org.firstinspires.ftc.teamcode.macros.arm.up.DumpBucketMacro;
import org.firstinspires.ftc.teamcode.macros.arm.up.TuckWristForRiseMacro;
import org.firstinspires.ftc.teamcode.macros.generic.RunActionMacro;
import org.firstinspires.ftc.teamcode.macros.tuckdown.ArbitraryDelayMacro;

import java.util.function.Supplier;

import javax.crypto.Mac;

@TeleOp(group = "aCompete", name = "Competition Drive")
public class CompDrive extends OpMode {

    View androidUI;

    private Input input;

    private int lastTowerGoalTicks = 0;

    private SampleMecanumDrive drive = null;



    private int current_pos = 0;
    private int next_pos = 0;

    private boolean isArmUp = false;  // Use initializers

    public static final double PIXEL_RELEASE_POSITION = 0.5;
    public static final double PIXEL_HOLD_POSITION = 1.0;
    private static final double CLIMBER_HOLD_POSITION = 0.87;
    private static final double CLIMBER_RELEASE_POSITION = 0.623;

    private static final double LAUNCH_HOLD_POSITION = 0.555;
    private static final double LAUNCH_RELEASE_POSITION = 0;

    public static final double ARM_RETRACT_POSITION = 0.7;
    public static final double ARM_FULL_EXTEND = 0.0;
    public static final double ARM_HALF_EXTEND = (ARM_RETRACT_POSITION + ARM_FULL_EXTEND) / 2.0;

    public static final Supplier<MacroSequence> TOWER_UP_SEQUENCE =
            () -> MacroSequence.compose(
                "Lift And Dump Sequence",
                //new IntakePoseMacro(),
                new TuckWristForRiseMacro(),
                new ArmToDumpPointMacro()
                //new DumpBucketMacro(),
            );

    public static final Supplier<MacroSequence> TOWER_DOWN_SEQUENCE =
            () -> MacroSequence.compose(
                "Lower and Tuck Sequence",
                new LowerArmMacro(),
                new TuckWristDownMacro(),
                new IntakePoseMacro()
            );


    @Override
    public void start() {
        if (Math.random() < 0.2)
            if (Math.random() < 0.5) {
                telemetry.speak("Beep boop beep boop... I am a robot.");
            } else {
                if (Math.random() < 0.5) {
                    telemetry.speak("What's up, Doc");
                } else {
                    telemetry.speak("File the intake again.");
                }
            }

        setUIColor(Color.GRAY);



    }

    @Override
    public void init() {


        //telemetry.speak("Arm the drone again");

        drive = new SampleMecanumDrive(hardwareMap);

        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        input = new Input();

        RobotComponents.init(hardwareMap);
        RobotComponents.tower_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //RobotComponents.climb_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        RobotComponents.left_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION);
        RobotComponents.right_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION);
        RobotComponents.climber_clasp_servo.setPosition(CLIMBER_HOLD_POSITION);
        // RE_ENABLE BEFORE POSES
        //MacroSequence.compose("Init Intake Macro", new IntakePoseMacro()).start();

        RobotComponents.extendo_servo.setPosition(ARM_RETRACT_POSITION);

        RobotComponents.launch_servo.setPosition(LAUNCH_HOLD_POSITION);

        intakeOn.toggle();

        isArmUp = false;  // Use initializers
        // Start in the 0 position
        RobotComponents.tower_motor.setTargetPosition(0);
        //RobotComponents.climb_motor.setTargetPosition(0);
        telemetry.addData("tower pos ", RobotComponents.tower_motor.getCurrentPosition());
        //telemetry.addData("climb pos ", RobotComponents.climb_motor.getCurrentPosition());
        RobotComponents.tower_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        RobotComponents.tower_motor.setPower(0.3);
        //RobotComponents.climb_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //RobotComponents.climb_motor.setPower(CLIMB_POWER);

        int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
        androidUI = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);

    }



    double CLIMB_POWER = 1;

    private void setUIColor(int color) {
        if (androidUI == null) return;
        androidUI.post(new Runnable() {
            public void run() {
                androidUI.setBackgroundColor(color);
            }
        });
    }

    Flag intakeOn = new Flag();



    @Override
    public void loop() {



        RobotComponents.tickSystems(this);
//
//        RobotComponents.tower_motor.setTargetPosition(-80);
//        telemetry.addData("tower pos ", RobotComponents.tower_motor.getCurrentPosition());
//        RobotComponents.tower_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        RobotComponents.tower_motor.setPower(0.3);

        input.pollGamepad(gamepad1);



        pollTowerInputs();

        pollClimbInputs();

        pollIntakeInputs();

        pollDriveInputs();

        pollPixelInputs();


       // telemetry.addData("climb pos ", RobotComponents.climb_motor.getCurrentPosition());

        YawPitchRollAngles orientation = RobotComponents.imu.getRobotYawPitchRollAngles();

        telemetry.addData("Orientation: ",
                "H (Zrot): " + orientation.getYaw(AngleUnit.DEGREES));
                        //", P (Xrot): " + orientation.getPitch(AngleUnit.DEGREES) +
                        //", R (Yrot): " + orientation.getRoll(AngleUnit.DEGREES));

        //MacroSequence.setTimeoutMs(100_000_000);
        MacroSequence.appendDebugTo(telemetry);

        telemetry.update();

    }

    public void pollPixelInputs() {

        if (input.a.down()) {
            RobotComponents.left_pixel_hold_servo.setPosition(PIXEL_HOLD_POSITION);
            RobotComponents.right_pixel_hold_servo.setPosition( PIXEL_HOLD_POSITION);
        }
        else if (input.b.down()) {
                RobotComponents.left_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION);
                RobotComponents.right_pixel_hold_servo.setPosition(PIXEL_RELEASE_POSITION);
        }
    }

    boolean initializedClimb = false;

    public void pollClimbInputs(){
        if (!initializedClimb) {
            telemetry.addData("PRESS BACK + Y TO INIT CLIMB", "");
        } else {
            telemetry.addData("HOLD Y TO CLIMB", "");
        }

        if (gamepad1.back && input.x.down()) {
            RobotComponents.launch_servo.setPosition(LAUNCH_RELEASE_POSITION);
        }


        if ((gamepad1.back || initializedClimb) && gamepad1.y) {
            RobotComponents.climb_motor.setPower(1);
            RobotComponents.climber_clasp_servo.setPosition(CLIMBER_RELEASE_POSITION);

            if (!initializedClimb) {
                try {
                    RobotComponents.soundPool.play("turret-lock.mp3");
                } catch (Exception e) {

                }
            }

            initializedClimb = true;

        }
        else {
            RobotComponents.climb_motor.setPower(0);
        }
    }




    /*public void pollClimbInputs() {

        current_pos = RobotComponents.climb_motor.getCurrentPosition();

        if (input.y.down() && current_pos < 100000) {
           next_pos = current_pos + 65000;
            RobotComponents.climber_clasp_servo.setPosition(CLIMBER_RELEASE_POSITION);
        }

        if (gamepad1.back) {
            RobotComponents.climb_motor.setPower(0);
            RobotComponents.climb_motor.setTargetPosition(RobotComponents.climb_motor.getCurrentPosition());

        } else {
            RobotComponents.climb_motor.setPower(CLIMB_POWER);
            RobotComponents.climb_motor.setTargetPosition(next_pos);
        }

    }*/


    int intakeIn = 1;
    int intakeOut = -1;

    int activeExtendoStepIndex = 0;

    public CoroutineAction smallStepExtendo(double current, double goal, int stepCount) {

        double[] steps = new double[stepCount];

        double stepSize = (goal - current) / stepCount;

        if (goal < current) stepSize *= -1;

        for (int i = 0; i < stepCount; i++) {
            steps[i] = current + (stepSize * i);
        }

        return (opmode, data) -> {

            // delay between steps
            if (data.MsAlive < (activeExtendoStepIndex * 40)) return CoroutineResult.Continue;

            if (activeExtendoStepIndex >= stepCount - 1) {
                return CoroutineResult.Stop;
            }

            RobotComponents.extendo_servo.setPosition(steps[activeExtendoStepIndex]);

            activeExtendoStepIndex++;

            return CoroutineResult.Continue;
        };
    }


    int extensionStatus = 0;

    public void pollTowerInputs() {
        if ((input.right_trigger.down() || input.left_trigger.down())&& !MacroSequence.isRunning()) {

            if (input.left_trigger.down()) {
                if (isArmUp == false) {
                    return;
                }

                TOWER_DOWN_SEQUENCE.get().append(
                        new RunActionMacro((o) -> {
                            isArmUp = false;
                            MotorPath.runToPosition(RobotComponents.tower_motor, 0, 0.2);
                            return false;
                        })
                ).start();

            } else {
                if (input.right_trigger.down()) {
                    if (isArmUp == true) {
                        /*ArmToDumpPointMacro.TUNED_ARM_POS -= 25;
                        if (ArmToDumpPointMacro.TUNED_ARM_POS < (ArmToDumpPointMacro.DEFAULT_TUNED_ARM_POS - 250)) {
                            ArmToDumpPointMacro.TUNED_ARM_POS = ArmToDumpPointMacro.DEFAULT_TUNED_ARM_POS;
                        }
                        ArmToDumpPointMacro.runToTunedArmPos(0.6);*/

                        if (extensionStatus == 2) {
                            /*activeExtendoStepIndex = 0;
                            RobotComponents.coroutines.startRoutine(smallStepExtendo(
                                    ARM_FULL_EXTEND, ARM_RETRACT_POSITION,
                                    10
                            ));*/
                            extensionStatus = 0;
                            RobotComponents.extendo_servo.setPosition(ARM_RETRACT_POSITION);
                        } else if (extensionStatus == 1) {
                            /*activeExtendoStepIndex = 0;
                            RobotComponents.coroutines.startRoutine(smallStepExtendo(
                                    ARM_HALF_EXTEND, ARM_FULL_EXTEND,
                                    10
                            ));*/
                            extensionStatus = 2;
                            RobotComponents.extendo_servo.setPosition(ARM_FULL_EXTEND);
                        } else {
                            /*RobotComponents.coroutines.startRoutine(smallStepExtendo(
                                    ARM_RETRACT_POSITION, ARM_HALF_EXTEND,
                                    10
                            ));*/

                            extensionStatus = 1;
                            RobotComponents.extendo_servo.setPosition(ARM_HALF_EXTEND);
                        }

                        return;
                    }

                    extensionStatus = 0;

                    TOWER_UP_SEQUENCE.get().append(
                            new RunActionMacro((o) -> {
                                telemetry.speak("standing by");
                                isArmUp = true;
                                return false;
                            })
                    ).start();
                }
            }
        }
    }

    int direction = -999;

    double speedMult = 1f;

    public void pollIntakeInputs() {
        int newDir = -999;
        if (input.left_bumper.held()) {
            newDir = intakeIn;
        } else if (input.right_bumper.held()) {
            newDir = intakeOut;
        } else {
            newDir = direction;
        }

        //if (input.x.down()) speedMult -= 0.02;
        //if (input.y.down()) speedMult += 0.02;

        speedMult = RMath.clamp(speedMult, 0, 1);

        // if you hit same button that's already on, it turns off - unless it is running for tower
        if (!MacroSequence.isRunning()) {
            if (newDir == direction) {
                direction = 0;
            } else if (newDir != -999) {
                direction = newDir;
            }
            telemetry.addData("Intake on in dir: ", direction);

            RobotComponents.back_intake_servo.setPower(direction * speedMult);
            RobotComponents.front_intake_motor.setPower(direction * speedMult);
        }
    }

    public boolean flipControls = false;

    public double inputMult = normalInputMult;

    private static final double normalInputMult = 1.0;
    private static final double slowInputMult = 0.8;
    private static final double preciseInputMult = 0.6;

    ElapsedTime periodicAlertTimer = new ElapsedTime();

    public void pollDriveInputs() {

        if (input.dpad_down.down()) {
            flipControls = !flipControls;
            telemetry.speak((flipControls) ? "Reverse." : "Normal.");
            if (flipControls)
                setUIColor(Color.DKGRAY);
            else
                setUIColor(Color.GRAY);
        }

        if (input.dpad_left.down()) {
            if (inputMult == normalInputMult) {
                inputMult = slowInputMult;
                telemetry.speak("Slow.");
            }
            else if (inputMult == slowInputMult) {
                inputMult = preciseInputMult;
                telemetry.speak("Crawl.");
            }
            else if (inputMult == preciseInputMult) {
                inputMult = normalInputMult;
                telemetry.speak("Fast.");
            }
        }

        if (input.dpad_up.down() && !MacroSequence.isRunning()) {
            MacroSequence.begin("Fix Bucket Pos",
                    new TuckWristForRiseMacro(),
                    new ArmToDumpPointMacro().limitToJustUp(),
                    new ArbitraryDelayMacro(100),
                    new TuckWristDownMacro(),
                    new IntakePoseMacro()
            );
        }





        drive.setWeightedDrivePower(
                new Pose2d(
                        -DriveTest_2.deadZone(-gamepad1.left_stick_y) * ((flipControls) ? -1 : 1), // swapped 1 & 2
                        DriveTest_2.deadZone(gamepad1.left_stick_x) * ((flipControls) ? -1 : 1),
                        -DriveTest_2.deadZone(-gamepad1.right_stick_x)// * ((flipControls) ? -1 : 1)
                ).times(inputMult)
        );


        drive.update();

        Pose2d poseEstimate = drive.getPoseEstimate();
        telemetry.addData("x", poseEstimate.getX());
        telemetry.addData("y", poseEstimate.getY());
        telemetry.addData("heading", poseEstimate.getHeading());
    }
}
