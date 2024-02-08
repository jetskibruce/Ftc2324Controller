package org.firstinspires.ftc.teamcode.excutil;

/**
 * Math utilities I find frequently useful.
 */
public class RMath {

    /**
     * @return v clamped to the range [min, max]. Values over max are returned as max, values under
                                                         min are returned as min.
     */
    public static double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

    /**
     * @return true if the difference between a and b is less than or equal to threshold.
     */
    public static boolean approx(int a, int b, int threshold) {
        return Math.abs(a - b) <= threshold;
    }

    /**
     * @return true if the difference between a and b is less than or equal to threshold.
     */
    public static boolean approx(double a, double b, double threshold) {
        return Math.abs(a - b) <= threshold;
    }

}
