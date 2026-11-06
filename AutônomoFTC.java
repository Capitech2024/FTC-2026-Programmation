package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;


@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Autonomous", group = "REV")
public class autonomous extends LinearOpMode {

    private IMU imu;
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotorEx Shooter;
    private DcMotorEx Shooter2;
    private DcMotorEx Intake;
    private DcMotorEx Indexer;

    @Override
    public void runOpMode() {

        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        Shooter2 = hardwareMap.get(DcMotorEx.class, "Shooter2");
        Shooter = hardwareMap.get(DcMotorEx.class, "Shooter");
        Intake = hardwareMap.get(DcMotorEx.class, "Intake");
        Indexer = hardwareMap.get(DcMotorEx.class, "Indexer");
        imu = hardwareMap.get(IMU.class, "imu");


        RevHubOrientationOnRobot.LogoFacingDirection logo = RevHubOrientationOnRobot.LogoFacingDirection.UP;
        RevHubOrientationOnRobot.UsbFacingDirection usb = RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;
        RevHubOrientationOnRobot orientation = new RevHubOrientationOnRobot(logo, usb);


        double maxPower = 1.0;

        imu.initialize(new IMU.Parameters(orientation));


        telemetry.addData("Status", "Pronto para iniciar");
        telemetry.update();

        // -- INIT --
        waitForStart();

        if (opModeIsActive()) {

            frontRight.setPower(0.41);
            backRight.setPower(-0.41);
            frontLeft.setPower(0.41);
            backLeft.setPower(0.41);

            sleep(380);
            frontRight.setPower(0);
            backRight.setPower(0);
            frontLeft.setPower(0);
            backLeft.setPower(0);
            Shooter.setPower(-0.66);
            Shooter2.setPower(-0.64);
            Indexer.setPower(1.4);



        }


        imu.resetYaw();

        while (opModeIsActive()); {
            double heading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

            telemetry.addData("Angulo do Giroscópio", "%.2f graus", heading);

            if (Shooter != null) {
                telemetry.addData("Potencia Shooter", Shooter.getPower());
                telemetry.update();
            }
        }
    }
}