package org.firstinspires.ftc.teamcode.excutil.rrextensions;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.TrajectoryMarker;

import org.firstinspires.ftc.teamcode.trajectorysequence.sequencesegment.SequenceSegment;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

public final class ConditionalSegment extends SequenceSegment {

    private BooleanSupplier isDone;

    public ConditionalSegment(Pose2d pose, BooleanSupplier isDone, double seconds, List<TrajectoryMarker> markers) {
        super(seconds, pose, pose, markers);
        this.isDone = isDone;
    }

    public boolean isDone() {
        return isDone.getAsBoolean();
    }
}
