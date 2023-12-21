package org.firstinspires.ftc.teamcode.opmodes.teleop.debug;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.macros.MacroSequence;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@TeleOp
public class MacroDebugger extends OpMode {

    private List<Supplier<MacroSequence>> sequences = new ArrayList<>();

    static {
        //sequences.add(MacroSequence.compose(""))
    }


    @Override
    public void init() {

    }

    @Override
    public void loop() {

    }
}
