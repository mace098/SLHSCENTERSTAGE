package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Testing, Omni", group = "testing")
public class TestOmni extends LinearOpMode {
    @Override
    public void runOpMode() {
        ChaosAutoHardwareMap hw = new ChaosAutoHardwareMap(hardwareMap);
        waitForStart();

        /*
        _____
        \   /
         \ /
          x
         / \
        ^   \
       O------

       O = start point, end point
         */
        hw.Omni(0.4, 15, Math.PI / 4.0);
        report_func(hw); // won't work; Brake() & co. is called at the end of Omni()
        hw.Omni(0.4, 10, -1.0 * Math.PI);
        report_func(hw);
        hw.Omni(0.4, 15, -1.0 * Math.PI / 4.0);
        report_func(hw);
        hw.Omni(0.4, 10, -1.0 * Math.PI);
        report_func(hw);
    }

    void report_func(ChaosAutoHardwareMap hw) {
        telemetry.addData("fr_p", hw.frontRightMotor.getTargetPosition());
        telemetry.addData("fl_p", hw.frontLeftMotor.getTargetPosition());
        telemetry.addData("br_p", hw.backRightMotor.getTargetPosition());
        telemetry.addData("bl_p", hw.backLeftMotor.getTargetPosition());
        telemetry.addData("fr_s", hw.frontRightMotor.getPower());
        telemetry.addData("fl_s", hw.frontLeftMotor.getPower());
        telemetry.addData("br_s", hw.backRightMotor.getPower());
        telemetry.addData("bl_s", hw.backLeftMotor.getPower());
        telemetry.update();
    }
}
