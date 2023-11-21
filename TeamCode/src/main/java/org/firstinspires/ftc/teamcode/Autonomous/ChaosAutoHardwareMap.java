package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

// Based on code provided by Fire Robotics
public abstract class ChaosAutoHardwareMap extends LinearOpMode {
    // Create Drive Motors
    public DcMotor frontRightMotor = null;
    public DcMotor frontLeftMotor = null;
    public DcMotor backRightMotor = null;
    public DcMotor backLeftMotor = null;

    // Create weed wacker motor
    public DcMotor weedWackerMotor = null;
    // Create lifting and wheel motor
    public DcMotor liftWheelMotor = null;
    // Create belt motor
    public DcMotor beltMotor = null;
    // Create bench press motor
    public DcMotor benchPressMotor = null;

    // a.k.a. "launch motor"
    // 0 to 1 --> 0 degrees to 180 degrees (clockwise or counter-clockwise?)
    public Servo launchServo = null;

//    com.qualcomm.robotcore.hardware.HardwareMap HwMap = null;
    public ElapsedTime runtime = new ElapsedTime();

//    public ChaosAutoHardwareMap(com.qualcomm.robotcore.hardware.HardwareMap hwMap) {
//        init(hwMap);
//    }

    // Initialize devices
    public void init(HardwareMap hardwareMap) {
//        hardwareMap = hardwareMap;

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

        // servo connection
        launchServo = hardwareMap.get(Servo.class, "launchServo");

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
        // servo direction
        launchServo.setDirection(Servo.Direction.FORWARD);

        // Set the modes for the motors

        // First setting up the drive motors
        // Starting by resetting the encoders
        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // And then set them to run to position mode
        frontRightMotor.setTargetPosition(0);
        frontLeftMotor.setTargetPosition(0);
        backRightMotor.setTargetPosition(0);
        backLeftMotor.setTargetPosition(0);

        frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

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

        // Set all motors to brake upon init
        frontRightMotor.setPower(0.0);
        frontLeftMotor.setPower(0.0);
        backRightMotor.setPower(0.0);
        backLeftMotor.setPower(0.0);

        liftWheelMotor.setPower(0.0);
        weedWackerMotor.setPower(0.0);
        beltMotor.setPower(0.0);
        benchPressMotor.setPower(0.0);

        // set launch motor position to zero
        launchServo.setPosition(0.0);
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

    public void Brake() {
        // Brake all drive motors
        frontRightMotor.setPower(0);
        frontLeftMotor.setPower(0);
        backRightMotor.setPower(0);
        backLeftMotor.setPower(0);
    }
}

