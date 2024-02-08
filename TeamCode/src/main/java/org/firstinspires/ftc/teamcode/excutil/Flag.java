package org.firstinspires.ftc.teamcode.excutil;

/**
 * Very occasionally useful class allowing you a simple toggle or resettable one-time boolean.
 */
public class Flag {

    private boolean activated = false;

    /**
     * Sets the activation status of the flag.
     */
    public void setActivated(boolean truth) {
        activated = truth;
    }

    /**
     * Makes the flag inactive.
     */
    public void reset() {
        activated = false;
    }

    /**
     * Switches from inactive to active, or vice versa.
     * @return - true if the flag is activated after toggling it
     */
    public boolean toggle() {
        activated = !activated;
        return activated;
    }

    /**
     * @return - true the first time it is called. It will return true again after every time the
            Flag is toggled off from a post-once() state as well.
     */
    public boolean once() {
        boolean truth = !activated;
        activated = true;
        return truth;
    }

    /**
     * @return - true if the flag is activated.
     */
    public boolean yes() {
        return activated;
    }

}
