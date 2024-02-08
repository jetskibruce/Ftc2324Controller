package org.firstinspires.ftc.teamcode.excutil;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

@Deprecated
// "no reason i just don't really use it and don't remember how effective it is"
public class PathManager {

    private OpMode owner;

    public PathManager(OpMode owner) {
        this.owner = owner;
    }

    private List<EncodedPath> paths = new ArrayList<>();

    public EncodedPath create() {
        EncodedPath path =  new EncodedPath();
        paths.add(path);
        return path;
    }

    public void tickAll() {
        for (EncodedPath p : paths) {
            p.tick();
            owner.telemetry.addData("started", p.isStarted());
            owner.telemetry.addData("step index", p.getStepIndex());
            owner.telemetry.addData("step ticks running", p.numExecutionTicks);
        }

    }

}
