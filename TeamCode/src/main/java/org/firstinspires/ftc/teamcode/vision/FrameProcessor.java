package org.firstinspires.ftc.teamcode.vision;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;

import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.Mat;

public class FrameProcessor implements VisionProcessor {

    View relativeLayout;
    public FrameProcessor(View relativeLayout) {
        this.relativeLayout = relativeLayout;
    }

    @Override
    public void init(int width, int height, CameraCalibration calibration) {

    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
        double r = 0, g = 0, b = 0;
        for (int x = 0; x < frame.cols(); x++) {
            for (int y = 0; y < frame.rows(); y++) {
                double[] val = frame.get(x, y);
                r += val[0];
                g += val[1];
                b += val[2];
            }
        }
        int numPx = frame.cols() * frame.rows();
        r /= numPx;
        g /= numPx;
        b /= numPx;

        float finalR = (float)r;
        float finalG = (float)g;
        float finalB = (float)b;
        relativeLayout.post(new Runnable() {
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    relativeLayout.setBackgroundColor(Color.argb(1, finalR, finalG, finalB));
                }
            }
        });
        return null;
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {

    }

}
