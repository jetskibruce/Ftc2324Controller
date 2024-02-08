package org.firstinspires.ftc.teamcode.excutil;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Wraps gamepad inputs to allow for simpler rising/falling edge button signal detection and
        tracking of how long a button has been held.
 */
public class Input {


    public static class ButtonState {
        public int numTicksReleased = -1;
        public int numTicksHeld = -1;
        public boolean Held = false;

        /**
         * @return - true during first tick of button press
         */
        public boolean down() {
            return numTicksHeld == 0;
        }

        /**
         * @return - true during every tick of button press excluding release
         */
        public boolean held() {
            return numTicksHeld > -1;
        }

        /**
         * @return - true during every tick of button press excluding first and release
         */
        public boolean heldExclusive() {
            return numTicksHeld > 0;
        }

        /**
         * @return - true during first tick of release
         */
        public boolean up() {
            return numTicksReleased == 0;
        }
    }

    public static class DisplacingButtonState extends ButtonState {

        /**
         * The value read directly off the gamepad variable.
         */
        public double displacement = 0;

        /**
         * The change in displacement between this tick and last.
         * Negative is the "towards released" direction and positive "towards pressed".
         */
        public double displacementDelta = 0;

    }

    public ButtonState a = new ButtonState();
    public ButtonState b = new ButtonState();
    public ButtonState x = new ButtonState();
    public ButtonState y = new ButtonState();

    public DisplacingButtonState left_trigger = new DisplacingButtonState();
    public DisplacingButtonState right_trigger = new DisplacingButtonState();

    public ButtonState left_bumper = new ButtonState();
    public ButtonState right_bumper = new ButtonState();

    public ButtonState dpad_down = new ButtonState();
    public ButtonState dpad_left = new ButtonState();
    public ButtonState dpad_right = new ButtonState();
    public ButtonState dpad_up = new ButtonState();

    public ButtonState back = new ButtonState();
    public ButtonState start = new ButtonState();

    /**
     * @see #pollGamepad(Gamepad)
     */
    public Input() {

    }

    /**
     * Call this on every loop() to update variable states, or input won't work
     * @param gamepad - the gamepad to scrape inputs from
     */
    public void pollGamepad(Gamepad gamepad) {
        updateState(a, gamepad.a);
        updateState(b, gamepad.b);
        updateState(x, gamepad.x);
        updateState(y, gamepad.y);

        updateState(left_trigger, gamepad.left_trigger > 0.3, gamepad.left_trigger);
        updateState(right_trigger, gamepad.right_trigger > 0.3, gamepad.right_trigger);

        updateState(left_bumper, gamepad.left_bumper);
        updateState(right_bumper, gamepad.right_bumper);

        updateState(dpad_down, gamepad.dpad_down);
        updateState(dpad_left, gamepad.dpad_left);
        updateState(dpad_right, gamepad.dpad_right);
        updateState(dpad_up, gamepad.dpad_up);

        updateState(back, gamepad.back);
        updateState(start, gamepad.start);
    }

    private void updateState(ButtonState state, boolean heldNow) {
        if (heldNow) {
            state.numTicksHeld++;
            state.numTicksReleased = -1;
        } else {
            state.numTicksHeld = -1;
            state.numTicksReleased++;
        }
    }

    private void updateState(DisplacingButtonState state, boolean heldNow, double displacement) {
        updateState(state, heldNow);

        state.displacementDelta = displacement - state.displacement;
        state.displacement = displacement;
    }



}
