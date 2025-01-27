package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Backstage parking (HW) - RED, closest to backstage, right of red backstage", group = "Parking")
public class ParkRedCloseRight extends LinearOpMode {
    @Override
    public void runOpMode() {
        ChaosAutoHardwareMap hw = new ChaosAutoHardwareMap(hardwareMap);
        waitForStart();

        // assume the robot starts facing the spike marks
        // assume the robot starts at F4 (check Appendix B in Game Manual Part 2)
        // make a little bit of space, don't slide against the playing field's wall
        // drive to D4
        hw.Drive(0.5, 1 + (2 * 2 * 12));
        // simply strafe to D6
        hw.Strafe(0.5, (2 * 2 * 12));
    }
}
