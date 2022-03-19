// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
package frc.robot

import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj2.command.Command
import frc.robot.commands.ManualCurvatureDrive
import frc.robot.subsystems.Drivetrain
import frc.robot.subsystems.NavX
import frc.robot.subsystems.OI
import frc.robot.subsystems.Powertrain

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the [Robot]
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
class RobotContainer {
    // The robot's subsystems and commands are defined here...
    private val navx = NavX()
    private val oi = OI()
    private val powertrain = Powertrain()
    private val drivetrain = Drivetrain(powertrain, navx)
    private val manualDrive: Command = ManualCurvatureDrive(oi, drivetrain)
    private val driverController = XboxController(0)
    private val operatorController = XboxController(1)

    /** The container for the robot. Contains subsystems, OI devices, and commands.  */
    init {
        // Configure the button bindings
        configureButtonBindings()
        drivetrain.defaultCommand = manualDrive
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a [GenericHID] or one of its subclasses ([ ] or [XboxController]), and then passing it to a [ ].
     */
    private fun configureButtonBindings() {}// An ExampleCommand will run in autonomous

    /**
     * Use this to pass the autonomous command to the main [Robot] class.
     *
     * @return the command to run in autonomous
     */
    val autonomousCommand: Command?
        get() = null
}