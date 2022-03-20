// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import frc.robot.commands.ExampleCommand;
import frc.robot.commands.ManualCurvatureDrive;
import frc.robot.subsystems.*;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final NavX navx = new NavX();
  private final OI oi = new OI();
  private final Drivetrain drivetrain = new Drivetrain();
  private final IntakeRoller roller = new IntakeRoller();
  private final IntakeArm arm = new IntakeArm();

  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
  private final ExampleCommand m_autoCommand = new ExampleCommand(m_exampleSubsystem);

  XboxController driver_controller = new XboxController(0);
  XboxController operator_controller = new XboxController(1);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();

    drivetrain.setDefaultCommand(
      new RunCommand(
        () ->
          drivetrain.curvatureDrive(
            oi.get_curvature_output()[1], oi.get_curvature_output()[0]),
        drivetrain)
    );

    roller.setDefaultCommand(
      new RunCommand(
        () ->
          roller.stop(),
        roller)
    );

    arm.setDefaultCommand(
      new InstantCommand(
        () ->
          arm.set_match_start(),
        arm)
    );
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    new JoystickButton(operator_controller, Button.kA.value)
      .whileHeld(new InstantCommand(roller::outtake, roller));
    new JoystickButton(operator_controller, Button.kB.value)
      .whileHeld(new InstantCommand(roller::intake, roller));

    new JoystickButton(operator_controller, Button.kLeftBumper.value)
      .whenPressed(new InstantCommand(arm::send_down, arm));
    new JoystickButton(operator_controller, Button.kRightBumper.value)
      .whenPressed(new InstantCommand(arm::send_up, arm));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return m_autoCommand;
  }
}
