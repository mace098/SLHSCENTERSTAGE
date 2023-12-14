package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

// Based on code provided by Fire Robotics
public abstract class ChaosAutoHardwareMap extends LinearOpMode {
    static final int COUNTS_PER_MOTOR_REV_NEVEREST20    = 560;
    static final double     DRIVE_GEAR_REDUCTION    = 1 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4 ;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV_NEVEREST20 * DRIVE_GEAR_REDUCTION) * (WHEEL_DIAMETER_INCHES * Math.PI);

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

        // Set the modes for the motors

        // First setting up the drive motors
        SetupDriveMotors();

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
        launchServo.setPosition(0.5);
    }

    /*
       call this after every motor drive function...
       ... or else "distance" won't work correctly!
       ex. when turning, some motors will be set to position X, and others to position -X.
       if you don't reset the encoders, and set position to, say, X + Y
         then the position X motors will only go Y distance...
         ... and the position -X motors will go a longer 2X + Y distance.
         that's not correct. so reset the drive encoders after every drive function!
       perhaps we could just define a "master" drive function where we just put in
         the directions we want the motors to go in.
    */
    public void ResetDriveEncoders() {
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

    public void SetupDriveMotors() {
        // Set motor directions
        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        frontLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        backRightMotor.setDirection(DcMotor.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotor.Direction.FORWARD);

        ResetDriveEncoders();

        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void Drive(double power, int distance) {
        // Set the target position for motors
        frontRightMotor.setTargetPosition(distance * COUNTS_PER_MOTOR_REV_NEVEREST20);
        frontLeftMotor.setTargetPosition(distance * COUNTS_PER_MOTOR_REV_NEVEREST20);
        backRightMotor.setTargetPosition(distance * COUNTS_PER_MOTOR_REV_NEVEREST20);
        backLeftMotor.setTargetPosition(distance * COUNTS_PER_MOTOR_REV_NEVEREST20);

        // Power up the motors
        frontRightMotor.setPower(power);
        frontLeftMotor.setPower(power);
        backRightMotor.setPower(power);
        backLeftMotor.setPower(power);

        // Wait for the motors to finish moving
        while (IsDriving()) {
            telemetry.addData("Status", "Driving");
            telemetry.update();
        }
        // Stop the motors
        Brake();
        ResetDriveEncoders();
    }

    // position in inches
    // goes to the right:
    /*
    front
    ^---v
    |bot| -=> movement direction
    v---^
    back
     */
    public void Strafe(double power, int distance) {
        // Set the target position for motors
        frontRightMotor.setTargetPosition(-distance * COUNTS_PER_MOTOR_REV_NEVEREST20);
        frontLeftMotor.setTargetPosition(distance * COUNTS_PER_MOTOR_REV_NEVEREST20);
        backRightMotor.setTargetPosition(distance * COUNTS_PER_MOTOR_REV_NEVEREST20);
        backLeftMotor.setTargetPosition(-distance * COUNTS_PER_MOTOR_REV_NEVEREST20);


        // Power up the motors
        frontRightMotor.setPower(-power);
        frontLeftMotor.setPower(power);
        backRightMotor.setPower(power);
        backLeftMotor.setPower(-power);

        // Wait for the motors to finish moving
        while (IsDriving()) {
            telemetry.addData("Status", "Driving");
            telemetry.update();
        }
        // Stop the motors
        Brake();
        ResetDriveEncoders();
    }

    public void Turn(double power, int distance) {
        // Turn the robot clockwise

        // Set the target position for motors
        frontRightMotor.setTargetPosition(-distance * COUNTS_PER_MOTOR_REV_NEVEREST20);
        frontLeftMotor.setTargetPosition(distance * COUNTS_PER_MOTOR_REV_NEVEREST20);
        backRightMotor.setTargetPosition(-distance * COUNTS_PER_MOTOR_REV_NEVEREST20);
        backLeftMotor.setTargetPosition(distance * COUNTS_PER_MOTOR_REV_NEVEREST20);

        // Power up the motors
        frontRightMotor.setPower(-power);
        frontLeftMotor.setPower(power);
        backRightMotor.setPower(-power);
        backLeftMotor.setPower(power);

        // Wait for the motors to finish moving
        while (IsDriving()) {
            telemetry.addData("Status", "Driving");
            telemetry.update();
        }

        // Stop the motors
        Brake();
        ResetDriveEncoders();
    }

    public void Brake() {
        // Brake all drive motors
        frontRightMotor.setPower(0);
        frontLeftMotor.setPower(0);
        backRightMotor.setPower(0);
        backLeftMotor.setPower(0);
    }

    public boolean IsDriving() {
        return frontLeftMotor.isBusy() || frontRightMotor.isBusy() || backLeftMotor.isBusy() || backRightMotor.isBusy();
    }

    /*
    public void encoderDrive(double speed, double leftInches, double rightInches, double leftBackInches, double rightBackInches, double timeoutS) {
        telemetry.addData("CAUTION", "YOU'RE ILLEGAL, ENSURE THAT THE WHEEL HAS BEEN REMEASURED");
        boolean quit = true;
        if (quit)
        {
            return;
        }

        int newLeftTarget;
        int newRightTarget;
        int newLeftBackTarget;
        int newRightBackTarget;

        if (opModeIsActive()) {
            newLeftTarget = frontLeftMotor.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = frontRightMotor.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            newLeftBackTarget = backLeftMotor.getCurrentPosition() + (int)(leftBackInches * COUNTS_PER_INCH);
            newRightBackTarget = backRightMotor.getCurrentPosition() + (int)(rightBackInches * COUNTS_PER_INCH);
            frontLeftMotor.setTargetPosition(newLeftTarget);
            frontRightMotor.setTargetPosition(newRightTarget);
            backLeftMotor.setTargetPosition(newLeftBackTarget);
            backRightMotor.setTargetPosition(newRightBackTarget);

            frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            frontLeftMotor.setPower(Math.abs(speed));
            frontRightMotor.setPower(Math.abs(speed));
            backLeftMotor.setPower(Math.abs(speed));
            backRightMotor.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (frontLeftMotor.isBusy() && frontRightMotor.isBusy() && backLeftMotor.isBusy() && backRightMotor.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Going to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Currently at %7d :%7d",
                        frontLeftMotor.getCurrentPosition(),
                        frontRightMotor.getCurrentPosition(),
                        backLeftMotor.getCurrentPosition(),
                        backRightMotor.getCurrentPosition()
                );
                telemetry.update();
            }

            // Stop all motion;
            frontLeftMotor.setPower(0);
            frontRightMotor.setPower(0);
            backLeftMotor.setPower(0);
            backRightMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }
    */
}

