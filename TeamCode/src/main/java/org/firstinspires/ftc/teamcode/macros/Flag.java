package org.firstinspires.ftc.teamcode.macros;

public class Flag {

    private boolean activated = false;

    public void finish() {
        activated = true;
    }

    public boolean toggle() {
        activated = !activated;
        return activated;
    }

    public boolean once() {
        boolean truth = !activated;
        activated = true;
        return truth;
    }

    public boolean yes() {
        return activated;
    }

}
