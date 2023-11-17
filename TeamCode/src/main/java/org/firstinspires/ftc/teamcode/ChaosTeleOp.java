package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="Chaos")
public class ChaosTeleOp extends LinearOpMode {
    // Declare motors

    // Drive motors
    public DcMotor frontRightMotor;
    public DcMotor frontLeftMotor;
    public DcMotor backRightMotor;
    public DcMotor backLeftMotor;

    // Create weed wacker motor
    public DcMotor weedWackerMotor;

    // Create lifting and wheel motor
    public DcMotor liftWheelMotor;

    // Create belt motor
    public DcMotor beltMotor;

    // Create bench press motor
    public DcMotor benchPressMotor;

    @Override
    public void runOpMode() {
        // Drive motors connection
        frontRightMotor = hardwareMap.get(DcMotor.class, "frontRightMotor");
        frontLeftMotor = hardwareMap.get(DcMotor.class, "frontLeftMotor");
        backRightMotor = hardwareMap.get(DcMotor.class, "backRightMotor");
        backLeftMotor = hardwareMap.get(DcMotor.class, "backLeftMotor");

        // function motors connection
        liftWheelMotor = hardwareMap.get(DcMotor.class, "liftWheelMotor");
        weedWackerMotor = hardwareMap.get(DcMotor.class, "weedWackerMotor");
        beltMotor = hardwareMap.get(DcMotor.class, "beltMotor");
        benchPressMotor = hardwareMap.get(DcMotor.class, "benchPressMotor");

        // Motor directions; subject to change
        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        frontLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        backRightMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        // most definitely subject to change
        liftWheelMotor.setDirection(DcMotor.Direction.FORWARD);
        weedWackerMotor.setDirection(DcMotor.Direction.REVERSE);
        beltMotor.setDirection(DcMotor.Direction.FORWARD);
        benchPressMotor.setDirection(DcMotor.Direction.REVERSE);

        // Set the modes for the motors

        // First setting up the drive motors
        // Starting by resetting the encoders
        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Likewise for those function motors
        // Reset encoders
        liftWheelMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        weedWackerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        beltMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        benchPressMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // These are TBD
        liftWheelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        weedWackerMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        beltMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        benchPressMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Set all motors to break when power = 0
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        // These ones might change later
        liftWheelMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        weedWackerMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        beltMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        benchPressMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status", "Running");
            telemetry.update();
            double drivePower = -this.gamepad1.left_stick_y;
            Drive(drivePower);
            telemetry.addData("Target Power", drivePower);
            telemetry.addData("Motor Power", GetMotorPower());
        }
    }

    public void Drive(double power) {
        // Power up the motors
        frontRightMotor.setPower(power);
        frontLeftMotor.setPower(power);
        backRightMotor.setPower(power);
        backLeftMotor.setPower(power);
    }

    public double GetMotorPower() {
        return (frontLeftMotor.getPower() + frontRightMotor.getPower() + backLeftMotor.getPower() + backRightMotor.getPower())/4;
    }
}
