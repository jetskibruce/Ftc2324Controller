package org.firstinspires.ftc.teamcode.opmodes.teleop.misc;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;
import static org.firstinspires.ftc.teamcode.components.RobotConstants.BUCKET_IDLE_POSITION;
import static org.firstinspires.ftc.teamcode.components.RobotConstants.BUCKET_SCORE_POSITION;
import static org.firstinspires.ftc.teamcode.components.RobotConstants.BUCKET_TELEOP_POSITION;
import static org.firstinspires.ftc.teamcode.components.RobotConstants.WRIST_IDLE_POSITION;
import static org.firstinspires.ftc.teamcode.components.RobotConstants.WRIST_SCORE_POSITION;
import static org.firstinspires.ftc.teamcode.components.RobotConstants.WRIST_TELEOP_POSITION;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import org.firstinspires.ftc.teamcode.components.RobotComponents;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.excutil.MotorPath;
import org.firstinspires.ftc.teamcode.excutil.coroutines.SimpleAction;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequenceBuilder;


import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import fi.iki.elonen.NanoHTTPD;

@TeleOp(name="Demo Control", group="zPromo")
public class HTTPControl extends OpMode {

    @FunctionalInterface
    interface TrajectoryStep {
        void addTo(TrajectorySequenceBuilder builder);
    }

    SampleMecanumDrive drive;
    Pose2d startPose;

    @Override
    public void init() {
        RobotComponents.init(hardwareMap);
        drive = new SampleMecanumDrive(hardwareMap);

        startPose = new Pose2d(11, -61, Math.toRadians(90));


        drive.setPoseEstimate(startPose);

        try {
            initWeb();
        } catch (IOException ex) {
            telemetry.addData("ERROR", "Failed to start web server");
            telemetry.speak(ex.toString());
            //this.requestOpModeStop();
        }

    }

    static final int PORT = 6765;

    void initWeb() throws IOException {
        try {
            new WebApp();
        } catch (Exception e) {
            telemetry.addData("fuck", e.toString());
        }

        /*ThreadPoolExecutor threadPoolExecutor =
                (ThreadPoolExecutor) Executors.newFixedThreadPool(2);

        server.createContext("/remote", new BotHttpHandler());
        server.setExecutor(threadPoolExecutor);*/
        //server.start();
        telemetry.addData("INFO", "Server started on port " + PORT);

    }

    static ConcurrentLinkedQueue<TrajectoryStep> trajectorySteps;
    static TrajectorySequence executingSequence = null;

    @Override
    public void start() {

    }

    @Override
    public void loop() {

       /* if (!drive.isBusy()) {
            executingSequence = null;
        }

        if (executingSequence == null) {
            TrajectorySequenceBuilder newBuilder = sequence();
            while (trajectorySteps.size() > 0) {
                trajectorySteps.poll().addTo(newBuilder);
            }
            executingSequence = newBuilder.build();
            follow(
                executingSequence
            );
        }*/
    }

    void runActionBasedOnCommand(String command) {
        String lc = command.toLowerCase(Locale.ROOT);
        String[] lcSplit = lc.split(" ");

        if (lcSplit.length == 0) return;

        TrajectoryStep out = null;
        switch (lcSplit[0]) {
            case "forward":
                out = forward(lcSplit);
                break;
            case "left":

                break;
        }

        if (out != null) {
            trajectorySteps.add(out);
        }
    }

    TrajectoryStep forward(String[] lcSplit) {
        if (lcSplit.length < 2) return null;
        double distance = Double.parseDouble(lcSplit[1]);

        return (seq) -> seq.forward(distance);
    }

    TrajectorySequenceBuilder sequence() {
        return drive.trajectorySequenceBuilder(startPose);
    }

    void follow(TrajectorySequence traj) {
        drive.followTrajectorySequence(traj);
    }

    class WebApp extends NanoHTTPD {

        static final int PORT = 6765;

        public WebApp() throws IOException {
            super(PORT);
            start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
            telemetry.addData("Info", "Running, localhost:" + PORT);
        }

        @Override
        public Response serve(IHTTPSession session) {
            String message = "<html><body><h1>heyo</h1>\n";
            Map<String, List<String>> params = session.getParameters();

            if (params.get("command") == null) {
                message += "<p>shit yourself</p>";
            }

            return newFixedLengthResponse(message + "</body></html>\n");
        }
    }
}
