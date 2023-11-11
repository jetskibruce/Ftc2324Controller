package org.firstinspires.ftc.teamcode.derived;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

import org.apache.commons.math3.analysis.function.Max;
import org.apache.commons.math3.analysis.function.Min;
import org.firstinspires.ftc.teamcode.excutil.RMath;

public class NerdServo extends PositionableServo {

    protected Servo servo;
    private double MinPosition;
    private double MaxPosition;

    public NerdServo(Servo servo) {
        this(servo, 0, 1);
    }

    public NerdServo(Servo servo, double min, double max) {
        super(servo);
        setLimits(min, max);
    }

    public void setLimits(double min, double max) {
        MinPosition = min;
        MaxPosition = max;
    }

    @Override
    public void setPosition(double position) {
        super.setDPosition(RMath.clamp(position, MinPosition, MaxPosition));
    }

}
