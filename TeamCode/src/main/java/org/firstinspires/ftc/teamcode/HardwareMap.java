package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

// based on code provided by Fire Robotics
public class HardwareMap {
    //Create Drive Motors
    public DcMotor frontRightMotor = null;
    public DcMotor frontLeftMotor = null;
    public DcMotor backRightMotor = null;
    public DcMotor backLeftMotor = null;

    //Create weed wacker motor
    public DcMotor weedWackerMotor = null;

    //Create lifting and wheel motor
    public DcMotor liftWheelMotor = null;

    //Create belt motor
    public DcMotor beltMotor = null;

    //Create bench press motor
    public DcMotor benchPressMotor = null;

    com.qualcomm.robotcore.hardware.HardwareMap hardwareMap = null;
    public ElapsedTime runtime = new ElapsedTime();

    public HardwareMap(com.qualcomm.robotcore.hardware.HardwareMap hwMap) {
        init(hwMap);
    }

    private void init(com.qualcomm.robotcore.hardware.HardwareMap hwMap) {
        hardwareMap = hwMap;

        // movement (wheel) motors connection
        frontRightMotor = hardwareMap.get(DcMotor.class, "frontRightMotor");
        frontLeftMotor = hardwareMap.get(DcMotor.class, "frontLeftMotor");
        backRightMotor = hardwareMap.get(DcMotor.class, "backRightMotor");
        backLeftMotor = hardwareMap.get(DcMotor.class, "backLeftMotor");

        // function motors connection
        liftWheelMotor = hardwareMap.get(DcMotor.class, "liftWheelMotor");
        weedWackerMotor = hardwareMap.get(DcMotor.class, "weedWackerMotor");
        beltMotor = hardwareMap.get(DcMotor.class, "beltMotor");
        benchPressMotor = hardwareMap.get(DcMotor.class, "benchPressMotor");

        // motor directions; subject to change?
        frontRightMotor.setDirection(DcMotor.Direction.FORWARD);
        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backRightMotor.setDirection(DcMotor.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        // most definitely subject to change
        liftWheelMotor.setDirection(DcMotor.Direction.FORWARD);
        weedWackerMotor.setDirection(DcMotor.Direction.REVERSE);
        beltMotor.setDirection(DcMotor.Direction.FORWARD);
        benchPressMotor.setDirection(DcMotor.Direction.REVERSE);

        // set motor mode
        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // likewise for those function motors
        liftWheelMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        weedWackerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        beltMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        benchPressMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        liftWheelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        weedWackerMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        beltMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        benchPressMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // set zero power behavior
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        // these ones below likely subject to change
        liftWheelMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        weedWackerMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        beltMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        benchPressMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // set motors to use no power upon init
        frontRightMotor.setPower(0.0);
        frontLeftMotor.setPower(0.0);
        backRightMotor.setPower(0.0);
        backLeftMotor.setPower(0.0);

        liftWheelMotor.setPower(0.0);
        weedWackerMotor.setPower(0.0);
        beltMotor.setPower(0.0);
        benchPressMotor.setPower(0.0);
    }
}

