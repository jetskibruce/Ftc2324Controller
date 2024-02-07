package org.firstinspires.ftc.teamcode.opmodes.auto.newsensing;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.robot.Robot;

import org.firstinspires.ftc.teamcode.components.RobotComponents;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.macros.MacroSequence;
import org.firstinspires.ftc.teamcode.macros.auto.AutoMoveMacro;
import org.firstinspires.ftc.teamcode.macros.generic.RunActionMacro;
import org.firstinspires.ftc.teamcode.opmodes.auto.Webcam.PrimaryDetectionPipeline;
import org.firstinspires.ftc.teamcode.opmodes.auto.Webcam.Webcam;
import org.firstinspires.ftc.teamcode.opmodes.auto.paths.CloseToBackboardPath;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Config
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="REDREDREREDE Auto")
public class RedSensingAuto extends OpMode {
    Webcam webcam = new Webcam();

    SampleMecanumDrive drive;

    public TrajectorySequence scoreSpikeMark, scoreBackDrop, park;
    public static double power = 0.25;

    protected PrimaryDetectionPipeline.Color colorToDetect = PrimaryDetectionPipeline.Color.BLUE;

    @Override
    public void init() {
        RobotComponents.init(hardwareMap);
        webcam.initCamera(hardwareMap, colorToDetect);

        drive = new SampleMecanumDrive(this.hardwareMap);
    }


    protected PrimaryDetectionPipeline.ItemLocation elementLocation = null;

    @Override
    public void start() {

        RobotComponents.coroutines.runLater(() -> {
                    telemetry.addData("Location: ", webcam.getLocation());
                    telemetry.update();
                    elementLocation = webcam.getLocation();
                    if (webcam.getLocation() == PrimaryDetectionPipeline.ItemLocation.CENTER) {
                        telemetry.addLine();
                    } else if (webcam.getLocation() == PrimaryDetectionPipeline.ItemLocation.RIGHT) {
                        telemetry.addLine();
                    } else if (webcam.getLocation() == PrimaryDetectionPipeline.ItemLocation.LEFT) {
                        telemetry.addLine();
                    }

                    RobotComponents.coroutines.runLater(() -> {

                        startPath();

                    }, 500);
                },
                1000
        );

    }

    void startPath() {
        MacroSequence path = CloseToBackboardPath.SIDES_POINTING_TO_BACKBOARD_PATH.get(
                new CloseToBackboardPath.SidePointingPathArguments(
                        drive,
                        90
                )
        );

        path.append(
                new RunActionMacro((op) -> {
                    telemetry.speak("Found an element at location: " + elementLocation);
                    return false;
                })
        );

        path.start();
    }

    @Override
    public void loop() {
        RobotComponents.tickSystems(this);


    }
}
