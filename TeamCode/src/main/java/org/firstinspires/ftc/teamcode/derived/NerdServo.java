package org.firstinspires.ftc.teamcode.derived;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

import org.apache.commons.math3.analysis.function.Max;
import org.firstinspires.ftc.teamcode.excutil.RMath;

public class NerdServo implements Servo {

    private Servo servo;
    private double MinPosition;
    private double MaxPosition;


    public NerdServo(Servo servo) {
        servo = servo;
    }

    public void setLimits(double min, double max) {
        MinPosition = min;
        MaxPosition = max;
    }

    @Override
    public ServoController getController() {
        return servo.getController();
    }

    @Override
    public int getPortNumber() {
        return servo.getPortNumber();
    }

    @Override
    public void setDirection(Direction direction) {
        servo.setDirection(direction);
    }

    @Override
    public Direction getDirection() {
        return servo.getDirection();
    }

    @Override
    public void setPosition(double position) {
        servo.setPosition(RMath.clamp(position, MinPosition, MaxPosition));
    }

    @Override
    public double getPosition() {
        return servo.getPosition();
    }

    @Override
    public void scaleRange(double min, double max) {
        servo.scaleRange(min, max);
    }

    @Override
    public Manufacturer getManufacturer() {
        return servo.getManufacturer();
    }

    @Override
    public String getDeviceName() {
        return servo.getDeviceName();
    }

    @Override
    public String getConnectionInfo() {
        return servo.getConnectionInfo();
    }

    @Override
    public int getVersion() {
        return servo.getVersion();
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {
        servo.resetDeviceConfigurationForOpMode();
    }

    @Override
    public void close() {
        servo.close();
    }
}
