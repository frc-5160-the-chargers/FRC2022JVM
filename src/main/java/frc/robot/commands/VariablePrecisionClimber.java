package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

import static frc.robot.Constants.climberConstants.*;
import static java.lang.Math.abs;


public class VariablePrecisionClimber extends CommandBase {
    private final Climber climber;
    private final Direction direction;

    public VariablePrecisionClimber(final Direction direction, final Climber climber) {
        this.direction = direction;
        this.climber = climber;
        addRequirements(this.climber);
    }

    @Override
    public void execute() {
        climber.run(getAdjustedSpeed() * direction.multiplier);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    private double getClimberDistanceFromEnd() {
        return abs(climber.getPosition() - winchActuationDistanceInches);
    }

    private double getAdjustedSpeed() {
        if (getClimberDistanceFromEnd() < precisionAreaSizeInches) {
            return precisionSpeed;
        } else {
            return motorRunSpeed;
        }
    }

    public static enum Direction {
        FORWARDS(1), BACKWARDS(-1);

        final double multiplier;
        Direction(final double multiplier) {
            this.multiplier = multiplier;
        }
    }
}
