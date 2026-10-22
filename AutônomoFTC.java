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

    @Override
    public void runOpMode() {
        // Mapeamento dos Hardwares
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        Shooter = hardwareMap.get(DcMotorEx.class, "Shooter");
        imu = hardwareMap.get(IMU.class, "imu");

        // Orientação do Control Hub
        RevHubOrientationOnRobot.LogoFacingDirection logo = RevHubOrientationOnRobot.LogoFacingDirection.UP;
        RevHubOrientationOnRobot.UsbFacingDirection usb = RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;
        RevHubOrientationOnRobot orientation = new RevHubOrientationOnRobot(logo, usb);

        imu.initialize(new IMU.Parameters(orientation));

        // Telemetria
        telemetry.addData("Status", "Pronto para iniciar");
        telemetry.update();

        // Normalização para que a potência não ultrapasse 1.0
        double maxPower = 1.0;

        waitForStart();
        Shooter.setPower(0.7);
        sleep (6031);
        frontRight.setPower(-0.6);
        backRight.setPower(0.6);
        frontLeft.setPower(-0.6);
        backLeft.setPower(0.6);
        Shooter.setPower(0);
        sleep(1000);
        frontRight.setPower(-0.6);
        backRight.setPower(0.6);
        frontLeft.setPower(-0.6);
        backLeft.setPower(0.6);
        sleep(1000);
        // Zera o giroscópio
        imu.resetYaw();

        while (opModeIsActive()) {
            double heading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

            telemetry.addData("Angulo do Giroscópio", "%.2f graus", heading);
            telemetry.update();
        }
    }
}