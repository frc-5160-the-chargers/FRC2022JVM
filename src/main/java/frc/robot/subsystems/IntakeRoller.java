package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.intakeRollerConstants;

import static frc.robot.subsystems.IntakeRoller.State.*;

public class IntakeRoller extends SubsystemBase {
    private final CANSparkMax intakeMotor = new CANSparkMax(intakeRollerConstants.motorPort, MotorType.kBrushed);

    public State state;
    private double power = 0;

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

    public void setMotorRaw(double power){
        this.power = power;
    }

    @Override
    public void periodic(){
        // switch (state){
        //     case INTAKING:
        //         intakeMotor.set(intakeRollerConstants.rollerPower);
        //     case OUTTAKING:
        //         intakeMotor.set(-intakeRollerConstants.rollerPower);
        //     case STOPPED:
        //         intakeMotor.stopMotor();
        // }
        intakeMotor.set(power);
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