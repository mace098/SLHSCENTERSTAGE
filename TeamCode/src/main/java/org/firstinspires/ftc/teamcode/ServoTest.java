package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.concurrent.TimeUnit;

@TeleOp(name="servotest")
public class ServoTest extends LinearOpMode {

    // Create claw servo
    public Servo basketServo;

    // Set up variables for handling the gamepads
    Gamepad currentGamepad1 = new Gamepad();
    Gamepad currentGamepad2 = new Gamepad();

    Gamepad previousGamepad1 = new Gamepad();
    Gamepad previousGamepad2 = new Gamepad();

    public ElapsedTime time = new ElapsedTime();

    @Override
    public void runOpMode() {
        // servo connection
        basketServo = hardwareMap.get(Servo.class, "basketServo");

        // servo direction
        basketServo.setDirection(Servo.Direction.FORWARD);

        // set launch motor position to zero
//        launchServo.setPower(0.0);
//        clawServo.setPosition(0.0);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        final double max_position_step_size = 0.001;
        double target_position = 0;
        //final double time_step_scale_factor = 1.0;
        long last_time = time.now(TimeUnit.MILLISECONDS);

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

            // this must be time based. there's no telling how many times
            // this code runs per second
            long current_time = time.now(TimeUnit.MILLISECONDS);
            double step = (double)(current_time - last_time) * max_position_step_size;

            // Handle claw servo
            if (currentGamepad2.right_stick_x != previousGamepad2.right_stick_x) {
                target_position = (currentGamepad2.right_stick_x + 1.0) / 2.0;
            }

            // this first "if" part must go first, or else we may input an out-of-bounds value
            //   for setPosition (e.g. getPosition() = 0.995, step = 0.006, sum = 1.001)
            if (Math.abs(basketServo.getPosition() - target_position + step) < max_position_step_size
                    ||
                    Math.abs(basketServo.getPosition() - target_position - step) < max_position_step_size) {
                // simply set position to target_position when close enough
                basketServo.setPosition(target_position);
            } else {
                // add or subtract depending on what gets us closer to target_position
                if (basketServo.getPosition() > target_position) {
                    basketServo.setPosition(basketServo.getPosition() - step);
                } else if (basketServo.getPosition() < target_position) {
                    basketServo.setPosition(basketServo.getPosition() + step);
                }
            }

            // these two below don't work (they were for testing purposes)
            if (currentGamepad2.a == true) {
                basketServo.setPosition(0.0);
                telemetry.addData("a", "true");
            }
            if (currentGamepad2.b == true) {
                basketServo.setPosition(1.0);
                telemetry.addData("b", "true");
            }

            last_time = time.now(TimeUnit.MILLISECONDS);

            telemetry.addData("Basket Servo", basketServo.getPosition());
            telemetry.update();
        }
    }
}
