package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "TeleOP", group = "REV")
public class TeleOP extends OpMode {

    private DcMotorEx Motor0;
    private DcMotorEx Motor1;
    private DcMotorEx Motor2;
    private DcMotorEx Motor3;
    private DcMotorEx MotorShooter0;
    private DcMotorEx Motor4;

    private 

    @Override
    public void init() {
        try {
            Motor0 = hardwareMap.get(DcMotorEx.class, "Motor0");
            Motor1 = hardwareMap.get(DcMotorEx.class, "Motor1");
            Motor2 = hardwareMap.get(DcMotorEx.class, "Motor2");
            Motor3 = hardwareMap.get(DcMotorEx.class, "Motor3");
            Motor4 = hardwareMap.get(DcMotorEx.class, "Motor4");
            MotorShooter0 = hardwareMap.get(DcMotorEx.class, "MotorShooter0");

            Motor0.setDirection(DcMotorSimple.Direction.REVERSE);
            Motor2.setDirection(DcMotorSimple.Direction.REVERSE);
            Motor4.setDirection(DcMotorSimple.Direction.REVERSE);

            Motor0.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            Motor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            Motor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            Motor3.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            if (Motor4 != null) {
                Motor4.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            }

            if (MotorShooter0 != null) {
                MotorShooter0.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            }

            telemetry.addData("Status", "Inicializado com Sucesso!");
        } catch (IllegalArgumentException e) {
            telemetry.addData("ERRO DE HARDWARE", "Confira os nomes no Driver Station!");
            telemetry.addData("Detalhe do Erro", e.getMessage());
        }
    }

    @Override
    public void loop() {
        double x = gamepad1.left_stick_x;
        double y = -gamepad1.left_stick_y;
        double turn = gamepad1.right_stick_x;

        if (Math.abs(x) < 0.05) x = 0;
        if (Math.abs(y) < 0.05) y = 0;
        if (Math.abs(turn) < 0.05) turn = 0;

        double Motor0Power = y + x + turn;
        double Motor1Power = y - x - turn;
        double Motor2Power = y - x + turn;
        double Motor3Power = y + x - turn;

        double maxPower = Math.max(Math.abs(Motor0Power), Math.max(Math.abs(Motor1Power), Math.max(Math.abs(Motor2Power), Math.abs(Motor3Power))));

        if (maxPower > 0.6) {
            Motor0Power /= maxPower;
            Motor1Power /= maxPower;
            Motor2Power /= maxPower;
            Motor3Power /= maxPower;
        }

        if (Motor0 != null && Motor1 != null && Motor2 != null && Motor3 != null) {
            Motor0.setPower(Motor0Power);
            Motor1.setPower(Motor1Power);
            Motor2.setPower(Motor2Power);
            Motor3.setPower(Motor3Power);
        }

        double gatilho = gamepad1.right_trigger;
        if (MotorShooter0 != null) {
            if (gatilho > 0.7) {
                MotorShooter0.setPower(gatilho);
            } else if (gamepad1.a) {
                MotorShooter0.setPower(0.9);
            } else {
                MotorShooter0.setPower(0.0);
            }
        }

        if (Motor4 != null) {
            if (gamepad1.b) {
                Motor4.setPower(0.9);
            } else {
                Motor4.setPower(0.0);
            }
        }

        telemetry.addData("Shooter Conectado?", MotorShooter0 != null ? "SIM" : "NAO");
        if (MotorShooter0 != null) {
            telemetry.addData("Potencia Shooter", MotorShooter0.getPower());
        }
        telemetry.addData("Motor4 Conectado?", Motor4 != null ? "SIM" : "NAO");
        telemetry.addData("Botao B Pressionado?", gamepad1.b);
        telemetry.update();
    }
}
