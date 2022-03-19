package frc.robot.utils

import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMax.IdleMode
import java.util.*

/**
 * A data structure representing the configuration parameters of a [CANSparkMax] motor.
 */
class SparkMotorConfig(
    val voltageCompensation: Double,
    val stallCurrentLimit: Int,
    val defaultMode: IdleMode,
    val rampRate: Double,
    val reverseMotor: Boolean
) : MotorConfig<CANSparkMax> {
    override fun configure(motor: CANSparkMax) {
        motor.restoreFactoryDefaults()
        motor.enableVoltageCompensation(voltageCompensation)
        motor.setSmartCurrentLimit(stallCurrentLimit)
        motor.idleMode = defaultMode
        motor.openLoopRampRate = rampRate
        motor.closedLoopRampRate = rampRate
        motor.inverted = reverseMotor
    }
}