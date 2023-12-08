package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="servotest")
public class ServoTest extends LinearOpMode {

    // Create claw servo
    public Servo basketServo;

    // Set up variables for handling the gamepads
    Gamepad currentGamepad1 = new Gamepad();
    Gamepad currentGamepad2 = new Gamepad();

    Gamepad previousGamepad1 = new Gamepad();
    Gamepad previousGamepad2 = new Gamepad();

    @Override
    public void runOpMode() {
        // servo connection
        basketServo = hardwareMap.get(Servo.class, "clawServo");

        // servo direction
        basketServo.setDirection(Servo.Direction.FORWARD);

        // set launch motor position to zero
//        launchServo.setPower(0.0);
//        clawServo.setPosition(0.0);

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

            // Handle claw servo
            if (currentGamepad2.right_stick_x != previousGamepad2.right_stick_x) {
                basketServo.setPosition((currentGamepad2.right_stick_x +1) /2);
            }

            telemetry.addData("claw servo", basketServo.getPosition());
            telemetry.update();
        }
    }
}
