package org.firstinspires.ftc.teamcode.excutil.liveconfig;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.excutil.Input;
import org.firstinspires.ftc.teamcode.excutil.RMath;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LiveSettings {

    public static class Setting {
        public Object value;
        public final Object defaultValue;
        public String name;
        public Class<?> type;
        public int hashId;

        public Setting(Object val, String name, Class<?> type, int hashId) {
            this.value = val;
            this.defaultValue = val;
            this.name = name;
            this.type = type;
            this.hashId = hashId;
        }
    }

    public static Map<Integer, Setting> storedValues = new HashMap<>();

    public static Setting registerSetting(Object value, String name, Class<?> type) {
        int strHash = name.hashCode();

        Setting existing = storedValues.get(strHash);
        // values persist between opmodes
        if (existing == null) {
            Setting newSetting = new Setting(
                    value,
                    name,
                    type,
                    strHash
            );
            storedValues.put(strHash, newSetting);
            return newSetting;
        } else {
            return existing;
        }
    }

    static boolean inSettingMode;

    static boolean didInitialSqueeze = false;

    public static void tick(OpMode opmode) {
        if (opmode.gamepad1.back && opmode.gamepad1.start
        && opmode.gamepad1.left_trigger > 0.3 && opmode.gamepad1.right_trigger > 0.3) {
            didInitialSqueeze = true;

            if (inSettingMode) opmode.telemetry.speak("Exited settings");
            inSettingMode = false;
        }

        if (didInitialSqueeze &&
                opmode.gamepad1.left_bumper && opmode.gamepad1.right_bumper) {

            enterSettingsMode();
            didInitialSqueeze = false;
            opmode.telemetry.speak("Settings menu");
        }

        if (inSettingMode)
            runSettingModificationMenu(opmode);

    }

    private static void enterSettingsMode() {
        inSettingMode = true;
        settingsIterator = storedValues.values().iterator();
        currentSettingSelected = null;
        valueChangeMultiplier = 1;
    }

    static Iterator<Setting> settingsIterator;
    static Input input2 = new Input();
    static Setting currentSettingSelected = null;
    static int valueChangeMultiplier = 1;

    static final int BASE_TICK_CHANGE = 1;
    static final double BASE_POS_OR_POWER_CHANGE = 0.001;

    private static void runSettingModificationMenu(OpMode opmode) {
        opmode.telemetry.clear();

        opmode.telemetry.addData("== Debug Mode", "Tuning ==");

        input2.pollGamepad(opmode.gamepad2);

        if (input2.dpad_right.down()) {

            if (!settingsIterator.hasNext())
                settingsIterator = storedValues.values().iterator();

            currentSettingSelected = settingsIterator.next();
            opmode.telemetry.speak("selected " + currentSettingSelected.name);
        }



        if (currentSettingSelected != null) {
            if (input2.dpad_left.down()) {
                opmode.telemetry.speak("set to default");
                currentSettingSelected.value = currentSettingSelected.defaultValue;
            }

            if (input2.dpad_up.down()) {
                valueChangeMultiplier *= 10;
            }
            if (input2.dpad_down.down()) {
                valueChangeMultiplier /= 10;
            }
            valueChangeMultiplier = RMath.clamp(valueChangeMultiplier, 1, 100_000);

            double valMult = 0;

            if (input2.b.down()) {
                valMult = 1;
            } else if (input2.a.down()) {
                valMult = -1;
            }

            if (currentSettingSelected.type == Double.class) {
                currentSettingSelected.value =
                        ((Double)currentSettingSelected.value) +
                                (
                                        valMult * (BASE_POS_OR_POWER_CHANGE * valueChangeMultiplier)
                                );
            } else if (currentSettingSelected.type == Integer.class) {
                currentSettingSelected.value =
                        ((Integer)currentSettingSelected.value) +
                                (
                                        valMult * (BASE_TICK_CHANGE * valueChangeMultiplier)
                                );
            }


            opmode.telemetry.addData("Setting", "");
            opmode.telemetry.addData("  Selected variable:", currentSettingSelected.name);
            opmode.telemetry.addData("  Current value: ", currentSettingSelected.value);

            if (input2.x.held()) {
                opmode.telemetry.addData("  Default: ", currentSettingSelected.defaultValue);
                opmode.telemetry.addData("  Variable type: ", currentSettingSelected.type.getSimpleName());
                opmode.telemetry.addData("  Variable ID: ", currentSettingSelected.hashId);
            } else {
                opmode.telemetry.addData("X", "More info");
            }

        }

        opmode.telemetry.addLine();

        opmode.telemetry.addData("Adjustments", "");
        opmode.telemetry.addData("  Change Mult: ", valueChangeMultiplier);

    }

}












