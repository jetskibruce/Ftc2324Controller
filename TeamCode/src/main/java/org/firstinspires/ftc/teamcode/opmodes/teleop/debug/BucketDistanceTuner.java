package org.firstinspires.ftc.teamcode.opmodes.teleop.debug;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.components.RobotComponents;
import org.firstinspires.ftc.teamcode.excutil.Input;
import org.firstinspires.ftc.teamcode.excutil.coroutines.Coroutine;
import org.firstinspires.ftc.teamcode.excutil.coroutines.CoroutineAction;
import org.firstinspires.ftc.teamcode.excutil.coroutines.CoroutineResult;
import org.firstinspires.ftc.teamcode.sensing.BucketDistanceSensingArray;
import static org.firstinspires.ftc.teamcode.components.RobotComponents.*;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@TeleOp(name="Bucket Tuner", group="yConfigure")
public class BucketDistanceTuner extends OpMode {

    private DecimalFormat df = new DecimalFormat("#.##");
    private BucketDistanceSensingArray bucketSensors;

    @Override
    public void init() {
        RobotComponents.init(hardwareMap);
        bucketSensors = new BucketDistanceSensingArray(left_pixel_color_sensor, right_pixel_color_sensor);

        df.setRoundingMode(RoundingMode.DOWN);
    }

    private Input input = new Input();

    public void start() {
        telemetry.clear();
        menuScreen();;

    }

    void menuScreen() {
        telemetry.addLine("Bucket Distance Tuning");
        telemetry.addLine(" Press 'X' to tune!");
        telemetry.addLine(" Press 'Y' to test!");
        telemetry.addLine();
        telemetry.addLine("NOTE: Directions are stated from the 'robot's perspective,' where the intake is its face pointing out.");

    }

    boolean testingTune = false;

    @Override
    public void loop() {
        RobotComponents.tickSystems(this);
        input.pollGamepad(gamepad1);

        if (currentTunePhase != null) {
            tuningLoop();
            return;
        }

        if (testingTune) {
            testingLoop();
            return;
        }

        if (input.x.down()) {
            currentTunePhase = TunePhase.Start;
            telemetry.clear();
            telemetry.addLine("1) Ensure no unrealistic shadows/lighting will affect tuning.");
            telemetry.addLine("2) REMOVE any pixels already in the bucket, turn holders out of the way.");
            telemetry.addLine("3) Place bucket in the intake position, as if stowed for a game.");


            telemetry.addLine();
            return;
        }

        if (input.y.down()) {
            testingTune = true;
            leftWasIn = false;
            rightWasIn = false;
        }
    }

    enum TunePhase {
        Start,
        Idle,
        Load,
        End,
        WaitForExit
    }

    private TunePhase currentTunePhase = TunePhase.Start;

    List<Double> leftValuesOverTime = new ArrayList<>(100);
    List<Double> rightValuesOverTime = new ArrayList<>(100);





    ElapsedTime timeInPhase = new ElapsedTime();



    private double averageNoPixelLeft, averageNoPixelRight;
    private double averagePixelLeft, averagePixelRight;

    double averageList(List<Double> values) {
        double sum = 0;
        for (Double d : values) {
            sum += d;
        }
        return sum / values.size();
    }

    boolean showYPromptOnce = true;

    void tuningLoop() {
        switch (currentTunePhase) {
            case Start:
                tune_startPhaseLoop();
                break;

            case Idle:
                tune_idlePhaseLoop();
                break;

            case Load:
                tune_loadPhaseLoop();
                break;

            case End:
                tune_endPhaseLoop();
                break;

            case WaitForExit:
                if (showYPromptOnce) {
                    showYPromptOnce = false;
                    telemetry.addLine("'B' to exit.");
                }

                if (input.y.down()) {
                    telemetry.clear();
                    currentTunePhase = null;
                    menuScreen();
                    showYPromptOnce = true;
                }
                break;
        }
    }

    boolean continuePastStartReady = false;
    boolean isFirstTickStartRoutine = true;

    void tune_startPhaseLoop() {




        if (isFirstTickStartRoutine) {
            telemetry.speak("he he, tuning time");
            isFirstTickStartRoutine = false;
            coroutines.runLater(() -> {
                continuePastStartReady = true;

                telemetry.addLine("Press 'A' when it is properly positioned.");
            }, 700);
        }

        if (input.a.down() && continuePastStartReady) {
            continuePastStartReady = false;
            isFirstTickStartRoutine = true;

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

            telemetry.speak("done");
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

    boolean redoLoadReady = false;

    boolean skipOrRunAgainReady = false;
    boolean firstTickLoadRoutine = true;

    void tune_loadPhaseLoop() {

        if (firstTickLoadRoutine) {
            firstTickLoadRoutine = false;
            coroutines.runLater(() -> {
                loadStartReady = true;

                telemetry.addLine("Press 'A' when they are fully inserted.");
            }, 700);
        }

        if (input.a.down() && loadStartReady) {
            loadStartReady = false;

            timeInPhase.reset();
            coroutines.startRoutine(loadPollAction);
        }

        if (calculateLoadNow) {
            calculateLoadNow = false;
            loadAlmostThere = false;

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

            telemetry.addLine();
            telemetry.addLine("Press 'A' to improve accuracy with another set of pixel colors.");
            telemetry.addLine("Press 'Y' once you are satisfied.");

            telemetry.speak("done");

            redoLoadReady = true;
        }

        if (input.a.down() && redoLoadReady) {
            redoLoadReady = false;
            telemetry.clear();
            timeInPhase.reset();

            telemetry.addLine("Please swap each pixel to a color it has not been before.");
            telemetry.addLine();
            currentTunePhase = TunePhase.Load;
        }

        if (input.y.down() && redoLoadReady) {
            redoLoadReady = false;
            firstTickLoadRoutine = true;

            telemetry.clear();
            timeInPhase.reset();

            currentTunePhase = TunePhase.End;
        }
    }

    void tune_endPhaseLoop() {

        averagePixelLeft = runningLoadLeftSum / loadLeftCount;
        averagePixelRight = runningLoadRightSum / loadRightCount;

        if (averagePixelLeft > (averageNoPixelLeft * 0.85)) {
            telemetry.addLine("WARN: Left no pixel/pixel are similar. Test if detection is good.");
        }
        if (averagePixelRight > (averageNoPixelRight * 0.85)) {
            telemetry.addLine("WARN: Right no pixel/pixel are similar. Test if detection is good.");
        }

        double gapBetweenLeftValues = averageNoPixelLeft - averagePixelLeft;
        double gapBetweenRightValues = averageNoPixelRight - averagePixelRight;



        telemetry.addData("Presented", "Left/Right");
        telemetry.addData("Avg. No Pixel",
                df.format(averageNoPixelLeft) + " / " + df.format(averageNoPixelRight));
        telemetry.addData("Avg. w/ Pixel",
                df.format(averagePixelLeft) + " / " + df.format(averagePixelRight));


         BucketDistanceSensingArray.tune(
                 averagePixelLeft + (gapBetweenLeftValues * 0.4),
                 averagePixelRight + (gapBetweenRightValues * 0.4)
         );

         currentTunePhase = TunePhase.WaitForExit;
    }

    boolean leftWasIn = false;
    boolean rightWasIn = false;


    void testingLoop() {
        telemetry.clear();

        if (bucketSensors.isLeftPixelIn() && !leftWasIn) {
            telemetry.speak("left in");
        } else if (!bucketSensors.isLeftPixelIn() && leftWasIn) {
            telemetry.speak("left out");
        }

        leftWasIn = bucketSensors.isLeftPixelIn();

        if (bucketSensors.isRightPixelIn() && !rightWasIn) {
            telemetry.speak("right in");
        } else if (!bucketSensors.isRightPixelIn() && rightWasIn) {
            telemetry.speak(/* 5 is */ "right out");
        }

        rightWasIn = bucketSensors.isRightPixelIn();

        telemetry.addData("Left Sensor", (bucketSensors.isLeftPixelIn()) ? "PIXEL" : "NO PIXEL");
        telemetry.addData("   Distance", df.format(bucketSensors.leftCentimeters()) + "cm");
        telemetry.addData(" Threshold", df.format(BucketDistanceSensingArray.leftThreshold()));

        telemetry.addLine();

        telemetry.addData("Right Sensor", (bucketSensors.isRightPixelIn()) ? "PIXEL" : "NO PIXEL");
        telemetry.addData("   Distance", df.format(bucketSensors.rightCentimeters()) + "cm");
        telemetry.addData(" Threshold", df.format(BucketDistanceSensingArray.rightThreshold()));

        telemetry.addLine();

        telemetry.addLine("Put good thresholds in the code!");
        telemetry.addLine("'B' to exit!");

        if (input.b.down()) {
            testingTune = false;
            telemetry.clear();
            menuScreen();
        }
    }
}
