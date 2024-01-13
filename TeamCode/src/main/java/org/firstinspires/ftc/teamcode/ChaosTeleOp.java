package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Autonomous.ChaosAutoHardwareMap;

@TeleOp(name="Chaos Tele Op")
public class ChaosTeleOp extends LinearOpMode {
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
        ChaosAutoHardwareMap hw = new ChaosAutoHardwareMap(hardwareMap);

        // set run modes of movement motors
        hw.frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hw.frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hw.backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hw.backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        hw.frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hw.frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hw.backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hw.backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        hw.weedWackerMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // set launch motor position to zero
        hw.launchServo.setPower(0.0);
        hw.basketServo.setPosition(closedPoint);

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
            double magnitude = Math.sqrt((leftStickX1 * leftStickX1) + (leftStickY1 * leftStickY1));
            double sawtooth = (Math.PI / 2.0) * // look at this mess...
                    (((2.0 * Math.PI) * (driveAngle - Math.PI / 4.0))
                            - Math.floor(((2.0 * Math.PI) * (driveAngle - Math.PI / 4.0)) + 0.5));
            double sec = 1.0 / Math.cos(sawtooth); // sec(x) = 1 / cos(x)
            double wheelA = Math.sin(driveAngle + Math.PI / 4);
            double wheelB = Math.sin(driveAngle - Math.PI / 4);
            driveSpeedA = magnitude * sec * wheelA;
            driveSpeedB = magnitude * sec * wheelB;
            // use driveSpeedA = magnitude * wheelA for old behavior
            driveSpeedScale = -1.0 * (currentGamepad1.right_trigger - 1.0);
            // Set drive motor powers
            hw.frontLeftMotor.setPower((driveSpeedB - rightStickX1) * driveSpeedScale);
            hw.backLeftMotor.setPower((driveSpeedA - rightStickX1) * driveSpeedScale);
            hw.backRightMotor.setPower((driveSpeedB + rightStickX1) * driveSpeedScale);
            hw.frontRightMotor.setPower((driveSpeedA + rightStickX1) * driveSpeedScale);

            // Handle launch servo
            if (currentGamepad2.a) {
                hw.launchServo.setPower(-launchSpeed);
            } else if (currentGamepad2.b) {
                hw.launchServo.setPower(launchSpeed);
            } else if (!currentGamepad2.a && !currentGamepad2.b) {
                hw.launchServo.setPower(0);
            }

            // Handle basket servo
            if (currentGamepad2.x) {
                hw.basketServo.setPosition(closedPoint);
            } else if (currentGamepad2.y) {
                hw.basketServo.setPosition(1.0);
            }

            // Handle lifting motor
            double liftPower = 0;

            if ((currentGamepad2.dpad_up) && (hw.liftWheelMotor.getCurrentPosition() > -4200)) {           // Move lift slider up as long as it is less than it's maximum range
                liftPower = -liftSpeed;
            } else if ((currentGamepad2.dpad_left)  && (hw.liftWheelMotor.getCurrentPosition() < -1300)) { // Move lift slider down unless the hook is too far down
                liftPower = liftSpeed;
            } else if ((currentGamepad2.dpad_down)  && (hw.liftWheelMotor.getCurrentPosition() < -1)) {    // Move lift slider down unless it is going below its minimum
                liftPower = liftSpeed;
            }

            if (!currentGamepad2.dpad_up && !currentGamepad2.dpad_down && !currentGamepad2.dpad_left) {
                hw.liftWheelMotor.setPower(0);
            } else {
                hw.liftWheelMotor.setPower(liftPower);
            }

            // Handle weed wacker
            // Check weather to stop or start the weed wacker
            if (currentGamepad2.right_bumper && !previousGamepad2.right_bumper) {
                hw.weedWackerMotor.setPower(-1.0);
            } else if (!currentGamepad2.right_bumper && previousGamepad2.right_bumper) {
                hw.weedWackerMotor.setPower(0.0);
            } else if (currentGamepad2.left_bumper && !previousGamepad2.left_bumper) {
                hw.weedWackerMotor.setPower(1.0);
            } else if (!currentGamepad2.left_bumper && previousGamepad2.left_bumper) {
                hw.weedWackerMotor.setPower(0.0);
            }

            telemetry.addData("Lift Location", hw.liftWheelMotor.getCurrentPosition());
            telemetry.addData("Basket Servo Position", hw.basketServo.getPosition());
            telemetry.addData("Weed Wacker State", hw.weedWackerMotor.getPower());
            telemetry.update();
        }
    }
}
