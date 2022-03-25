package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.utils.Utils;

/**
 * <p>A subsystem representing the typical drivetrain of 5160 robots.</p>
 * <p>See <a href="https://docs.wpilib.org/en/stable/docs/software/hardware-apis/motors/wpi-drive-classes.html#drive-modes">here</a> for an explanation of the various drive modes.</p>
 */
public class Drivetrain extends SubsystemBase {
    private final CANSparkMax left1 = new CANSparkMax(Constants.drivetrainConstants.motors_left[0], CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax left2 = new CANSparkMax(Constants.drivetrainConstants.motors_left[1], CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax right1 = new CANSparkMax(Constants.drivetrainConstants.motors_right[0], CANSparkMaxLowLevel.MotorType.kBrushless);
    private final CANSparkMax right2 = new CANSparkMax(Constants.drivetrainConstants.motors_right[1], CANSparkMaxLowLevel.MotorType.kBrushless);

    private final MotorControllerGroup leftMotors = new MotorControllerGroup(left1, left2);
    private final MotorControllerGroup rightMotors = new MotorControllerGroup(right1, right2);

    private final DifferentialDrive differentialDrive = new DifferentialDrive(leftMotors, rightMotors);

    private final RelativeEncoder encoderLeft = left1.getEncoder();
    private final RelativeEncoder encoderRight = right1.getEncoder();

    public Drivetrain() {
        CANSparkMax[] motors = {left1, left2, right1, right2};
        for (CANSparkMax motor : motors) {
            configureSpark(motor);
        }

//        rightMotors.setInverted(true); // TODO: Uncomment if necessary
    }

    private void configureSpark(CANSparkMax motor) {
        motor.enableVoltageCompensation(Constants.drivetrainConstants.voltage_compensation);
        motor.setSmartCurrentLimit(Constants.drivetrainConstants.stall_current_limit);
        motor.setIdleMode(Constants.drivetrainConstants.default_mode);
        motor.setClosedLoopRampRate(Constants.drivetrainConstants.ramp_rate);
        motor.setInverted(Constants.drivetrainConstants.reverse_motor);
        motor.burnFlash();
    }

    /**
     * Drives using "tank controls", a system by which each side of the drivetrain is controlled independently.
     * @param leftPower the power of the left side of the drivetrain (from [-1..1]).
     * @param rightPower the power of the right side of the drivetrain (from [-1..1]).
     */
    public void tankDrive(final double leftPower, final double rightPower) {
        differentialDrive.tankDrive(leftPower, rightPower, false);
    }

    /**
     * Drives the robot at a certain power forward and with a certain amount of rotation.
     * @param power the power with which to drive forward (from [-1..1]).
     * @param rotation the power with which to rotate (proportional to the angular velocity, or how quickly the heading changes). (Must be from [-1..1]).
     */
    public void arcadeDrive(final double power, final double rotation) {
        differentialDrive.arcadeDrive(power, rotation, false);
    }

    /**
     * Drives the robot at a certain power forward and with a certain amount of steering.
     * This method makes turning easier at high speeds.
     * @param power the power with which to drive forward (from [-1..1]).
     * @param steering the amount of steering (inversely proportional to the turn radius).
     *                 Changing this value is can be thought of as changing how far a car's steering wheel is turned.
     *                 (Must be from [-1..1]).
     */
    public void curvatureDrive(final double power, final double steering) {
        differentialDrive.curvatureDrive(
            power,
            steering,
            true // TODO: What happens if this is false?
        );
    }

    /**
     * Stops the robot.
     */
    public void stop() {
        differentialDrive.stopMotor();
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
