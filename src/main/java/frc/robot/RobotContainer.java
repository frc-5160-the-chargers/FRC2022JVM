// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.commands.DoNothing;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.OI;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    // The robot's subsystems and commands are defined here...
//    private final NavX navx = new NavX();
    private final OI oi = new OI();
    private final Drivetrain drivetrain = new Drivetrain();

    XboxController driver_controller = new XboxController(0);
//    XboxController operator_controller = new XboxController(1);

    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() {
        // Configure the button bindings
        configureButtonBindings();

        drivetrain.setDefaultCommand(
            new RunCommand(
                () -> drivetrain.curvatureDrive(
                    oi.get_curvature_output()[1], oi.get_curvature_output()[0]
                ),
                drivetrain
            )
        );
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
     * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() {

//        new JoystickButton(operator_controller, XboxController.Axis.kLeftTrigger.value) // TODO: Uncomment to use Variable Precision climbing
//            .whileHeld(new VariablePrecisionClimber(VariablePrecisionClimber.Direction.BACKWARDS, climber));
//
//        new JoystickButton(operator_controller, XboxController.Axis.kRightTrigger.value)
//            .whileHeld(new VariablePrecisionClimber(VariablePrecisionClimber.Direction.FORWARDS, climber));
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        return new DoNothing();
    }
}
