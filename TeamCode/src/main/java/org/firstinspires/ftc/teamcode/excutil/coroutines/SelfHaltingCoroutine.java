package org.firstinspires.ftc.teamcode.excutil.coroutines;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Used by other coroutine classes, you likely don't want to use this yourself.
 */
public class SelfHaltingCoroutine extends Coroutine {

    private double stopAfter = 0;

    /**
     * Used by other coroutine classes, you likely don't want to run this yourself.
     */
    public SelfHaltingCoroutine(CoroutineAction action, double now, int id, double stopAfter) {
        super(action, now, id);
        this.stopAfter = stopAfter;
    }

    @Override
    public CoroutineResult tick(OpMode mode, double now) {
        CoroutineResult result = super.tick(mode, now);

        return (data.MsAlive > stopAfter) ? CoroutineResult.Stop : result;
    }

    @Override
    public boolean shouldRun(double currentTime) {
        return true;
    }
}
