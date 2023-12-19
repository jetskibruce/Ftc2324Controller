package org.firstinspires.ftc.teamcode.opmodes.teleop.debug;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.robot.Robot;

import org.firstinspires.ftc.teamcode.excutil.Input;
import org.firstinspires.ftc.teamcode.excutil.RMath;
import org.firstinspires.ftc.teamcode.macros.PathStep;
import org.firstinspires.ftc.teamcode.components.RobotComponents;
import org.firstinspires.ftc.teamcode.macros.tuckdown.ArbitraryDelayMacro;
import org.firstinspires.ftc.teamcode.macros.tuckdown.DumpPoseMacro;
import org.firstinspires.ftc.teamcode.macros.arm.IntakePoseMacro;
import org.firstinspires.ftc.teamcode.macros.tuckdown.LowerArmMacro;
import org.firstinspires.ftc.teamcode.macros.tuckdown.RaiseTuckMacro;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@TeleOp(group = "drivez")
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

        RobotComponents.tower_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RobotComponents.climb_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    boolean inExactPoseMode = false;

    int numTicksLeftStickUp = 0, numTicksLeftStickDown = 0;
    int numTicksRightStickUp = 0, numTicksRightStickDown = 0;

    boolean intakeOn = false;

    @Override
    public void loop() {

        // update input, and macro/coroutine systems
        RobotComponents.tickSystems(this);
        input.pollGamepad(gamepad1);

        // begin tick trackers for pose modes

        if (gamepad1.left_stick_y < -0.6) numTicksLeftStickUp++;
        else numTicksLeftStickUp = 0;

        if (gamepad1.left_stick_y > 0.6) numTicksLeftStickDown++;
        else numTicksLeftStickDown = 0;

        if (gamepad1.right_stick_y < -0.6) numTicksRightStickUp++;
        else numTicksRightStickUp = 0;

        if (gamepad1.right_stick_y > 0.6) numTicksRightStickDown++;
        else numTicksRightStickDown = 0;

        // end pose mode trackers

        // go to separate logic depending on what mode we're on
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
        telemetry.addData("Tower ticks", RobotComponents.tower_motor.getCurrentPosition());

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

        if (gamepad1.left_stick_x > 0.8) {
            intakeOn = true;
        }

        if (gamepad1.right_stick_x > 0.8) {
            intakeOn = false;
        }

        boolean intake = intakeOn;

        double backPower = 1;
        double forePower = 1;
        RobotComponents.back_intake_servo.setPower(intake ? backPower : 0);
        RobotComponents.front_intake_servo.setPower(intake ? forePower : 0);

        telemetry.addData("Bucket Encoder Ticks: ", RobotComponents.parallelEncoder.getCurrentPosition());
        telemetry.addData("Wrist Encoder Ticks: ", RobotComponents.perpendicularEncoder.getCurrentPosition());

    }

    // resets speedup variables, used when switching between pose modes so you aren't suprised by old speed
    private void resetPoseSpecificVariables() {
        motorSpeed = 0.1;
        motorPoseTickrateMultiplier = 2;

        servoSpeedMultiplier = 1;
    }

    private Map<Integer, Double> servoIdxToPos = new HashMap<>();
    RobotComponents.ServoComponent currentServo = null;
    int currentServoIdx = -1;
    double currentServoPos = 0;
    double servoSpeedMultiplier = 1;

    // increment: amount it changes each time
    // loopspace : number of ticks in position between each increment
    private static final double SERVO_SPEED_INCREMENT = 0.02;
    private static final int SERVO_SPEED_LOOPSPACE = 90;
    private void handleServoPoseMode() {

        telemetry.addData("Posing servo " + currentServo.name, "(idx " + currentServoIdx + ")");

        if (inExactPoseMode) {
            if (input.x.down()) currentServoPos -= 0.01 * servoSpeedMultiplier;
            if (input.y.down()) currentServoPos += 0.01 * servoSpeedMultiplier;
        } else {
            currentServoPos += gamepad1.right_trigger * 0.002 * servoSpeedMultiplier;
            currentServoPos -= gamepad1.left_trigger * 0.002 * servoSpeedMultiplier;
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
        telemetry.addData("Position increment multiplier: ", servoSpeedMultiplier + "x");

        if (input.a.down()) {
            currentServoIdx = (currentServoIdx + 1) % RobotComponents.servos.size();

            switchToServoIdx(currentServoIdx);
        }

        for (Integer i : servoIdxToPos.keySet()) {
            telemetry.addData("servo " + i, servoIdxToPos.get(i));
        }

    }

    // switch all servo variables to new target and potentially load its old position
    private void switchToServoIdx(int newIdx) {

        if (currentServoIdx != -1) {
            servoIdxToPos.put(currentServoIdx, currentServoPos);
        }

        currentServoIdx = newIdx;

        currentServo = RobotComponents.servos.get(currentServoIdx);

        //Double savedPos = servoIdxToPos.get(currentServoIdx);
        //currentServoPos = (savedPos == null) ? 0 : savedPos;

        currentServoPos = currentServo.servo.getPosition();
    }


    private Map<Integer, Integer> motorIdxToTicks = new HashMap<>();
    RobotComponents.MotorComponent currentMotor = null;
    int currentMotorIdx = -1;
    int lastTicks = Integer.MIN_VALUE;
    int currentMotorTicks = 0;
    double motorSpeed = 0.1;
    int motorPoseTickrateMultiplier = 2;

    private static final double MOTOR_SPEED_INCREMENT = 0.015;
    private static final int MOTOR_SPEED_LOOPSPACE = 90;

    private static final double MOTOR_TICKRATE_INCREMENT = 1;
    private static final int MOTOR_TICKRATE_LOOPSPACE = 130;

    private static final int IMPRECISE_LOOPSPACE = 10;

    private void handleMotorPoseMode() {
        telemetry.addData("Posing motor " + currentMotor.name, "(idx " + currentMotorIdx + ")");

        if (inExactPoseMode) {
            if (input.x.down()) currentMotorTicks -= 1 * motorPoseTickrateMultiplier;
            if (input.y.down()) currentMotorTicks += 1 * motorPoseTickrateMultiplier;
        } else {
            if (input.right_trigger.held() && (input.right_trigger.numTicksHeld % IMPRECISE_LOOPSPACE == 0))
                currentMotorTicks += 1 * motorPoseTickrateMultiplier;

            if (input.left_trigger.held() && (input.left_trigger.numTicksHeld % IMPRECISE_LOOPSPACE == 0))
                currentMotorTicks -= 1 * motorPoseTickrateMultiplier;
        }

        // +1 because 0 % x always == 0
        if ((numTicksLeftStickUp + 1) % MOTOR_SPEED_LOOPSPACE == 0) {
            motorSpeed += MOTOR_SPEED_INCREMENT;
            //currentMotor.motor.setPower(motorSpeed);
        }

        if ((numTicksLeftStickDown + 1) % MOTOR_SPEED_LOOPSPACE == 0) {
            motorSpeed -= MOTOR_SPEED_INCREMENT;
            //currentMotor.motor.setPower(motorSpeed);
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
            motorSpeed = 0.1;

        if (gamepad1.right_stick_x < -0.8)
            motorPoseTickrateMultiplier = 2;

        motorSpeed = RMath.clamp(motorSpeed, 0, 1);

        currentMotor.motor.setPower(motorSpeed);

        if (currentMotorTicks != lastTicks) {
            currentMotor.motor.setTargetPosition(currentMotorTicks);
            currentMotor.motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            //currentMotor.motor.setDirection(DcMotorSimple.Direction.REVERSE);

        }



        telemetry.addData("Motor target position is ", currentMotorTicks);
        telemetry.addData("Tickrate increment multiplier: ", motorPoseTickrateMultiplier + "x");
        telemetry.addData("Motor power: ", motorSpeed + "/1");
        telemetry.addData("Current motor position: ", currentMotor.motor.getCurrentPosition());

        if (input.a.down()) {
            currentMotorIdx = (currentMotorIdx + 1) % RobotComponents.motors.size();

            switchToMotorIdx(currentMotorIdx);
        }

        lastTicks = currentMotorTicks;
    }

    // switch to motor at index and load its old position if it has one
    private void switchToMotorIdx(int newIdx) {
        lastTicks = Integer.MIN_VALUE;

        if (currentMotorIdx != -1) {
            motorIdxToTicks.put(currentMotorIdx, currentMotorTicks);
        }

        currentMotorIdx = newIdx;

        currentMotor = RobotComponents.motors.get(currentMotorIdx);
        //currentMotor.motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        Integer savedTicks = motorIdxToTicks.get(currentMotorIdx);
        currentMotorTicks = (savedTicks == null) ? 0 : savedTicks;

        //currentMotor.motor.setPower(motorSpeed);
    }

}
