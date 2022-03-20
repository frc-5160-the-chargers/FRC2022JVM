package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.pid.SuperPIDController;
import frc.robot.subsystems.Drivetrain;
import frc.robot.utils.Range;

import static frc.robot.Constants.drivetrainConstants.*;

/**
 * <p>A command that drives the robot forward by setting equal power to each side of the drivetrain.</p>
 * <p>Note that, depending on physical conditions, the robot may not actually follow a perfectly straight path. Use {@link DriveStraight} if an absolutely straight path is required.</p>
 */
public class Drive extends CommandBase {
    private final Drivetrain drivetrain;
    private final SuperPIDController pidController;

    private Drive(final double position, final Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
        addRequirements(drivetrain);

        pidController = new SuperPIDController.Builder(
            position_pid,
            drivetrain::getPosition,
            new Range(-max_motor_power, max_motor_power)
        )
            .tolerance(position_tolerance)
            .target(position)
            .build();
    }

    /**
     * Drives the robot to a specific position.
     */
    private static Drive toPosition(final double position, final Drivetrain drivetrain) {
        return new Drive(position, drivetrain);
    }

    /**
     * Drives the robot a specified distance.
     */
    private static Drive byDistance(final double distance, final Drivetrain drivetrain) {
        return new Drive(drivetrain.getPosition() + distance, drivetrain);
    }

    @Override
    public void execute() {
        drivetrain.arcadeDrive(pidController.calculateOutput(), 0.0);
    }

    @Override
    public boolean isFinished() {
        return pidController.isOnTarget();
    }

    @Override
    public void end(final boolean interrupted) {
        drivetrain.stop();
    }
}
