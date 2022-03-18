package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class ManualCurvatureDrive extends CommandBase{
    private final Drivetrain drivetrain;
    private final DoubleSupplier power;
    private final DoubleSupplier rotation;

    public ManualCurvatureDrive(
        Drivetrain drivetrain,
        DoubleSupplier power_stick,
        DoubleSupplier rotation_stick
    ){
        this.drivetrain = drivetrain;
        this.power = power_stick;
        this.rotation = rotation_stick;
    }

    @Override
    public void initialize() {}

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        drivetrain.curvatureDrive(power.getAsDouble(), rotation.getAsDouble());
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
