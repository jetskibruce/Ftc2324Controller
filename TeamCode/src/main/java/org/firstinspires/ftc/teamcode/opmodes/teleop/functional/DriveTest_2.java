package org.firstinspires.ftc.teamcode.opmodes.teleop.functional;

//import com.outoftheboxrobotics.photoncore.PhotonCore;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.excutil.EncodedPath;
import org.firstinspires.ftc.teamcode.excutil.Input;
import org.firstinspires.ftc.teamcode.excutil.PathManager;
import org.firstinspires.ftc.teamcode.excutil.RMath;
import org.firstinspires.ftc.teamcode.excutil.coroutines.CoroutineManager;
import org.firstinspires.ftc.teamcode.excutil.coroutines.CoroutineResult;

@Disabled
@TeleOp(name = "DriveTest_2", group = "debug")
public class DriveTest_2 extends OpMode {

    private DcMotor front_left = null;
    private DcMotor front_right = null;
    private DcMotor back_left = null;
    private DcMotor back_right = null;

    Gamepad currentGamepad1;
    Gamepad previousGamepad1;

    private CRServo intake_servo = null;

    private Servo intake_tilt_servo = null;

    private Servo back_wrist_servo = null;
    private Servo front_wrist_servo = null;

    static double BACK_WRIST_MAX_UP = 0.77;
    static double BACK_WRIST_MAX_DOWN = 0.32;

    static double FRONT_WRIST_MAX_UP = 0.77;
    static double FRONT_WRIST_MAX_DOWN = 0.17;

    EncodedPath tiltTestPath = null;

    @Override
    public void init() {

        front_left = hardwareMap.get(DcMotor.class, "front_left");
        front_right = hardwareMap.get(DcMotor.class, "front_right");
        back_left = hardwareMap.get(DcMotor.class, "back_left");
        back_right = hardwareMap.get(DcMotor.class, "back_right");

        intake_servo = hardwareMap.get(CRServo.class, "intake_servo");
        intake_tilt_servo = hardwareMap.get(Servo.class, "intake_tilt_servo");

        back_wrist_servo = hardwareMap.get(Servo.class, "back_wrist_servo");
        front_wrist_servo = hardwareMap.get(Servo.class, "front_wrist_servo");

        front_left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        front_right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        back_left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        back_right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        front_left.setDirection(DcMotorSimple.Direction.REVERSE);
        front_right.setDirection(DcMotorSimple.Direction.REVERSE);
        //back_right.setDirection(DcMotorSimple.Direction.REVERSE);

        // front_right.setDirection(DcMotor.Direction.REVERSE);
        currentGamepad1 = new Gamepad();
        previousGamepad1 = new Gamepad();

        input = new Input();

        intake_tilt_servo.setPosition(TILT_UP);

        back_wrist_servo.setPosition(BACK_WRIST_MAX_DOWN);
        front_wrist_servo.setPosition(FRONT_WRIST_MAX_DOWN);

        pathing = new PathManager(this);

        tiltTestPath = pathing.create()
                .finish(
                        (path) -> {
                            if (path.isFirstExecutionTick()) {
                                intake_tilt_servo.setPosition(TILT_DOWN);
                            }

                            return path.timedOut(1000) ||
                                    RMath.approx(intake_tilt_servo.getPosition(), TILT_DOWN, 0.05);
                        }
                ).finish(
                        (path) -> {
                            if (path.isFirstExecutionTick()) {
                                intake_tilt_servo.setPosition(TILT_UP);
                            }

                            return path.timedOut(1000) ||
                                    RMath.approx(intake_tilt_servo.getPosition(), TILT_UP, 0.05);
                        }
                );
    }


    double TILT_DOWN = 0.49;

    double TILT_UP = 0.38;

    Input input;

    boolean up = true;

    boolean spinForward = false;
    boolean shouldSpin = false;

    private CoroutineManager coroutines = new CoroutineManager();

    private PathManager pathing;

    private static float deadZoneThreshold = 0.6f;

    float deadZoneImproved(float x) {
        return (float)(
                (Math.abs(x) < deadZoneThreshold)
                        ? Math.pow(-8, -Math.pow(x, 2)) + 1.125
                        : 0
        );
    }

    public static float deadZone(float val) {
        return (val > -deadZoneThreshold && val < deadZoneThreshold) ? 0 : val;
    }

    @Override
    public void loop() {

        pathing.tickAll();

        coroutines.tick(this);

        input.pollGamepad(gamepad1);

        double drive = -deadZone(gamepad1.left_stick_x);
        double strafe = -deadZone(gamepad1.left_stick_y);
        double twist = -deadZone(gamepad1.right_stick_x);

        double[] speeds = {
                (drive + strafe + twist),
                (drive - strafe - twist),
                (drive - strafe + twist),
                (drive + strafe - twist)
        };
        double max = Math.abs(speeds[0]);
        for (int i = 0; i < speeds.length; i++) {
            if (max < Math.abs(speeds[i])) max = Math.abs(speeds[i]);
        }
        if (max > 1) {
            for (int i = 0; i < speeds.length; i++) speeds[i] /= max;
        }

        front_left.setPower(speeds[0]);
        front_right.setPower(speeds[1]);
        back_left.setPower(speeds[2]);
        back_right.setPower(speeds[3]);

        if (input.y.down()) {
            tiltTestPath.start();
        }

        if (input.x.down()) {
            intake_servo.setPower(0);
            coroutines.startRoutineLater((op, data) -> {
                up = !up;
                if (up) {
                    intake_tilt_servo.setPosition(TILT_UP);
                } else {
                    intake_tilt_servo.setPosition(TILT_DOWN);
                }
                return CoroutineResult.Stop;
            }, 200);
        }

        if (input.a.down()) {
            up = false;
            tiltIntakeWithPower(TILT_DOWN, 1);
        }

        if (input.b.down()) {
            up = false;
            tiltIntakeWithPower(TILT_DOWN, -1);
        }

        if (gamepad1.right_trigger > 0.5f) {
            intake_servo.setPower(0);
        }

        telemetry.addData("tilt pos", intake_tilt_servo.getPosition());
    }

    private void tiltIntakeWithPower(double tiltPos, double power) {
        intake_tilt_servo.setPosition(tiltPos);

        coroutines.startRoutineLater((op, data) -> {
            intake_servo.setPower(power);
            return CoroutineResult.Stop;
        }, 200);
    }
}
