package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.serializerConstants;

import static frc.robot.subsystems.Serializer.State.*;

public class Serializer extends SubsystemBase {
    private final WPI_TalonSRX serializerMotor1;

    private double power;
    private State state;

    public Serializer(){
        serializerMotor1 = new WPI_TalonSRX(serializerConstants.motorPort1);
        state = DISABLED;
    }

    //This exists so that all subsystems have a commonly-named reset method
    public void reset(){
        disable();
    }

    public void runForward(){
        state = FORWARD;
    }

    public void runReverse(){
        state = REVERSE;
    }
    
    public void disable(){
        state = DISABLED;
    }

    public boolean isEnabled(){
        if (state == FORWARD) { return true; }
        else { return false; }
    }

    @Override
    public void periodic(){
        switch (state){
            case FORWARD:
                power = serializerConstants.enablePower;
            case REVERSE:
                power = -serializerConstants.enablePower;
            case DISABLED:
                power = 0;
        }
        serializerMotor1.set(power);
    }
    
    enum State {
        FORWARD(0),
        REVERSE(1),
        DISABLED(2);

        final int value;
        State(int value){
            this.value = value;
        }
    }
}
