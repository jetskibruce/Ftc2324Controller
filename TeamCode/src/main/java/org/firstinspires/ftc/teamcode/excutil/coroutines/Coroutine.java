package org.firstinspires.ftc.teamcode.excutil.coroutines;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class Coroutine {

    

    protected double startMs = -1;
    protected CoroutineAction action;
    protected CoroutineData data;

    public Coroutine(CoroutineAction action, double now, int id)
    {
        this.action = action;
        this.data = new CoroutineData(0, id);
        this.startMs = now;
    }

    public CoroutineResult tick(OpMode mode, double now)
    {
        data.MsAlive = now - startMs;
        if (shouldRun(now)) return action.tick(mode, data);
        return CoroutineResult.Continue;
    }

    public boolean shouldRun(double currentTime) {
        return true;
    }

}
