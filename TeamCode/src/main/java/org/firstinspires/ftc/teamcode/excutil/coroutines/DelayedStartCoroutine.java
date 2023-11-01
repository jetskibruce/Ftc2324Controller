package org.firstinspires.ftc.teamcode.excutil.coroutines;

public class DelayedStartCoroutine extends Coroutine {

    private double runAfter = 0;

    public DelayedStartCoroutine(CoroutineAction action, double now, int id, double runAfter) {
        super(action, now, id);
        this.runAfter = runAfter;
    }

    @Override
    public boolean shouldRun(double currentTime) {
        return (data.MsAlive > runAfter);
    }


}
