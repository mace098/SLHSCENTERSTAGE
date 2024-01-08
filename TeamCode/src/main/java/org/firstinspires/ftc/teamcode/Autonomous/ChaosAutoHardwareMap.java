package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.concurrent.TimeUnit;

// Based on code provided by Fire Robotics
public class ChaosAutoHardwareMap {
    static final int       COUNTS_PER_MOTOR_REV_NEVEREST20    = 560;
    static final double     DRIVE_GEAR_REDUCTION    = 1;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4;     // For figuring circumference
    static final int       COUNTS_PER_INCH = (int)
            ((COUNTS_PER_MOTOR_REV_NEVEREST20 * DRIVE_GEAR_REDUCTION)
                    / (WHEEL_DIAMETER_INCHES * Math.PI));
                    //* -1; // TODO: remove this "* -1", figure out why directions are backwards
                          //   didn't need this with boxy... but needed with KOS...?

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

    // power --> [0.0, 1.0]
    // distance --> inches
    // angle --> radians
    // TODO: stops randomly... some number almost being zero?
    // does not rotate/turn the robot!
    public void Omni(double power, int distance, double radians) {
        /*
            driveSpeedA = Math.sqrt(Math.pow(leftStickX1,2) + Math.pow(leftStickY1,2)) * (Math.sin(driveAngle + Math.PI / 4));
            driveSpeedB = Math.sqrt(Math.pow(leftStickX1,2) + Math.pow(leftStickY1,2)) * (Math.sin(driveAngle - Math.PI / 4));
            // Set drive motor powers
            frontLeftMotor.setPower((driveSpeedB - rightStickX1) * driveSpeedScale);
            backLeftMotor.setPower((driveSpeedA - rightStickX1) * driveSpeedScale);
            backRightMotor.setPower((driveSpeedB + rightStickX1) * driveSpeedScale);
            frontRightMotor.setPower((driveSpeedA + rightStickX1) * driveSpeedScale);
         */
        double spd_a = Math.sin(radians + Math.PI / 4);
        double spd_b = Math.sin(radians - Math.PI / 4);
        // adjust based on power parameter
        spd_a *= power;
        //if (spd_a < 0.0001) spd_a = 0.0; // arbitrary number for comparison
        spd_b *= power;
        //if (spd_b < 0.0001) spd_b = 0.0;
        int sgn_a = (int)Math.signum(spd_a);
        int sgn_b = (int)Math.signum(spd_b);
        // TODO:
        //  * prevent divide-by-zeroes
        //  * lower power = lower distance --> make sure distance is correct
        /*
        double ratio;
        if (Math.abs(spd_a) > Math.abs(spd_b)) {
            ratio = Math.abs(spd_b / spd_a);
            frontRightMotor.setTargetPosition(distance * (int)ratio * COUNTS_PER_INCH);
            frontLeftMotor.setTargetPosition(distance * COUNTS_PER_INCH);
            backRightMotor.setTargetPosition(distance * (int)ratio * COUNTS_PER_INCH);
            backLeftMotor.setTargetPosition(distance * COUNTS_PER_INCH);
        } else if (Math.abs(spd_a) < Math.abs(spd_b)) {
            ratio = Math.abs(spd_a / spd_b);
            frontRightMotor.setTargetPosition(distance * COUNTS_PER_INCH);
            frontLeftMotor.setTargetPosition(distance * (int)ratio * COUNTS_PER_INCH);
            backRightMotor.setTargetPosition(distance * COUNTS_PER_INCH);
            backLeftMotor.setTargetPosition(distance * (int)ratio * COUNTS_PER_INCH);
        } else if (Math.abs(spd_a) == Math.abs (spd_b)) {
            frontRightMotor.setTargetPosition(distance * COUNTS_PER_INCH);
            frontLeftMotor.setTargetPosition(distance * COUNTS_PER_INCH);
            backRightMotor.setTargetPosition(distance * COUNTS_PER_INCH);
            backLeftMotor.setTargetPosition(distance * COUNTS_PER_INCH);
        }
        */
        if (Math.abs(spd_a) > Math.abs(spd_b)) {
            frontRightMotor.setTargetPosition(distance * sgn_a * COUNTS_PER_INCH);
            frontLeftMotor.setTargetPosition(distance * sgn_b * COUNTS_PER_INCH);
            backRightMotor.setTargetPosition(distance * sgn_b * COUNTS_PER_INCH);
            backLeftMotor.setTargetPosition(distance * sgn_a * COUNTS_PER_INCH);
        } else if (Math.abs(spd_a) < Math.abs(spd_b)) {
            frontRightMotor.setTargetPosition(distance * sgn_a * COUNTS_PER_INCH);
            frontLeftMotor.setTargetPosition(distance * sgn_b * COUNTS_PER_INCH);
            backRightMotor.setTargetPosition(distance * sgn_b * COUNTS_PER_INCH);
            backLeftMotor.setTargetPosition(distance * sgn_a * COUNTS_PER_INCH);
        } else if (Math.abs(spd_a) == Math.abs (spd_b)) {
            frontRightMotor.setTargetPosition(distance * sgn_a * COUNTS_PER_INCH);
            frontLeftMotor.setTargetPosition(distance * sgn_b * COUNTS_PER_INCH);
            backRightMotor.setTargetPosition(distance * sgn_b * COUNTS_PER_INCH);
            backLeftMotor.setTargetPosition(distance * sgn_a * COUNTS_PER_INCH);
        }

        frontRightMotor.setPower(spd_a);
        frontLeftMotor.setPower(spd_b);
        backRightMotor.setPower(spd_b);
        backLeftMotor.setPower(spd_a);

        while (IsDriving()) {
            ;
        }

        Brake();
        ResetDriveEncoders();
    }

    // note: sign of "power" may not matter
    //  see: https://ftcforum.firstinspires.org/forum/ftc-technology/android-studio/6851-reverse-direction-for-encoder-mode-run-to-position/page2
    //  and: https://ftcforum.firstinspires.org/forum/ftc-technology/android-studio/6851-reverse-direction-for-encoder-mode-run-to-position
    public void Drive(double power, int distance) {
        do {
            // Set the target position for motors
            frontRightMotor.setTargetPosition(distance * COUNTS_PER_INCH);
            frontLeftMotor.setTargetPosition(distance * COUNTS_PER_INCH);
            backRightMotor.setTargetPosition(distance * COUNTS_PER_INCH);
            backLeftMotor.setTargetPosition(distance * COUNTS_PER_INCH);

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
            frontRightMotor.setTargetPosition(-distance * COUNTS_PER_INCH);
            frontLeftMotor.setTargetPosition(distance * COUNTS_PER_INCH);
            backRightMotor.setTargetPosition(distance * COUNTS_PER_INCH);
            backLeftMotor.setTargetPosition(-distance * COUNTS_PER_INCH);

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

    // TODO: change "distance" to "angle"
    //  ...where do i even begin? circumference?
    //  yeah, it seems to be dependent on circumference
    //
    // this seems impossible to do without knowledge of the robot's dimensions
    public void Turn(double power, int distance) {
        // Turn the robot clockwise

        do {
            // Set the target position for motors
            frontRightMotor.setTargetPosition(-distance * COUNTS_PER_INCH);
            frontLeftMotor.setTargetPosition(distance * COUNTS_PER_INCH);
            backRightMotor.setTargetPosition(-distance * COUNTS_PER_INCH);
            backLeftMotor.setTargetPosition(distance * COUNTS_PER_INCH);

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

    // in milliseconds
    public void Wait(long milliseconds) {
        runtime.reset();
        long initial_time = runtime.now(TimeUnit.MILLISECONDS);
        long from_now = initial_time + milliseconds;
        long current_time = 0; // reassigned local variable...?
        do {
            current_time = runtime.now(TimeUnit.MILLISECONDS);
        } while (current_time < from_now);
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

