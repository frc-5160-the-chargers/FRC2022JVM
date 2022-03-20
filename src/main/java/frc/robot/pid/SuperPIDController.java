package frc.robot.pid;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.utils.Range;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.DoubleSupplier;

import static frc.robot.utils.Utils.clamp;

/**
 * Wraps WPILib's {@link edu.wpi.first.math.controller.PIDController}, adding various improvements.
 * <p>PID is a continuously-updating system that attempts to achieve a certain value ({@link #getTarget()}/{@link #setTarget(Double)}) by varying another ({@link #calculateOutput()}).
 * For example, you might use PID to a motor stay in a certain position, even when the motor is pushed, only by varying the power to the motor)</p>
 * <p>See <a href="https://www.ni.com/en-us/innovations/white-papers/06/pid-theory-explained.html">here</a> for an explanation of PID.</p>
 * <p>This class also adds feedforward capability, an augmentation to PID that can increase how quickly it reaches the target value.</p>
 * <p>See <a href="https://www.controleng.com/articles/feed-forwards-augment-pid-control/">here</a> for an explanation of feedforward.</p>
 */
@SuppressWarnings("unused")
public class SuperPIDController {
    private final DoubleSupplier input;
    private final double tolerance;
    private final Range outputRange;
    private final BiFunction<Double, Double, Double> feedForward;

    static private final BiFunction<Double, Double, Double> DEFAULT_FEED_FORWARD = (target, error) -> 0.0;
    static private final double DEFAULT_TOLERANCE = 1.0;

    private final PIDController pidController = new PIDController(0, 0, 0);

    /**
     * Calculates the next calculated output value. Should be called periodically, likely in {@link Command#execute()}
     */
    public double calculateOutput() {
        if (isOnTarget()) {
            return 0.0; // Why abruptly go to zero when on target?
        } else {
            final double pidOutput = pidController.calculate(input.getAsDouble());
            final double fedForwardOutput = applyFeedforward(pidOutput);
            return ensureInOutputRange(fedForwardOutput);
        }
    }

    private double applyFeedforward(double pidOutput) {
        return pidOutput + feedForward.apply(getTarget(), pidOutput);
    }

    private double ensureInOutputRange(double output) {
        return clamp(output, outputRange);
    }

    /**
     * The target is the value the PID controller is attempting to achieve.
     */
    public double getTarget() {
        return pidController.getSetpoint();
    }

    /**
     * The target is the value the PID controller is attempting to achieve.
     */
    public void setTarget(Double target) {
        pidController.reset();
        pidController.setSetpoint(target);
    }

    /**
     * The PID Constants control exactly how the PID Controller attempts to reach the target.
     */
    public PIDConstants getConstants() {
        return PIDConstants.getControllerConstants(pidController);
    }

    /**
     * The PID Constants control exactly how the PID Controller attempts to reach the target.
     * @param pidConstants a {@link PIDConstants} objects holding the new set of constants.
     */
    public void setConstants(final PIDConstants pidConstants) {
        pidController.reset();
        PIDConstants.setControllerConstants(this.pidController, pidConstants);
    }

    /**
     * The error is a signed value representing how far the PID system currently is from the target value.
     */
    public double getError() {
        return input.getAsDouble() - pidController.getSetpoint();
    }

    /**
     * A PID Controller is "on target" if its current value is within {@link #tolerance} of the target value.
     * @return if a target is set, whether the controller is currently on target; otherwise, true
     */
    public boolean isOnTarget() {
        return Math.abs(getError()) < tolerance;
    }

    private SuperPIDController(
        final PIDConstants pidConstants,
        final DoubleSupplier input,
        final Range outputRange,
        final Double tolerance,
        final BiFunction<Double, Double, Double> feedForward,
        final double target
    ) {
        this.input = input;
        this.feedForward = Objects.requireNonNullElse(feedForward, DEFAULT_FEED_FORWARD);
        this.tolerance = Objects.requireNonNullElse(tolerance, DEFAULT_TOLERANCE);
        this.outputRange = outputRange;

        PIDConstants.setControllerConstants(pidController, pidConstants);
        pidController.setSetpoint(target);
    }

    /**
     * Used to build new {@link SuperPIDController} instances.
     * (See <a href="https://blogs.oracle.com/javamagazine/post/exploring-joshua-blochs-builder-design-pattern-in-java">here</a> for an explanation of the builder pattern.)
     */
    public static class Builder {
        private final PIDConstants pidConstants;
        private final DoubleSupplier input;
        private final Range outputRange;
        private Double tolerance;
        private BiFunction<Double, Double, Double> feedForward;
        private double target;

        public Builder(
            final PIDConstants pidConstants,
            final DoubleSupplier input,
            final Range outputRange
        ) {
            this.input = input;
            this.pidConstants = pidConstants;
            this.outputRange = outputRange;
        }

        public Builder tolerance(final Double tolerance) {
            this.tolerance = tolerance;
            return this;
        }

        public Builder feedForward(final BiFunction<Double, Double, Double> feedForward) {
            this.feedForward = feedForward;
            return this;
        }

        public Builder target(final double target) {
            this.target = target;
            return this;
        }

        public SuperPIDController build() {
            return new SuperPIDController(pidConstants, input, outputRange, tolerance, feedForward, target);
        }
    }
}
