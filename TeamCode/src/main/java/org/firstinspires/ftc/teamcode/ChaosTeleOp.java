package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
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
    public CRServo launchServo;

    // Create claw servo
    public Servo basketServo;

    // Set up variables for handling the gamepads
    Gamepad currentGamepad1 = new Gamepad();
    Gamepad currentGamepad2 = new Gamepad();

    Gamepad previousGamepad1 = new Gamepad();
    Gamepad previousGamepad2 = new Gamepad();


    // Create variables for handling input from first controller
    double leftStickY1;
    double leftStickX1;
    double rightStickX1;

    // Create constants for the lifting mechanism
    double liftSpeed = 0.75;

    // Create constants for the basket servo
    double closedPoint = 0.47;

    // Create constants for the launch servo
    double launchSpeed = 0.8;

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
        launchServo = hardwareMap.get(CRServo.class, "launchServo");
        basketServo = hardwareMap.get(Servo.class, "clawServo");

        // Motor directions; subject to change
        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        frontLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        backRightMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        // most definitely subject to change
        liftWheelMotor.setDirection(DcMotor.Direction.FORWARD);
        weedWackerMotor.setDirection(DcMotor.Direction.FORWARD);
        beltMotor.setDirection(DcMotor.Direction.FORWARD);
        benchPressMotor.setDirection(DcMotor.Direction.REVERSE);
        // servo direction
        launchServo.setDirection(CRServo.Direction.FORWARD);
        basketServo.setDirection(Servo.Direction.FORWARD);

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
        liftWheelMotor.setTargetPosition(0);
        liftWheelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        weedWackerMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
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
        launchServo.setPower(0.0);
        basketServo.setPosition(closedPoint);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status", "Running");
            telemetry.update();

            // Update previous gamepad values
            previousGamepad1.copy(currentGamepad1);
            previousGamepad2.copy(currentGamepad2);
            // Update current gamepad values
            currentGamepad1.copy(gamepad1);
            currentGamepad2.copy(gamepad2);

            // Handle drive system
            // Get data from controller sticks
            leftStickY1 = -currentGamepad1.left_stick_y;
            leftStickX1 = -currentGamepad1.left_stick_x;
            rightStickX1 = -currentGamepad1.right_stick_x;
            // Calculate speeds and angles for drive motors
            driveAngle = (Math.atan2(leftStickY1,leftStickX1));
            driveSpeedA = Math.sqrt(Math.pow(leftStickX1,2) + Math.pow(leftStickY1,2)) * (Math.sin(driveAngle + Math.PI / 4));
            driveSpeedB = Math.sqrt(Math.pow(leftStickX1,2) + Math.pow(leftStickY1,2)) * (Math.sin(driveAngle - Math.PI / 4));
            // Set the drive speed based off of the right trigger
            driveSpeedScale = -1.0*(currentGamepad1.right_trigger-1.0);
            // Set drive motor powers
            frontLeftMotor.setPower((driveSpeedB - rightStickX1) * driveSpeedScale);
            backLeftMotor.setPower((driveSpeedA - rightStickX1) * driveSpeedScale);
            backRightMotor.setPower((driveSpeedB + rightStickX1) * driveSpeedScale);
            frontRightMotor.setPower((driveSpeedA + rightStickX1) * driveSpeedScale);

            // Handle launch servo
            if (currentGamepad2.a) {
                launchServo.setPower(-launchSpeed);
            } else if (currentGamepad2.b) {
                launchServo.setPower(launchSpeed);
            } else if (!currentGamepad2.a && !currentGamepad2.b) {
                launchServo.setPower(0);
            }

            // Handle basket servo
            if (currentGamepad2.x) {
                basketServo.setPosition(closedPoint);
            } else if (currentGamepad2.y) {
                basketServo.setPosition(1.0);
            }

            // Handle lifting motor
            double liftPower = 0;

            if ((currentGamepad2.dpad_up) && (liftWheelMotor.getCurrentPosition() > -4200)) {           // Move lift slider up as long as it is less than it's maximum range
                liftPower = -liftSpeed;
            } else if ((currentGamepad2.dpad_left)  && (liftWheelMotor.getCurrentPosition() < -1300)) { // Move lift slider down unless the hook is too far down
                liftPower = liftSpeed;
            } else if ((currentGamepad2.dpad_down)  && (liftWheelMotor.getCurrentPosition() < -1)) {    // Move lift slider down unless it is going below its minimum
                liftPower = liftSpeed;
            }

            if (!currentGamepad2.dpad_up && !currentGamepad2.dpad_down && !currentGamepad2.dpad_left) {
                liftWheelMotor.setPower(0);
            } else {
                liftWheelMotor.setPower(liftPower);
            }

            // Handle weed wacker
            // Check weather to stop or start the weed wacker
            if (currentGamepad2.right_bumper && !previousGamepad2.right_bumper) {
                weedWackerMotor.setPower(-1.0);
            } else if (!currentGamepad2.right_bumper && previousGamepad2.right_bumper) {
                weedWackerMotor.setPower(0.0);
            } else if (currentGamepad2.left_bumper && !previousGamepad2.left_bumper) {
                weedWackerMotor.setPower(1.0);
            } else if (!currentGamepad2.left_bumper && previousGamepad2.left_bumper) {
                weedWackerMotor.setPower(0.0);
            }

            telemetry.addData("Lift location", liftWheelMotor.getCurrentPosition());
            telemetry.addData("Basket servo position", basketServo.getPosition());
            telemetry.addData("Weed wacker state", weedWackerMotor.getPower());
            telemetry.update();
        }
    }
}
