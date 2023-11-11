package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.drive.Drive;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.excutil.RMath;
import org.firstinspires.ftc.teamcode.macros.MacroSequence;
import org.firstinspires.ftc.teamcode.macros.RobotComponents;
import org.firstinspires.ftc.teamcode.macros.auto.AutoOuttakePoseMacro;
import org.firstinspires.ftc.teamcode.macros.tuckdown.ArbitraryDelayMacro;
import org.firstinspires.ftc.teamcode.macros.tuckdown.DriveBackMacro;
import org.firstinspires.ftc.teamcode.macros.tuckdown.DumpPoseMacro;
import org.firstinspires.ftc.teamcode.macros.tuckdown.IntakePoseMacro;

@Autonomous(name="Drive Forward", group="Robot")
public class DriveForwardAuto extends OpMode {
    static final double     COUNTS_PER_ENCODER_REV    = 8192 ;    // eg: TETRIX Motor Encoder
    static final double     ENCODER_GEAR_REDUCTION    = 1.0 ;     // No External Gearing.
    static final double     WHEEL_DIAMETER_INCHES   = 2.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_ENCODER_REV * ENCODER_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);

    static final double     DRIVE_SPEED             = 0.3;
    static final double     TURN_SPEED              = 0.5;


    @Override
    public void init() {
        RobotComponents.init(this.hardwareMap);

        mFL = RobotComponents.front_left;
        mFR = RobotComponents.front_right;
        mBL = RobotComponents.back_left;
        mBR = RobotComponents.back_right;
    }

    static final double DISTANCE_IN = 32;
    static final double GOAL_TICKS = DISTANCE_IN * COUNTS_PER_INCH;

    static DcMotor mFL, mFR, mBL, mBR;

    public static void equalPowers(double pow) {
        mFL.setPower(pow);
        mFR.setPower(-pow);
        mBL.setPower(pow);
        mBR.setPower(-pow);
    }

    ElapsedTime runtime = new ElapsedTime();

    boolean debug = true;

    @Override
    public void start() {

        //equalPowers(-DRIVE_SPEED);
        runtime.reset();

        if (debug) {
            MacroSequence.compose(
                    new AutoOuttakePoseMacro(),
                    //new DriveBackMacro(1000),
                    new IntakePoseMacro()

            ).start();
        }

    }

    boolean dumponce = true;

    @Override
    public void loop() {
        if (true) return;
        RobotComponents.tickSystems(this);
        if (runtime.seconds() > 7 || Math.abs(RobotComponents.parallelEncoder.getCurrentPosition()) >= Math.abs(GOAL_TICKS)) {
            equalPowers(0);
            dumponce = false;


        };

    }
}
