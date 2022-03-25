//package frc.robot.subsystems;
//
//
//import com.revrobotics.CANSparkMax;
//import com.revrobotics.CANSparkMaxLowLevel;
//import com.revrobotics.RelativeEncoder;
//import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
//import edu.wpi.first.wpilibj2.command.SubsystemBase;
//import frc.robot.Constants;
//import frc.robot.utils.Utils;
//
//import static frc.robot.Constants.climberConstants.*;
//
//public class Climber extends SubsystemBase {
//    private final CANSparkMax climberMotor1 = new CANSparkMax(Constants.climberConstants.motorPorts[0], CANSparkMaxLowLevel.MotorType.kBrushless);
//    private final CANSparkMax climberMotor2 = new CANSparkMax(Constants.climberConstants.motorPorts[1], CANSparkMaxLowLevel.MotorType.kBrushless);
//
//    private final MotorControllerGroup motors = new MotorControllerGroup(climberMotor1, climberMotor2);
//
//    private final RelativeEncoder encoder1 = climberMotor1.getEncoder();
//    private final RelativeEncoder encoder2 = climberMotor2.getEncoder();
//
//    public Climber() {
////        climberMotor1.setInverted(true); // TODO: Uncomment after testing if necessary
////        climberMotor2.setInverted(true); // TODO: Uncomment after testing if necessary
//
//        final CANSparkMax[] sparkMaxes = {climberMotor1, climberMotor2};
//        for (CANSparkMax sparkMax : sparkMaxes) {
//            configureSpark(sparkMax);
//        }
//    }
//
//    private void configureSpark(CANSparkMax motor) { // TODO: Uncomment any lines as necessary
////        motor.enableVoltageCompensation(___);
////        motor.setSmartCurrentLimit(___);
//        motor.setIdleMode(Constants.climberConstants.defaultMotorMode);
////        motor.setClosedLoopRampRate(___);
//        motor.burnFlash();
//    }
//
//    public void run(double speed) {
//        motors.set(speed);
//    }
//
//    public void runForwards() {
//        run(motorRunSpeed);
//    }
//
//    public void runBackwards() {
//        run(-motorRunSpeed);
//    }
//
//    public void stop() {
//        motors.stopMotor();
//    }
//
//    public double getAngularPosition() {
//        double[] positions = { encoder1.getPosition(), encoder2.getPosition() };
//        double rotations = Utils.average(positions);
//        double rotations_adjusted = rotations * gearRatio;
//        double radians = rotations_adjusted*2*Math.PI;
//
//        return radians;
//    }
//
//    public double getPosition() {
//        double rotations = getAngularPosition();
//        double distance = rotations*(Constants.climberConstants.winchDiameter/2);
//        return distance;
//    }
//}
//
