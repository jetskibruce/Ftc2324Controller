package org.firstinspires.ftc.teamcode.opmodes.teleop.debug;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.components.RobotComponents;
import org.firstinspires.ftc.teamcode.excutil.coroutines.Coroutine;
import org.firstinspires.ftc.teamcode.excutil.coroutines.CoroutineAction;
import org.firstinspires.ftc.teamcode.excutil.coroutines.CoroutineResult;
import org.firstinspires.ftc.teamcode.sensing.BucketDistanceSensingArray;
import static org.firstinspires.ftc.teamcode.components.RobotComponents.*;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class BucketDistanceTuner extends OpMode {

    private DecimalFormat df = new DecimalFormat("#.##");
    private BucketDistanceSensingArray bucketSensors;

    @Override
    public void init() {
        RobotComponents.init(hardwareMap);
        bucketSensors = new BucketDistanceSensingArray(left_pixel_color_sensor, right_pixel_color_sensor);

        df.setRoundingMode(RoundingMode.DOWN);
    }

    public void start() {
        telemetry.addLine("Bucket Distance Tuning");
        bucketSensors.left().enableLed();

    }

    @Override
    public void loop() {
        RobotComponents.tickSystems(this);
    }

    enum TunePhase {
        Start,
        Idle,
        Load,
        End
    }

    private TunePhase currentTunePhase = TunePhase.Start;

    List<Double> leftValuesOverTime = new ArrayList<>(100);
    List<Double> rightValuesOverTime = new ArrayList<>(100);





    ElapsedTime timeInPhase = new ElapsedTime();



    private double averageNoPixelLeft, averageNoPixelRight;
    private double averagePixelLeft, getAveragePixelRight;

    double averageList(List<Double> values) {
        double sum = 0;
        for (Double d : values) {
            sum += d;
        }
        return sum / values.size();
    }

    void tuningLoop() {
        switch (currentTunePhase) {
            case Start:
                tune_startPhaseLoop();
                break;

            case Idle:
                tune_idlePhaseLoop();
                break;

            case LeftLoad:
                tune_loadPhaseLoop();
                break;
        }
    }

    boolean continuePastStartReady = false;

    void tune_startPhaseLoop() {
        telemetry.addLine("1) Ensure no unrealistic shadows/lighting will affect tuning.");
        telemetry.addLine("2) REMOVE any pixels already in the bucket, turn holders out of the way.");
        telemetry.addLine("3) Place bucket in the intake position, as if stowed for a game.");


        telemetry.addLine();

        coroutines.runLater(() -> {
            continuePastStartReady = true;

            telemetry.addLine("Press 'A' when it is properly positioned.");
        }, 700);

        if (gamepad1.a && continuePastStartReady) {
            continuePastStartReady = false;

            timeInPhase.reset();
            telemetry.clear();

            telemetry.addLine("Lovely! Now the bot will rest idle for a little while, collecting sensor data.");
            coroutines.startRoutine(idleTune);
            currentTunePhase = TunePhase.Idle;
        }
    }

    boolean idleAlmostThere = false;
    boolean calculateAvgIdleReady = false;

    CoroutineAction idleTune = (opmode, data) -> {

        if (timeInPhase.milliseconds() > 6250) {
            calculateAvgIdleReady = true;
            return CoroutineResult.Stop;
        }

        // will exclude the first (0-99 ms) period but it don't matter lol
        if ((int)(timeInPhase.milliseconds() / 100) > leftValuesOverTime.size()) {
            leftValuesOverTime.add(bucketSensors.leftCentimeters());
            rightValuesOverTime.add(bucketSensors.rightCentimeters());
        }

        if (timeInPhase.milliseconds() > 3500 && !idleAlmostThere) {
            idleAlmostThere = true;
            telemetry.addLine("Almost there...");
        }

        return CoroutineResult.Continue;
    };

    void tune_idlePhaseLoop() {


        if (calculateAvgIdleReady) {
            idleAlmostThere = false;
            calculateAvgIdleReady = false;

            averageNoPixelLeft = averageList(leftValuesOverTime);
            averageNoPixelRight = averageList(rightValuesOverTime);

            leftValuesOverTime.clear();
            rightValuesOverTime.clear();

            telemetry.clear();
            timeInPhase.reset();

            telemetry.addLine("Alright. We've established a baseline.");
            telemetry.addLine("Please fully insert two pixels of any color, one in each slot, into the bucket.");
            telemetry.addLine();
            currentTunePhase = TunePhase.Load;
        }
    }

    boolean loadStartReady = false;
    boolean loadAlmostThere = false;
    boolean calculateLoadNow = false;

    int flashNum = 0;
    CoroutineAction loadPollAction = (opMode, data) -> {

        if (timeInPhase.milliseconds() > 6250) {
            calculateLoadNow = true;
            return CoroutineResult.Stop;
        }

        // will exclude the first (0-99 ms) period but it don't matter lol
        if ((int)(timeInPhase.milliseconds() / 100) > leftValuesOverTime.size()) {
            leftValuesOverTime.add(bucketSensors.leftCentimeters());
            rightValuesOverTime.add(bucketSensors.rightCentimeters());
        }

        if (timeInPhase.milliseconds() > 3500 && !loadAlmostThere) {
            loadAlmostThere = true;
            telemetry.addLine("Almost there...");
        }

        return CoroutineResult.Continue;

    };

    double runningLoadLeftSum = 0;
    int loadLeftCount;
    double runningLoadRightSum = 0;
    int loadRightCount;

    boolean skipOrRunAgainReady = false;

    void tune_loadPhaseLoop() {


        coroutines.runLater(() -> {
            loadStartReady = true;

            telemetry.addLine("Press 'A' when they are fully inserted.");
        }, 700);

        if (gamepad1.a && loadStartReady) {
            loadStartReady = false;

            timeInPhase.reset();
            coroutines.startRoutine(loadPollAction);
        }

        if (calculateLoadNow) {
            calculateLoadNow = false;

            for (Double d : leftValuesOverTime) {
                runningLoadLeftSum += d;
            }
            loadLeftCount += leftValuesOverTime.size();

            for (Double d : rightValuesOverTime) {
                runningLoadRightSum += d;
            }
            loadRightCount += rightValuesOverTime.size();

            leftValuesOverTime.clear();
            rightValuesOverTime.clear();
        }
    }


    void testingLoop() {
        telemetry.clear();

        telemetry.addData("Left Sensor", (bucketSensors.isLeftPixelIn()) ? "PIXEL" : "NO PIXEL");
        telemetry.addData("   Distance", df.format(bucketSensors.leftCentimeters()) + "cm");

        telemetry.addLine();

        telemetry.addData("Right Sensor", (bucketSensors.isRightPixelIn()) ? "PIXEL" : "NO PIXEL");
        telemetry.addData("   Distance", df.format(bucketSensors.rightCentimeters()) + "cm");
    }
}
