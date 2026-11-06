package org.firstinspires.ftc.teamcode;

// FIX 1: Import all necessary hardware classes
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx; // Import DcMotorEx
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "teleOPTESTE", group = "REV")
public class teleOPTESTE extends OpMode {

    // 1. HARDWARE DECLARATIONS
    private DcMotor frontRight, backRight, frontLeft, backLeft;
    private DcMotorEx Intake, Shooter, Indexer; // CORRECT: Use DcMotorEx for velocity features
    private HuskyLens huskyLens;






    // 2. CONSTANTS (Ticks Per Revolution - TPR)
    private static final double SHOOTER_TPR = 28; // 20:1 UltraPlanetary
    private static final double TARGET_SHOOTER_RPM = -3000.0;
    private static final double RPM_TOLERANCE = 50.0; // +/- 50 RPM tolerance

    // 3. STATE VARIABLES
    private int shooterState = 0; // 0=OFF, 1=Speed A (0.8), 2=Speed B (0.7)
    private boolean wasRightTriggerPressed = false;
    private boolean wasAButtonPressed = false;
    private boolean wasBButtonPressed = false;
    private boolean wasXButtonPressed = false;
    private boolean wasYButtonPressed = false;

    // --- INIT ---
    @Override
    public void init() {







        // Map motors using DcMotorEx for velocity control on subsystems
        Intake = hardwareMap.get(DcMotorEx.class, "Intake");
        Shooter = hardwareMap.get(DcMotorEx.class, "Shooter");
        Indexer = hardwareMap.get(DcMotorEx.class, "Indexer");
        // Map drive motors using standard DcMotor (or DcMotorEx, if you prefer)
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");

        // Set directions
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);


        // Set motors to run with encoders (essential for velocity control)
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        // ... set all drive motors to RUN_USING_ENCODER ...
        Shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Set zero power behavior
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);





        // ... set all drive motors to BRAKE ...


        telemetry.addData("Status", "Initialization Complete");
        telemetry.update();
    }

    // --- START ---
    @Override
    public void start() {
    }

    // --- LOOP ---
    @Override
    public void loop() {
        // A. DRIVE TRAIN CONTROL
        final double DEADBAND_THRESHOLD = 0.1;
        double robotForward = gamepad1.left_stick_y;
        double robotStrafe = -gamepad1.left_stick_x;
        double robotRotate = -gamepad1.right_stick_x;

        if (Math.abs(robotForward) < DEADBAND_THRESHOLD) { robotForward = 0.0; }
        if (Math.abs(robotStrafe) < DEADBAND_THRESHOLD) { robotStrafe = 0.0; }
        if (Math.abs(robotRotate) < DEADBAND_THRESHOLD) { robotRotate = 0.0; }

        drive(robotForward, robotStrafe, robotRotate);

        // B. INPUTS & EDGE DETECTION


        boolean isRightTriggerPressed = gamepad1.right_trigger > 0.5;
        boolean isAButtonPressed = gamepad1.a;
        boolean isRightBumperPressed = gamepad1.right_bumper;
        boolean isBButtonPressed = gamepad1.b;
        boolean isXButtonPressed = gamepad1.x;
        boolean isYButtonPressed = gamepad1.y;


        // C. SHOOTER STATE TRANSITION LOGIC
        if (isAButtonPressed && !wasAButtonPressed) {
            shooterState = 0; // OFF
        }
        else if (isRightTriggerPressed && !wasRightTriggerPressed) {
            if (shooterState == 0) {
                shooterState = 1; // SPEED_A
            }
        }
        else if (isBButtonPressed && !wasBButtonPressed) {
            if (shooterState == 0) {
                shooterState = 2; // SPEED_B
            }
        }
        else if (isXButtonPressed && !wasXButtonPressed) {
            if (shooterState == 2) {
                shooterState = 0; // SPEED_A
            }
        }

        // D. SHOOTER MOTOR POWER APPLICATION (Unified Control)
        if (isRightBumperPressed) {
            Shooter.setPower(0.75); // Override: Reverse (Spit out)
        }
        else if (shooterState == 1) {
            Shooter.setPower(-0.65); // SPEED_A
        }
        else if (shooterState == 2) {
            Shooter.setPower(-0.5); // SPEED_B
        }
        else {
            Shooter.setPower(0.0); // OFF
        }


        // E. INTAKE MOTOR CONTROL (Simple)
        if (gamepad1.left_trigger > 0.1) {
            Intake.setPower(0.8);
        }
        else if (gamepad1.left_bumper) {
            Intake.setPower(-0.8);
        }
        else {
            Intake.setPower(0.0);
        }

        if (isYButtonPressed && !wasYButtonPressed) {
            Indexer.setPower(0.8);
        }
        else {
            Indexer.setPower(0);
        }

        // F. UPDATE STATE VARIABLES
        wasBButtonPressed = isBButtonPressed;
        wasXButtonPressed = isXButtonPressed;
        wasRightTriggerPressed = isRightTriggerPressed;
        wasAButtonPressed = isAButtonPressed;
        wasYButtonPressed = isYButtonPressed;

        // G. ENCODER READINGS & CALCULATIONS
        // The getVelocity() method of DcMotorEx gives Ticks/Second
        double shooterVelocity_TPS = Shooter.getVelocity();

        // Calculate RPM: (Ticks/Sec / Ticks/Rev) * 60 Sec/Min
        double shooterRPM = (shooterVelocity_TPS / SHOOTER_TPR) * 60.0;

        // Calculate RPS: (Ticks/Sec / Ticks/Rev)
        double shooterRPS = shooterVelocity_TPS / SHOOTER_TPR; // RPS

        double shooterTicks = Shooter.getCurrentPosition(); // Current Position (Ticks)


        boolean isShooterReady = (shooterRPM >= (TARGET_SHOOTER_RPM - RPM_TOLERANCE))
                && (shooterState != 0); // Must be spinning AND intended to be ON

        // The telemetry changes color by using a formatting tag, usually interpreted by the SDK.
        // ** (Bold) or * (Italic) often triggers color changes based on values.
        String readyStatus = isShooterReady ?
                "**READY (" + (int)TARGET_SHOOTER_RPM + " RPM)**" :
                "Warming Up (" + (int)TARGET_SHOOTER_RPM + " RPM)";


        // I. TELEMETRY DISPLAY

        telemetry.addData("--- SHOOTER STATUS ---", "");
        // Missing Element: Color-Changing Telemetry
        telemetry.addData("Shooter Status", readyStatus);
        telemetry.addData("Target RPM", TARGET_SHOOTER_RPM);
        telemetry.addData("Current RPM", "%.0f", shooterRPM);
        telemetry.addData("Current RPS", "%.2f", shooterRPS);
        telemetry.addData("Current Ticks", shooterTicks);

        telemetry.addData("--- SUBSYSTEMS ---", "");
        telemetry.addData("Shooter State (0/1/2)", shooterState);


        telemetry.update();
    }

    /**
     * Drives the robot with the given forward, strafe, and rotate values.
     */
    public void drive(double forward, double strafe, double rotate) {
        // Mecanum drive kinematics and power setting (code remains the same)
        double frontLeftPower = forward + strafe + rotate;
        double frontRightPower = forward - strafe - rotate;
        double backLeftPower = forward - strafe + rotate;
        double backRightPower = forward + strafe - rotate;

        double max = Math.abs(frontLeftPower);
        max = Math.max(max, Math.abs(frontRightPower));
        max = Math.max(max, Math.abs(backLeftPower));
        max = Math.max(max, Math.abs(backRightPower));

        if (max > 1.0) {
            frontLeftPower /= max;
            frontRightPower /= max;
            backLeftPower /= max;
            backRightPower /= max;
        }

        frontLeft.setPower(frontLeftPower);
        frontRight.setPower(frontRightPower);
        backLeft.setPower(backLeftPower);
        backRight.setPower(backRightPower);
    }
}