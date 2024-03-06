package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.checkerframework.checker.nullness.Opt;
import org.firstinspires.ftc.teamcode.opmodes.options.CloseRedSENSINGOption;
import org.firstinspires.ftc.teamcode.opmodes.options.CompetitionAutoOpMode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Autonomous(name="Select Competition Auto")
public class SelectCompetitionAuto extends OpMode {

    class Option {
        private Predicate<Gamepad> inputTrigger;
        private CompetitionAutoOpMode opmode;

        public Option(Predicate<Gamepad> in, CompetitionAutoOpMode mode) {
            this.inputTrigger = in;
            this.opmode = mode;
        }
    }

    private Option opt(
            Predicate<Gamepad> in,
            CompetitionAutoOpMode mode
    ) {
        return new Option(in, mode);
    }

    private List<Option> opmodeChoices = new ArrayList<>();

    void registerChoices() {
        opmodeChoices.add(opt(
                (pad) -> pad.right_trigger > 0.4f,
                new CloseRedSENSINGOption(this)
        ));
    }

    @Override
    public void init() {

    }

    @Override
    public void loop() {

    }
}
