package frc.robot.utils

import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.can.TalonSRX

data class TalonMotorConfig @JvmOverloads constructor(
    val voltageSaturation: Double,
    val deadband: Double,
    val peakCurrent: Int,
    val continuousCurrent: Int,
    val defaultMode: NeutralMode,
    val rampRate: Double,
    val reverseMotor: Boolean = true
) : MotorConfig<TalonSRX> {
    override fun configure(motor: TalonSRX) {
        motor.configFactoryDefault()
        motor.enableVoltageCompensation(true)
        motor.configVoltageCompSaturation(voltageSaturation)
        motor.enableCurrentLimit(true)
        motor.configPeakCurrentLimit(peakCurrent)
        motor.configContinuousCurrentLimit(continuousCurrent)
        motor.setNeutralMode(defaultMode)
        motor.configNeutralDeadband(deadband)
        motor.configOpenloopRamp(rampRate)
        motor.inverted = reverseMotor
    }
}
