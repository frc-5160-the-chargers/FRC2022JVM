package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.shooterConstants;

import static frc.robot.subsystems.Shooter.State.*;

public class Shooter extends SubsystemBase{
    private final CANSparkMax shooterMotor;

    private double power;
    private State state;

    public Shooter(){
        shooterMotor = new CANSparkMax(shooterConstants.motorPort, MotorType.kBrushless);
        shooterMotor.setInverted(true);
        state = DISABLED;
    }

    //This exists so that all subsystems have a commonly-named reset method
    public void reset(){
        disable();
    }

    public void enable(){
        state = ENABLED;
    }
    
    public void disable(){
        state = DISABLED;
    }

    public boolean isEnabled(){
        if (state == ENABLED) { return true; }
        else { return false; }
    }

    @Override
    public void periodic(){
        switch (state){
            case ENABLED:
                power = shooterConstants.enablePower;
            case DISABLED:
                power = 0;
        }
        shooterMotor.set(power);
    }
    
    enum State {
        ENABLED(0),
        DISABLED(1);

        final int value;
        State(int value){
            this.value = value;
        }
    }
}
