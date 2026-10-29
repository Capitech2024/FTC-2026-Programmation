package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Autonomous", group = "REV")
public class Autonomous extends LinearOpMode {

    private IMU imu;
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotorEx Shooter;
    private DcMotorEx Intake;

    @Override
    public void runOpMode() {
        // Mapeamento dos Hardwares
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        Shooter = hardwareMap.get(DcMotorEx.class, "Shooter");
        Intake = hardwareMap.get(DcMotorEx.class, "Intake");
        imu = hardwareMap.get(IMU.class, "imu");

        // Orientação do Control Hub
        RevHubOrientationOnRobot.LogoFacingDirection logo = RevHubOrientationOnRobot.LogoFacingDirection.UP;
        RevHubOrientationOnRobot.UsbFacingDirection usb = RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;
        RevHubOrientationOnRobot orientation = new RevHubOrientationOnRobot(logo, usb);

        imu.initialize(new IMU.Parameters(orientation));

        // Telemetria
        telemetry.addData("Status", "Pronto para iniciar");
        telemetry.update();

        // -- INIT --
        waitForStart();
        frontRight.setPower(0.5);
        backRight.setPower(0.5);
        frontLeft.setPower(0.5);
        backLeft.setPower (0.5);
        sleep(1502)
        frontRight.setPower(-0.5);
        backRight.setPower(-0.5);
        frontLeft.setPower(-0.5);
        backLeft.setPower(-0.5);
        sleep(500);
        frontRight.setPower(0);
        backRight.setPower(0;
        frontLeft.setPower(0);
        backLeft.setPower(0;

        // Zera o giroscópio
        imu.resetYaw();

        while (opModeIsActive()) {
            double heading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

            telemetry.addData("Angulo do Giroscópio", "%.2f graus", heading);

            if (Shooter != null) {
                telemetry.addData("Potencia Shooter", Shooter.getPower());
                telemetry.update();
            }
        }
    }
}
