package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * A placeholder command that does nothing and does not end unless interrupted.
 */
public class DoNothing extends CommandBase {
    @Override
    public boolean isFinished() {
        return false;
    }
}
