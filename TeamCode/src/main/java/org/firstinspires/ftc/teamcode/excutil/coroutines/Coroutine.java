package org.firstinspires.ftc.teamcode.excutil.coroutines;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Coroutines """run alongside""" the main code. Really they are ticked on the same thread,
 * but they allow you to work with something approaching an async system conveniently.
 *
 * For example, you can schedule an action to run in 10 seconds, and that code will execute
 * by itself 10 seconds later without needing further interaction. This is sometimes helpful.
 */
public class Coroutine {

    protected double startMs = -1;
    protected CoroutineAction action;
    protected CoroutineData data;

    /**
     * Used by other coroutine classes, you likely don't want to run this yourself.
     */
    public Coroutine(CoroutineAction action, double now, int id)
    {
        this.action = action;
        this.data = new CoroutineData(0, id);
        this.startMs = now;
    }

    /**
     * Used by other coroutine classes, you likely don't want to run this yourself.
     */
    public CoroutineResult tick(OpMode mode, double now)
    {
        data.MsAlive = now - startMs;
        if (shouldRun(now)) return action.tick(mode, data);
        return CoroutineResult.Continue;
    }

    /**
     * Used by other coroutine classes, you likely don't want to run this yourself.
     */
    @Deprecated
    public boolean shouldRun(double currentTime) {
        return true;
    }

}
