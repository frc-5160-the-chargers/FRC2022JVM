package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import static frc.robot.subsystems.Powertrain.Mode.*;

public class Powertrain extends SubsystemBase {
    public final CANSparkMax left1 = new CANSparkMax(1, MotorType.kBrushless);
    public final CANSparkMax left2 = new CANSparkMax(2, MotorType.kBrushless);
    public final CANSparkMax right1 = new CANSparkMax(3, MotorType.kBrushless);
    public final CANSparkMax right2 = new CANSparkMax(4, MotorType.kBrushless);

    public final CANSparkMax[] motors = {left1, left2, right1, right2};

    public final MotorControllerGroup left_motors = new MotorControllerGroup(left1, left2);
    public final MotorControllerGroup right_motors = new MotorControllerGroup(right1, right2);
    
    public final DifferentialDrive differentialDrive = new DifferentialDrive(left_motors, right_motors);

    private double power;
    private double rotation;
    private double leftPower;
    private double rightPower;

    public Mode mode;

    public Powertrain() {
        for (CANSparkMax motor : motors) {
            ConfigureSpark(motor);
        }
        reset();
    }

    private void ConfigureSpark(CANSparkMax motor) {
        motor.enableVoltageCompensation(11);
        motor.setSmartCurrentLimit(39);
        motor.setIdleMode(IdleMode.kBrake);
        motor.setClosedLoopRampRate(1);
        motor.setInverted(true);
        motor.burnFlash();
    }

    public void reset() {
        mode = CURVATURE_DRIVE;

        power = 0;
        rotation = 0;
        leftPower = 0;
        rightPower = 0;
    }

    public void setTankPowers(double left_power, double right_power) {
        mode = TANK_DRIVE;
        power = rotation = 0;

        this.leftPower = left_power;
        this.rightPower = right_power;
    }
    
    public void setArcadePowers(double power, double rotation) {
        mode = ARCADE_DRIVE;
        leftPower = rightPower = 0;

        this.power = power;
        this.rotation = rotation;
    }

    public void tankDrive(double left_power, double right_power) {
        mode = TANK_DRIVE;
        setTankPowers(left_power, right_power);
    }
    public void arcadeDrive(double power, double rotation) {
        mode = ARCADE_DRIVE;
        setArcadePowers(power, rotation);
    }
    public void curvatureDrive(double power, double rotation) {
        mode = CURVATURE_DRIVE;
        setArcadePowers(power, rotation);
    }

    @Override
    public void periodic() {
        if (mode == TANK_DRIVE){
            differentialDrive.tankDrive(leftPower, rightPower, false);
        }
        else if (mode == ARCADE_DRIVE){
            differentialDrive.arcadeDrive(power, rotation, false);
        }
        else if (mode == CURVATURE_DRIVE){
            differentialDrive.curvatureDrive(power, rotation, true);
        }
    }

    enum Mode {
        TANK_DRIVE(0),
        CURVATURE_DRIVE(1),
        ARCADE_DRIVE(2);

        final int value;
        Mode(int value) {
            this.value = value;
        }
    }
}
