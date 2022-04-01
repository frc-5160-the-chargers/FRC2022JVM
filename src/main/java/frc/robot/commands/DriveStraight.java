package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.pid.SuperPIDController;
import frc.robot.hardware.subsystems.Drivetrain;
import frc.robot.hardware.sensors.NavX;
import frc.robot.utils.Range;

import static frc.robot.Constants.drivetrainConstants.*;

/**
 * <p>A command that drives the robot forward, turning as necessary to continue facing in the same direction.</p>
 */
public class DriveStraight extends CommandBase {
    private final double power;
    private final Drivetrain drivetrain;
    private final SuperPIDController turnPidController;

    public DriveStraight(final double power, final Drivetrain drivetrain, final NavX navX) {
        this.power = power;

        this.drivetrain = drivetrain;
        addRequirements(drivetrain);

        turnPidController = new SuperPIDController.Builder(
            turn_pid,
            navX::getHeading,
            new Range(-max_motor_power, max_motor_power)
        )
            .tolerance(turn_tolerance)
            .target(navX.getHeading()) // Try to maintain the current heading
            .build();
    }

    @Override
    public void execute() {
        drivetrain.arcadeDrive(power, turnPidController.calculateOutput());
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(final boolean interrupted) {
        drivetrain.stop();
    }
}
