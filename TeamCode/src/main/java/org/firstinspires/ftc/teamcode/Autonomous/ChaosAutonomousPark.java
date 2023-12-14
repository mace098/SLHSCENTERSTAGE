package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "Park by the backdrop", group = "Chaos")
public class ChaosAutonomousPark extends LinearOpMode {
    static final int COUNTS_PER_MOTOR_REV_NEVEREST20 = 560;
    static final int WHEEL_DIAMETER_INCHES = 4 ;     // For figuring circumference
    static final int COUNTS_PER_INCH = (int) ((COUNTS_PER_MOTOR_REV_NEVEREST20)/(WHEEL_DIAMETER_INCHES*Math.PI));

    public DcMotor frontRightMotor = null;
    public DcMotor frontLeftMotor = null;
    public DcMotor backRightMotor = null;
    public DcMotor backLeftMotor = null;

    public ElapsedTime runtime = new ElapsedTime();

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

        // assume the robot starts facing the spike marks
        // assume the robot starts on the left side (A4 in Appendix B in the manual part 2)
        telemetry.addLine("Segment #1: Go forth, go left. Hopefully");
        telemetry.update();
        Drive(0.5, 2);
        Strafe(-0.8, -1 * (12 * 4));
    }

    public void ResetEncoders() {
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
    }

    public void SetupMotors() {
        // Set motor directions
        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        frontLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        backRightMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotor.Direction.FORWARD);

        ResetEncoders();

        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    // position in inches
    public void Drive(double power, int position) {
        // Set the target position for motors
        frontRightMotor.setTargetPosition(position*COUNTS_PER_INCH);
        frontLeftMotor.setTargetPosition(position*COUNTS_PER_INCH);
        backRightMotor.setTargetPosition(position*COUNTS_PER_INCH);
        backLeftMotor.setTargetPosition(position*COUNTS_PER_INCH);

        // Power up the motors
        frontRightMotor.setPower(power);
        frontLeftMotor.setPower(power);
        backRightMotor.setPower(power);
        backLeftMotor.setPower(power);

        // Wait for the motors to finish moving
        while (frontRightMotor.isBusy() || frontLeftMotor.isBusy() || backRightMotor.isBusy() || backLeftMotor.isBusy()) {}

        // Stop the motors
        Brake();
        ResetEncoders();
    }

    // prelim
    public void Strafe(double power, int position) {
        // Set the target position for motors
        frontRightMotor.setTargetPosition(-position*COUNTS_PER_INCH);
        frontLeftMotor.setTargetPosition(position*COUNTS_PER_INCH);
        backRightMotor.setTargetPosition(position*COUNTS_PER_INCH);
        backLeftMotor.setTargetPosition(-position*COUNTS_PER_INCH);

        /*
        front

        ^---v
        |bot| -=> direction
        v---^

        back
         */

        // Power up the motors
        frontRightMotor.setPower(-power);
        frontLeftMotor.setPower(power);
        backRightMotor.setPower(power);
        backLeftMotor.setPower(-power);

        // Wait for the motors to finish moving
        while (frontRightMotor.isBusy() || frontLeftMotor.isBusy() || backRightMotor.isBusy() || backLeftMotor.isBusy()) {}

        // Stop the motors
        Brake();
        ResetEncoders();
    }

    public void Brake() {
        frontRightMotor.setPower(0.0);
        frontLeftMotor.setPower(0.0);
        backRightMotor.setPower(0.0);
        backLeftMotor.setPower(0.0);
    }
}
