package org.firstinspires.ftc.teamcode.excutil;

public class RMath {

    public static double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

}
