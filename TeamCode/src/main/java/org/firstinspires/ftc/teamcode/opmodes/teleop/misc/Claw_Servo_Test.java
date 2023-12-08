
package org.firstinspires.ftc.teamcode.opmodes.teleop.misc;

//import com.outoftheboxrobotics.photoncore.PhotonCore;
        import com.qualcomm.robotcore.eventloop.opmode.OpMode;
        import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
        import com.qualcomm.robotcore.hardware.CRServo;
        import com.qualcomm.robotcore.hardware.DcMotor;
        import com.qualcomm.robotcore.hardware.Gamepad;
        import com.qualcomm.robotcore.hardware.Servo;




@TeleOp(name="Claw_Servo_Test")
public class Claw_Servo_Test extends OpMode {
    private DcMotor front_left  = null;
    private DcMotor front_right = null;
    private DcMotor back_left   = null;
    private DcMotor back_right  = null;

    private DcMotor tower_motor  = null;
    private CRServo intake_servo = null;
    private Servo intake_tilt_servo = null;
    private Servo front_claw_servo = null;
    private Servo back_claw_servo = null;
    private Servo front_wrist_servo = null;
    private Servo back_wrist_servo = null;

    // Variables for controlling servo update rate
    private int servoUpdateCounter = 0;
    private int servoUpdateInterval = 50;
    Servo test_servo = front_claw_servo;
    Gamepad currentGamepad1;
    Gamepad previousGamepad1;

    static double FRONT_CLAW_RELEASE = 0.61;
    static double FRONT_CLAW_GRAB = 0.44;
    static double BACK_CLAW_RELEASE = 0.45;
    static double BACK_CLAW_GRAB = 0.69;

    static double BACK_WRIST_MAX_UP = 0.77;
    static double BACK_WRIST_MAX_DOWN = 0.32;

    static double FRONT_WRIST_MAX_UP = 0.77;
    static double FRONT_WRIST_MAX_DOWN = 0.17;

    static double MIN_SERVO = 0;
    static double MAX_SERVO = 1;

    private void increment_servo(Servo active_servo) {
        double at = active_servo.getPosition();
        if (at < MAX_SERVO) {
            at += .01;
            active_servo.setPosition(at);
        }
        else {
            active_servo.setPosition(MAX_SERVO);
        }
    }

    private void decrement_servo(Servo active_servo) {
        double at = active_servo.getPosition();
        if (at > MIN_SERVO) {
            at -= .01;
            active_servo.setPosition(at);
        }
        else {
            active_servo.setPosition(MIN_SERVO);
        }
    }

    @Override
    public void init() {

            front_left   = hardwareMap.get(DcMotor.class, "front_left");
            front_right  = hardwareMap.get(DcMotor.class, "front_right");
            back_left    = hardwareMap.get(DcMotor.class, "back_left");
            back_right   = hardwareMap.get(DcMotor.class, "back_right");

            tower_motor  = hardwareMap.get(DcMotor.class, "tower_motor");

            intake_servo = hardwareMap.get(CRServo.class, "intake_servo");
            intake_tilt_servo = hardwareMap.get(Servo.class, "intake_tilt_servo");
            front_claw_servo = hardwareMap.get(Servo.class, "front_claw_servo");
            back_claw_servo = hardwareMap.get(Servo.class, "back_claw_servo");
            front_wrist_servo = hardwareMap.get(Servo.class, "front_wrist_servo");
            back_wrist_servo = hardwareMap.get(Servo.class, "back_wrist_servo");

        front_left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        front_right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        back_left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        back_right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        front_right.setDirection(DcMotor.Direction.REVERSE);
        currentGamepad1 = new Gamepad();
        previousGamepad1 = new Gamepad();

    }

    @Override
    public void loop() {

        telemetry.addData("front_claw_servo:",front_claw_servo.getPosition());
        telemetry.addData("back_claw_servo:",back_claw_servo.getPosition());
        telemetry.addData("front_wrist_servo:",front_wrist_servo.getPosition());
        telemetry.addData("back_wrist_servo:",back_wrist_servo.getPosition());
        telemetry.addData("intake_tilt_servo:",intake_tilt_servo.getPosition());
        telemetry.addData("tower_motor:",tower_motor.getCurrentPosition());

        double drive = gamepad1.left_stick_y;
        double strafe = -gamepad1.left_stick_x;
        double twist = -gamepad1.right_stick_x;
        double raise = gamepad1.right_stick_y;
        boolean intake_out = gamepad1.left_bumper;
        boolean intake_in = gamepad1.right_bumper;
        boolean right_trigger = gamepad1.right_trigger > .1;
        boolean left_trigger = gamepad1.left_trigger > .1;

        // Only update the servo position every servoUpdateInterval iterations
        if (servoUpdateCounter >= servoUpdateInterval) {

            if (left_trigger) {
                decrement_servo(test_servo);
            }
            else {
                if (right_trigger) {
                    increment_servo(test_servo);
                }
            }
            // Reset the update counter
            servoUpdateCounter = 0;
        } else {
            // Increment the update counter
            servoUpdateCounter++;
        }

        if (gamepad1.a) {
            //test_servo = front_claw_servo;
            front_claw_servo.setPosition(FRONT_CLAW_GRAB);
            back_claw_servo.setPosition(BACK_CLAW_GRAB);
        } else if (gamepad1.b) {
            //test_servo = back_claw_servo;
            front_claw_servo.setPosition(FRONT_CLAW_RELEASE);
            back_claw_servo.setPosition(BACK_CLAW_RELEASE);
        } else if (gamepad1.x) {
            test_servo = front_wrist_servo;
        } else if (gamepad1.y) {
            test_servo = back_wrist_servo;
        }

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
//
//        front_left.setPower(speeds[0]);
//        front_right.setPower(speeds[1]);
//        back_left.setPower(speeds[2]);
//        back_right.setPower(speeds[3]);
        tower_motor.setPower(raise);

        if (intake_in)
        {
            intake_servo.setPower(0.50);
            intake_tilt_servo.setPosition(0.51);
            //decrement_servo(intake_tilt_servo);
        }
        else
        {
            if (intake_out)
            {
                intake_servo.setPower(-0.9);
                intake_tilt_servo.setPosition(0.48);
                //increment_servo(intake_tilt_servo);
            }
            else
            {
                intake_servo.setPower(0);
                intake_tilt_servo.setPosition(0.38);
            }
        }
    }
}
