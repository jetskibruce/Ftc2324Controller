package org.firstinspires.ftc.teamcode.opmodes.teleop.debug;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.excutil.Input;
import org.firstinspires.ftc.teamcode.excutil.Flag;
import org.firstinspires.ftc.teamcode.opmodes.teleop.functional.DriveTest_2;


@TeleOp(name = "Wheel Debugger", group="zDebug")
public class WheelDebugger extends LinearOpMode {

    Flag debugOn = new Flag();

    Input input;

    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        input = new Input();

        Flag spin0 = new Flag(), spin1 = new Flag(), spin2 = new Flag(), spin3 = new Flag();
        waitForStart();
        float force = 0.3f;

        /*
        0 - front left
        1 - front right
        2 - back left
        3 - back right
         */
        float[] wheelMultipliers = new float[] {
                1f, 1f, 1f, 1f
        };

        int targetedWheelToModify = -1;

        while (!isStopRequested()) {

            input.pollGamepad(gamepad1);

            int newTarget = -999;
            if (input.dpad_up.down()) {
                newTarget = 0;
            } else if (input.dpad_right.down()) {
                newTarget = 1;
            } else if (input.dpad_left.down()) {
                newTarget = 2;
            } else if (input.dpad_down.down()) {
                newTarget = 3;
            }

            if (targetedWheelToModify == newTarget) {
                targetedWheelToModify = -1;
            } else if (newTarget != -999) {
                targetedWheelToModify = newTarget;
            }

            if (input.right_trigger.down()) {
                force = 0.3f;
                spin0 = new Flag(); spin1 = new Flag(); spin2 = new Flag(); spin3 = new Flag();

                debugOn.toggle();
            }

            if (!debugOn.yes()) {

                if (targetedWheelToModify != -1) {
                    if (input.y.down()) {
                        wheelMultipliers[targetedWheelToModify] += 0.04;
                    } else if (input.a.down()) {
                        wheelMultipliers[targetedWheelToModify] -= 0.04;
                    }
                }

                drive.setWheelMultipliers(wheelMultipliers);

                telemetry.addData("Mults:", "FL: " + wheelMultipliers[0] + ", RL: " + wheelMultipliers[1] + ", BL: " + wheelMultipliers[2] + ", BR: " + wheelMultipliers[3]);


                drive.setWeightedDrivePower(
                        new Pose2d(
                                -DriveTest_2.deadZone(-gamepad1.left_stick_y), // swapped 1 & 2
                                DriveTest_2.deadZone(gamepad1.left_stick_x),
                                -DriveTest_2.deadZone(-gamepad1.right_stick_x)
                        )
                );
            } else {
                telemetry.addData("Right trigger to disable debug individuals", "");
                telemetry.addData("XABY to toggle wheels", "Left stick up/down for power");

                float v0 = 0, v1 = 0, v2 = 0, v3 = 0;

                if (gamepad1.left_stick_y < -0.5) {
                    force += 0.005f;
                } else if (gamepad1.left_stick_y > 0.5){
                    force -= 0.005f;
                }

                if (input.a.down()) {
                    spin0.toggle();
                }
                v0 = (spin0.yes()) ? force : 0;

                if (input.b.down()) {
                    spin1.toggle();
                }
                v1 = (spin1.yes()) ? force : 0;

                if (input.x.down()) {
                    spin2.toggle();
                }
                v2 = (spin2.yes()) ? force : 0;

                if (input.y.down()) {
                    spin3.toggle();
                }
                v3 = (spin3.yes()) ? force : 0;


                drive.setMotorPowers(v0, v1, v2, v3);

                telemetry.addData("Force", force);
                telemetry.addData("Wheel Velocities", v0 + ", " + v1 + ", " + v2 + ", " + v3);
            }

            drive.update();

            Pose2d poseEstimate = drive.getPoseEstimate();
            telemetry.addData("x", poseEstimate.getX());
            telemetry.addData("y", poseEstimate.getY());
            telemetry.addData("heading", poseEstimate.getHeading());

            telemetry.addData("Right trigger to enable debug individuals", "");
            telemetry.update();
        }
    }
}
