// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.serializerConstants;
import frc.robot.subsystems.Serializer;
import frc.robot.subsystems.Shooter;

public class AutoShoot extends CommandBase {
  private final Shooter shooter;
  private final Serializer serializer;
  private final double runTime;
  private double startTime;

  public AutoShoot(final Shooter shooter, final Serializer serializer) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);

    this.shooter = shooter;
    this.serializer = serializer;
    runTime = 2;
  }

  // Called just before this Command runs the first time
  @Override
  public void initialize() {
    startTime = Timer.getFPGATimestamp();
    shooter.enable();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() {
    if (Timer.getFPGATimestamp() - startTime < runTime){  
      serializer.setMotorRaw(serializerConstants.enablePower);
    }
    else {
      shooter.disable();
      serializer.setMotorRaw(0);
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  public boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  public void end(boolean interrupted) {}

}