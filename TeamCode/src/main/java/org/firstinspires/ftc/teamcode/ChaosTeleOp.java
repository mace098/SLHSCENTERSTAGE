package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
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
    public Servo launchServo;

    // Create claw servo
    public Servo clawServo;

    // Set up variables for handling the gamepads
    Gamepad currentGamepad1 = new Gamepad();
    Gamepad currentGamepad2 = new Gamepad();

    Gamepad previousGamepad1 = new Gamepad();
    Gamepad previousGamepad2 = new Gamepad();


    // Create variables for handling input from first controller
    double leftStickY1;
    double leftStickX1;
    double rightStickX1;

    // Create variables for the lifting mechanism and claw
    int lift_location;

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
        clawServo = hardwareMap.get(Servo.class, "clawServo");

        // Motor directions; subject to change
        frontRightMotor.setDirection(DcMotor.Direction.FORWARD);
        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backRightMotor.setDirection(DcMotor.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        // most definitely subject to change
        liftWheelMotor.setDirection(DcMotor.Direction.FORWARD);
        weedWackerMotor.setDirection(DcMotor.Direction.FORWARD);
        beltMotor.setDirection(DcMotor.Direction.FORWARD);
        benchPressMotor.setDirection(DcMotor.Direction.REVERSE);
        // servo direction
        launchServo.setDirection(Servo.Direction.FORWARD);
        clawServo.setDirection(Servo.Direction.FORWARD);

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
        liftWheelMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
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
        launchServo.setPosition(0.0);
        clawServo.setPosition(0.0);

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

            // Handle gamepad 1
            // Get data from controller sticks
            leftStickY1 = -currentGamepad1.left_stick_y;
            leftStickX1 = currentGamepad1.left_stick_x;
            rightStickX1 = -currentGamepad1.right_stick_x;
            // Calculate speeds and angles for drive motors
            driveAngle = (Math.atan2(leftStickY1,leftStickX1));
            driveSpeedA = Math.sqrt(Math.pow(leftStickX1,2) + Math.pow(leftStickY1,2)) * (Math.sin(driveAngle + Math.PI / 4));
            driveSpeedB = Math.sqrt(Math.pow(leftStickX1,2) + Math.pow(leftStickY1,2)) * (Math.sin(driveAngle - Math.PI / 4));
            // Set drive motor powers
            frontLeftMotor.setPower((driveSpeedB - rightStickX1) * driveSpeedScale);
            backLeftMotor.setPower((driveSpeedA - rightStickX1) * driveSpeedScale);
            backRightMotor.setPower((driveSpeedB + rightStickX1) * driveSpeedScale);
            frontRightMotor.setPower((driveSpeedA + rightStickX1) * driveSpeedScale);

            // Handle launch servo
            if (currentGamepad2.x && !previousGamepad2.x) {
                launchServo.setPosition(1);
            } else if (currentGamepad2.y && !previousGamepad2.y) {
                launchServo.setPosition(0);
            }

            // Handle claw servo
            if (currentGamepad2.a && !previousGamepad2.a) {
                clawServo.setPosition(0.5);
            } else if (!currentGamepad2.a && previousGamepad2.a) {
                clawServo.setPosition(0.0);
            }

            // Handle lifting motor
            if (currentGamepad2.dpad_left && !previousGamepad2.dpad_left && (lift_location == 0)) {
                liftWheelMotor.setTargetPosition(1000);  // These locations are TBD
                liftWheelMotor.setPower(0.2);
                lift_location = liftWheelMotor.getTargetPosition();
            } else if (currentGamepad2.dpad_up && !previousGamepad2.dpad_up && (lift_location == 0)) {
                liftWheelMotor.setTargetPosition(2500);  // These locations are TBD
                liftWheelMotor.setPower(0.2);
                lift_location = liftWheelMotor.getTargetPosition();
            } else if (currentGamepad2.dpad_right && !previousGamepad2.dpad_right && (lift_location == 0)) {
                liftWheelMotor.setTargetPosition(3000);  // These locations are TBD
                liftWheelMotor.setPower(0.2);
                lift_location = liftWheelMotor.getTargetPosition();
            } else if (currentGamepad2.dpad_down && !previousGamepad2.dpad_down && (lift_location != 0)) {
                liftWheelMotor.setTargetPosition(0);
                liftWheelMotor.setPower(0.2);
                lift_location = 0;
            }

            // Handle weed wacker
            // Check weather to stop or start the weed wacker
            if (currentGamepad1.right_bumper && !previousGamepad1.right_bumper) {
                weedWackerMotor.setPower(1.0);
            } else if (!currentGamepad1.right_bumper && previousGamepad1.right_bumper) {
                weedWackerMotor.setPower(0.0);
            }

            telemetry.addData("Lift location", lift_location);
            telemetry.addData("Servo movement", launchServo.getPosition());
            telemetry.addData("Weed wacker state", weedWackerMotor.getPower());
            telemetry.update();
        }
    }
}
