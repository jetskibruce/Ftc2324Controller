package org.firstinspires.ftc.teamcode.opmodes.debug;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.excutil.Input;
import org.firstinspires.ftc.teamcode.excutil.RMath;
import org.firstinspires.ftc.teamcode.macros.PathStep;
import org.firstinspires.ftc.teamcode.macros.RobotComponents;
import org.firstinspires.ftc.teamcode.macros.tuckdown.ArbitraryDelayMacro;
import org.firstinspires.ftc.teamcode.macros.tuckdown.DumpPoseMacro;
import org.firstinspires.ftc.teamcode.macros.tuckdown.IntakePoseMacro;
import org.firstinspires.ftc.teamcode.macros.tuckdown.LowerArmMacro;
import org.firstinspires.ftc.teamcode.macros.tuckdown.RaiseTuckMacro;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@TeleOp(group = "drive")
public class PoseFinder extends OpMode {

    private enum PoseMode {
        POSE_SERVO,
        POSE_MOTOR,
        NONE
    }

    private static final Supplier<PathStep>[] macros = new Supplier[] {
            IntakePoseMacro::new,
            RaiseTuckMacro::new,
            () -> new ArbitraryDelayMacro(1000),
            DumpPoseMacro::new,
            LowerArmMacro::new
    };

    private PoseMode currentPoseMode = PoseMode.NONE;





    private Input input = new Input();

    @Override
    public void init() {
        RobotComponents.init(this.hardwareMap);
    }

    boolean inExactPoseMode = false;

    int numTicksLeftStickUp = 0, numTicksLeftStickDown = 0;
    int numTicksRightStickUp = 0, numTicksRightStickDown = 0;


    @Override
    public void loop() {

        RobotComponents.tickSystems(this);
        input.pollGamepad(gamepad1);

        if (gamepad1.left_stick_y < -0.6) numTicksLeftStickUp++;
        else numTicksLeftStickUp = 0;

        if (gamepad1.left_stick_y > 0.6) numTicksLeftStickDown++;
        else numTicksLeftStickDown = 0;

        if (gamepad1.right_stick_y < -0.6) numTicksRightStickUp++;
        else numTicksRightStickUp = 0;

        if (gamepad1.right_stick_y > 0.6) numTicksRightStickDown++;
        else numTicksRightStickDown = 0;

        switch (currentPoseMode) {
            case POSE_SERVO:
                handleServoPoseMode();
                break;
            case POSE_MOTOR:
                handleMotorPoseMode();
                break;
            default:
                telemetry.addData("Press LB to enter servo pose mode, or RB to enter motor pose mode.", "");
                break;
        }

        if (input.left_bumper.down()) {
            // go to servo mode and target first servo again
            currentPoseMode = PoseMode.POSE_SERVO;
            switchToServoIdx(0);
            resetPoseSpecificVariables();
        }

        if (input.right_bumper.down()) {
            // go to servo mode and target first servo again
            currentPoseMode = PoseMode.POSE_MOTOR;
            switchToMotorIdx(0);
            resetPoseSpecificVariables();
        }

        if (inExactPoseMode) telemetry.addData("Using precise positioning", "");

        telemetry.addData((inExactPoseMode)
                        ? "Press B again at any time to exit precise positioning mode."
                        : "Press B at any time to enter precise positioning mode."
                , "");

        if (input.b.down()) {
            inExactPoseMode = !inExactPoseMode;
        }


    }

    private void resetPoseSpecificVariables() {
        motorSpeed = 0.7;
        motorPoseTickrateMultiplier = 2;

        servoSpeedMultiplier = 1;
    }

    private Map<Integer, Double> servoIdxToPos = new HashMap<>();
    RobotComponents.ServoComponent currentServo = null;
    int currentServoIdx = -1;
    double currentServoPos = 0;
    double servoSpeedMultiplier = 1;

    private static final double SERVO_SPEED_INCREMENT = 0.05;
    private static final int SERVO_SPEED_LOOPSPACE = 30;
    private void handleServoPoseMode() {

        telemetry.addData("Posing servo " + currentServo.name, "(idx " + currentServoIdx + ")");

        if (inExactPoseMode) {
            if (input.x.down()) currentServoPos -= 0.01 * servoSpeedMultiplier;
            if (input.y.down()) currentServoPos += 0.01 * servoSpeedMultiplier;
        } else {
            currentServoPos += gamepad1.right_trigger * 0.01 * servoSpeedMultiplier;
            currentServoPos -= gamepad1.left_trigger * 0.01 * servoSpeedMultiplier;
        }

        // +1 because 0 % x always == 0
        if ((numTicksLeftStickUp + 1) % SERVO_SPEED_LOOPSPACE == 0) {
            servoSpeedMultiplier += SERVO_SPEED_INCREMENT;
        }

        if ((numTicksLeftStickDown + 1) % SERVO_SPEED_LOOPSPACE == 0) {
            servoSpeedMultiplier -= SERVO_SPEED_INCREMENT;
        }

        // reset multiplier control
        if (gamepad1.left_stick_x < -0.8)
            servoSpeedMultiplier = 1;


        currentServoPos = RMath.clamp(currentServoPos, 0, 1);

        currentServo.servo.setPosition(currentServoPos);

        telemetry.addData("Servo target position is ", currentServoPos);
        telemetry.addData("Speedup factor: ", servoSpeedMultiplier + "x");

        if (input.a.down()) {
            currentServoIdx = (currentServoIdx + 1) % RobotComponents.servos.size();

            switchToServoIdx(currentServoIdx);
        }

    }

    private void switchToServoIdx(int newIdx) {

        if (currentServoIdx != -1) {
            servoIdxToPos.put(currentServoIdx, currentServoPos);
        }

        currentServoIdx = newIdx;

        currentServo = RobotComponents.servos.get(currentServoIdx);

        Double savedPos = servoIdxToPos.get(currentServoIdx);
        currentServoPos = (savedPos == null) ? 0 : savedPos;
    }


    private Map<Integer, Integer> motorIdxToTicks = new HashMap<>();
    RobotComponents.MotorComponent currentMotor = null;
    int currentMotorIdx = -1;
    int lastTicks = Integer.MIN_VALUE;
    int currentMotorTicks = 0;
    double motorSpeed = 0.7;
    int motorPoseTickrateMultiplier = 2;

    private static final double MOTOR_SPEED_INCREMENT = 0.05;
    private static final int MOTOR_SPEED_LOOPSPACE = 30;

    private static final double MOTOR_TICKRATE_INCREMENT = 1;
    private static final int MOTOR_TICKRATE_LOOPSPACE = 120;

    private void handleMotorPoseMode() {
        telemetry.addData("Posing motor " + currentMotor.name, "(idx " + currentMotorIdx + ")");

        if (inExactPoseMode) {
            if (input.x.down()) currentMotorTicks -= 1 * motorPoseTickrateMultiplier;
            if (input.y.down()) currentMotorTicks += 1 * motorPoseTickrateMultiplier;
        } else {
            if (input.right_trigger.held())
                currentMotorTicks += 1 * motorPoseTickrateMultiplier;

            if (input.left_trigger.held())
                currentMotorTicks -= 1 * motorPoseTickrateMultiplier;
        }

        // +1 because 0 % x always == 0
        if ((numTicksLeftStickUp + 1) % MOTOR_SPEED_LOOPSPACE == 0) {
            motorSpeed += MOTOR_SPEED_INCREMENT;
            currentMotor.motor.setPower(motorSpeed);
        }

        if ((numTicksLeftStickDown + 1) % MOTOR_SPEED_LOOPSPACE == 0) {
            motorSpeed -= MOTOR_SPEED_INCREMENT;
            currentMotor.motor.setPower(motorSpeed);
        }

        // +1 because 0 % x always == 0
        if ((numTicksRightStickUp + 1) % MOTOR_TICKRATE_LOOPSPACE == 0) {
            motorPoseTickrateMultiplier += MOTOR_TICKRATE_INCREMENT;
        }

        if ((numTicksRightStickDown + 1) % MOTOR_TICKRATE_LOOPSPACE == 0) {
            motorPoseTickrateMultiplier -= MOTOR_TICKRATE_INCREMENT;
        }

        // reset multiplier control
        if (gamepad1.left_stick_x < -0.8)
            motorSpeed = 0.7;

        if (gamepad1.right_stick_x < -0.8)
            motorPoseTickrateMultiplier = 2;

        if (currentMotorTicks != lastTicks) {
            currentMotor.motor.setTargetPosition(currentMotorTicks);
            currentMotor.motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        motorSpeed = RMath.clamp(motorSpeed, 0, 1);

        telemetry.addData("Motor target position is ", currentMotorTicks);
        telemetry.addData("Tickrate speedup factor: ", motorPoseTickrateMultiplier + "x");
        telemetry.addData("Motor power: ", motorSpeed + "/1");

        if (input.a.down()) {
            currentMotorIdx = (currentMotorIdx + 1) % RobotComponents.motors.size();

            switchToMotorIdx(currentMotorIdx);
        }

        lastTicks = currentMotorTicks;
    }

    private void switchToMotorIdx(int newIdx) {
        lastTicks = Integer.MIN_VALUE;

        if (currentMotorIdx != -1) {
            motorIdxToTicks.put(currentMotorIdx, currentMotorTicks);
        }

        currentMotorIdx = newIdx;

        currentMotor = RobotComponents.motors.get(currentMotorIdx);

        Integer savedTicks = motorIdxToTicks.get(currentMotorIdx);
        currentMotorTicks = (savedTicks == null) ? 0 : savedTicks;
    }

}
