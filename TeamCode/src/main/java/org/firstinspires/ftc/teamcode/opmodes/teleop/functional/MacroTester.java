package org.firstinspires.ftc.teamcode.opmodes.teleop.functional;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.excutil.Input;
import org.firstinspires.ftc.teamcode.macros.Flag;
import org.firstinspires.ftc.teamcode.macros.MacroSequence;
import org.firstinspires.ftc.teamcode.components.RobotComponents;
import org.firstinspires.ftc.teamcode.macros.PathStep;
import org.firstinspires.ftc.teamcode.macros.arm.up.ArmToDumpPointMacro;
import org.firstinspires.ftc.teamcode.macros.arm.up.DumpBucketMacro;
import org.firstinspires.ftc.teamcode.macros.arm.IntakePoseMacro;
import org.firstinspires.ftc.teamcode.macros.arm.down.LowerArmMacro;
import org.firstinspires.ftc.teamcode.macros.arm.up.TuckWristForRiseMacro;
import org.firstinspires.ftc.teamcode.macros.generic.RunActionMacro;
import org.firstinspires.ftc.teamcode.macros.tuckdown.RaiseTuckMacro;
import org.firstinspires.ftc.teamcode.macros.arm.down.TuckWristDownMacro;

import static org.firstinspires.ftc.teamcode.components.RobotComponents.ServoComponent;


@TeleOp(group = "yOld")
public class MacroTester extends LinearOpMode {

    class ServoIntakeMacro extends PathStep {

        @Override
        public void onStart() {

        }

        @Override
        public void onTick(OpMode opMode) {

        }
    }

    Flag debugOn = new Flag();

    Flag intakeOn = new Flag();

    Input input;

    int servoIndex = 0;
    ServoComponent targetedServo = null;
    float servoPos = 0;

    boolean backingOutServo = false;

    int targetUpTowerPosition = 0;

    int goalTicks = 0;
    int lastGoalTicks = 0;

    float liftPower = 0;

    boolean isUp = false;

    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        input = new Input();

        RobotComponents.init(hardwareMap);
        RobotComponents.tower_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        MacroSequence.compose("Init Intake Macro", new IntakePoseMacro()).start();

        waitForStart();

        while (!isStopRequested()) {
            lastGoalTicks = goalTicks;

            RobotComponents.tickSystems(this);

            input.pollGamepad(gamepad1);

            if (input.right_trigger.down() && !MacroSequence.isRunning()) {

                if (!isUp) {


                    MacroSequence.begin(
                            "Lift And Dump Sequence",
                            new IntakePoseMacro(),
                            new TuckWristForRiseMacro(),
                            new ArmToDumpPointMacro(),
                            new DumpBucketMacro(),
                            new RunActionMacro((o) -> {
                               telemetry.speak("get dunked on");
                               return false;
                            })
                    );

                    isUp = true;
                } else {
                    MacroSequence.begin(
                            "Lower and Tuck Sequence",
                            new LowerArmMacro(),
                            new TuckWristDownMacro(),
                            new IntakePoseMacro()
                    );

                    isUp = false;
                }

                /*MacroSequence.compose(

                        new ArbitraryDelayMacro(0),
                        new RaiseTuckMacro(),
                        new ArbitraryDelayMacro(300),
                        new DumpPoseMacro(),
                        new ArbitraryDelayMacro(1200),
                        new LowerArmMacro()

                ).start();*/

                //RobotComponents.tower_motor.setTargetPosition(2830);
                //RobotComponents.wrist_servo.setPosition(0.48);

                //RobotComponents.coroutines.startRoutineLater((a, b) -> CoroutineResult.Stop, 100);

            }

            if (MacroSequence.getActiveMacro() != null) {
                if (MacroSequence.getActiveMacro() instanceof RaiseTuckMacro) {
                    MacroSequence.getActiveMacro().onTick(this);
                } else {
                    telemetry.addData("executing macro", "not raise tuck, " + MacroSequence.getActiveMacro().getClass().getName());
                }
            }

            if (input.a.down() && !backingOutServo) {
                targetedServo = RobotComponents.servos.get(servoIndex);
                servoIndex++;
                if (servoIndex == RobotComponents.servos.size()) {
                    servoIndex = 0;
                }
                //servoPos = 0;
            }

            if (input.x.down()) {
                servoPos -= 0.04f;
            }

            if (input.y.down()) {
                servoPos += 0.04f;
            }

            if (input.b.down()) {
                servoPos = 0;
                targetedServo.servo.setPosition(0);
                targetedServo = null;
            }

            if (targetedServo != null) {
                targetedServo.servo.setPosition(servoPos);

                telemetry.addData("Targeted Servo Goal Position: ", servoPos);
                telemetry.addData("Targeted Servo Name: ", targetedServo.name);
            }



            if (input.right_bumper.down()) {
                liftPower += 0.1f;
                RobotComponents.tower_motor.setPower(liftPower);
            }

            //if (input.right_trigger.down()) {
             //   liftPower -= 0.1f;
            //    RobotComponents.tower_motor.setPower(liftPower);
            //}

            boolean intake = intakeOn.yes();

            double backPower = 1;
            double forePower = 1;
            RobotComponents.back_intake_servo.setPower(intake ? backPower : 0);

            telemetry.addData("Tower Goal Position: ", goalTicks);


            drive.setWeightedDrivePower(
                    new Pose2d(
                            -DriveTest_2.deadZone(-gamepad1.left_stick_y), // swapped 1 & 2
                            DriveTest_2.deadZone(gamepad1.left_stick_x),
                            -DriveTest_2.deadZone(-gamepad1.right_stick_x)
                    )
            );


            drive.update();

            Pose2d poseEstimate = drive.getPoseEstimate();
            telemetry.addData("x", poseEstimate.getX());
            telemetry.addData("y", poseEstimate.getY());
            telemetry.addData("heading", poseEstimate.getHeading());
            telemetry.update();
        }
    }
}
