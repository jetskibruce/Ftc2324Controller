package org.firstinspires.ftc.teamcode.excutil;

import com.qualcomm.robotcore.hardware.DcMotor;

public class MotorPath {

    public static MotorPath runToPosition(DcMotor motor, int goalTicks, double power) {
        motor.setPower(power);
        motor.setTargetPosition(goalTicks);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        return new MotorPath(motor, goalTicks);
    }

    private DcMotor motor;
    private int goalTicks;

    private MotorPath(DcMotor motor, int goalTicks) {
        this.motor = motor;
        this.goalTicks = goalTicks;
    }

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
