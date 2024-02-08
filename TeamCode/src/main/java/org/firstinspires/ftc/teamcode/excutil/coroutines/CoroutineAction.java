package org.firstinspires.ftc.teamcode.excutil.coroutines;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * The actions coroutines run, with return value allowing you to stop or continue them
            after every tick of execution.
    Useful if you want a process that can determine when it should terminate independently
            and otherwise continue unsupervised.
 */
@FunctionalInterface
public interface CoroutineAction {
    CoroutineResult tick(OpMode opMode, CoroutineData data);
}
