package frc.robot;

import edu.wpi.first.math.controller.PIDController;
import frc.robot.utils.PIDConstants;
import frc.robot.utils.Range;
import frc.robot.utils.SmartDashboardUtils;
import frc.robot.utils.Utils;

import java.util.Objects;
import java.util.function.*;

import static frc.robot.utils.Utils.*;

public final class SuperPIDController {
    private final BiFunction<Double, Double, Double> feedForward; //TODO: Extract into functional interface
    private final PIDController pidController;
    private PIDConstants pidConstants;
    private final DoubleSupplier input;
    private final DoubleConsumer output;
    private boolean active;

    final String dashPidKey;
    Range outputRange;
    double tolerance;

    /**
     * @deprecated
     * Passing raw nulls is prone to confusion.
     * Use {@link Builder#build()} instead.
     * (See https://blogs.oracle.com/javamagazine/post/exploring-joshua-blochs-builder-design-pattern-in-java for an explanation.)
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

    public double getTarget() {
        return active ? pidController.getSetpoint() : 0;
    }

    public void setConstants(final PIDConstants pidConstants) {
        this.pidConstants = pidConstants;
        PIDConstants.updateController(this.pidController, this.pidConstants);
    }

    public double getError() {
        return input.getAsDouble() - pidController.getSetpoint();
    }

    public void maybeSetConstantsFromDash() {
        if (isPidDashEnabled()) {
            pidConstants = SmartDashboardUtils.getPID(dashPidKey);
        } // TODO: Should it really do nothing if the pidKey is unset?
    }

    public void maybePutConstantsToDash() {
        if (isPidDashEnabled()) {
            SmartDashboardUtils.putPID(dashPidKey, pidConstants);
        }
    }

    public boolean isOnTarget() {
        if (active) {
            return Math.abs(getError()) < tolerance;
        } else {
            return true; // Why?
        }
    }

    public double calculateOutput() {
        final double pidOutput = pidController.calculate(input.getAsDouble());
        double fedForwardOutput = pidOutput + feedForward.apply(getTarget(), pidOutput);
        return clamp(fedForwardOutput, outputRange);
    }

    public void execute() {
        if (active) {
            if (isOnTarget()) {
                output.accept(0.0);
            } else {
                output.accept(calculateOutput());
            }
        }
    }

    public void stop() {
        reset();
        active = false;
    }

    public void start() {
        reset();
        active = true;
    }

    public void reset() {
        pidController.reset();
    }

    public void runToSetpoint(Double setpoint) {
        ensureActive();
        pidController.setSetpoint(setpoint);
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
