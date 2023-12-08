package org.firstinspires.ftc.teamcode.opmodes.teleop.debug;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.VisionTestComponents;
import org.firstinspires.ftc.teamcode.macros.MacroSequence;
import org.firstinspires.ftc.teamcode.macros.PathStep;
import org.firstinspires.ftc.teamcode.macros.generic.ServoPositionMacro;
import org.firstinspires.ftc.teamcode.macros.tuckdown.ArbitraryDelayMacro;

@Disabled
@TeleOp(name = "Silly Vision")
public class SillyVisionTester extends OpMode {



    @Override
    public void init() {
        VisionTestComponents.init(hardwareMap);


    }

    @Override
    public void start() {

        final int numRepetitions = 10;

        PathStep[] steps = new PathStep[3 * numRepetitions + 1];
        for (int i = 0; i < numRepetitions; i++) {
            steps[i * 3] = new ServoPositionMacro(VisionTestComponents.servo, 1.0, 1000);
            steps[i * 3 + 1] = new ArbitraryDelayMacro(500);
            steps[i * 3 + 2] = new ServoPositionMacro(VisionTestComponents.servo, 0, 1000);
        }

        steps[steps.length - 1] = new ServoPositionMacro(VisionTestComponents.servo, 0.5, 500);

        MacroSequence.begin(
                "Servo Wiggle",
                steps

        );
    }

    @Override
    public void loop() {
        VisionTestComponents.tickSystems(this);

        MacroSequence.appendDebugTo(telemetry);
    }
}
