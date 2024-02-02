package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Autonomous(name="RedEndzone FAR/BOARD/SCOOT Placement Auto", group="aCompete")
@Disabled
public class CompRedFarBackboardScootAuto extends CompFarBackboardAuto {

    @Override
    public void init() {
        super.init();
        doOpp = true;
    }
}
