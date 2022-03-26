package frc.robot.subsystems;


import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ShooterNonPeriodic extends SubsystemBase {
    private final CANSparkMax shooterMotor;
    private boolean isEnabled = false;

    public ShooterNonPeriodic() {
        shooterMotor = new CANSparkMax(Constants.shooterConstants.motorPort, CANSparkMaxLowLevel.MotorType.kBrushless);
        shooterMotor.setInverted(true);
    }

    public void reset(){
        disable();
    }

    public void enable(){
        shooterMotor.set(Constants.shooterConstants.enablePower);
        isEnabled = true;
    }

    public void disable(){
        shooterMotor.stopMotor();
        isEnabled = false;
    }

    public boolean isEnabled(){
        return isEnabled;
    }
}

