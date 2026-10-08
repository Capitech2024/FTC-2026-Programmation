package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Autonomous", group = "REV")
public class Autonomous extends LinearOpMode {

    private IMU imu;
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor frontLeft;
    private DcMotor backLeft;

    @Override
    public void runOpMode() {
        // Mapeamento dos Hardwares
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
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

        frontRight.setPower(0.8);
        backRight.setPower(0.8);
        frontLeft.setPower(0.8);
        backLeft.setPower(0.8);

        // Aguarda a inicialização 
        waitForStart();

        // Zera o giroscópio pra começar do 0°
        imu.resetYaw();

        while (opModeIsActive()) {

            double heading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

            telemetry.addData("Angulo do Giroscópio", "%.2f graus", heading);
            telemetry.update();
        }
    }
}
