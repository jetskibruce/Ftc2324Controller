package org.firstinspires.ftc.teamcode.opmodes.options;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public abstract class CompetitionAutoOpMode {

    protected final OpMode op;
    protected final Telemetry telemetry;
    protected final HardwareMap hardwareMap;

    public CompetitionAutoOpMode(OpMode parentOpMode) {
        op = parentOpMode;
        telemetry = op.telemetry;
        hardwareMap = op.hardwareMap;
    }

    abstract void init();

    public void start() {
        // override
    }

    public void stop() {
        // override
    }

    public void init_loop() {
        // override
    }

    abstract void loop();

}
