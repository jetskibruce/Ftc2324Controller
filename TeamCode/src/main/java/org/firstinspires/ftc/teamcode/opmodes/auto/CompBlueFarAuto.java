package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.components.RobotComponents;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.macros.MacroSequence;
import org.firstinspires.ftc.teamcode.macros.arm.up.TuckWristForRiseMacro;
import org.firstinspires.ftc.teamcode.macros.auto.AutoMoveMacro;
import org.firstinspires.ftc.teamcode.macros.generic.RunActionMacro;
import org.firstinspires.ftc.teamcode.macros.tuckdown.ArbitraryDelayMacro;
import org.firstinspires.ftc.teamcode.opmodes.NerdOpMode;
import org.firstinspires.ftc.teamcode.opmodes.teleop.functional.CompDrive;

@Autonomous(name = "BlueEndzone FAR Placement Auto", group = "aCompete")
public class CompBlueFarAuto extends CompFarAuto  {

    SampleMecanumDrive drive;

    @Override
    public void init() {
        super.init();
        strafeDirection = AutoMoveMacro.LateralDirection.Left;
    }


}
