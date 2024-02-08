package org.firstinspires.ftc.teamcode.macros.tuckdown;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.excutil.RMath;
import org.firstinspires.ftc.teamcode.excutil.Flag;
import org.firstinspires.ftc.teamcode.macros.PathStep;

public class ArmTuckMacro extends PathStep {

    public static DcMotor TOWER_MOTOR = null;
    private static final int TUCK_MOTOR_TICKS = 0;





    private Flag armTucked = new Flag();


    @Override
    public void onStart(){
        ;
        TOWER_MOTOR.setTargetPosition(TUCK_MOTOR_TICKS);
    }

    @Override
    public void onTick(OpMode opMode) {
        if (RMath.approx(TOWER_MOTOR.getCurrentPosition(), TUCK_MOTOR_TICKS, 30)) {
            finish();
        }
    }

}
