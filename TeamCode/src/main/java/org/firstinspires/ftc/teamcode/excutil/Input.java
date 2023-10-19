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

    public ButtonState a = new ButtonState();
    public ButtonState b = new ButtonState();
    public ButtonState x = new ButtonState();
    public ButtonState y = new ButtonState();


    public void pollGamepad(Gamepad gamepad) {
        updateState(a, gamepad.a);
        updateState(b, gamepad.b);
        updateState(x, gamepad.x);
        updateState(y, gamepad.y);

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
