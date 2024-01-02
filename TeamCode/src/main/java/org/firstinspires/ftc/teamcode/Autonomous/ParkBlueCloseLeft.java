package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Backstage parking (HW) - BLUE, closest to backstage, left of blue backstage", group = "Parking")
public class ParkBlueCloseLeft extends LinearOpMode {
    @Override
    public void runOpMode() {
        ChaosAutoHardwareMap hw = new ChaosAutoHardwareMap(hardwareMap);
        waitForStart();

        // assume the robot starts facing the spike marks
        // assume the robot starts at A4 (check Appendix B in Game Manual Part 2)
        // make a little bit of space, don't slide against the playing field's wall
        hw.Drive(0.5, 1);
        // simply strafe to A6
        hw.Strafe(-0.5, -1 * (12 * 4));
    }
}
