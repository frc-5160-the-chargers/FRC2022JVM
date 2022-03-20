package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakeArm;

import static frc.robot.Constants.intakeArmConstants.down_position;

/**
 * A command that lowers the arm in a controlled way until it gets close enough to the lowered position, at which point it disables the motors and lets the arm fall.
 */
public class LowerIntake extends CommandBase {
    private final IntakeArm intakeArm;
    private static final double DISTANCE_FROM_LOWERED_POSITION_TO_DROP = 0.2;

    public LowerIntake(final IntakeArm intakeArm) {
        this.intakeArm = intakeArm;
        addRequirements(intakeArm);
    }

    @Override
    public void initialize() {
        intakeArm.setTargetPosition(down_position);
    }

    @Override
    public boolean isFinished() {
        return intakeArm.getDistanceFromTargetPosition() < DISTANCE_FROM_LOWERED_POSITION_TO_DROP;
    }

    @Override
    public void end(boolean interrupted) {
        if (interrupted) {
            intakeArm.dropIfSafe(); // Don't want to waste time if interrupted; if it's not safe to drop,
                                    // leave the arm where it is for now and let the next command deal with it.
        } else {
            intakeArm.drop();
        }
    }
}
