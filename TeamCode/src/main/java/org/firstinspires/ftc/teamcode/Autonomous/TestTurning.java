package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.concurrent.TimeUnit;

@Autonomous(name = "Testing, Turning", group = "testing")
public class TestTurning extends LinearOpMode {
    @Override
    public void runOpMode() {
        ChaosAutoHardwareMap hw = new ChaosAutoHardwareMap(hardwareMap);
        waitForStart();

        telemetry.addData("Turning", "10");
        telemetry.update();
        hw.Turn(0.4, 10);
        hw.Turn(0.4, -10);

        hw.Wait(2000);

        telemetry.addData("Turning", "20");
        telemetry.update();
        hw.Turn(0.4, 20);
        hw.Turn(0.4, -20);

        hw.Wait(2000);

        telemetry.addData("Turning", "30");
        telemetry.update();
        hw.Turn(0.4, 30);
        hw.Turn(0.4, -30);
    }
}
