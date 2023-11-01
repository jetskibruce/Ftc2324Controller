package org.firstinspires.ftc.teamcode.excutil.coroutines;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class SelfHaltingCoroutine extends Coroutine {

    private double stopAfter = 0;

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
