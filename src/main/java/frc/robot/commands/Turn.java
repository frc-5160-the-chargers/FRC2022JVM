package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.pid.SuperPIDController;
import frc.robot.hardware.subsystems.Drivetrain;
import frc.robot.hardware.sensors.NavX;
import frc.robot.utils.Range;

import static frc.robot.Constants.drivetrainConstants.*;

/**
 * A command that turns the robot.
 */
public class Turn extends CommandBase {
    private final Drivetrain drivetrain;
    private final SuperPIDController pidController;

    private Turn(final double heading, final Drivetrain drivetrain, final NavX navX) {
        this.drivetrain = drivetrain;
        addRequirements(drivetrain);

        pidController = new SuperPIDController.Builder(
            turn_pid,
            navX::getHeading,
            new Range(-max_motor_power, max_motor_power)
        )
            .tolerance(turn_tolerance)
            .target(heading)
            .build();
    }

    /**
     * Turns the robot to a specified heading.
     */
    public static Turn toHeading(final double heading, final Drivetrain drivetrain, final NavX navX) {
        return new Turn(heading, drivetrain, navX);
    }

    /**
     * Turns the robot by a specified angle.
     */
    public static Turn byAngle(final double angle, final Drivetrain drivetrain, final NavX navX) {
        return new Turn(navX.getHeading() + angle, drivetrain, navX);
    }

    @Override
    public void execute() {
        drivetrain.arcadeDrive(0.0, pidController.calculateOutput());
    }

    @Override
    public boolean isFinished() {
        return pidController.isOnTarget();
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.stop();
    }
}
