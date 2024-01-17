package org.firstinspires.ftc.teamcode.macros.generic;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.macros.MacroSequence;
import org.firstinspires.ftc.teamcode.macros.PathStep;

public class RunExtraMacro extends PathStep {

    private MacroSequence nextSequence;

    public RunExtraMacro(MacroSequence next) {
        this.nextSequence = next;
    }

    @Override
    public void onStart() {
        nextSequence.start();
    }

    @Override
    public void onTick(OpMode opMode) {

    }

}
