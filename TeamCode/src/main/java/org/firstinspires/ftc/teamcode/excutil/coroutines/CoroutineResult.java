package org.firstinspires.ftc.teamcode.excutil.coroutines;

/**
 * Returned from a coroutine action to determine if it should keep running or not.
 */
public enum CoroutineResult {
    /**
     * Continue running this coroutine next tick.
     */
    Continue,
    /**
     * Stop this coroutine before next tick, not running it from now on.
     */
    Stop,
    /**
     * Stop this coroutine before next tick, not running it from now on.
     */
    Explode
}
