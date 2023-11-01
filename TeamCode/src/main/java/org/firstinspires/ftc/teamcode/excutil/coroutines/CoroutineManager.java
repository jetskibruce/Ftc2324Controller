package org.firstinspires.ftc.teamcode.excutil.coroutines;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class CoroutineManager {

    private ElapsedTime timer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    private int idCounter = Integer.MIN_VALUE;
    private Map<Integer, Coroutine> storedRoutines = new HashMap<>();

    public int startRoutine(CoroutineAction action)
    {
        idCounter++;
        return addCoroutine(new Coroutine(action, timer.time(), idCounter));
    }

    public int startRoutineLater(CoroutineAction action, double msDelay)
    {
        idCounter++;
        Coroutine coroutine = new DelayedStartCoroutine(action, timer.time(), idCounter, msDelay);
        return addCoroutine(coroutine);
    }

    public int stopRoutineAfter(CoroutineAction action, double msDelay)
    {
        idCounter++;
        Coroutine coroutine = new SelfHaltingCoroutine(action, timer.time(), idCounter, msDelay);
        return addCoroutine(coroutine);
    }

    private int addCoroutine(Coroutine coroutine) {
        storedRoutines.put(idCounter, coroutine);
        return idCounter;
    }

    public void cancelRoutine(Integer id)
    {
        storedRoutines.remove(id);
    }

    private Queue<Integer> toRemove = new LinkedList<>();

    public void tick(OpMode opMode)
    {
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
