package org.firstinspires.ftc.teamcode.macros;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Oh boy, quite the mess of code. Documented methods are relevant to an end user.
 * This class is used to construct macro sequences that can then be executed through it,
 * tracing through a number of steps.
 *
 * @implNote NOTE that MacroSequence objects store their steps and state directly. Steps
 * are not automatically re-constructed, so unless you have a proper reset built in when
 * all of them and their dependencies start, you need to construct the sequence once for each
 * time it is started. This is what I currently do. If you are trying to create a shared set of
 * steps and realize a static instance won't work due to the above, try a Supplier of MacroSequence.
 */
public class MacroSequence {

    private static ElapsedTime executionTime = new ElapsedTime();

    private static PathStep executingMacro = null;

    private static double TIMEOUT_MS = 5000;

    /**
     * Set the static TIMEOUT_MS after which a step is automatically skipped if it hasn't finished.
     * @param maxExecutionTime - defaults to 5000
     */
    public static void setTimeoutMs(double maxExecutionTime) {
        TIMEOUT_MS = maxExecutionTime;
    }

    public static boolean RunningMacro = false;

    private static void run(PathStep step) {
        executingMacro = step;
        executingMacro.start();
        RunningMacro = true;
        executionTime.reset();
    }

    /**
     * Reset variables to default, perhaps useful between OpMode starts.
     */
    public static void reset() {
        executionTime = new ElapsedTime();
        executingMacro = null;
        TIMEOUT_MS = 5000;
        RunningMacro = false;
    }

    /**
     * Ends the currently executing macro sequence.
     */
    public static void stopActiveMacro() {
        RunningMacro = false;
    }

    public static void tick(OpMode opMode) {
        if (executingMacro != null)  {
            if (RunningMacro) {
                executingMacro.onTick(opMode);

                // force executing path step to stop if it exceeds TIMEOUT_MS
                if (executingMacro.canTimeout() && executionTime.milliseconds() > TIMEOUT_MS) {
                    executingMacro.finish();
                }
            }
        }

    }

    /**
     * Construct a sequence from a number of path steps.
     * @param label - the name of the sequence to be displayed in macro debugging
     */
    public static MacroSequence compose(String label, PathStep... steps) {
        return new MacroSequence(label, steps);
    }

    /**
     * Construct and immediately start a sequence from a number of path steps.
     * @implNote NOTE that this will immediately start the sequence, overwriting any that
     *             are currently running indiscriminately. Be careful how you use this from inside
     *             macro code, as any steps after a step that starts a new macro will not be executed,
     *             and should probably be moved into the new macro.
     * @param label - the name of the sequence to be displayed in macro debugging
     */
    public static void begin(String label, PathStep... steps) {
        compose(label, steps).start();
    }

    /**
     * @return The currently executing PathStep.
     */
    public static PathStep getActiveStep() {
        return executingMacro;
    }

    /**
     * @return - true if path steps are currently executing at every tick
     */
    public static boolean isRunning() {
        return RunningMacro;
    }

    /**
     * Appends debug information about the current macro and its steps to telemetry output.
     * @param telemetry - telemetry object used for Driver Station output.
     */
    public static void appendDebugTo(Telemetry telemetry) {
        if (executingMacro == null) return;
        if (executingMacro.hostPath != null) {
            telemetry.addData("Executing Sequence: ", executingMacro.hostPath.label);
            telemetry.addData("\tIs finished?", executingMacro.hostPath.Finished);
            telemetry.addData("\tIdx Progress: ", executingMacro.hostPath.getStepIndex() + " / " + executingMacro.hostPath.getNumSteps());
        }
        telemetry.addData("Executing Step: ", executingMacro.getClass().getSimpleName());
        telemetry.addData("Timeout: ", executionTime.milliseconds() + " / " + TIMEOUT_MS);

    }

    // this class is split into static "manager" code and instanced "worker" code

    private List<PathStep> steps = new ArrayList<>();
    private int stepIndex = -1;
    public boolean Finished = false;

    private String label;


    /**
     * You can manually construct sequences, or use the helpers.
     * @see #compose(String, PathStep...)
     * @see #begin(String, PathStep...)
     */
    public MacroSequence(String label, PathStep[] steps) {
        this.label = label;
        this.steps.addAll(Arrays.asList(steps));

    }

    /**
     * Used for internal purposes but can be used by an end user if managed carefully.
     * Runs the next PathStep in the sequence, skipping the rest of the current one if it
                    isn't already done. Internally used to progress steps as they finish.
     * @throws RuntimeException if a sequence is marked as 'Finished' and you try to call this
     */
    public void runNext() {
        if (Finished) throw new RuntimeException("Tried to run the next sequence step on a stopped sequence!");
        stepIndex++;
        if (stepIndex >= steps.size()) {
            stop();
            return;
        }
        steps.get(stepIndex).setHostPath(this);
        MacroSequence.run(steps.get(stepIndex));
    }

    /**
     * Marks the sequence as finished and stops executing macro code.
     */
    public void stop() {
        Finished = true;
        MacroSequence.stopActiveMacro();
        return;
    }

    /**
     * The most commonly user-facing method, just starts the sequence after you're done making it.
     * @implNote NOTE that this will immediately start the sequence, overwriting any that
            are currently running indiscriminately. Be careful how you use this from inside
            macro code, as any steps after a step that starts a new macro will not be executed,
            and should probably be moved into the new macro.
     * @see #begin(String, PathStep...)
     */
    public void start() {
        stepIndex = -1;
        Finished = false;
        runNext();
    }

    /**
     * Inserts any number of PathSteps BEFORE the currently present ones in the sequence.
     * I have not provided an insert method, to discourage fragile position-dependent code.
     * @return the sequence object
     */
    public MacroSequence prepend(PathStep... newSteps) {
        this.steps.addAll(0, Arrays.asList(newSteps));
        return this;
    }

    /**
     * Inserts any number of PathSteps AFTER the currently present ones in the sequence.
     * I have not provided an insert method, to discourage fragile position-dependent code.
     * @return the sequence object
     */
    public MacroSequence append(PathStep... newSteps) {
        this.steps.addAll(Arrays.asList(newSteps));
        return this;
    }

    public String getLabel() {
        return label;
    }

    public int getStepIndex() {
        return stepIndex;
    }

    public int getNumSteps() {
        return steps.size();
    }

}
