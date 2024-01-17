package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.macros.auto.AutoMoveMacro;

@Autonomous(name="RedEndzone CLOSE/SCOOT Placement Auto", group="aCompete")
public class CompRedScootAuto extends CompAuto {

    @Override
    public void init() {
        super.init();
        doOpp = true;
    }
}
