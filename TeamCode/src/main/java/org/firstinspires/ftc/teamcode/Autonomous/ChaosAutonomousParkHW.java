package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "Backstage parking (HW) - Blue, closest to backstage", group = "Parking")
public class ChaosAutonomousParkHW extends LinearOpMode {
    @Override
    public void runOpMode() {
        ChaosAutoHardwareMap hw = new ChaosAutoHardwareMap(hardwareMap);
        waitForStart();

        // assume the robot starts facing the spike marks
        // assume the robot starts on the left side (A4 in Appendix B in the manual part 2)
        telemetry.addLine("Segment #1: Go forth, go left. Hopefully");
        telemetry.update();
        hw.Drive(0.5, 2);
        hw.Strafe(-0.8, -1 * (12 * 4));
    }
}
