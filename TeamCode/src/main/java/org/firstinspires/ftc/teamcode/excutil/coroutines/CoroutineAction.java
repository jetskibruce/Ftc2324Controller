package org.firstinspires.ftc.teamcode.excutil.coroutines;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@FunctionalInterface
public interface CoroutineAction {
    CoroutineResult tick(OpMode opMode, CoroutineData data);
}
