package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.components.RobotComponents;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.macros.MacroSequence;
import org.firstinspires.ftc.teamcode.macros.arm.autodump.AutoDumpPointMacro;
import org.firstinspires.ftc.teamcode.macros.auto.AutoAdvancedMoveMacro;
import org.firstinspires.ftc.teamcode.macros.auto.AutoCorrectHeadingDumb;
import org.firstinspires.ftc.teamcode.macros.auto.AutoMoveMacro;
import org.firstinspires.ftc.teamcode.macros.generic.RunActionMacro;
import org.firstinspires.ftc.teamcode.macros.tuckdown.ArbitraryDelayMacro;
import org.firstinspires.ftc.teamcode.opmodes.NerdOpMode;
import org.firstinspires.ftc.teamcode.opmodes.teleop.functional.CompDrive;

@Autonomous(name = "Around the World", group = "bMeme")
public class AroundTheWorldAuto extends NerdOpMode  {

    SampleMecanumDrive drive;

    @Override
    public void init() {
        RobotComponents.init(this.hardwareMap);

        drive = new SampleMecanumDrive(this.hardwareMap);

        try {
            //RobotComponents.soundPool.preloadSound("aroundtheworldtrim.mp3");
        } catch (Exception e) {

        }
    }

    ElapsedTime time = new ElapsedTime();

    Pose2d spinPower = new Pose2d(0, 0, 1);

    @Override
    public void start() {
        time.reset();
        RobotComponents.init(hardwareMap);

        drive.setWeightedDrivePower(spinPower);

        try {
            RobotComponents.soundPool.play("aroundtheworldtrim.mp3");
        } catch (Exception e) {

        }
    }

    int lastPhaseNum = 0;

    @Override
    public void loop() {
        RobotComponents.tickSystems(this);

        // every ~3500pi milliseconds
        int phaseNum = (int) (time.milliseconds() / 10_996);

        if (phaseNum != lastPhaseNum) {
            if (phaseNum % 2 == 0) {
                spinPower = new Pose2d(0, 0, 1);
            } else {
                spinPower = new Pose2d(0, 0, -1);
            }

            lastPhaseNum = phaseNum;
        }

        drive.setWeightedDrivePower(spinPower.times(
                (0.25 * Math.sin(time.milliseconds() / 1000.0)) + 0.5
        ));
    }

}
