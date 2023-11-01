package org.firstinspires.ftc.teamcode.excutil;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class PathManager {

    private List<EncodedPath> paths = new ArrayList<>();

    public EncodedPath create() {
        EncodedPath path =  new EncodedPath();
        paths.add(path);
        return path;
    }

    public void tickAll() {
        for (EncodedPath p : paths) {
            p.tick();
        }
    }

}
