// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants.climberConstants;
import frc.robot.Constants.controlBindings;
import frc.robot.Constants.intakeArmConstants;
import frc.robot.Constants.intakeRollerConstants;
import frc.robot.Constants.serializerConstants;
import frc.robot.Constants.shooterConstants;
import frc.robot.commands.AutoStuff;
import frc.robot.commands.DoNothing;
import frc.robot.commands.HoldClimber;
import frc.robot.commands.ToggleShooter;
import frc.robot.subsystems.*;
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
    private final Shooter shooter = new Shooter();
    private final Serializer serializer = new Serializer();
    private final Climber climber = new Climber();

    XboxController driver_controller = new XboxController(0);
    XboxController operator_controller = new XboxController(1);

    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() {
        // Configure the button bindings
        configureButtonBindings();

        drivetrain.setDefaultCommand(
            new RunCommand(
                () -> {
                    if (!serializer.getAuto()){
                        if (driver_controller.getRightBumper()){
                            drivetrain.curvatureDrive(-oi.get_curvature_output()[1], oi.get_curvature_output()[0]);
                        } else {
                            drivetrain.curvatureDrive(oi.get_curvature_output()[1], oi.get_curvature_output()[0]);
                        }
                    }
                },
                drivetrain
            )
        );

        shooter.setDefaultCommand(new RunCommand(() -> {if (!serializer.getAuto()){shooter.setPowerRaw(0);}}, shooter));

        roller.setDefaultCommand(new RunCommand(() -> {roller.setMotorRaw(0);}, roller));

        serializer.setDefaultCommand(new RunCommand(() -> {if (!serializer.getAuto()){serializer.setPowerRaw(0);}}, serializer));

        climber.setDefaultCommand(new RunCommand(() -> {climber.setPowerRaw(
            driver_controller.getRightTriggerAxis() - driver_controller.getLeftTriggerAxis()
        );}, climber));
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
     * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() {
        new JoystickButton(operator_controller, controlBindings.outtake)
            .whileHeld(new RunCommand(() -> {roller.setMotorRaw(intakeRollerConstants.rollerPower);}, roller));
        new JoystickButton(operator_controller, controlBindings.intake)
            .whileHeld(new RunCommand(() -> {roller.setMotorRaw(-intakeRollerConstants.rollerPower);}, roller));

        // new JoystickButton(operator_controller, controlBindings.lowerArm)
        //     .whenPressed(new RunCommand(arm::drop, arm));

        // new JoystickButton(operator_controller, controlBindings.raiseArm)
        //     .whenPressed(new RunCommand(() -> arm.setTargetPosition(intakeArmConstants.up_position), arm));
        
        new JoystickButton(operator_controller, controlBindings.toggleShooter)
            .whileHeld(new RunCommand(()-> {serializer.setAuto(false); shooter.setPowerRaw(shooterConstants.enablePower);}, shooter));
        
        new JoystickButton(operator_controller, controlBindings.runSerializerForward)
            .whileHeld(new RunCommand(() -> {serializer.setAuto(false); serializer.setPowerRaw(serializerConstants.enablePower);}, serializer));
        new JoystickButton(operator_controller, controlBindings.runSerializerReverse)
            .whileHeld(new RunCommand(() -> {serializer.setAuto(false); serializer.setPowerRaw(-serializerConstants.enablePower);}, serializer));

        // new JoystickButton(operator_controller, XboxController.Axis.kLeftTrigger.value)
        //     .whileHeld(new InstantCommand(climber::runBackwards, climber));

        // new JoystickButton(operator_controller, controlBindings.climberForward)
        //     .whileHeld(new InstantCommand(climber::runForwards, climber));

        // new JoystickButton(operator_controller, XboxController.Axis.kLeftTrigger.value) // TODO: Uncomment to use Variable Precision climbing
        //     .whileHeld(new VariablePrecisionClimber(VariablePrecisionClimber.Direction.BACKWARDS, climber));

        // new JoystickButton(operator_controller, XboxController.Axis.kRightTrigger.value)
        //     .whileHeld(new VariablePrecisionClimber(VariablePrecisionClimber.Direction.FORWARDS, climber));

        // new JoystickButton(driver_controller, XboxController.Button.kY.value)
        //     .whileHeld(new RunCommand(() -> {climber.setPowerRaw(climberConstants.motorRunSpeed);}, climber));
        
        // new JoystickButton(driver_controller, XboxController.Button.kA.value)
        //     .whileHeld(new RunCommand(() -> {climber.setPowerRaw(-climberConstants.motorRunSpeed);}, climber));

    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        return new AutoStuff(shooter, serializer, drivetrain);
    }
}
