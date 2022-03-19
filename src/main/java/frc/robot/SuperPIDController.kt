package frc.robot

import edu.wpi.first.math.controller.PIDController
import frc.robot.utils.PIDConstants
import frc.robot.utils.getPID
import frc.robot.utils.putPID
import frc.robot.utils.setConstants
import kotlin.math.abs

class SuperPIDController(
    pidConstants: PIDConstants,
    val input: () -> Double,
    val output: (Double) -> Unit,
    val feedForward: FeedForward = FeedForward.Constant(0.0),
    val outputRange: ClosedRange<Double> = -1.0..1.0,
    val tolerance: Double = 1.0,
    val dashPidKey: String? = null
) {
    var pidConstants: PIDConstants = pidConstants
        set(value) {
            pidController.setConstants(value)
            field = value
        }

    private val pidController = PIDController(0.0, 0.0, 0.0)
        .apply {
            setConstants(pidConstants)
        }

    private var active = false

    /**
     * The value the PID controller is attempting to achieve.
     */
    var target: Double
        get() = if (active) pidController.setpoint else 0.0
        set(value) {
            ensureActive()
            pidController.setpoint = value
        }

    /**
     * A signed value representing how far the PID system currently is from the target value.
     */
    val error: Double
        get() = input() - pidController.setpoint

    /**
     * A PID Controller is "on target" if its current value is within {@link #tolerance} of the target value.
     * @return if a target is set, whether the controller is currently on target; otherwise, true
     */
    val isOnTarget: Boolean
        get() =
            if (active)
                abs(error) < tolerance
            else true // Why?

    /**
     * If a [dashPidKey] is set, get a new set of PID constants from the SmartDashboard.
     */
    fun maybeSetConstantsFromDash() {
        dashPidKey?.let { key ->
            pidConstants = getPID(key)
        } // TODO: Should it really do nothing if the pidKey is unset?
    }

    /**
     * If a [dashPidKey] is set, update the SmartDashboard to display the current PID Constants.
     */
    fun maybePutConstantsToDash() {
        dashPidKey?.let { key ->
            putPID(key, pidConstants)
        }
    }

    /**
     * Sends to [output] the next calculated output value.
     */
    fun execute() {
        if (active) {
            if (isOnTarget) {
                output(0.0)
            } else {
                output(calculateOutput())
            }
        }
    }

    private fun calculateOutput(): Double =
        pidController.calculate(input())
            .let { pidOutput ->
                pidOutput + feedForward(target, pidOutput)
            }
            .coerceIn(outputRange)

    /**
     * Stops the controller attempting to reach the previously set target.
     */
    fun stop() {
        reset()
        active = false
    }

    /**
     * Resumes the controller attempting to reach the previously set target.
     */
    fun start() {
        reset()
        active = true
    }

    /**
     * Resets all memory of previous errors and corrections.
     */
    fun reset() = pidController.reset()

    private fun ensureActive() {
        if (!active) {
            start()
        }
    }
}

fun interface FeedForward : (Double, Double) -> Double {
    override fun invoke(target: Double, error: Double): Double

    class Constant(private val value: Double) : FeedForward {
        override fun invoke(target: Double, error: Double): Double = value
    }
}