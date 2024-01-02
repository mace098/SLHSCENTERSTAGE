package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Backstage parking (HW) - BLUE, furthest from backstage, left of blue backstage", group = "Parking")
public class ParkBlueFarLeft extends LinearOpMode {
    @Override
    public void runOpMode() {
        ChaosAutoHardwareMap hw = new ChaosAutoHardwareMap(hardwareMap);
        waitForStart();

        // assume the robot starts facing the spike marks
        // assume the robot starts on A2 (check Appendix B in Game Manual Part 2)
        //  field is 6x6 tiles, tiles are 2'x2', i.e. field is 12'x12'
        // space from wall (2" enough) + go to C2 ((2 tiles * 2 feet * 12 convert to in)")
        // NOTE: may want to wait... may crash into other team?
        hw.Drive(0.5, 1 + (2 * 2 * 12));
        // now go to C4
        hw.Strafe(-0.5, -1 * (2 * 2 * 12));
        // backwards to A4
        hw.Drive(-0.5, -1 * (2 * 2 * 12));
        // strafe to A6
        hw.Strafe(-0.5, -1 * (2 * 2 * 12));
    }
}
