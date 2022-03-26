package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.ShooterNonPeriodic;

public class ToggleShooterNonPeriodic extends CommandBase {
    private final ShooterNonPeriodic shooter;

    public ToggleShooterNonPeriodic(final ShooterNonPeriodic shooter) {
        this.shooter = shooter;
        addRequirements(shooter);
    }

    @Override
    public void execute() {
        if (shooter.isEnabled()){
            shooter.disable();
        } else {
            shooter.enable();
        }
    }
}
