package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.concurrent.TimeUnit;

@Autonomous(name = "Testing, Continuous Driving", group = "testing")
public class TestContinuous extends LinearOpMode {
    @Override
    public void runOpMode() {
        ChaosAutoHardwareMap hw = new ChaosAutoHardwareMap(hardwareMap);
        waitForStart();

        hw.continuous = true;
        telemetry.addData("Continuous", hw.continuous ? "True" : "False");
        telemetry.update();
        hw.Drive(0.3, 10);
        hw.Drive(0.6, 10);
        hw.Strafe(0.4, 10);

        hw.Wait(2000);

        hw.continuous = false;
        telemetry.addData("Continuous", hw.continuous ? "True" : "False");
        telemetry.update();
        hw.Drive(0.3, -10);
        hw.Drive(0.6, -10);
        hw.Strafe(0.4, -10);
    }
}
