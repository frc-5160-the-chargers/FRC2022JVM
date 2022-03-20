package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.OI;

public class ManualCurvatureDrive extends CommandBase{
    private final OI oi;
    private final Drivetrain drivetrain;

    public ManualCurvatureDrive(
        OI oi,
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
        drivetrain.stop();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
