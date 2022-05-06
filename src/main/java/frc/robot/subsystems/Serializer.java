package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.serializerConstants;

import static frc.robot.subsystems.Serializer.State.*;

public class Serializer extends SubsystemBase {
    private final CANSparkMax serializerMotor1;

    private double power;
    private State state;

    private boolean isAuto;

    public Serializer(){
        serializerMotor1 = new CANSparkMax(serializerConstants.motorPort1, MotorType.kBrushed);
        state = DISABLED;
        power = 0;
        isAuto = false;
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

    public void setPowerRaw(double power){
        this.power = power;
    }

    public void setAuto(boolean auto){
        isAuto = auto;
    }

    public boolean getAuto(){
        return isAuto;
    }

    @Override
    public void periodic(){
        // switch (state){
        //     case FORWARD:
        //         power = serializerConstants.enablePower;
        //     case REVERSE:
        //         power = -serializerConstants.enablePower;
        //     case DISABLED:
        //         power = 0;
        // }
        serializerMotor1.set(power);
        System.out.println("Running serializer at " + power);
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
