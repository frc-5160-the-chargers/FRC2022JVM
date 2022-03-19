package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.NavX;
import frc.robot.subsystems.Oi;
import frc.robot.subsystems.Powertrain;

public class ManualCurvatureDrive extends CommandBase{
    private final Oi oi;
    private final Drivetrain drivetrain;

    public ManualCurvatureDrive(
        Oi oi,
        Drivetrain drivetrain
    ){
        this.oi = oi;
        this.drivetrain = drivetrain;
    }

    @Override
    public void initialize() {}

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        drivetrain.curvatureDrive(oi.get_curvature_output()[1], oi.get_curvature_output()[0]);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        drivetrain.state = Drivetrain.State.STOPPED;
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
