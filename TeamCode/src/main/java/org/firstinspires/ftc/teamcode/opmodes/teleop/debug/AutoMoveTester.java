package org.firstinspires.ftc.teamcode.opmodes.teleop.debug;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.macros.MacroSequence;
import org.firstinspires.ftc.teamcode.macros.auto.AutoMoveMacro;
import org.firstinspires.ftc.teamcode.opmodes.NerdOpMode;

@TeleOp(name="Auto Move Tester", group = "bControl")
public class AutoMoveTester extends NerdOpMode {


    @Override
    public void init() {
        super.init();

    }

    @Override
    public void loop() {
        super.loop();

        if (input.dpad_up.down()) {
            MacroSequence.begin("Forward",
                    new AutoMoveMacro(
                            drive, AutoMoveMacro.LateralDirection.Forward,
                            24, 0.7
                    )
            );
        }
        else if (input.dpad_left.down()) {
            MacroSequence.begin("Left",
                    new AutoMoveMacro(
                            drive, AutoMoveMacro.LateralDirection.Left,
                            24, 0.7
                    )
            );
        }
        else if (input.dpad_right.down()) {
            MacroSequence.begin("Right",
                    new AutoMoveMacro(
                            drive, AutoMoveMacro.LateralDirection.Right,
                            24, 0.7
                    )
            );
        }
        else if (input.dpad_down.down()) {
            MacroSequence.begin("Backward",
                    new AutoMoveMacro(
                            drive, AutoMoveMacro.LateralDirection.Backward,
                            24, 0.7
                    )
            );
        }
    }
}
