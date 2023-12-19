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

    public static CRServo back_intake_servo;
    public static CRServo front_intake_servo;

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
        climb_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        front_intake_motor = hardwareMap.get(DcMotor.class, "front_intake_motor");

        parallelEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "back_right"));
        perpendicularEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "back_left"));

        wrist_servo = registerServo(hardwareMap, "wrist_servo", "Wrist Servo");
        bucket_servo = registerServo(hardwareMap, "bucket_servo", "Bucket Servo");

        front_intake_servo = hardwareMap.get(CRServo.class, "front_intake_servo");
        back_intake_servo = hardwareMap.get(CRServo.class, "back_intake_servo");


    }

    public static void tickSystems(OpMode activeMode) {
        coroutines.tick(activeMode);
        MacroSequence.tick(activeMode);
    }
}
