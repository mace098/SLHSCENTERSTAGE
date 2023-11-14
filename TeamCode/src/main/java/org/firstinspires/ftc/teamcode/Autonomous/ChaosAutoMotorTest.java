package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class ChaosAutoMotorTest extends ChaosAutoHardwareMap {
    @Override
    public void runOpMode() {
        // Startup robot
        init(hardwareMap);

        waitForStart();

        // Drive around
        Drive(1.0, 1000);
        Drive(-1.0, 1000);
        Drive(0.5,1000);

        // End
    }
}
