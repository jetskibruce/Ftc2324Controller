package org.firstinspires.ftc.teamcode.excutil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class EncodedPath {

    private List<Predicate> actions = new ArrayList<>();

    public void addStep(Predicate p) {
        actions.add(p);
    }

}
