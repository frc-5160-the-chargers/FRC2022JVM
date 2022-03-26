package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;

public class ToggleShooter extends CommandBase{
    private final Shooter shooter;
    private boolean hasRan;

    public ToggleShooter(final Shooter shooter) {
        this.shooter = shooter;
        addRequirements(shooter);
        hasRan = false;
    }

    @Override
    public void execute() {
        if (shooter.isEnabled() && !hasRan){
            shooter.disable();
            hasRan = true;
        }
        else if (!hasRan) {
            shooter.enable();
            hasRan = true;
        }
    }

      // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        hasRan = false;
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return hasRan;
    }
}
