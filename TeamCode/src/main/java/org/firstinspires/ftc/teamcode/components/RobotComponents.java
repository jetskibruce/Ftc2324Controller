package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.excutil.coroutines.CoroutineManager;
import org.firstinspires.ftc.teamcode.macros.MacroSequence;
import org.firstinspires.ftc.teamcode.util.Encoder;

import java.util.ArrayList;
import java.util.List;

public class RobotComponents {

    public static class ServoComponent {
        public Servo servo;
        public String name;

        public ServoComponent(Servo servo, String name) {
            this.servo = servo;
            this.name = name;
        }
    }

    public static class MotorComponent {
        public DcMotor motor;
        public String name;

        public MotorComponent(DcMotor motor, String name) {
            this.motor = motor;
            this.name = name;
        }
    }

    public static DcMotorEx front_left = null;
    public static DcMotorEx front_right = null;
    public static DcMotorEx back_left = null;
    public static DcMotorEx back_right = null;

    public static DcMotor tower_motor = null;
    public static DcMotor climb_motor = null;

    public static DcMotor front_intake_motor = null;

    public static Servo wrist_servo;
    public static Servo bucket_servo;
    public static Servo left_pixel_hold_servo;
    public static Servo right_pixel_hold_servo;

    public static CRServo back_intake_servo;
    public static Servo climber_clasp_servo;

    public static Encoder parallelEncoder, perpendicularEncoder;

    public static List<ServoComponent> servos = new ArrayList<>();
    // only positionable (read: encoders attached) motors
    public static List<MotorComponent> motors = new ArrayList<>();

    public static CoroutineManager coroutines = new CoroutineManager();

    private static Servo registerServo(HardwareMap hardwareMap, String id, String debugName) {
        Servo servo = hardwareMap.get(Servo.class, id);

        servos.add(new ServoComponent(servo, debugName));

        return servo;
    }

    private static DcMotor registerEncodedMotor(HardwareMap hardwareMap, String id, String debugName) {
        DcMotor motor = hardwareMap.get(DcMotor.class, id);

        motors.add(new MotorComponent(motor, debugName));

        return motor;
    }

    public static void init(HardwareMap hardwareMap) {
        front_left = hardwareMap.get(DcMotorEx.class, "front_left");
        front_right = hardwareMap.get(DcMotorEx.class, "front_right");
        back_left = hardwareMap.get(DcMotorEx.class, "back_left");
        back_right = hardwareMap.get(DcMotorEx.class, "back_right");

        tower_motor = registerEncodedMotor(hardwareMap, "tower_motor", "Tower Motor");
        climb_motor = registerEncodedMotor(hardwareMap, "climb_motor", "Climb Motor");
        tower_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //climb_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        front_intake_motor = hardwareMap.get(DcMotor.class, "front_intake_motor");

        parallelEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "back_right"));
        perpendicularEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "back_left"));

        wrist_servo = registerServo(hardwareMap, "wrist_servo", "Wrist Servo");
        bucket_servo = registerServo(hardwareMap, "bucket_servo", "Bucket Tilt Servo");

        climber_clasp_servo = registerServo(hardwareMap, "climber_clasp", "Climber Clasp Servo");

        back_intake_servo = hardwareMap.get(CRServo.class, "back_intake_servo");

        left_pixel_hold_servo = registerServo(hardwareMap, "bucket_left", "Left Pixel Hold Servo");
        right_pixel_hold_servo = registerServo(hardwareMap, "bucket_right", "Right Pixel Hold Servo");
    }

    public static void tickSystems(OpMode activeMode) {
        coroutines.tick(activeMode);
        MacroSequence.tick(activeMode);
    }

    public static final double     COUNTS_PER_ENCODER_REV    = 8192 ;    // eg: TETRIX Motor Encoder
    public static final double     ENCODER_GEAR_REDUCTION    = 1.0 ;     // No External Gearing.
    public static final double     WHEEL_DIAMETER_INCHES   = 2.0 ;     // For figuring circumference
    public static final double     ENCODER_COUNTS_PER_INCH         = (COUNTS_PER_ENCODER_REV * ENCODER_GEAR_REDUCTION) /
                                                                (WHEEL_DIAMETER_INCHES * 3.1415);

    public static int encoderDistance(double inches) {
        return (int) (inches * ENCODER_COUNTS_PER_INCH);
    }
}
