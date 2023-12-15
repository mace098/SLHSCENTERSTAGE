package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

// Based on code provided by Fire Robotics
public class ChaosAutoHardwareMap {
    static final int        COUNTS_PER_MOTOR_REV_NEVEREST20    = 560;
    static final double     DRIVE_GEAR_REDUCTION    = 1 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4 ;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV_NEVEREST20 * DRIVE_GEAR_REDUCTION) * (WHEEL_DIAMETER_INCHES * Math.PI);

    // Create Drive Motors
    public DcMotor frontRightMotor = null;
    public DcMotor frontLeftMotor = null;
    public DcMotor backRightMotor = null;
    public DcMotor backLeftMotor = null;
    boolean is_driving = false;
    public enum MoveType {
        STRAIGHT,
        STRAFE,
        TURN
    }

    // Create weed wacker motor
    public DcMotor weedWackerMotor = null;

    // a.k.a. "launch motor"
    // 0 to 1 --> 0 degrees to 180 degrees (clockwise or counter-clockwise?)
    public Servo launchServo = null;
    public Servo basketServo = null;

    com.qualcomm.robotcore.hardware.HardwareMap internal_hw_map = null;
    public ElapsedTime runtime = new ElapsedTime();

    public ChaosAutoHardwareMap(HardwareMap hardwareMap) {
        init(hardwareMap);
    }

    // Initialize devices
    public void init(HardwareMap hwMap) throws NullPointerException {
        if (hwMap == null) {
            // this message will look different
            // but... really?
            throw new NullPointerException();
        }
        internal_hw_map = hwMap;

        // using tryGet so that we can test things without having a complete robot
        //   e.g. with boxy
        // Drive motors connection
        frontRightMotor = hwMap.tryGet(DcMotor.class, "frontRightMotor");
        frontLeftMotor = hwMap.tryGet(DcMotor.class, "frontLeftMotor");
        backRightMotor = hwMap.tryGet(DcMotor.class, "backRightMotor");
        backLeftMotor = hwMap.tryGet(DcMotor.class, "backLeftMotor");

        // function motors connection
        weedWackerMotor = hwMap.tryGet(DcMotor.class, "weedWackerMotor");

        // servo connection
        launchServo = hwMap.tryGet(Servo.class, "launchServo");
        basketServo = hwMap.tryGet(Servo.class, "basketServo");

        // Motor directions; subject to change
        SetupDriveMotor(frontRightMotor, DcMotor.Direction.FORWARD);
        SetupDriveMotor(frontLeftMotor, DcMotor.Direction.REVERSE);
        SetupDriveMotor(backRightMotor, DcMotor.Direction.FORWARD);
        SetupDriveMotor(backLeftMotor, DcMotor.Direction.REVERSE);
        if (weedWackerMotor != null) {
            weedWackerMotor.setDirection(DcMotor.Direction.FORWARD);
            weedWackerMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            weedWackerMotor.setPower(0.0);
            weedWackerMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        if (launchServo != null) {
            launchServo.setDirection(Servo.Direction.FORWARD);
            launchServo.setPosition(0.5); // in the middle; halfway
        }
        if (basketServo != null) {
            basketServo.setDirection(Servo.Direction.FORWARD);
            basketServo.setPosition(0.5);
        }
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

    // resets encoder; sets target position to zero
    public void ResetDriveMotorEncoder(DcMotor motor) {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setTargetPosition(0);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void SetupDriveMotor(DcMotor motor, DcMotorSimple.Direction dir) {
        if (motor != null) {
            motor.setDirection(dir);
            ResetDriveMotorEncoder(motor);
            motor.setPower(0.0); // just in case
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
    }

    public void Drive(double power, int distance) {
        do {
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
        } while (IsDriving());

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
        do {
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
        } while (IsDriving());

        // Stop the motors
        Brake();
        ResetDriveEncoders();
    }

    public void Turn(double power, int distance) {
        // Turn the robot clockwise

        do {
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
        } while (IsDriving()); // wait to finish driving

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
        is_driving = (frontLeftMotor.isBusy()
                || frontRightMotor.isBusy()
                || backLeftMotor.isBusy()
                || backRightMotor.isBusy());
        return is_driving;
    }

    // distance is in inches
    public void typeDrive(MoveType type, double power, long distance) {

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

