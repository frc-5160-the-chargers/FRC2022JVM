package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakeArm;

import static frc.robot.Constants.intakeArmConstants.down_position;
import static frc.robot.Constants.intakeArmConstants.up_position;

/**
 * A command that uses PID to keep the arm in the lowered position, moving it if not already lowered.
 */
public class LowerAndHoldIntake extends CommandBase {
    private final IntakeArm intakeArm;

    public LowerAndHoldIntake(final IntakeArm intakeArm) {
        this.intakeArm = intakeArm;
        addRequirements(intakeArm);
    }

    @Override
    public void initialize() {
        intakeArm.setTargetPosition(down_position);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        // TODO: Should drop the intake here?
    }
}
