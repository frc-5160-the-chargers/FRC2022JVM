// package frc.robot.commands;

// import edu.wpi.first.wpilibj2.command.CommandBase;
// import frc.robot.pid.SuperPIDController;
// import frc.robot.subsystems.Climber;
// import frc.robot.utils.Range;

// import static frc.robot.Constants.climberConstants.*;


// public class HoldClimber extends CommandBase {
//     private final Climber climber;

//     private final SuperPIDController pidController;

//     public HoldClimber(final Climber climber) {
//         this.climber = climber;
//         addRequirements(climber);

//         pidController = new SuperPIDController.Builder(
//             holdPIDValues,
//             climber::getAngularPosition,
//             new Range(-maxMotorHoldPower, maxMotorHoldPower)
//         )
//             .feedForward((target, error) -> holdFeedForwardFactor)
//             .build();
//     }

//     @Override
//     public void initialize() {
//         final double startPosition = climber.getAngularPosition();
//         pidController.setTarget(startPosition);
//     }

//     @Override
//     public void execute() {
//         climber.run(pidController.calculateOutput());
//     }

//     @Override
//     public boolean isFinished() {
//         return false;
//     }
// }
