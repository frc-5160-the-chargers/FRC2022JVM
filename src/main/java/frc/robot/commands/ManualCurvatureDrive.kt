package frc.robot.commands

import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.subsystems.Drivetrain
import java.util.function.DoubleSupplier

class ManualCurvatureDrive(
    private val drivetrain: Drivetrain,
    private val power: DoubleSupplier,
    private val rotation: DoubleSupplier
) : CommandBase() {
    override fun initialize() {}

    // Called every time the scheduler runs while the command is scheduled.
    override fun execute() {
        drivetrain.curvatureDrive(power.asDouble, rotation.asDouble)
    }

    // Called once the command ends or is interrupted.
    override fun end(interrupted: Boolean) {
        drivetrain.stop()
    }

    // Returns true when the command should end.
    override fun isFinished(): Boolean = false
}