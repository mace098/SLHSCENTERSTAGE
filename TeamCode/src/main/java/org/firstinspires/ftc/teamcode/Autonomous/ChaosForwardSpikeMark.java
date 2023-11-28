package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Disabled
@Autonomous(name = "ChaosForwardSpikeMark", group = "Chaos")
public class ChaosForwardSpikeMark extends ChaosAutoHardwareMap {
    @Override
    public void runOpMode() {
        //Pls work should start robot
        init(hardwareMap);

        waitForStart();

        Drive(1, 18);
        while (IsBusy()) {}
        Brake();

        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }
}
