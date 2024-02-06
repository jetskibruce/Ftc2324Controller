package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.macros.auto.AutoMoveMacro;

@Autonomous(name = "BlueEndzone FAR/BOARD/SCOOT Placement Auto", group = "aCompete")
@Disabled
public class CompBlueFarBackboardScootAuto extends CompFarBackboardAuto  {

    SampleMecanumDrive drive;

    @Override
    public void init() {
        super.init();
        strafeDirection = AutoMoveMacro.LateralDirection.Left;
        oppDirection = AutoMoveMacro.LateralDirection.Right;
        doOpp = true;
        
    }


}
