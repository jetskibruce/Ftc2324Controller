package org.firstinspires.ftc.teamcode.excutil.liveconfig;

public class LiveSetting<Var extends Object> {

    private LiveSettings.Setting mySettingInstance;

    /**
     *
     * @param name Must be unique, identifies this setting
     * @param value
     */
    public LiveSetting(String name, Var value) {

        mySettingInstance = LiveSettings.registerSetting(
                value,
                name,
                value.getClass()
        );
    }

    public Var value() {
        return (Var)mySettingInstance.value;
    }

    public boolean set(Var newVal) {
        if (mySettingInstance.type.isInstance(newVal.getClass())) {
            mySettingInstance.value = newVal;
            return true;
        }
        return false;
    }

}
