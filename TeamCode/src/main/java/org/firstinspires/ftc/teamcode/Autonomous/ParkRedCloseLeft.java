package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Backstage parking (HW) - RED, closest to backstage, left of red backstage", group = "Parking")
public class ParkRedCloseLeft extends LinearOpMode {
    @Override
    public void runOpMode() {
        ChaosAutoHardwareMap hw = new ChaosAutoHardwareMap(hardwareMap);
        waitForStart();

        // assume the robot starts facing the spike marks
        // assume the robot starts at F4 (check Appendix B in Game Manual Part 2)
        // make a little bit of space, don't slide against the playing field's wall
        hw.Drive(0.5, 1);
        // simply strafe to F6
        hw.Strafe(0.5, (2 * 2 * 12));
        // TODO: function like "int TilesToInches(float/int tiles)"?
        //  12 * 2 * 2 might look a little arbitrary
    }
}
