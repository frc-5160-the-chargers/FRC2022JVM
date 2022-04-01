package frc.robot.hardware.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.intakeRollerConstants;

public class IntakeRollerNonPeriodic extends SubsystemBase {
    private final CANSparkMax intakeMotor = new CANSparkMax(intakeRollerConstants.motorPort, MotorType.kBrushed);

    public IntakeRollerNonPeriodic(){
        reset();
    }

    public void reset(){
        stop();
    }

    public void intake(){
        intakeMotor.set(intakeRollerConstants.rollerPower);
    }

    public void outtake(){
        intakeMotor.set(-intakeRollerConstants.rollerPower);
    }

    public void stop(){
        intakeMotor.stopMotor();
    }
}