package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;

public class ToggleShooter extends CommandBase{
    private final Shooter shooter;

    public ToggleShooter(final Shooter shooter) {
        this.shooter = shooter;
        addRequirements(shooter);
    }

    @Override
    public void execute() {
        if (shooter.isEnabled()){
            shooter.disable();
        }
        else {
            shooter.enable();
        }
    }
}
