package frc.robot.commands

import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.subsystems.Drivetrain
import frc.robot.subsystems.OI

class ManualCurvatureDrive(
    private val oi: OI,
    private val drivetrain: Drivetrain
) : CommandBase() {
    init {
        addRequirements(oi, drivetrain)
    }

    // Called every time the scheduler runs while the command is scheduled.
    override fun execute() {
        drivetrain.curvatureDrive(power = oi.curvatureOutput.y, rotation = oi.curvatureOutput.x)
    }

    // Called once the command ends or is interrupted.
    override fun end(interrupted: Boolean) {
        drivetrain.stop()
    }

    // Returns true when the command should end.
    override fun isFinished(): Boolean {
        return false
    }
}