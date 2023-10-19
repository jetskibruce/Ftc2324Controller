
package org.firstinspires.ftc.teamcode;

//import com.outoftheboxrobotics.photoncore.PhotonCore;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.checkerframework.checker.units.qual.C;
import org.firstinspires.ftc.teamcode.excutil.Input;
import org.firstinspires.ftc.teamcode.excutil.coroutines.CoroutineManager;
import org.firstinspires.ftc.teamcode.excutil.coroutines.CoroutineResult;

@TeleOp(name="Servo_Arm_Test_2")
public class Servo_Arm_Test_2 extends OpMode {

    private static class Constants {
        public static final float FRONT_CLAW_GRAB = 0.49f;
        public static final float BACK_CLAW_GRAB = 0.69f;

        public static final float FRONT_CLAW_RELEASE = 0.75f;
        public static final float BACK_CLAW_RELEASE = 0.49f;
    }

    private DcMotor front_left  = null;
    private DcMotor front_right = null;
    private DcMotor back_left   = null;
    private DcMotor back_right  = null;

    private DcMotor tower_motor = null;

    private CRServo intake_servo = null;

    private Servo intake_tilt_servo = null;

    private Servo front_claw_servo = null;
    private Servo back_claw_servo = null;
    private Servo front_wrist_servo = null;
    private Servo back_wrist_servo = null;


    Gamepad currentGamepad1;
    Gamepad previousGamepad1;

    private DcMotor getMotor(String name) {
        return hardwareMap.get(DcMotor.class, name);
    }

    private Servo getServo(String name) {
        return hardwareMap.get(Servo.class, name);
    }



    Servo[] servosToTest;

    @Override
    public void init() {

        front_left   = getMotor("front_left");
        front_right  = getMotor("front_right");
        back_left    = getMotor("back_left");
        back_right   = getMotor("back_right");

        tower_motor = getMotor("tower_motor");

        intake_servo = hardwareMap.get(CRServo.class, "intake_servo");

        intake_tilt_servo = getServo("intake_tilt_servo");


        front_claw_servo = getServo("front_claw_servo");
        back_claw_servo = getServo("back_claw_servo");
        front_wrist_servo = getServo("front_wrist_servo");
        back_wrist_servo = getServo("back_wrist_servo");

        servosToTest = new Servo[] {
                // 0
                front_claw_servo,
                // 1
                back_claw_servo,
                // 2
                front_wrist_servo,
                // 3
                back_wrist_servo
        };

        front_left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        front_right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        back_left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        back_right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        front_right.setDirection(DcMotor.Direction.REVERSE);
        currentGamepad1 = new Gamepad();
        previousGamepad1 = new Gamepad();
    }

    float servoTestMult = 0.1f;


    float selectedPosition = 0f;

    Input input1 = new Input();
    Input input2 = new Input();

    int selectedServoIndex = 0;

    private CoroutineManager coroutines = new CoroutineManager();

    private boolean clawClosed = false;

    @Override
    public void loop() {

        coroutines.tick(this);

        input1.pollGamepad(gamepad1);
        input2.pollGamepad(gamepad2);

        Servo selectedServo = servosToTest[selectedServoIndex];

        if (input1.a.down()) {
            coroutines.startRoutineLater((opmode, data) ->
            CoroutineResult.Explode,
            2000
            );
            //servoTestMult *= -1f;
        }

        if (input1.b.held()) {
            tower_motor.setPower(0.08f * (input1.b.numTicksHeld / 110f));
        } else {
            tower_motor.setPower(0f);
        }

        if (input1.y.held()) {
            tower_motor.setPower(-0.08f * (input1.y.numTicksHeld / 110f));
        } else {
            tower_motor.setPower(0f);
        }

        // claw toggle
        if (input1.x.down()) {
            clawClosed = !clawClosed;
            if (clawClosed) {
                front_claw_servo.setPosition(Constants.FRONT_CLAW_GRAB);
                back_claw_servo.setPosition(Constants.BACK_CLAW_GRAB);
            } else {
                front_claw_servo.setPosition(Constants.FRONT_CLAW_RELEASE);
                back_claw_servo.setPosition(Constants.BACK_CLAW_RELEASE);
            }
        }


        telemetry.addData("Selected Servo", selectedServoIndex);
        telemetry.addData("Selected Position", selectedPosition);
        telemetry.addData("# Coroutines Active", coroutines.numActive());

        telemetry.addData("claw state", clawClosed);

        return;
        /*if (input1.b.down()) {
            selectedPosition += servoTestMult;
            selectedServo.setPosition(selectedPosition);
        }

        if (input1.x.down()) {
            selectedPosition += servoTestMult;
            selectedServo.setPosition(selectedPosition);
        }

        if (input1.y.down()) {
            selectedServoIndex = ((++selectedServoIndex) % 4);
            selectedPosition = 0f;
        }


        return;*/

        /*
        double drive = gamepad1.left_stick_y;
        double strafe = -gamepad1.left_stick_x;
        double twist = -gamepad1.right_stick_x;
        boolean intake_out = gamepad1.left_bumper;
        boolean intake_in = gamepad1.right_bumper;

        double[] speeds = {
                (drive + strafe + twist),
                (drive - strafe - twist),
                (drive - strafe + twist),
                (drive + strafe - twist)
        };
        double max = Math.abs(speeds[0]);
        for (int i = 0; i < speeds.length; i++) {
            if (max < Math.abs(speeds[i])) max = Math.abs(speeds[i]);
        }
        if (max > 1) {
            for (int i = 0; i < speeds.length; i++) speeds[i] /= max;
        }
//
//        front_left.setPower(speeds[0]);
//        front_right.setPower(speeds[1]);
//        back_left.setPower(speeds[2]);
//        back_right.setPower(speeds[3]);

        if (intake_in)
        {
            intake_servo.setPower(0.50);
            intake_tilt_servo.setPosition(0.80);
        }
        else
        {
            if (intake_out)
            {
                intake_servo.setPower(-1);
                intake_tilt_servo.setPosition(0.85);
            }
            else
            {
                intake_servo.setPower(0);
                intake_tilt_servo.setPosition(0.91);
            }
        }
        */
    }
}
