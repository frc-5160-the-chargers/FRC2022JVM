package frc.robot;

import edu.wpi.first.math.controller.PIDController;
import frc.robot.utils.PIDConstants;
import frc.robot.utils.Range;
import frc.robot.utils.SmartDashboardUtils;

import java.util.Objects;
import java.util.function.*;

import static frc.robot.utils.Utils.*;

/**
 * Wraps WPILib's {@link edu.wpi.first.math.controller.PIDController}, adding various improvements.
 * <p>PID is a continuously-updating system that attempts to achieve a certain value ({@link #getTarget()}/{@link #setTarget(Double)}) by varying another ({@link #output})
 * (for example, keeping a motor in a certain position, even when the motor is pushed, only by varying the speed of the motor).</p>
 * <p>See <a href="https://www.ni.com/en-us/innovations/white-papers/06/pid-theory-explained.html">here</a> for an explanation of PID.</p>
 * <p>This class also adds feedforward capability, an augmentation to PID that can increase its speed in reaching the target value</p>
 * <p>See <a href="https://www.controleng.com/articles/feed-forwards-augment-pid-control/">here</a> for an explanation of feedforward.</p>
 */
public final class SuperPIDController {
    private final BiFunction<Double, Double, Double> feedForward; //TODO: Extract into functional interface
    private final PIDController pidController;
    private PIDConstants pidConstants;
    private final DoubleSupplier input;
    private final DoubleConsumer output;
    private boolean active;

    /**
     * The optional key to be passed to the {@link edu.wpi.first.wpilibj.smartdashboard.SmartDashboard} allowing access to the PID Constants from the SmartDashboard.
     */
    public final String dashPidKey;
    public Range outputRange;
    public double tolerance;

    /**
     * @deprecated
     * Passing raw nulls is prone to confusion.
     * Use {@link Builder#build()} instead.
     * (See <a href="https://blogs.oracle.com/javamagazine/post/exploring-joshua-blochs-builder-design-pattern-in-java">here</a> for an explanation.)
     */
    @Deprecated
    public SuperPIDController(
        final PIDConstants pidConstants,
        final DoubleSupplier input,
        final DoubleConsumer output,
        final BiFunction<Double, Double, Double> feedForward,
        final Range outputRange,
        final Double tolerance,
        final String dashPidKey
    ) {
        this.pidConstants = pidConstants;
        this.input = input;
        this.output = output;
        this.dashPidKey = dashPidKey;
        this.feedForward = Objects.requireNonNullElseGet(feedForward, () -> (target, error) -> 0.0);
        this.outputRange = Objects.requireNonNullElseGet(outputRange, () -> new Range(-1.0, 1.0));
        this.tolerance = Objects.requireNonNullElse(tolerance, 1.0);

        this.pidController = new PIDController(0, 0, 0);
        PIDConstants.updateController(pidController, pidConstants);

        active = false;
    }

    private SuperPIDController(final Builder builder) {
        Objects.requireNonNull(builder);

        this.pidConstants = builder.pidConstants;
        this.input = builder.input;
        this.output = builder.output;
        this.dashPidKey = builder.dashPidKey;
        this.feedForward = Objects.requireNonNullElseGet(builder.feedForward, () -> (target, error) -> 0.0);
        this.outputRange = Objects.requireNonNullElseGet(builder.outputRange, () -> new Range(-1.0, 1.0));
        this.tolerance = Objects.requireNonNullElse(builder.tolerance, 1.0);

        this.pidController = new PIDController(0, 0, 0);
        PIDConstants.updateController(pidController, pidConstants);

        active = false;
    }

    /**
     * The target is the value the PID controller is attempting to achieve.
     * @return the target or 0 if no target set
     */
    public double getTarget() {
        return active ? pidController.getSetpoint() : 0;
    }

    /**
     * The target is the value the PID controller is attempting to achieve.
     */
    public void setTarget(Double target) {
        ensureActive();
        pidController.setSetpoint(target);
    }

    /**
     * The PID Constants control exactly how the PID Controller attempts to reach the target.
     * @param pidConstants a {@link PIDConstants} objects holding the new set of constants.
     */
    public void setConstants(final PIDConstants pidConstants) {
        this.pidConstants = pidConstants;
        PIDConstants.updateController(this.pidController, this.pidConstants);
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
        if (active) {
            return Math.abs(getError()) < tolerance;
        } else {
            return true; // Why?
        }
    }

    /**
     * If a {@link #dashPidKey} is set, get a new set of PID constants from the SmartDashboard.
     */
    public void maybeSetConstantsFromDash() {
        if (isPidDashEnabled()) {
            setConstants(SmartDashboardUtils.getPID(dashPidKey));
        } // TODO: Should it really do nothing if the pidKey is unset?
    }

    /**
     * If a {@link #dashPidKey} is set, update the SmartDashboard to display the current PID Constants.
     */
    public void maybePutConstantsToDash() {
        if (isPidDashEnabled()) {
            SmartDashboardUtils.putPID(dashPidKey, pidConstants);
        }
    }

    /**
     * Sends to {@link #output} the next calculated output value.
     */
    public void execute() {
        if (active) {
            if (isOnTarget()) {
                output.accept(0.0);
            } else {
                output.accept(calculateOutput());
            }
        }
    }

    /**
     * Stops the controller attempting to reach the previously set target.
     */
    public void stop() {
        reset();
        active = false;
    }

    /**
     * Resumes the controller attempting to reach the previously set target.
     */
    public void start() {
        reset();
        active = true;
    }

    /**
     * Resets all memory of previous errors and corrections.
     */
    public void reset() {
        pidController.reset();
    }

    private double calculateOutput() {
        final double pidOutput = pidController.calculate(input.getAsDouble());
        double fedForwardOutput = pidOutput + feedForward.apply(getTarget(), pidOutput);
        return clamp(fedForwardOutput, outputRange);
    }

    private void ensureActive() {
        if (!active) {
            start();
        }
    }

    private boolean isPidDashEnabled() {
        return dashPidKey != null;
    }

    public static class Builder {
        private final PIDConstants pidConstants;
        private final DoubleSupplier input;
        private final DoubleConsumer output;
        private BiFunction<Double, Double, Double> feedForward;
        private String dashPidKey;
        private Range outputRange;
        private double tolerance;

        public Builder(
            final PIDConstants pidConstants,
            final DoubleSupplier input,
            final DoubleConsumer output
        ) {
            this.pidConstants = pidConstants;
            this.input = input;
            this.output = output;
        }

        public Builder feedForward(final BiFunction<Double, Double, Double> feedForward) {
            this.feedForward = feedForward;
            return this;
        }

        public Builder outputRange(final Range outputRange) {
            this.outputRange = outputRange;
            return this;
        }

        public Builder tolerance(final double tolerance) {
            this.tolerance = tolerance;
            return this;
        }

        public Builder dashPidKey(final String dashPidKey) {
            this.dashPidKey = dashPidKey;
            return this;
        }

        public SuperPIDController build() {
            return new SuperPIDController(this);
        }
    }
}
