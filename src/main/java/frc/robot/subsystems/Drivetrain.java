package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.utils.Utils;

public class Drivetrain extends SubsystemBase {
    private final CANSparkMax left1 = new CANSparkMax(Constants.drivetrainConstants.motors_left[0], CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax left2 = new CANSparkMax(Constants.drivetrainConstants.motors_left[1], CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax right1 = new CANSparkMax(Constants.drivetrainConstants.motors_right[0], CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax right2 = new CANSparkMax(Constants.drivetrainConstants.motors_right[1], CANSparkMaxLowLevel.MotorType.kBrushless);

    public final MotorControllerGroup leftMotors = new MotorControllerGroup(left1, left2);
    public final MotorControllerGroup rightMotors = new MotorControllerGroup(right1, right2);

    public final DifferentialDrive differentialDrive = new DifferentialDrive(leftMotors, rightMotors);

    private final RelativeEncoder encoderLeft = left1.getEncoder();
    private final RelativeEncoder encoderRight = right1.getEncoder();

    public Drivetrain() {
        CANSparkMax[] motors = {left1, left2, right1, right2};
        for (CANSparkMax motor : motors) {
            configureSpark(motor);
        }
    }

    private void configureSpark(CANSparkMax motor) {
        motor.enableVoltageCompensation(Constants.drivetrainConstants.voltage_compensation);
        motor.setSmartCurrentLimit(Constants.drivetrainConstants.stall_current_limit);
        motor.setIdleMode(Constants.drivetrainConstants.default_mode);
        motor.setClosedLoopRampRate(Constants.drivetrainConstants.ramp_rate);
        motor.setInverted(Constants.drivetrainConstants.reverse_motor);
        motor.burnFlash();
    }
    
    public void tankDrive(final double leftPower, final double rightPower) {
        differentialDrive.tankDrive(leftPower, rightPower, false);
    }

    public void arcadeDrive(final double power, final double rotation) {
        differentialDrive.arcadeDrive(power, rotation, false);
    }

    public void curvatureDrive(final double power, final double rotation) {
        differentialDrive.curvatureDrive(power, rotation, true);
    }

    public void stop() {
        tankDrive(0, 0);
    }

    public double getAngularPosition() {
        double[] positions = { encoderLeft.getPosition(), encoderRight.getPosition() };
        double rotations = Utils.average(positions);
        double rotations_adjusted = rotations* Constants.drivetrainConstants.gear_ratio;
        double radians = rotations_adjusted*2*Math.PI;

        return radians;
    }

    public double getAngularVelocity() {
        double[] velocities = { encoderLeft.getVelocity(), encoderRight.getVelocity() };
        double rpm = Utils.average(velocities);
        double rpm_adjusted = rpm * Constants.drivetrainConstants.gear_ratio;
        double omega = (2*rpm_adjusted*Math.PI)/60;

        return omega;
    }

    public double getPosition() {
        double rotations = getAngularPosition();
        double distance = rotations*(Constants.drivetrainConstants.wheel_diameter/2);
        return distance;
    }

    public double getVelocity() {
        double radsec = getAngularVelocity();
        double vel = radsec*(Constants.drivetrainConstants.wheel_diameter/2);
        return vel;
    }
}
