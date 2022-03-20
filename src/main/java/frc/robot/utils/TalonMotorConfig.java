package frc.robot.utils;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;

import java.util.Objects;

/**
 * A data structure representing the configuration parameters of a {@link TalonSRX} motor.
 */
public class TalonMotorConfig {
    private final double voltageSaturation;
    private final double deadband;
    private final int peakCurrent;
    private final int continuousCurrent;
    private final NeutralMode defaultMode;
    private final double rampRate;
    private final boolean reverseMotor;

    public TalonMotorConfig(
        final double voltageSaturation,
        final double deadband,
        final int peakCurrent,
        final int continuousCurrent,
        final NeutralMode defaultMode,
        final double rampRate,
        final boolean reverseMotor
    ) {
        this.voltageSaturation = voltageSaturation;
        this.deadband = deadband;
        this.peakCurrent = peakCurrent;
        this.continuousCurrent = continuousCurrent;
        this.defaultMode = defaultMode;
        this.rampRate = rampRate;
        this.reverseMotor = reverseMotor;
    }

    public TalonMotorConfig(
        final double voltageSaturation,
        final double deadband,
        final int peakCurrent,
        final int continuousCurrent,
        final NeutralMode defaultMode,
        final double rampRate
    ) {
        this(voltageSaturation, deadband, peakCurrent, continuousCurrent, defaultMode, rampRate, true);
    }

    static void configureTalon(TalonSRX talon, TalonMotorConfig motorConfig) {
        talon.configFactoryDefault();
        talon.enableVoltageCompensation(true);
        talon.configVoltageCompSaturation(motorConfig.voltageSaturation);
        talon.enableCurrentLimit(true);
        talon.configPeakCurrentLimit(motorConfig.peakCurrent);
        talon.configContinuousCurrentLimit(motorConfig.continuousCurrent);
        talon.setNeutralMode(motorConfig.defaultMode);
        talon.configNeutralDeadband(motorConfig.deadband);
        talon.configOpenloopRamp(motorConfig.rampRate);
        talon.setInverted(motorConfig.reverseMotor);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final TalonMotorConfig that = (TalonMotorConfig) o;
        return Double.compare(that.voltageSaturation, voltageSaturation) == 0 && Double.compare(that.deadband, deadband) == 0 && peakCurrent == that.peakCurrent && continuousCurrent == that.continuousCurrent && Double.compare(that.rampRate, rampRate) == 0 && reverseMotor == that.reverseMotor && defaultMode == that.defaultMode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(voltageSaturation, deadband, peakCurrent, continuousCurrent, defaultMode, rampRate, reverseMotor);
    }
}