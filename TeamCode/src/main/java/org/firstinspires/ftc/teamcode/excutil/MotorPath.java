package org.firstinspires.ftc.teamcode.excutil;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * A class for abstracting away the mechanics of motor run-to-position sequences.
 * A path to, for example, a tick count is constructed with a one-liner, and its status
 * can be monitored with more condensed code. Used for my macros sometimes.
 */
public class MotorPath {

    /**
     * Creates a MotorPath object that will run an encoded motor to a position.
     * Motor must be in a RunMode that supports encoder reading.
     * @see com.qualcomm.robotcore.hardware.DcMotor.RunMode
     * @param motor - The DcMotor with connected encoder in a valid RunMode
     * @param goalTicks - The absolute goal position in encoder ticks
     * @param power - The power at which the motor is driven to the position
     * @return
     */
    public static MotorPath runToPosition(DcMotor motor, int goalTicks, double power) {
        motor.setPower(power);
        motor.setTargetPosition(goalTicks);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        return new MotorPath(motor, goalTicks);
    }

    private DcMotor motor;
    private int goalTicks;
    private ElapsedTime time = new ElapsedTime();

    /**
     * Used internally by public static construction methods
     */
    private MotorPath(DcMotor motor, int goalTicks) {
        this.motor = motor;
        this.goalTicks = goalTicks;
    }

    /**
     * Checks if a MotorPath has reached its goal position yet.
     * @param threshold - The margin of error, path will be considered complete if the difference
     *                  between goal and current position is less than or equal to this.
     * @param msLimit - Supply a maximum time the path is allowed to run, in milliseconds. The
     *                method will begin returning true after that time has elapsed.
     * @return
     */
    public boolean isComplete(int threshold, double msLimit) {
        if (time.milliseconds() > msLimit) return true;

        return isComplete(threshold);
    }

    /**
    * Checks if a MotorPath has reached its goal position yet.
    * @param threshold - The margin of error, path will be considered complete if the difference
    *                  between goal and current position is less than or equal to this.
    **/
    public boolean isComplete(int threshold) {
        return RMath.approx(motor.getCurrentPosition(), goalTicks, threshold);
    }

    @Override
    public String toString() {
        return "MotorPath{" +
                "motor=" + motor +
                ", goalTicks=" + goalTicks +
                '}';
    }
}
