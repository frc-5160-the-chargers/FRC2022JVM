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
    private boolean isEnabledBool;

    public Shooter(){
        shooterMotor = new CANSparkMax(shooterConstants.motorPort, MotorType.kBrushless);
        shooterMotor.setInverted(true);
        state = DISABLED;
        isEnabledBool = false;
    }

    //This exists so that all subsystems have a commonly-named reset method
    public void reset(){
        disable();
    }

    public void enable(){
        state = ENABLED;
        isEnabledBool = true;
    }
    
    public void disable(){
        state = DISABLED;
        isEnabledBool = false;
    }

    public boolean isEnabled(){
        return isEnabledBool;
    }

    @Override
    public void periodic(){
        /**switch (state){
            case ENABLED:
                power = shooterConstants.enablePower;
            case DISABLED:
                power = 0;
        }**/
        if(isEnabledBool) {
            shooterMotor.set(shooterConstants.enablePower);
            //System.out.println("the shooter is ENABLED");
        } else {
            shooterMotor.set(0);
            //System.out.println("the shooter is DISABLED");
        }

        //shooterMotor.set(power);
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
