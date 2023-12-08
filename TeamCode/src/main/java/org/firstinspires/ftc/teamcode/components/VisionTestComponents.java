package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.macros.MacroSequence;

public class VisionTestComponents {

    public static Servo servo = null;

    public static void init(HardwareMap hardwareMap) {
        servo = hardwareMap.get(Servo.class, "servo");
    }

    public static void tickSystems(OpMode opMode) {
        MacroSequence.tick(opMode);
        RobotComponents.coroutines.tick(opMode);
    }

}
