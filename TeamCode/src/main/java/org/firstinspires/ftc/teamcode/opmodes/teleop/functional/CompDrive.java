package org.firstinspires.ftc.teamcode.opmodes.teleop.functional;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.components.RobotComponents;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.excutil.Input;
import org.firstinspires.ftc.teamcode.excutil.RMath;
import org.firstinspires.ftc.teamcode.macros.Flag;
import org.firstinspires.ftc.teamcode.macros.MacroSequence;
import org.firstinspires.ftc.teamcode.macros.arm.IntakePoseMacro;
import org.firstinspires.ftc.teamcode.macros.arm.down.LowerArmMacro;
import org.firstinspires.ftc.teamcode.macros.arm.down.TuckWristDownMacro;
import org.firstinspires.ftc.teamcode.macros.arm.up.ArmToDumpPointMacro;
import org.firstinspires.ftc.teamcode.macros.arm.up.DumpBucketMacro;
import org.firstinspires.ftc.teamcode.macros.arm.up.TuckWristForRiseMacro;
import org.firstinspires.ftc.teamcode.macros.generic.RunActionMacro;

@TeleOp(group = "drive", name = "Competition Drive")
public class CompDrive extends OpMode {

    View androidUI;

    private Input input;

    private int lastTowerGoalTicks = 0;

    private SampleMecanumDrive drive = null;

    private int current_pos = 0;
    private int next_pos = 0;

    private boolean isArmUp = false;  // Use initializers

    @Override
    public void start() {
        if (Math.random() < 0.12)
            if (Math.random() < 0.8)
                telemetry.speak("Beep boop beep boop... I am a robot.");
            else
                telemetry.speak("File the intake again.");

        setUIColor(Color.GRAY);
    }

    @Override
    public void init() {

        drive = new SampleMecanumDrive(hardwareMap);

        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        input = new Input();

        RobotComponents.init(hardwareMap);
        RobotComponents.tower_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RobotComponents.climb_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // RE_ENABLE BEFORE POSES
        //MacroSequence.compose("Init Intake Macro", new IntakePoseMacro()).start();

        intakeOn.toggle();

        isArmUp = false;  // Use initializers
        // Start in the 0 position
        RobotComponents.tower_motor.setTargetPosition(0);
        RobotComponents.climb_motor.setTargetPosition(0);
        telemetry.addData("tower pos ", RobotComponents.tower_motor.getCurrentPosition());
        telemetry.addData("climb pos ", RobotComponents.climb_motor.getCurrentPosition());
        RobotComponents.tower_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        RobotComponents.tower_motor.setPower(0.3);
        RobotComponents.climb_motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        RobotComponents.climb_motor.setPower(CLIMB_POWER);

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

        telemetry.addData("climb pos ", RobotComponents.climb_motor.getCurrentPosition());

        telemetry.update();

    }


    public void pollClimbInputs() {

        current_pos = RobotComponents.climb_motor.getCurrentPosition();

        if (input.x.down() && current_pos < 100_000) {
           next_pos = current_pos + 45_000;
        } else if (input.y.down()  && current_pos > 0) {
            next_pos = current_pos - 45_000;
        }

        if (gamepad1.back) {
            RobotComponents.climb_motor.setPower(0);
            RobotComponents.climb_motor.setTargetPosition(RobotComponents.climb_motor.getCurrentPosition());
        } else {
            RobotComponents.climb_motor.setPower(CLIMB_POWER);
            RobotComponents.climb_motor.setTargetPosition(next_pos);
        }

    }

    public void pollTowerInputs() {
        if ((input.right_trigger.down() || input.left_trigger.down())&& !MacroSequence.isRunning()) {

            if (input.right_trigger.down()) {
                MacroSequence.begin(
                        "Lower and Tuck Sequence",
                        new LowerArmMacro(),
                        new TuckWristDownMacro(),
                        new IntakePoseMacro(),
                        new RunActionMacro((o) -> isArmUp = false)
                );

            } else {
                if (input.left_trigger.down()) {
                    MacroSequence.begin(
                            "Lift And Dump Sequence",
                            new IntakePoseMacro(),
                            new TuckWristForRiseMacro(),
                            new ArmToDumpPointMacro(),
                            new DumpBucketMacro(),
                            new RunActionMacro((o) -> {
                                //telemetry.speak("get dunked on");
                                isArmUp = true;
                                return false;
                            })
                    );
                }
            }
        }
    }

    int direction = -999;

    double speedMult = 0.66f;

    public void pollIntakeInputs() {
        int newDir = -999;
        if (input.left_bumper.down()) {
            newDir = 1;
        } else if (input.right_bumper.down()) {
            newDir = -1;
        }

        if (input.x.down()) speedMult -= 0.02;
        if (input.y.down()) speedMult += 0.02;

        speedMult = RMath.clamp(speedMult, 0, 1);

        // if you hit same button that's already on, it turns off
        if (newDir == direction) {
            direction = 0;
        } else if (newDir != -999) {
            direction = newDir;
        }

        telemetry.addData("Intake on in dir: ", direction);

        RobotComponents.back_intake_servo.setPower(-direction * speedMult);
        RobotComponents.front_intake_motor.setPower(-direction * speedMult);
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
            telemetry.speak((flipControls) ? "Reverse, reverse." : "Ahead standard.");
            if (flipControls)
                setUIColor(Color.DKGRAY);
            else
                setUIColor(Color.GRAY);
        }

        if (input.dpad_left.down()) {
            if (inputMult == normalInputMult) {
                inputMult = slowInputMult;
                telemetry.speak("Slowing down.");
            }
            else if (inputMult == slowInputMult) {
                inputMult = preciseInputMult;
                telemetry.speak("Snail's pace.");
            }
            else if (inputMult == preciseInputMult) {
                inputMult = normalInputMult;
                telemetry.speak("Speed normalized.");
            }

        }





        drive.setWeightedDrivePower(
                new Pose2d(
                        -DriveTest_2.deadZone(-gamepad1.left_stick_y) * ((flipControls) ? -1 : 1), // swapped 1 & 2
                        DriveTest_2.deadZone(gamepad1.left_stick_x) * ((flipControls) ? -1 : 1),
                        -DriveTest_2.deadZone(-gamepad1.right_stick_x) * ((flipControls) ? -1 : 1)
                ).times(inputMult)
        );


        drive.update();

        Pose2d poseEstimate = drive.getPoseEstimate();
        telemetry.addData("x", poseEstimate.getX());
        telemetry.addData("y", poseEstimate.getY());
        telemetry.addData("heading", poseEstimate.getHeading());
    }
}
