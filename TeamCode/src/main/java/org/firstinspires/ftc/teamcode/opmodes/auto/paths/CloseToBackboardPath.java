package org.firstinspires.ftc.teamcode.opmodes.auto.paths;

import org.firstinspires.ftc.teamcode.components.RobotComponents;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.excutil.funcs.Supplier;
import org.firstinspires.ftc.teamcode.macros.MacroSequence;
import org.firstinspires.ftc.teamcode.macros.auto.AutoCorrectHeadingDumb;
import org.firstinspires.ftc.teamcode.macros.auto.AutoMoveMacro;
import org.firstinspires.ftc.teamcode.macros.generic.RunActionMacro;
import org.firstinspires.ftc.teamcode.macros.tuckdown.ArbitraryDelayMacro;
import org.firstinspires.ftc.teamcode.opmodes.teleop.functional.CompDrive;

public class CloseToBackboardPath {

    public static class TowerPointingPathArguments {
        public final SampleMecanumDrive drive;
        public final AutoMoveMacro.LateralDirection strafeDirection;

        private TowerPointingPathArguments() {
            drive = null;
            strafeDirection = null;
        }

        public TowerPointingPathArguments(SampleMecanumDrive drive, AutoMoveMacro.LateralDirection strafeDirection) {
            this.drive = drive;
            this.strafeDirection = strafeDirection;
        }

    }

    public static class SidePointingPathArguments {
        public SampleMecanumDrive drive;
        public double goalHeadingTowardsBoard;

        public SidePointingPathArguments(SampleMecanumDrive drive, double goalHeadingTowardsBoard) {
            this.drive = drive;
            this.goalHeadingTowardsBoard = goalHeadingTowardsBoard;
        }
    }



    // for auto situations where we place it on the close tile, with the
    // arm tower pointing towards the backboard
    public static Supplier<TowerPointingPathArguments, MacroSequence> TOWER_POINTING_TO_BACKBOARD_PATH = null;

    public static Supplier<SidePointingPathArguments, MacroSequence> SIDES_POINTING_TO_BACKBOARD_PATH = null;

    static {
        TOWER_POINTING_TO_BACKBOARD_PATH = (args) -> MacroSequence.compose("Route to Backboard",
                new RunActionMacro((o) -> {
                    RobotComponents.left_pixel_hold_servo.setPosition(
                            CompDrive.PIXEL_HOLD_POSITION
                    );
                    RobotComponents.right_pixel_hold_servo.setPosition(
                            CompDrive.PIXEL_HOLD_POSITION
                    );
                    return false;
                }),
                new AutoMoveMacro(
                        args.drive, args.strafeDirection,
                        22.5, 0.35
                ),
                new AutoCorrectHeadingDumb(
                        args.drive, 0, 0.06, 2.5
                ).giveTimeout(750),
                new ArbitraryDelayMacro(400),
                new AutoMoveMacro(
                        args.drive, AutoMoveMacro.LateralDirection.Backward,
                        35, 0.35
                ),
                new AutoCorrectHeadingDumb(
                        args.drive, 0, 0.06, 2.5
                ).giveTimeout(750)
        );

        SIDES_POINTING_TO_BACKBOARD_PATH = (args) -> MacroSequence.compose("Route to Backboard",
                new RunActionMacro((o) -> {
                    RobotComponents.left_pixel_hold_servo.setPosition(
                            CompDrive.PIXEL_HOLD_POSITION
                    );
                    RobotComponents.right_pixel_hold_servo.setPosition(
                            CompDrive.PIXEL_HOLD_POSITION
                    );
                    return false;
                }),
                new AutoMoveMacro(
                        args.drive, AutoMoveMacro.LateralDirection.Backward,
                        22.5, 0.35
                ),
                new AutoCorrectHeadingDumb(
                        args.drive, args.goalHeadingTowardsBoard, 0.3, 2.5
                ).giveTimeout(750),
                new ArbitraryDelayMacro(400),
                new AutoMoveMacro(
                        args.drive, AutoMoveMacro.LateralDirection.Backward,
                        35, 0.35
                ),
                new AutoCorrectHeadingDumb(
                        args.drive, 0, 0.06, 2.5
                ).giveTimeout(750)
        );
    }
}
