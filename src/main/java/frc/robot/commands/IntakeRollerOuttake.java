package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IntakeRoller;
import frc.robot.subsystems.Oi;

public class IntakeRollerOuttake extends CommandBase{
    private final Oi oi;
    private final IntakeRoller roller;
    
    public IntakeRollerOuttake(Oi oi, IntakeRoller roller){
        this.oi = oi;
        this.roller = roller;
    }

    @Override
    public void initialize() {}

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        roller.outtake();
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        roller.stop();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}