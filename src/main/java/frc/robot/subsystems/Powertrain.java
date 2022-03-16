package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Powertrain extends SubsystemBase{
    final CANSparkMax left1 = new CANSparkMax(1, MotorType.kBrushless);
    final CANSparkMax left2 = new CANSparkMax(2, MotorType.kBrushless);
    final CANSparkMax right1 = new CANSparkMax(3, MotorType.kBrushless);
    final CANSparkMax right2 = new CANSparkMax(4, MotorType.kBrushless);

    final CANSparkMax[] motors = {left1, left2, right1, right2};

    final MotorControllerGroup left_motors = new MotorControllerGroup(left1, left2);
    final MotorControllerGroup right_motors = new MotorControllerGroup(right1, right2);    
    
    final DifferentialDrive differential_drive = new DifferentialDrive(left_motors, right_motors);

    private double power;
    private double rotation;
    private double left_power;
    private double right_power;

    public final int TANK_DRIVE = 0;
    public final int CURVATURE_DRIVE = 1;
    public final int ARCADE_DRIVE = 2;

    public int mode;

    public Powertrain() {
        for (CANSparkMax motor : motors) {
            ConfigureSpark(motor);
        }
        reset();
    }

    private void ConfigureSpark(CANSparkMax motor){
        motor.enableVoltageCompensation(11);
        motor.setSmartCurrentLimit(39);
        motor.setIdleMode(IdleMode.kBrake);
        motor.setClosedLoopRampRate(1);
        motor.setInverted(true);
        motor.burnFlash();
    }

    public void reset(){
        mode = CURVATURE_DRIVE;

        power = 0;
        rotation = 0;
        left_power = 0;
        right_power = 0;
    }

    public void set_tank_powers(double left_power, double right_power){
        mode = TANK_DRIVE;
        power = rotation = 0;

        this.left_power = left_power;
        this.right_power = right_power;
    }
    
    public void set_arcade_powers(double power, double rotation){
        mode = ARCADE_DRIVE;
        left_power = right_power = 0;

        this.power = power;
        this.rotation = rotation;
    }

    public void tank_drive(double left_power, double right_power){
        mode = TANK_DRIVE;
        set_tank_powers(left_power, right_power);
    }
    public void arcade_drive(double power, double rotation){
        mode = ARCADE_DRIVE;
        set_arcade_powers(power, rotation);
    }
    public void curvature_drive(double power, double rotation){
        mode = CURVATURE_DRIVE;
        set_arcade_powers(power, rotation);
    }

    @Override
    public void periodic() {
        if (mode == TANK_DRIVE){
            differential_drive.tankDrive(left_power, right_power, false);
        }
        else if (mode == ARCADE_DRIVE){
            differential_drive.arcadeDrive(power, rotation, false);
        }
        else if (mode == CURVATURE_DRIVE){
            differential_drive.curvatureDrive(power, rotation, true);
        }
    }
}
