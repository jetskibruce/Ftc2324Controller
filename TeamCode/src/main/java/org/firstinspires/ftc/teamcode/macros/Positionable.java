package org.firstinspires.ftc.teamcode.macros;

public interface Positionable {

    enum PositionFormat {
        Normalized,
        Ticks
    }

    void setDPosition(double position);
    double getDPosition();
    PositionFormat getPositionFormat();
}
