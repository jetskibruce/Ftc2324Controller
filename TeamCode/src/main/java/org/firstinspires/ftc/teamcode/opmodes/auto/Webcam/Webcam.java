package org.firstinspires.ftc.teamcode.opmodes.auto.Webcam;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;
import static org.firstinspires.ftc.teamcode.opmodes.auto.Webcam.GlobalVars.WEBCAM;
import static org.firstinspires.ftc.teamcode.opmodes.auto.Webcam.GlobalVars.xResolution;
import static org.firstinspires.ftc.teamcode.opmodes.auto.Webcam.GlobalVars.yResolution;

import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

public class Webcam {

    OpenCvCamera camera;
    PrimaryDetectionPipeline pipeline = new PrimaryDetectionPipeline();

    public void initCamera(HardwareMap hardwareMap, PrimaryDetectionPipeline.Color color){

        pipeline.initPipeline(color);

        int cameraMonitorId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId","id",hardwareMap.appContext.getPackageName());

        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "webcam"), cameraMonitorId);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {

            @Override
            public void onOpened() {
                try {
                    camera.setPipeline(pipeline);
                }
                catch(Exception exception){
                    telemetry.addLine("Error!");
                }
                camera.startStreaming(xResolution, yResolution, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
            }
        });
    }
    public PrimaryDetectionPipeline.ItemLocation getLocation(){
        return pipeline.getLocation();
    }
    public void stopStreaming(){
        camera.stopStreaming();

    }



}
