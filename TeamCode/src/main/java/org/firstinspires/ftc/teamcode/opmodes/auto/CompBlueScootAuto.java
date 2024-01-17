package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.macros.auto.AutoMoveMacro;

@Autonomous(name="BlueEndzone CLOSE/SCOOT Placement Auto", group="aCompete")
public class CompBlueScootAuto extends CompAuto {

    @Override
    public void init() {
        super.init();
        strafeDirection = AutoMoveMacro.LateralDirection.Left;
        oppDirection = AutoMoveMacro.LateralDirection.Right;
        doOpp = true;
    }
}
