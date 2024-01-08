package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Backstage parking (HW) - RED, furthest from backstage, right of red backdrop", group = "Parking")
public class ParkRedFarRight extends LinearOpMode {
    @Override
    public void runOpMode() {
        ChaosAutoHardwareMap hw = new ChaosAutoHardwareMap(hardwareMap);
        waitForStart();

        // assume the robot starts facing the spike marks
        // assume the robot starts at F4 (check Appendix B in Game Manual Part 2)
        // drive to D2
        hw.Drive(0.5, 1 + (2 * 2 * 12));
        // strafe to D4
        hw.Strafe(0.5, 2 * 2 * 12);
        // drive backwards to F4
        hw.Drive(-0.5, -1 * (2 * 2 * 12));
        // strafe to F6
        hw.Strafe(0.5, 2 * 2 * 12);
    }
}
