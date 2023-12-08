package org.firstinspires.ftc.teamcode.opmodes.auto;

import android.app.Activity;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.vision.FrameProcessor;
import org.firstinspires.ftc.vision.VisionPortal;

@Autonomous(name="Vision Auto", group = "Robot")
public class SmarterAuto extends OpMode {

    private VisionPortal visionPortal;

    private View relativeLayout;

    private FrameProcessor frameProcessor;


    @Override
    public void init() {

        int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
        relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);

        // Wait for the DS start button to be touched.
        telemetry.addData("DS preview on/off", "3 dots, Camera Stream");
        telemetry.addData(">", "Touch Play to start OpMode");
        telemetry.update();

        frameProcessor = new FrameProcessor(relativeLayout);

        visionPortal = VisionPortal.easyCreateWithDefaults(
                hardwareMap.get(WebcamName.class, "Webcam 1"), frameProcessor);

    }

    @Override
    public void loop() {

        // Push telemetry to the Driver Station.
        telemetry.update();

    }

    @Override
    public void stop() {
        visionPortal.close();
    }
}
