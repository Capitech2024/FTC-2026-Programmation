package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "Clauvalcante", group = "REV")
public class Clauvalcante extends OpMode {

    // 1. HARDWARE DECLARATIONS
    private DcMotor frontRight, backRight, frontLeft, backLeft;
    private DcMotorEx Intake, Shooter, Indexer;
    private DcMotor Shooter2; // NEW: second shooter motor

    // 2. CONSTANTS
    private static final double SHOOTER_TPR = 28;
    private static final double TARGET_SHOOTER_RPM = -3000.0;
    private static final double RPM_TOLERANCE = 50.0;
    private static final double INDEXER_POWER = 0.8;

    // 3. STATE VARIABLES
    private int shooterState = 0; // 0=OFF, 1=Speed A, 2=Speed B
    private boolean wasRightTriggerPressed = false;
    private boolean wasAButtonPressed = false;
    private boolean wasBButtonPressed = false;

    // --- INIT ---
    @Override
    public void init() {
        Intake = hardwareMap.get(DcMotorEx.class, "Intake");
        Shooter = hardwareMap.get(DcMotorEx.class, "Shooter");
        Indexer = hardwareMap.get(DcMotorEx.class, "Indexer");
        Shooter2 = hardwareMap.get(DcMotor.class, "Shooter2");

        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        Shooter2.setDirection(DcMotor.Direction.REVERSE);

        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Shooter2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        telemetry.addData("Status", "Initialization Complete");
        telemetry.update();
    }

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

        // C. SHOOTER STATE TRANSITION LOGIC
        // (X was removed from here because it now controls the Indexer)
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

        // D. SHOOTER + SHOOTER2 POWER (run together)
        double shooterPower;
        if (isRightBumperPressed) {
            shooterPower = 0.55;   // Reverse (spit out)
        }
        else if (shooterState == 1) {
            shooterPower = -0.5;   // SPEED_A
        }
        else if (shooterState == 2) {
            shooterPower = 0.55;   // SPEED_B
        }
        else {
            shooterPower = 0.0;    // OFF
        }
Shooter.setPower(shooterPower);

// Shooter2 always runs at -0.8 whenever the shooter is active, off when it is off
if (shooterPower != 0.0) {
    Shooter2.setPower(0.6);
} else {
    Shooter2.setPower(0.0);
}

        // E. INTAKE MOTOR CONTROL
        if (gamepad1.left_trigger > 0.1) {
            Intake.setPower(0.8);
        }
        else if (gamepad1.left_bumper) {
            Intake.setPower(-0.5);
        }
        else {
            Intake.setPower(0.0);
        }

        // F. INDEXER: runs while X is held
        if (isXButtonPressed) {
            Indexer.setPower(INDEXER_POWER);
        }
        else {
            Indexer.setPower(0.0);
        }

        // G. UPDATE STATE VARIABLES
        wasBButtonPressed = isBButtonPressed;
        wasRightTriggerPressed = isRightTriggerPressed;
        wasAButtonPressed = isAButtonPressed;

        // H. ENCODER READINGS & CALCULATIONS
        double shooterVelocity_TPS = Shooter.getVelocity();
        double shooterRPM = (shooterVelocity_TPS / SHOOTER_TPR) * 60.0;
        double shooterRPS = shooterVelocity_TPS / SHOOTER_TPR;
        double shooterTicks = Shooter.getCurrentPosition();

        boolean isShooterReady = (shooterRPM >= (TARGET_SHOOTER_RPM - RPM_TOLERANCE))
                                 && (shooterState != 0);

        String readyStatus = isShooterReady ?
                             "**READY (" + (int)TARGET_SHOOTER_RPM + " RPM)**" :
                             "Warming Up (" + (int)TARGET_SHOOTER_RPM + " RPM)";

        // I. TELEMETRY DISPLAY
        telemetry.addData("--- SHOOTER STATUS ---", "");
        telemetry.addData("Shooter Status", readyStatus);
        telemetry.addData("Target RPM", TARGET_SHOOTER_RPM);
        telemetry.addData("Current RPM", "%.0f", shooterRPM);
        telemetry.addData("Current RPS", "%.2f", shooterRPS);
        telemetry.addData("Current Ticks", shooterTicks);

        telemetry.addData("--- SUBSYSTEMS ---", "");
        telemetry.addData("Shooter State (0/1/2)", shooterState);
        telemetry.addData("Shooter2 power", Shooter2.getPower());
        telemetry.addData("Indexer power", Indexer.getPower());

        telemetry.update();
    }

    public void drive(double forward, double strafe, double rotate) {
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
