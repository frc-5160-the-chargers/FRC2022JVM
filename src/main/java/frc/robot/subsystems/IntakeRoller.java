package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.intakeConstants;

import static frc.robot.subsystems.IntakeRoller.State.*;

public class IntakeRoller extends SubsystemBase {
    private final WPI_TalonSRX intakeMotor = new WPI_TalonSRX(intakeConstants.motorPort);

    public State state;

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
                intakeMotor.set(intakeConstants.controlMode, intakeConstants.rollerPower);
            case OUTTAKING:
                intakeMotor.set(intakeConstants.controlMode, -intakeConstants.rollerPower);
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