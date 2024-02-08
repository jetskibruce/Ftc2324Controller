package org.firstinspires.ftc.teamcode.excutil.coroutines;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Phaser;

/**
 * The big kahuna. Construct this if you want to use coroutines, and tick it at every loop
            so they execute properly.
 */
public class CoroutineManager {

    private ElapsedTime timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    private int idCounter = Integer.MIN_VALUE;
    private Map<Integer, Coroutine> storedRoutines = new HashMap<>();

    private Map<Integer, Coroutine> routineQueue = new HashMap<>();

    /**
     * Runs a simple coroutine after a given millisecond delay.
     * @return - the coroutine's internal id, for stopping it early
     * @see #cancelRoutine(Integer)
     */
    public int runLater(SimpleAction action, double delay) {
        return startRoutineLater((mode, d) -> {
            action.tick();
            return CoroutineResult.Stop;
        }, delay);
    }

    /**
     * Runs a classic coroutine immediately. (first tick at next manager update)
     * @return - the coroutine's internal id, for stopping it early
     * @see #cancelRoutine(Integer)
     */
    public int startRoutine(CoroutineAction action)
    {
        idCounter++;
        return addCoroutine(new Coroutine(action, timer.time(), idCounter));
    }

    /**
     * Runs a classic coroutine, starting after a millisecond delay has passed.
     * @return - the coroutine's internal id, for stopping it early
     * @see #runLater(SimpleAction, double)
     * @see #cancelRoutine(Integer)
     */
    public int startRoutineLater(CoroutineAction action, double msDelay)
    {
        idCounter++;
        Coroutine coroutine = new DelayedStartCoroutine(action, timer.time(), idCounter, msDelay);
        return addCoroutine(coroutine);
    }

    /**
     * Runs a classic coroutine immediately, (first tick at next manager update) but
            automatically ends it after a millisecond delay.
     * @return - the coroutine's internal id, for stopping it early
     * @see #cancelRoutine(Integer)
     */
    public int stopRoutineAfter(CoroutineAction action, double msDelay)
    {
        idCounter++;
        Coroutine coroutine = new SelfHaltingCoroutine(action, timer.time(), idCounter, msDelay);
        return addCoroutine(coroutine);
    }

    private int addCoroutine(Coroutine coroutine) {
        // avoids concurrent modification
        routineQueue.put(idCounter, coroutine);
        return idCounter;
    }

    /**
     * Stop executing a coroutine from now on.
     * @param id - You should be supplied this when starting the routine.
     */
    public void cancelRoutine(Integer id)
    {
        storedRoutines.remove(id);
    }

    private Queue<Integer> toRemove = new LinkedList<>();

    /**
     * Call every loop of your opmode, or coroutines cannot run. Very important. The OpMode's loop
            serves as the clock that constantly ticks and updates coroutines.
     * @param opMode - pass in the OpMode that's powering the loop, usually the class you're in
     */
    public void tick(OpMode opMode)
    {
        // avoids concurrent modification
        for (Integer id : routineQueue.keySet()) {
            storedRoutines.put(id, routineQueue.get(id));
        }

        routineQueue.clear();

        if (storedRoutines.size() == 0) return;

        double time = timer.time();

        for (Integer id : storedRoutines.keySet())
        {
            Coroutine coroutine = storedRoutines.get(id);

                CoroutineResult result = coroutine.tick(opMode, time);

                if (result == CoroutineResult.Stop)
                {
                    toRemove.add(id);
                } else if (result == CoroutineResult.Explode)
                {
                    toRemove.add(id);
                }

        }

        while (toRemove.size() > 0)
        {
            storedRoutines.remove(toRemove.poll());
        }

        toRemove.clear();
    }

    public int numActive() {
        return storedRoutines.size();
    }
}
