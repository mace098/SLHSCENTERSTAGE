package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Backstage parking (HW) - RED, furthest from backstage, left of red backdrop", group = "Parking")
public class ParkRedFarLeft extends LinearOpMode {
    @Override
    public void runOpMode() {
        ChaosAutoHardwareMap hw = new ChaosAutoHardwareMap(hardwareMap);
        waitForStart();

        // assume the robot starts facing the spike marks
        // assume the robot starts at F4 (check Appendix B in Game Manual Part 2)
        // drive to D2
        hw.Drive(0.5, 1 + (2 * 2 * 12));
        // strafe to D6
        hw.Strafe(0.5, 4 * 2 * 12);
    }
}
