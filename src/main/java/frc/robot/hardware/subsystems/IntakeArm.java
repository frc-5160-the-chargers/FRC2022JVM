package frc.robot.hardware.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.intakeArmConstants;
import frc.robot.pid.SuperPIDController;
import frc.robot.utils.Range;

import static frc.robot.Constants.intakeArmConstants.down_position;
import static frc.robot.Constants.intakeArmConstants.maxSafePositionToDrop;

/**
 * A subsystem representing the intake arm typical of 5160 robots.
 */
public class IntakeArm extends SubsystemBase {
    private final CANSparkMax armMotor;
    private final RelativeEncoder armEncoder;

    private final SuperPIDController armPid = new SuperPIDController.Builder(
        intakeArmConstants.pid_values,
        this::getTargetPosition,
        new Range(intakeArmConstants.min_power, intakeArmConstants.max_power)
    )
        .feedForward((target, error) -> 0.0)
        .tolerance(intakeArmConstants.tolerance)
        .build();

    private State state = State.OFF;

    public IntakeArm(){
        armMotor = new CANSparkMax(intakeArmConstants.motor_port, MotorType.kBrushless);
        armEncoder = armMotor.getEncoder();
    }

    /**
     * The target position is the angular position the intake arm is trying to reach.
     */
    public void setTargetPosition(final double position) {
        state = State.TARGETING_POSITION;
        armPid.setTarget(position);
    }

    /**
     * The target position is the angular position the intake arm is trying to reach.
     */
    public double getTargetPosition() {
        double rawPos = armEncoder.getPosition();
        double adjustedPos = rawPos*intakeArmConstants.gear_ratio;
        return adjustedPos*2*Math.PI;
    }

    /**
     * Returns how far the arm currently is from its target position.
     */
    public double getDistanceFromTargetPosition() {
        switch (state) {
            case TARGETING_POSITION:
                return armPid.getError();
            case OFF:
                return 0;
            default:
                throw new IllegalStateException();
        }
    }

    /**
     * Drops the arm by disabling the motors, but only if doing so will not cause damage to the robot.
     */
    public boolean dropIfSafe() {
        if (isSafeToDrop()) {
            state = State.OFF;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Drops the arm by moving it in a controlled way until it is safe to disable the motors without causing damage to the robot.
     */
    public void drop() {
        setTargetPosition(down_position);
        state = State.SAFE_DROPPING;
        dropIfSafe(); // TODO: Fill this in depending on the arm design (will likely use PID to get into a safe area, then disable motors).
    }

    private boolean isSafeToDrop() {
        return armEncoder.getPosition() < maxSafePositionToDrop; // TODO: Fill this in depending on the arm design (likely will check if arm is close enough to drop position to not cause damage)
    }

    @Override
    public void periodic() {
        switch (state) {
            case TARGETING_POSITION:
                armMotor.set(armPid.calculateOutput());
                break;
            case SAFE_DROPPING:
                if (!isSafeToDrop()) {
                    armMotor.set(armPid.calculateOutput());
                } else {
                    state = State.OFF;
                }
                break;
            case OFF:
                armMotor.setIdleMode(CANSparkMax.IdleMode.kCoast);
                armMotor.stopMotor();
                break;
        }

        SmartDashboard.putNumber("Intake Arm Encoder Position", armEncoder.getPosition());
    }

    private enum State {
        TARGETING_POSITION, SAFE_DROPPING, OFF
    }
}
