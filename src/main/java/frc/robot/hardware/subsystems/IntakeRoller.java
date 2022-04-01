package frc.robot.hardware.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.intakeRollerConstants;

import static frc.robot.hardware.subsystems.IntakeRoller.State.*;

public class IntakeRoller extends SubsystemBase {
    private final CANSparkMax intakeMotor = new CANSparkMax(intakeRollerConstants.motorPort, MotorType.kBrushed);

    public State state;

    public IntakeRoller(){
        reset();
    }

    public void reset(){
        state = STOPPED;
    }

    public void intake(){
        state = INTAKING;
    }

    public void outtake(){
        state = OUTTAKING;
    }

    public void stop(){
        state = State.STOPPED;
    }

    @Override
    public void periodic(){
        switch (state){
            case INTAKING:
                intakeMotor.set(intakeRollerConstants.rollerPower);
            case OUTTAKING:
                intakeMotor.set(-intakeRollerConstants.rollerPower);
            case STOPPED:
                intakeMotor.stopMotor();
        }
    }

    enum State {
        STOPPED(0),
        INTAKING(1),
        OUTTAKING(2);

        final int value;
        State(int value){
            this.value = value;
        }
    }
}