package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "ChaosAutoMotorTest1")
public class ChaosAutoMotorTest1 extends ChaosAutoHardwareMap {
    @Override
    public void runOpMode() {
        //Pls work should start robot
        init(hardwareMap);

        waitForStart();

        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }
}
