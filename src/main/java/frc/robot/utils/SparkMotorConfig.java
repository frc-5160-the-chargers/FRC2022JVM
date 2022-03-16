package frc.robot.utils;

import com.revrobotics.CANSparkMax;

import java.util.Objects;

/**
 * A data structure representing the configuration parameters of a {@link CANSparkMax} motor.
 */
public class SparkMotorConfig {
    public final double voltageCompensation;
    public final double stallCurrentLimit;
    public final CANSparkMax.IdleMode defaultMode;
    public final double rampRate;
    public final boolean reverseMotor;

    public SparkMotorConfig(
        final double voltageCompensation,
        final double stallCurrentLimit,
        final CANSparkMax.IdleMode defaultMode,
        final double rampRate,
        final boolean reverseMotor
    ) {
        this.voltageCompensation = voltageCompensation;
        this.stallCurrentLimit = stallCurrentLimit;
        this.defaultMode = defaultMode;
        this.rampRate = rampRate;
        this.reverseMotor = reverseMotor;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SparkMotorConfig that = (SparkMotorConfig) o;
        return Double.compare(that.voltageCompensation, voltageCompensation) == 0 && Double.compare(that.stallCurrentLimit, stallCurrentLimit) == 0 && Double.compare(that.rampRate, rampRate) == 0 && reverseMotor == that.reverseMotor && defaultMode == that.defaultMode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(voltageCompensation, stallCurrentLimit, defaultMode, rampRate, reverseMotor);
    }
}