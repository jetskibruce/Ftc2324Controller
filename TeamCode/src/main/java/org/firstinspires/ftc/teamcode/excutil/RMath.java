package org.firstinspires.ftc.teamcode.excutil;

public class RMath {

    public static double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

    public static boolean approx(int a, int b, int threshold) {
        return Math.abs(a - b) <= threshold;
    }

    public static boolean approx(double a, double b, double threshold) {
        return Math.abs(a - b) <= threshold;
    }

}
