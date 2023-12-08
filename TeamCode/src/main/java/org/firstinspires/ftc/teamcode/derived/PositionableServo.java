package org.firstinspires.ftc.teamcode.derived;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

import org.firstinspires.ftc.teamcode.excutil.RMath;
import org.firstinspires.ftc.teamcode.macros.Positionable;

public class PositionableServo implements Positionable, Servo {

    protected Servo servo;


    public PositionableServo(Servo servo) {
        this.servo = servo;
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
        servo.setPosition(position);
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

    @Override
    public void setDPosition(double position) {
        setPosition(position);
    }

    @Override
    public double getDPosition() {
        return getPosition();
    }

    @Override
    public PositionFormat getPositionFormat() {
        return PositionFormat.Normalized;
    }
}
