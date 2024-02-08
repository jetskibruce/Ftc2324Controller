package org.firstinspires.ftc.teamcode.excutil.coroutines;

/**
 * Used by other coroutine classes, you likely don't want to use this yourself.
 */
public class DelayedStartCoroutine extends Coroutine {

    private double runAfter = 0;

    /**
     * Used by other coroutine classes, you likely don't want to run this yourself.
     */
    public DelayedStartCoroutine(CoroutineAction action, double now, int id, double runAfter) {
        super(action, now, id);
        this.runAfter = runAfter;
    }

    @Override
    public boolean shouldRun(double currentTime) {
        return (data.MsAlive > runAfter);
    }


}
