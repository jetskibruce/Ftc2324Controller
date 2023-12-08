package org.firstinspires.ftc.teamcode.excutil;

import com.qualcomm.robotcore.hardware.Gamepad;

public class Input {

    public class ButtonState {
        public int numTicksReleased = -1;
        public int numTicksHeld = -1;
        public boolean Held = false;

        // true during first tick of button press
        public boolean down() {
            return numTicksHeld == 0;
        }

        // true during every tick of button press excluding release
        public boolean held() {
            return numTicksHeld > -1;
        }

        // true during every tick of button press excluding first and release
        public boolean heldExclusive() {
            return numTicksHeld > 0;
        }

        // first tick of release
        public boolean up() {
            return numTicksReleased == 0;
        }
    }

    public class TriggerState extends ButtonState {

    }

    public ButtonState a = new ButtonState();
    public ButtonState b = new ButtonState();
    public ButtonState x = new ButtonState();
    public ButtonState y = new ButtonState();

    public ButtonState left_trigger = new ButtonState();
    public ButtonState right_trigger = new ButtonState();

    public ButtonState left_bumper = new ButtonState();
    public ButtonState right_bumper = new ButtonState();


    public void pollGamepad(Gamepad gamepad) {
        updateState(a, gamepad.a);
        updateState(b, gamepad.b);
        updateState(x, gamepad.x);
        updateState(y, gamepad.y);

        updateState(left_trigger, gamepad.left_trigger > 0.3);
        updateState(right_trigger, gamepad.right_trigger > 0.3);

        updateState(left_bumper, gamepad.left_bumper);
        updateState(right_bumper, gamepad.right_bumper);
    }

    public void updateState(ButtonState state, boolean heldNow) {
        if (heldNow) {
            state.numTicksHeld++;
            state.numTicksReleased = -1;
        } else {
            state.numTicksHeld = -1;
            state.numTicksReleased++;
        }
    }



}
