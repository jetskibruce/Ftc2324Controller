package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.components.RobotComponents;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.macros.MacroSequence;
import org.firstinspires.ftc.teamcode.macros.auto.AutoMoveMacro;
import org.firstinspires.ftc.teamcode.macros.generic.RunActionMacro;
import org.firstinspires.ftc.teamcode.macros.tuckdown.ArbitraryDelayMacro;
import org.firstinspires.ftc.teamcode.opmodes.teleop.functional.CompDrive;

@Autonomous(name="BlueEndzone CLOSE Placement Auto", group="aCompete")
// THIS OPMODE HAS TO BE RUN ONE TIME BEFORE IT WILL WORK PER_RESTART
//KEEP IN MIND IF YOU ENABLE
@Disabled
public class CompBlueAuto extends CompAuto {

    @Override
    public void init() {
        super.init();
        strafeDirection = AutoMoveMacro.LateralDirection.Left;
        //oppDirection = AutoMoveMacro.LateralDirection.Right;
    }
}
