package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

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

    // a.k.a. "launch motor"
    // 0 to 1 --> 0 degrees to 180 degrees (clockwise or counter-clockwise?)
    public Servo launchServo;

    // Create variables for handling input from first controller
    double leftStickY1;
    double leftStickX1;
    double rightStickX1;

    // Create variables for handling input from second controller
    double servoPosition;

    // Create variables to hold values for speed calculations
    double driveAngle;
    double driveSpeedA;
    double driveSpeedB;
    double driveSpeedScale = 1.0; // Used to slow down the robot's movement speed. Range from 0.0 (stop) to 1.0 (max speed)

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

        // set launch motor position to zero
        launchServo.setPosition(0.0);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status", "Running");
            telemetry.update();

            servoPosition = (-this.gamepad2.right_stick_x + 1) / 2;

            // Handle gamepad 1
            // Get data from controller sticks
            leftStickY1 = -this.gamepad1.left_stick_y;
            leftStickX1 = this.gamepad1.left_stick_x;
            rightStickX1 = -this.gamepad1.right_stick_x;
            // Calculate speeds and angles for drive motors
            driveAngle = (Math.atan2(leftStickY1,leftStickX1));
            driveSpeedA = Math.sqrt(Math.pow(leftStickX1,2) + Math.pow(leftStickY1,2)) * (Math.sin(driveAngle + Math.PI / 4));
            driveSpeedB = Math.sqrt(Math.pow(leftStickX1,2) + Math.pow(leftStickY1,2)) * (Math.sin(driveAngle - Math.PI / 4));
            // Set drive motor powers
            frontLeftMotor.setPower((driveSpeedB - rightStickX1) * driveSpeedScale);
            backLeftMotor.setPower((driveSpeedA - rightStickX1) * driveSpeedScale);
            backRightMotor.setPower((driveSpeedB + rightStickX1) * driveSpeedScale);
            frontRightMotor.setPower((driveSpeedA + rightStickX1) * driveSpeedScale);

            // Set servo power
            launchServo.setPosition(servoPosition);

            telemetry.addData("Drive power A", driveSpeedA);
            telemetry.addData("Drive power B", driveSpeedB);
            telemetry.addData("Drive angle", driveAngle);
            telemetry.addData("Servo movement", servoPosition);
        }
    }
}
