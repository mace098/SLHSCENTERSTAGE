package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.robocol.RobocolParsable;
import com.qualcomm.robotcore.robocol.TelemetryMessage;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Autonomous(name = "ChaosAutoMotorTest_A", group = "Testing")
public class ChaosAutoMotorTestNoHwMap extends LinearOpMode {
    DcMotor frontRightMotor = null;
    DcMotor frontLeftMotor = null;
    public DcMotor backRightMotor = null;
    public DcMotor backLeftMotor = null;

    @Override
    public void runOpMode() {
        //int ticks_per_rev = 1400;
        frontRightMotor = hardwareMap.dcMotor.get("frontRightMotor");
        frontLeftMotor = hardwareMap.dcMotor.get("frontLeftMotor");
        backRightMotor = hardwareMap.dcMotor.get("backRightMotor");
        backLeftMotor = hardwareMap.dcMotor.get("backLeftMotor");

        // Run motor setup
        SetupMotors();

        waitForStart();

        // Some demo moves
        telemetry.addLine("Doing drive #1");
        telemetry.update();
        Drive(1.0, 1000);
        telemetry.addLine("Doing turn #2");
        telemetry.update();
        Turn(0.5, 1000);
        telemetry.addLine("Doing drive #3");
        telemetry.update();
        Drive(-1.0, 1000);
        telemetry.addLine("Doing turn #4");
        telemetry.update();
        Turn(1.0, 500);
        telemetry.addLine("Doing drive #5");
        telemetry.update();
        Drive(0.5,1000);
        telemetry.addLine("Done!");
        telemetry.update();
    }

    public void SetupMotors() {
        // Set motor directions
        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        frontLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        backRightMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotor.Direction.FORWARD);

        // Reset encoders
        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Set mode and brake motors on power = 0
        frontRightMotor.setTargetPosition(0);
        frontLeftMotor.setTargetPosition(0);
        backRightMotor.setTargetPosition(0);
        backLeftMotor.setTargetPosition(0);

        frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void Drive(double power, int position) {
        // Set the target position for motors
        frontRightMotor.setTargetPosition(position);
        frontLeftMotor.setTargetPosition(position);
        backRightMotor.setTargetPosition(position);
        backLeftMotor.setTargetPosition(position);

        // Power up the motors
        frontRightMotor.setPower(power);
        frontLeftMotor.setPower(power);
        backRightMotor.setPower(power);
        backLeftMotor.setPower(power);

        // Wait for the motors to finish moving
        while (frontRightMotor.isBusy() || frontLeftMotor.isBusy() || backRightMotor.isBusy() || backLeftMotor.isBusy()) {}

        // Stop the motors
        Brake();
    }

    public void Turn(double power, int position) {
        // Turn the robot clockwise

        // Set the target position for motors
        frontRightMotor.setTargetPosition(position);
        frontLeftMotor.setTargetPosition(-position);
        backRightMotor.setTargetPosition(position);
        backLeftMotor.setTargetPosition(-position);

        // Power up the motors
        frontRightMotor.setPower(power);
        frontLeftMotor.setPower(power);
        backRightMotor.setPower(power);
        backLeftMotor.setPower(power);

        // Wait for the motors to finish moving
        while (frontRightMotor.isBusy() || frontLeftMotor.isBusy() || backRightMotor.isBusy() || backLeftMotor.isBusy()) {}

        // Stop the motors
        Brake();
    }

    public void Brake() {
        frontRightMotor.setPower(0);
        frontLeftMotor.setPower(0);
        backRightMotor.setPower(0);
        backLeftMotor.setPower(0);
    }
}
