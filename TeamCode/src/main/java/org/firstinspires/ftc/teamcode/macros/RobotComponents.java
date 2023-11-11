package org.firstinspires.ftc.teamcode.macros;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.excutil.coroutines.CoroutineManager;
import org.opencv.core.Mat;

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

    public static DcMotorEx front_left = null;
    public static DcMotorEx front_right = null;
    public static DcMotorEx back_left = null;
    public static DcMotorEx back_right = null;

    public static DcMotor tower_motor = null;

    public static Servo wrist_servo;
    public static Servo bucket_servo;

    public static CRServo back_intake_servo;
    public static CRServo front_intake_servo;

    public static List<ServoComponent> servos = new ArrayList<>();

    public static CoroutineManager coroutines = new CoroutineManager();

    public static void init(HardwareMap hardwareMap) {
        front_left = hardwareMap.get(DcMotorEx.class, "front_left");
        front_right = hardwareMap.get(DcMotorEx.class, "front_right");
        back_left = hardwareMap.get(DcMotorEx.class, "back_left");
        back_right = hardwareMap.get(DcMotorEx.class, "back_right");

        tower_motor = hardwareMap.get(DcMotor.class, "tower_motor");

        wrist_servo = hardwareMap.get(Servo.class, "wrist_servo");
        bucket_servo = hardwareMap.get(Servo.class, "bucket_servo");

        servos.add(new ServoComponent(wrist_servo, "Wrist Servo"));
        servos.add(new ServoComponent(bucket_servo, "Bucket Servo"));

        front_intake_servo = hardwareMap.get(CRServo.class, "front_intake_servo");
        back_intake_servo = hardwareMap.get(CRServo.class, "back_intake_servo");


    }

    public static void tickSystems(OpMode activeMode) {
        coroutines.tick(activeMode);
        MacroPath.tick(activeMode);
    }
}
