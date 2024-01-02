package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Backstage parking (HW) - BLUE, closest to backstage, right of blue backstage", group = "Parking")
public class ParkBlueCloseRight extends LinearOpMode {
    @Override
    public void runOpMode() {
        ChaosAutoHardwareMap hw = new ChaosAutoHardwareMap(hardwareMap);
        waitForStart();

        // assume the robot starts facing the spike marks
        // assume the robot starts at A4 (check Appendix B in Game Manual Part 2)
        //drive to C4
        hw.Drive(0.5, 1 + (2 * 2 * 12));
        // simply strafe to C6
        hw.Strafe(-0.5, -1 * (2 * 2 * 12));
    }
}
