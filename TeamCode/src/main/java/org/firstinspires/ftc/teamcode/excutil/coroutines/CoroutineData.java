package org.firstinspires.ftc.teamcode.excutil.coroutines;

/**
 * Used by other module classes to track coroutines and schedule their execution/termination.
 */
public class CoroutineData {
    public double MsAlive;
    public int RoutineID;

    /**
     * Used by other coroutine classes, you likely don't want to run this yourself.
     */
    public CoroutineData(double msAlive, int routineID)
    {
        this.MsAlive = msAlive;
        this.RoutineID = routineID;
    }
}
